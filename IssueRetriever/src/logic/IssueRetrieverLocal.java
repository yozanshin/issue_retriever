package logic;

import java.util.List;

import org.gitlab.api.models.GitlabIssue;
import org.gitlab.api.models.GitlabLabel;
import org.gitlab.api.models.GitlabMilestone;
import org.gitlab.api.models.GitlabProject;

public interface IssueRetrieverLocal {

	public List<GitlabProject> getProjects() throws Exception;

	public List<GitlabLabel> getLabels(String projectId) throws Exception;

	public List<GitlabMilestone> getMilestones(String projectId) throws Exception;

	public List<GitlabIssue> getIssues(String projectId, String[] labels, String milestone, String after, String before)
			throws Exception;
}
