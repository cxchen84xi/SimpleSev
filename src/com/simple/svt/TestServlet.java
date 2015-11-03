package com.simple.svt;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;

import com.simple.ann.Action;
import com.simple.ann.Param;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/test")
public class TestServlet extends SimpleServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see SimpleServlet#SimpleServlet()
     */
    public TestServlet() {
        super();
        
    }
    
    @Action()
    public String Hello(@Param(value="msg") String msg,@Param(value="request") HttpServletRequest request){
    	Object obj =  request.getServletContext().getAttribute("token");
    	System.out.println(obj);
    	if(obj==null){
    		System.out.println("Is null");
    		request.getServletContext().setAttribute("token",DateTime.now());
    	}else{
    		System.out.println("Not null");
    	
    	}
    	
    	return " (Hello invoked "+msg+")"+request.getParameter("t");
    }

}
