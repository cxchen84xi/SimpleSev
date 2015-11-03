package com.simple.svt;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

@WebServlet(name = "Log4jInit", urlPatterns = {"/Log4jInit"}, initParams = {
	    @WebInitParam(name = "test", value = "值")})
public class Log4jInit extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public void init(ServletConfig config) throws ServletException {
		
       String prefix = config.getServletContext().getRealPath("/");
        String file = config.getServletContext().getInitParameter("log4j-init-file");
        if(file!=null){
            
            System.out.println(prefix); 
            
            PropertyConfigurator.configure(prefix+file);
            Logger logger = Logger.getLogger(this.getClass());
            logger.debug("Sensu Server Logger Open.");
            
        }
    }
}
