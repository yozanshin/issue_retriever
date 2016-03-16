package controller;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.gitlab.api.models.GitlabLabel;
import org.gitlab.api.models.GitlabMilestone;
import org.gitlab.api.models.GitlabProject;

import logic.IssueRetrieverLocal;

@ManagedBean
@ViewScoped
public class IssueRetrieverController {

	@EJB
	private IssueRetrieverLocal issueRetriever;

	private List<GitlabProject> projects;
	private List<GitlabLabel> labels;

	private GitlabProject project;
	private GitlabLabel label;

	public List<GitlabProject> getProjects() {
		try {

			if (projects == null) {
				long initTime = System.currentTimeMillis();
				projects = issueRetriever.getProjects();
				System.out.println("PROJECTS: " + projects.size() + ". TIME: "
						+ (float) (System.currentTimeMillis() - initTime) / 1000 + " seconds");
				// for (GitlabProject project : results) {
				// System.out.println(project.getNameWithNamespace() + " : " +
				// project.getTagList());
				// }
			}

			return projects;

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("ERROR: " + ex.getMessage());
			return null;
		}
	}

	public void onChange() {
		if (project != null && !project.equals(""))
			labels = getLabels(project.getId());
		else
			labels = new ArrayList<GitlabLabel>();
	}

	public GitlabProject getProject() {
		return project;
	}

	public void setProject(GitlabProject project) {
		this.project = project;
	}

	public List<GitlabLabel> getLabels(Integer projectId) {
		try {
			return issueRetriever.getLabels(projectId.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<GitlabLabel>();
	}

	public List<GitlabMilestone> getMilestones(String projectId) throws Exception {
		return issueRetriever.getMilestones(projectId);
	}

	public GitlabLabel getLabel() {
		return label;
	}

	public void setLabel(GitlabLabel label) {
		this.label = label;
	}

	
	
	public List<GitlabLabel> getLabels() {
		return labels;
	}

	public void setLabels(List<GitlabLabel> labels) {
		this.labels = labels;
	}

	public void refresh() {
		projects = null;
	}
}
