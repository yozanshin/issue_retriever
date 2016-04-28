package controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import config.LoadConfig;

@ManagedBean
@ViewScoped
public class ProblemController {

	private String problemCode;
	
	public void init(){
		if(problemCode == null || problemCode.isEmpty()){
			problemCode="0";
		}
	}

	public String getProblemCode() {
		return problemCode;
	}

	public void setProblemCode(String errorCode) {
		this.problemCode = errorCode;
	}
	
	public String reloadConfig(){
		
		LoadConfig.getInstance().readConfig();
		
		return "index.xhtml?faces-redirect=true";
		
	}
}
