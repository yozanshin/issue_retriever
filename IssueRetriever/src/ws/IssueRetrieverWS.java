package ws;

import java.text.MessageFormat;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import org.gitlab.api.models.GitlabLabel;
import org.gitlab.api.models.GitlabProject;

import logic.IssueRetrieverLocal;

@WebService(endpointInterface = "ws.IssueRetrieverWS_SEI")
public class IssueRetrieverWS implements IssueRetrieverWS_SEI {

	@EJB
	private IssueRetrieverLocal issueRetriever;

	@Resource
	private WebServiceContext ctx;

	@Override
	public String test() {
		try {
			long initTime = System.currentTimeMillis();
			// TODO
			System.out.println(". TIME: " + (float) (System.currentTimeMillis() - initTime) / 1000 + " seconds");
			return "No processing assigned";
		} catch (Exception ex) {
			ex.printStackTrace();
			return MessageFormat.format("ERROR: {0}", ex.getMessage());
		}
	}

	@Override
	public List<GitlabProject> getAllProjects() {
		try {
			long initTime = System.currentTimeMillis();
			List<GitlabProject> results = issueRetriever.getProjects();
			System.out.println("PROJECTS: " + results.size() + ". TIME: "
					+ (float) (System.currentTimeMillis() - initTime) / 1000 + " seconds");
			for (GitlabProject project : results) {
				System.out.println(project.getNameWithNamespace() + " : " + project.getTagList());
			}
			return results;

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("ERROR: " + ex.getMessage());
			return null;
		}
	}

	@Override
	public List<GitlabLabel> getLabelsByProject(String projectId) {
		try {
			return issueRetriever.getLabels(projectId);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public String getIssuesByProject(String projectId) {
		// try {
		// long initTime = System.currentTimeMillis();
		// List<GitlabIssue> issues = issueRetriever.getIssues(projectId);
		// for (GitlabIssue issue : issues) {
		// System.out.println(issue.getCreatedAt());
		// }
		// return "ISSUES: " + issues.size() + ". TIME: " + (float)
		// (System.currentTimeMillis() - initTime) / 1000
		// + " seconds";
		// } catch (Exception ex) {
		// ex.printStackTrace();
		// return "ERROR: " + ex.getMessage();
		// }
		return "Not supported yet";
	}

}
