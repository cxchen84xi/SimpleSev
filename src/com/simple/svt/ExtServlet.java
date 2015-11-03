package com.simple.svt;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.prolib.C;
import com.prolib.G;
import com.simple.logs.NLog;

public abstract class ExtServlet extends HttpServlet {
	public NLog log=null;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request,response);
	}
	
	protected abstract void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	
	protected String getCookie(HttpServletRequest request,String name){
		Cookie[] cookies = request.getCookies();
		if(cookies!=null){
			for(Cookie cookie:cookies){
				if(StringUtils.equalsIgnoreCase(cookie.getName(),name)){
					return cookie.getValue();
				}
			}
		}
		
		return StringUtils.EMPTY;
	}
	protected void setCookie(HttpServletRequest request,HttpServletResponse response,String name,String value){
		String host=request.getHeader("host");
		
		 Cookie  cookie = new Cookie(name,value);
		 cookie.setDomain(host);
		 cookie.setMaxAge(93312000);//
		 response.addCookie(cookie);
	}
	
	protected void gotoPage(HttpServletRequest request,HttpServletResponse response,String pageName) throws ServletException, IOException{
		RequestDispatcher dispatcher =request.getRequestDispatcher(pageName);
		
		dispatcher.forward(request, response);
	}
	
	
}//~
