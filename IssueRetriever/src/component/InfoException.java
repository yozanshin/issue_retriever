package component;

public class InfoException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5168317563509985544L;
	
	  public InfoException() {}

	  public InfoException(String paramString)
	  {
		super(paramString);
	  }

	  public InfoException(String paramString, Throwable paramThrowable)
	  {
	   super(paramString, paramThrowable);
	  }

	  public InfoException(Throwable paramThrowable)
	  {
	   super(paramThrowable);
	  }

}
