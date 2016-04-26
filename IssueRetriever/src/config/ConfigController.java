package config;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class ConfigController {

	private String dateFormat;
	private String gitlabUrl;
	private String gitlabToken;
	private String dateFormatTmp;
	private String gitlabUrlTmp;
	private String gitlabTokenTmp;
	private String toConfig;
	
	public void init(){
		dateFormat=LoadConfig.getInstance().getDateFormat();
		gitlabUrl=LoadConfig.getInstance().getGitlabUrl();
		gitlabToken=LoadConfig.getInstance().getGitlabToken();
	}
	
	public String submit() throws ConfigException{
		if(!this.dateFormat.equals(this.dateFormatTmp) && this.dateFormatTmp != null  && !this.dateFormatTmp.isEmpty()){
			this.dateFormat=this.dateFormatTmp;
			LoadConfig.getInstance().setDateFormat(this.dateFormat);
		}
		if(!this.gitlabUrl.equals(this.gitlabUrlTmp) && this.gitlabUrlTmp != null  && !this.gitlabUrlTmp.isEmpty()){
			this.gitlabUrl=this.gitlabUrlTmp;
			LoadConfig.getInstance().setGitlabUrl(this.gitlabUrl);
		}
		if(!this.gitlabToken.equals(this.gitlabTokenTmp) && this.gitlabTokenTmp != null  && !this.gitlabTokenTmp.isEmpty()){
			this.gitlabToken=this.gitlabTokenTmp;
			LoadConfig.getInstance().setGitlabToken(this.gitlabToken);
		}
		
		return "index?faces-redirect=true";
	}
	
	public String getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(String dateFormat) {
		if(!dateFormat.isEmpty()) this.dateFormatTmp = dateFormat;
	}
	public String getGitlabUrl() {
		return gitlabUrl;
	}
	public void setGitlabUrl(String gitlabUrl) {
		if(!gitlabUrl.isEmpty()) this.gitlabUrlTmp = gitlabUrl;
	}
	public String getGitlabToken() {
		return gitlabToken;
	}
	public void setGitlabToken(String gitlabToken) {
		if(!gitlabToken.isEmpty()) this.gitlabTokenTmp = gitlabToken;
	}

	public String getToConfig() {
		return toConfig;
	}

	public void setToConfig(String toConfig) {
		this.toConfig = toConfig;
	}
	
}
