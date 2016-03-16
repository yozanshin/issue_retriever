package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

public class LoadConfig {

	private static final String PROPERTY_DATEFORMAT = "date_format";
	private static final String PROPERTY_ENDPOINT = "gitlab_endpoint";
	private static final String PROPERTY_TOKEN = "gitlab_token";
	private static String DATE_FORMAT = "dd/MM/yyyy";
	private static String GITLAB_URL;
	private static String GITLAB_TOKEN;
	private static LoadConfig config;
	static Properties prop;

	private static String errorMessage;
	private static boolean error = false;
	
	static {
		prop = new Properties();
		ServletContext servletContext = (ServletContext) FacesContext
			    .getCurrentInstance().getExternalContext().getContext();
		try (InputStream inputStream = servletContext.getResourceAsStream("/WEB-INF/config.properties")) {

			prop.load(inputStream);

			DATE_FORMAT = prop.getProperty(PROPERTY_DATEFORMAT);
			if (DATE_FORMAT == null || "".equals(DATE_FORMAT)) {
				error = true;
				errorMessage = "Missing date forma configuration";
			}

			GITLAB_TOKEN = prop.getProperty(PROPERTY_TOKEN);
			if (GITLAB_TOKEN == null || "".equals(GITLAB_TOKEN)) {
				error = true;
				errorMessage = "Missing Gitlab authentication token configuration";
			}

			GITLAB_URL = prop.getProperty(PROPERTY_ENDPOINT);
			if (GITLAB_URL == null || "".equals(GITLAB_URL)) {
				error = true;
				errorMessage = "Missing Gitlab endpoint configuration";
			}

		} catch (IOException e) {
			// log.severe("Unable to load properties: " + e.getMessage());
		}
	}

	public static boolean isError() {
		return error;
	}

	public static void setError(boolean error) {
		LoadConfig.error = error;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public static LoadConfig getConfig() {
		if (config == null) {
			config = new LoadConfig();
		}

		return config;
	}

	public String getDateFormat() {
		String format = prop.getProperty(PROPERTY_DATEFORMAT);
		if (format == null || "".equals(format)) {
			// errorMessage = "Missing date forma configuration";
			return DATE_FORMAT;
		}
		return format;
	}

	public String getGitLabUrl() {
		GITLAB_URL = prop.getProperty(PROPERTY_ENDPOINT);
		if (GITLAB_URL == null || "".equals(GITLAB_URL)) {
			// errorMessage = "Missing Gitlab endpoint configuration";
		}
		return GITLAB_URL;
	}

	public String getGitLabToken() {
		GITLAB_TOKEN = prop.getProperty(PROPERTY_TOKEN);
		if (GITLAB_TOKEN == null || "".equals(GITLAB_TOKEN)) {
			// errorMessage = "Missing Gitlab authentication token
			// configuration";
		}
		return GITLAB_TOKEN;
	}

}
