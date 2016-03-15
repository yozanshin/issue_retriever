package ws;

import java.util.List;

import javax.jws.WebService;

import org.gitlab.api.models.GitlabLabel;
import org.gitlab.api.models.GitlabProject;

@WebService
public interface IssueRetrieverWS_SEI {

	public String test();

	public List<GitlabProject> getAllProjects();

	public List<GitlabLabel> getLabelsByProject(String projectId);

	public String getIssuesByProject(String projectId);

}
