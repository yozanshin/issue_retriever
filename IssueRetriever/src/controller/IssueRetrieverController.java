package controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.gitlab.api.models.GitlabLabel;
import org.gitlab.api.models.GitlabMilestone;
import org.gitlab.api.models.GitlabProject;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import config.LoadConfig;
import logic.IssueRetrieverLocal;

@ManagedBean
@ViewScoped
public class IssueRetrieverController {

	@EJB
	private IssueRetrieverLocal issueRetriever;

	private List<GitlabProject> projects;
	private List<GitlabLabel> labels;
	private List<GitlabMilestone> milestones;

	private String project;
	private String label1;
	private String label2;
	private String label3;
	private String milestone;
	
	private Date startDate;
	private Date endDate;

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
		if (project != null && !project.equals("")){
			setLabels(getLabels(Integer.parseInt(project)));
			setMilestones(getMilestones(Integer.parseInt(project)));
		}
		else{
			setLabels(new ArrayList<GitlabLabel>());
		setMilestones(new ArrayList<GitlabMilestone>());
		}
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
		onChange();
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

	public List<GitlabMilestone> getMilestones(Integer projectId){
		try {
		return issueRetriever.getMilestones(projectId.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<GitlabMilestone>();
	}

	public String getLabel1() {
		return label1;
	}

	public void setLabel(String label1) {
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

	public List<GitlabLabel> getLabels() {
		return labels;
	}

	public void setLabels(List<GitlabLabel> labels) {
		this.labels = labels;
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

    public void onDateSelect(SelectEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat format = new SimpleDateFormat(LoadConfig.getInstance().getDateFormat());
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", format.format(event.getObject())));
    }
     
    public void click() {
        RequestContext requestContext = RequestContext.getCurrentInstance();
         
        requestContext.update("form:display");
        requestContext.execute("PF('dlg').show()");
    }

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
