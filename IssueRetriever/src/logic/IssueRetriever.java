package logic;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.servlet.ServletContext;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.gitlab.api.AuthMethod;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.TokenType;
import org.gitlab.api.http.GitlabHTTPRequestor;
import org.gitlab.api.models.GitlabIssue;
import org.gitlab.api.models.GitlabLabel;
import org.gitlab.api.models.GitlabMilestone;
import org.gitlab.api.models.GitlabProject;

@Stateless
@Startup
public class IssueRetriever implements IssueRetrieverLocal {

	private static final String GET_ISSUES_PATH = "projects/{0}/issues?";
	private static final String QUERY_LABELS = "labels={0}";
	private static final String QUERY_MILESTONE = "milestone={0}";

	private static final String PROPERTY_DATEFORMAT = "date_format";
	private static final String PROPERTY_ENDPOINT = "gitlab_endpoint";
	private static final String PROPERTY_TOKEN = "gitlab_token";

	private static String DATE_FORMAT = "dd/MM/yyyy";

	private String GITLAB_URL;
	private String GITLAB_TOKEN;

	private String errorMessage;

	@Resource
	private WebServiceContext ctx;

	public IssueRetriever() {

	}

	public String getErrorMessage() {
		return errorMessage;
	}

	private GitlabAPI getGitlabAPI() {
		GitlabAPI gitlabApi = GitlabAPI.connect(GITLAB_URL, GITLAB_TOKEN, TokenType.PRIVATE_TOKEN,
				AuthMethod.URL_PARAMETER);
		gitlabApi.ignoreCertificateErrors(true);
		return gitlabApi;
	}

	private GitlabHTTPRequestor getGitlabHTTPRequestor() {
		GitlabHTTPRequestor httpRequestor = new GitlabHTTPRequestor(getGitlabAPI());
		httpRequestor.authenticate(GITLAB_TOKEN, TokenType.PRIVATE_TOKEN, AuthMethod.URL_PARAMETER);
		return httpRequestor;
	}

	@Override
	public List<GitlabIssue> getIssues(String projectId, String[] labels, String milestone, String after, String before)
			throws Exception {
		try {
			GitlabHTTPRequestor httpRequestor = getGitlabHTTPRequestor();
			String path = getIssuesResource(projectId, labels, milestone);
			List<GitlabIssue> issues = new ArrayList<GitlabIssue>();
			List<GitlabIssue> origIssues = httpRequestor.getAll(path, GitlabIssue[].class);
			if (origIssues.isEmpty()) {
				throw new LogicException("No issues found");
			}
			Date afterDate = toDate(after);
			Date beforeDate = toDate(before);
			for (GitlabIssue issue : origIssues) {
				if (issue.getCreatedAt().after(afterDate) && issue.getCreatedAt().before(beforeDate)) {
					issues.add(issue);
				}
			}
			if (issues.isEmpty()) {
				throw new LogicException("No issue found matches required params");
			}
			return issues;
		} catch (Exception ex) {
			throw new LogicException("Unable to get issues: " + ex.getMessage());
		}
	}

	private String getIssuesResource(String projectId, String[] labels, String milestone) {
		StringBuilder sb = new StringBuilder("");
		sb.append(MessageFormat.format(GET_ISSUES_PATH, projectId));
		sb.append(MessageFormat.format(QUERY_LABELS, String.join(",", labels)));
		if (!"".equals(milestone)) {
			sb.append("&");
			sb.append(MessageFormat.format(QUERY_MILESTONE, milestone));
		}
		return sb.toString();
	}

	@Override
	public List<GitlabLabel> getLabels(String projectId) throws Exception {
		return getGitlabAPI().getLabels(projectId);
	}

	@Override
	public List<GitlabMilestone> getMilestones(String projectId) throws Exception {
		return getGitlabAPI().getMilestones(projectId);
	}

	@Override
	public List<GitlabProject> getProjects() throws Exception {
		List<GitlabProject> projects = getGitlabAPI().getProjects();
		if (projects.isEmpty()) {
			throw new LogicException("No projects found");
		}
		return projects;
	}

	@PostConstruct
	public void init() {
		try {
			String contextPath = ((ServletContext) ctx.getMessageContext().get(MessageContext.SERVLET_CONTEXT))
					.getRealPath("/");
			String propertiesFile = contextPath + File.separator + "config.properties";

			Properties prop = new Properties();
			prop.load(new FileReader(propertiesFile));

			GITLAB_URL = prop.getProperty(PROPERTY_ENDPOINT);
			if (GITLAB_URL == null || "".equals(GITLAB_URL)) {
				errorMessage = "Missing Gitlab endpoint configuration";
			}
			System.out.println("GITLAB_URL: " + GITLAB_URL);

			GITLAB_TOKEN = prop.getProperty(PROPERTY_TOKEN);
			if (GITLAB_TOKEN == null || "".equals(GITLAB_TOKEN)) {
				errorMessage = "Missing Gitlab authentication token configuration";
			}
			System.out.println("GITLAB_TOKEN: " + GITLAB_TOKEN);

			DATE_FORMAT = prop.getProperty(PROPERTY_DATEFORMAT);
			if (DATE_FORMAT == null || "".equals(DATE_FORMAT)) {
				errorMessage = "Missing date forma configuration";
			}
			System.out.println("DATE_FORMAT: " + DATE_FORMAT);

		} catch (IOException ex) {
			ex.printStackTrace();
			errorMessage = ex.getMessage();
		}
	}

	private Date toDate(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		return sdf.parse(date);
	}

	private String toString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		return sdf.format(date);
	}

}
