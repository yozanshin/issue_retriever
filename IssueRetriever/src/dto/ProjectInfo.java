package dto;

import org.gitlab.api.models.GitlabProject;

public class ProjectInfo {

	private String name;
	private String description;
	private int projectId;
	private String path;
	private String httpUrl;
	private String sshUrl;
	private String webUrl;

	public ProjectInfo() {
	}

	public ProjectInfo(GitlabProject gitProject) {
		this.name = gitProject.getName();
		this.description = gitProject.getDescription();
		this.projectId = gitProject.getId();
		this.path = gitProject.getPathWithNamespace();
		this.httpUrl = gitProject.getHttpUrl();
		this.sshUrl = gitProject.getSshUrl();
		this.webUrl = gitProject.getWebUrl();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getHttpUrl() {
		return httpUrl;
	}

	public void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl;
	}

	public String getSshUrl() {
		return sshUrl;
	}

	public void setSshUrl(String sshUrl) {
		this.sshUrl = sshUrl;
	}

	public String getWebUrl() {
		return webUrl;
	}

	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

}
