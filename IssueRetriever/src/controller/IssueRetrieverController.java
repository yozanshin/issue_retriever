package controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.gitlab.api.models.GitlabIssue;
import org.gitlab.api.models.GitlabLabel;
import org.gitlab.api.models.GitlabMilestone;
import org.gitlab.api.models.GitlabProject;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import config.ConfigException;
import config.LoadConfig;
import logic.IssueRetrieverLocal;

@ManagedBean
@SessionScoped
public class IssueRetrieverController {

	@EJB
	private IssueRetrieverLocal issueRetriever;

	private List<GitlabProject> projects;
	private List<GitlabLabel> labels1;
	private List<GitlabMilestone> milestones;
	
	private List<GitlabIssue> issues;

	private String project;
	private String label1;
	private String label2;
	private String label3;
	private String milestone;
	private String issue;
	
	private Date startDate;
	private Date endDate;
	
	private String startDateSt;
	private String endDateSt;
	
	private String dateFormat;

	public String getStartDateSt() {
		return startDateSt;
	}

	public void setStartDateSt(String startDateSt) {
		this.startDateSt = startDateSt;
	}

	public String getEndDateSt() {
		return endDateSt;
	}

	public void setEndDateSt(String endDateSt) {
		this.endDateSt = endDateSt;
	}

	public List<GitlabProject> getProjects() {
		try {

			if (projects == null) {
				long initTime = System.currentTimeMillis();
				projects = issueRetriever.getProjects();
				System.out.println("PROJECTS: " + projects.size() + ". TIME: "
						+ (float) (System.currentTimeMillis() - initTime) / 1000 + " seconds");
			}

			return projects;

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("ERROR: " + ex.getMessage());
			return null;
		}
	}

	public void onChange() throws Exception {
		if (project != null && !project.equals("")){
			setLabels1(getLabels(Integer.parseInt(project)));
			setMilestones(getMilestones(Integer.parseInt(project)));
		}
		else{
			setLabels1(new ArrayList<GitlabLabel>());
			setLabels1(new ArrayList<GitlabLabel>());
			setLabels1(new ArrayList<GitlabLabel>());
		setMilestones(new ArrayList<GitlabMilestone>());
		}
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) throws Exception {
		this.project = project;
		onChange();
	}

	public List<GitlabLabel> getLabels(Integer projectId) throws Exception {
			return issueRetriever.getLabels(projectId.toString());
	}

	public List<GitlabMilestone> getMilestones(Integer projectId) throws Exception{
		return issueRetriever.getMilestones(projectId.toString());
		
	}

	public String getLabel1() {
		return label1;
	}

	public void setLabel1(String label1) {
		this.label1 = label1;
	}

	public String getLabel2() {
		return label2;
	}

	public void setLabel2(String label2) {
		this.label2 = label2;
	}

	public String getLabel3() {
		return label3;
	}

	public void setLabel3(String label3) {
		this.label3 = label3;
	}

	public List<GitlabLabel> getLabels1() {
		return labels1;
	}

	public void setLabels1(List<GitlabLabel> labels1) {
		this.labels1 = labels1;
	}
	
	public List<GitlabLabel> getLabels2() {
		return labels1;
	}
	
	public String getMilestone() {
		return milestone;
	}

	public void setMilestone(String milestone) {
		this.milestone = milestone;
	}
	
	public void refresh() {
		projects = null;
	}

	public List<GitlabMilestone> getMilestones() {
		return milestones;
	}

	public void setMilestones(List<GitlabMilestone> milestones) {
		this.milestones = milestones;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;

		SimpleDateFormat formatter = new SimpleDateFormat(LoadConfig.getInstance().getDateFormat());
		startDateSt=formatter.format(this.startDate);
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;

		SimpleDateFormat formatter = new SimpleDateFormat(LoadConfig.getInstance().getDateFormat());
		endDateSt=formatter.format(this.endDate);
	}
	
	public String submit() throws Exception{		
		
		ArrayList<String> labels=new ArrayList<String>();
		
		if(label1 != null && !label1.isEmpty()){ 
			label1=label1.replaceAll(" ", "%20"); 
			labels.add(label1);
		}
		
		if(label2 != null && !label2.isEmpty()){ 
			label2=label2.replaceAll(" ", "%20"); 
			if(!labels.contains(label2)){ 
				labels.add(label2);
			}
		}
		
		if(label3 != null && !label3.isEmpty()){ 
			label3=label3.replaceAll(" ", "%20"); 
			if(!labels.contains(label3)){
				labels.add(label3);
			}
		}
		
		if(milestone != null && !milestone.isEmpty()){ 
			milestone=milestone.replaceAll(" ", "%20"); 
		}
			setIssues(issueRetriever.getIssues(project, labels, milestone, startDateSt, endDateSt));

		
		return "tablePage";
	}
	
	public boolean checkConfig() throws ConfigException{
		return !LoadConfig.getInstance().isError();
	}

	public List<GitlabIssue> getIssues() {
		return issues;
	}

	public void setIssues(List<GitlabIssue> issues) {
		this.issues = issues;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public String getDateFormat() {
		setDateFormat(LoadConfig.getInstance().getDateFormat());
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

}

