package config;

public class ConfigException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3948190902307248554L;
	
	  public ConfigException() {}

	  public ConfigException(String paramString)
	  {
		super(paramString);
	  }

	  public ConfigException(String paramString, Throwable paramThrowable)
	  {
	   super(paramString, paramThrowable);
	  }

	  public ConfigException(Throwable paramThrowable)
	  {
	   super(paramThrowable);
	  }
}
