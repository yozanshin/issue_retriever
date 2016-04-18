package config;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;

@ManagedBean
@RequestScoped
public class ConfigController {

	private String dateFormat;
	private String gitlabUrl;
	private String gitlabToken;
	private String toConfig;
	
	public void init(){
		setDateFormat(LoadConfig.getInstance().getDateFormat());
		setGitlabUrl(LoadConfig.getInstance().getGitlabUrl());
		setGitlabToken(LoadConfig.getInstance().getGitlabToken());
	}
	
	public String submit(){
		if(!this.dateFormat.isEmpty()) LoadConfig.getInstance().setDateFormat(this.dateFormat);
		if(!this.gitlabUrl.isEmpty()) LoadConfig.getInstance().setGitlabUrl(this.gitlabUrl);
		if(!this.gitlabToken.isEmpty()) LoadConfig.getInstance().setGitlabToken(this.gitlabToken);
		
		return "index?faces-redirect=true";
	}
	
	public String getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	public String getGitlabUrl() {
		return gitlabUrl;
	}
	public void setGitlabUrl(String gitlabUrl) {
		this.gitlabUrl = gitlabUrl;
	}
	public String getGitlabToken() {
		return gitlabToken;
	}
	public void setGitlabToken(String gitlabToken) {
		this.gitlabToken = gitlabToken;
	}

	public String getToConfig() {
		return toConfig;
	}

	public void setToConfig(String toConfig) {
		this.toConfig = toConfig;
	}
	
}
