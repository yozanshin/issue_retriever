package controller;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.gitlab.api.models.GitlabProject;

import logic.IssueRetrieverLocal;

@ManagedBean
@SessionScoped
public class IssueRetrieverController {
	
	@EJB
	private IssueRetrieverLocal issueRetriever;

	public List<GitlabProject> getProjects() {
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
}
