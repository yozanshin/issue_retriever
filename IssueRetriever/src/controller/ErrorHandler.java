package controller;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ErrorHandler")
public class ErrorHandler  extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        processError(request, response);
    }
 
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        processError(request, response);
    }
 
    private void processError(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        // Analyze the servlet exception
        Throwable throwable = (Throwable) request
                .getAttribute("javax.servlet.error.exception");
        String servletName = (String) request
                .getAttribute("javax.servlet.error.servlet_name");
        if (servletName == null) {
            servletName = "Unknown";
        }
        String requestUri = (String) request
                .getAttribute("javax.servlet.error.request_uri");
        if (requestUri == null) {
            requestUri = "Unknown";
        }
         
        // Set response content type
          response.setContentType("text/html");
         
          PrintWriter out = response.getWriter();
          
          if(throwable.getMessage().contains("Token error")){
        	  out.write("<html><head><title>Error in configuration file</title></head><body>");
        	  out.write("<h3>Error Details</h3>");
              out.write("<strong>Token Used is incorrect, you need to change it</strong>");
              out.write("<br>");
	          out.write("<a href=\"configureProperties.xhtml?toConfig=token\"><strong>Change it now</strong></a>");
          }
          else if(throwable.getMessage().contains("HOST error")){
        	  out.write("<html><head><title>Error in configuration file</title></head><body>");
        	  out.write("<h3>Error Details</h3>");
              out.write("<strong>HOST Used is incorrect, you need to change it</strong>");
              out.write("<br>");
	          out.write("<a href=\"configureProperties.xhtml?toConfig=url\"><strong>Change it now</strong></a>");
          }
          else if(throwable.getMessage().contains("Date format error")){
        	  out.write("<html><head><title>Error in configuration file</title></head><body>");
        	  out.write("<h3>Error Details</h3>");
              out.write("<strong>Date format used is incorrect, you need to change it</strong>");
              out.write("<br>");
	          out.write("<a href=\"configureProperties.xhtml?toConfig=date\"><strong>Change it now</strong></a>");
          }
          else if(throwable.getMessage().contains("Other")){
        	  out.write("<html><head><title>Unknow config error in configuration file</title></head><body>");
        	  out.write("<h3>Error Details</h3>");
              out.write("<strong>Unable to configure properly, please configure manually file config.properties located in standalone directoty.</strong>");
              out.write("<br>");
          }
          else if(throwable.getMessage().contains("ConfigException")){
        	  out.write("<html><head><title>Error in configuration file</title></head><body>");
        	  out.write("<h3>Error Details</h3>");
              out.write("<strong>You need to reconfigure file config.properties</strong>");
              out.write("<br>");
	          out.write("<a href=\"configureProperties.xhtml?toConfig=full\"><strong>Configure now</strong></a>");
          }
          
          else{
	          out.write("<html><head><title>Fail/Error Details</title></head><body>");
	          if(throwable.getMessage().contains("Bad date range")){
            	  out.write("<h3>Error in range of dates</h3>");
            	  out.write("<strong>End date before start date, please check date range</strong>");	  
	          }else if(throwable.getMessage().contains("Unable to get issues")){
            	  out.write("<h3>Unable to get issues</h3>");
            	  out.write("<strong>"+throwable.getMessage().split(":")[2]+"</strong>");	          	  
	          }else{
	              out.write("<h3>Unable to complete your request.</h3>");
	              out.write("<li>Due to conflicts was impossible to complete your request.</li>");
	              out.write("<li>Please, redefine your search or try to use less restrictive parameters.</li>");
	              out.write("</ul>");
	          }
	           
	          out.write("<br><br>");
	          out.write("<a href=\"index.xhtml\">Home Page</a>");
	          out.write("</body></html>");
          }
    }
}
