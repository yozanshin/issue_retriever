package config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.security.auth.callback.ChoiceCallback;
import javax.servlet.ServletContext;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.gitlab.api.AuthMethod;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.TokenType;

@Stateless
public class LoadConfig {

	private static final String PROPERTY_DATEFORMAT = "date_format";
	private static final String PROPERTY_ENDPOINT = "gitlab_endpoint";
	private static final String PROPERTY_TOKEN = "gitlab_token";
	private String dateFormat;
	private String gitlabUrl;
	private String gitlabToken;
	private static LoadConfig config;

	static Properties prop;

	private String errorMessage;
	private boolean error = false;

	@Resource
	private WebServiceContext ctx;
	
	public LoadConfig() {
		prop = new Properties();
		
		
		
		if(FacesContext.getCurrentInstance() == null){
			String propertiesFile;
			String contextPath;
			if(ctx != null){
				contextPath = ((ServletContext) ctx.getMessageContext().get(MessageContext.SERVLET_CONTEXT))
						.getRealPath("/");
				
			}else{
				File fil = new File(".");
				contextPath = fil.getAbsolutePath()+ File.separator + "/WebContent";
			}
			propertiesFile = contextPath + File.separator + "WEB-INF/config.properties";

			try {
				prop.load(new FileReader(propertiesFile));
			} catch (IOException e) {
				//TODO
			}	
		}else{
			ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
					.getContext();
			try (InputStream inputStream = servletContext.getResourceAsStream("/WEB-INF/config.properties")) {
				prop.load(inputStream);
			} catch (IOException e) {
				errorMessage = "Unable to load properties: " + e.getMessage();
			}
		}
		

			dateFormat = prop.getProperty(PROPERTY_DATEFORMAT);
			if (dateFormat == null || "".equals(dateFormat)) {
				error = true;
				errorMessage = "Missing date forma configuration";
			}

			gitlabToken = prop.getProperty(PROPERTY_TOKEN);
			if (gitlabToken == null || "".equals(gitlabToken)) {
				error = true;
				errorMessage = "Missing Gitlab authentication token configuration";
			}

			gitlabUrl = prop.getProperty(PROPERTY_ENDPOINT);
			if (gitlabUrl == null || "".equals(gitlabUrl)) {
				error = true;
				errorMessage = "Missing Gitlab endpoint configuration";
			}
	}

	public boolean isError() throws ConfigException {
		if(error){
			throw new ConfigException();
		}
		else{
			checkDateFormat();
			checkConnection();
		}
		
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	
	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) throws ConfigException {
		if (dateFormat != null || !"".equals(dateFormat)) {
			this.dateFormat = dateFormat;
			
			checkDateFormat();
			
			ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
					.getContext();
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			try (InputStream inputStream = servletContext.getResourceAsStream("/WEB-INF/config.properties")) {
				prop = new Properties();
				
				prop.load(inputStream);
					FileOutputStream outputStream = new FileOutputStream(externalContext.getRealPath("/WEB-INF/config.properties"));
				
				prop.setProperty(PROPERTY_DATEFORMAT, this.dateFormat);
				prop.store(outputStream, null);
				outputStream.close();
			} catch (IOException e) {
				errorMessage = "Unable to load properties: " + e.getMessage();
			}
			
			error = false;
		}
	}

	public String getGitlabUrl() {
		return gitlabUrl;
	}

	public void setGitlabUrl(String gitlabUrl) throws ConfigException {
		if (gitlabUrl != null || !"".equals(gitlabUrl)) {
			this.gitlabUrl = gitlabUrl;
			checkConnection();
			
			ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
					.getContext();
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			try (InputStream inputStream = servletContext.getResourceAsStream("/WEB-INF/config.properties")) {
				prop = new Properties();
				
				prop.load(inputStream);
					FileOutputStream outputStream = new FileOutputStream(externalContext.getRealPath("/WEB-INF/config.properties"));
				
				prop.setProperty(PROPERTY_ENDPOINT, this.gitlabUrl);
				prop.store(outputStream, null);
				outputStream.close();
			} catch (IOException e) {
				errorMessage = "Unable to update properties: " + e.getMessage();
			}
			
			error = false;
		}
	}

	public String getGitlabToken() {
		return gitlabToken;
	}

	public void setGitlabToken(String gitlabToken) throws ConfigException {
		if (gitlabToken != null || !"".equals(gitlabToken)) {
			this.gitlabToken = gitlabToken;
			checkConnection();
			
			ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
					.getContext();
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			try (InputStream inputStream = servletContext.getResourceAsStream("/WEB-INF/config.properties")) {
				prop = new Properties();
				
				prop.load(inputStream);
					FileOutputStream outputStream = new FileOutputStream(externalContext.getRealPath("/WEB-INF/config.properties"));
				
				prop.setProperty(PROPERTY_TOKEN, this.gitlabToken);
				prop.store(outputStream, null);
				outputStream.close();
			} catch (IOException e) {
				errorMessage = "Unable to update properties: " + e.getMessage();
			}
			
			error = false;
		}
	}

	public static LoadConfig getInstance() {
		if (config == null) {
			config = new LoadConfig();
		}
		return config;
	}

	private void checkDateFormat() throws ConfigException{
	
		try{
			SimpleDateFormat formatter = new SimpleDateFormat(LoadConfig.getInstance().getDateFormat());
			formatter.format(new Date());
		}catch(Exception ex){
			throw new ConfigException("Date format error");
		}
	}
	
	private void checkConnection() throws ConfigException{
	    try {
			URL myURL = new URL(this.gitlabUrl+"/api/v3/projects?private_token="+this.gitlabToken);
			HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();
		    conn.setRequestMethod("GET");
	        int responseCode = conn.getResponseCode();
	        if (responseCode == 401) {
	        	throw new ConfigException("Token error");
	        }
	    } catch (java.net.UnknownHostException e) {
	    	throw new ConfigException("HOST error");
	    } catch (IOException e) {
	    //	throw new ConfigException("Other");
		}

	}
}
