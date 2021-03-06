package com.simple.svt;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.prolib.entity.ResultMsg;
import com.prolib.err.ThrowError;
import com.prolib.utils.GConvert;
import com.simple.ann.Param;
import com.simple.logs.ConsoleLog;
import com.simple.utils.GsonHelper;

/**
 * Servlet implementation class SimpleServlet
 */
@WebServlet("/SimpleServlet")
public class SimpleServlet extends ExtServlet {
	private static final long serialVersionUID = 1L;
	
	public HashMap<String,String[]> methodsMap = new HashMap<>();
	public HashMap<String,Class<?>[]> methodsList = new HashMap<>();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SimpleServlet() {
        super();
        this.log = new ConsoleLog();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
    @Override
	public void init(ServletConfig config) throws ServletException {
		buildMethods();
	}

	
	private void buildMethods(){
		if(methodsMap.size()<=0){
			Method[] methods =  this.getClass().getDeclaredMethods();
			if(methods!=null){
				for(Method method :methods){
					if(method.getAnnotations().length<=0)continue;
					
					boolean isSameAnn = false;
					for(Annotation ann: method.getAnnotations()){
						if(ann.annotationType().getSimpleName().equalsIgnoreCase("Action")){
							isSameAnn = true;
						}
					}
					if(!isSameAnn)continue;
					
					
					
					
					Parameter[] parameters = method.getParameters();
					String[] methodItem =new String[parameters.length];
					Class<?>[] methodListItem = new Class[parameters.length];
					StringBuilder builder = new StringBuilder();
					
					for(int i =0;i<parameters.length;i++){
						Parameter parameter =parameters[i];
						if(method.getParameterAnnotations().length<0)continue;
						if(method.getParameterAnnotations()[i].length<=0){
							System.out.printf("方法 %s 未添加参数\n ", method.getName()); 
							isSameAnn =false;
							break;
						}
						Param param = (Param)method.getParameterAnnotations()[i][0];  
			            //System.out.printf("Parameter name #%d: %s\n", i, param.value());  
			            
						builder.append(parameter.getType().getSimpleName() +" "+param.value()).append(", ");
						
						methodItem[i]=param.value();
						methodListItem[i]=parameter.getType();
					}
					if(!isSameAnn)continue;
					 
					System.out.println("绑定:"+method.getName()+":"+builder.toString());	
					methodsList.put(method.getName(), methodListItem);
					methodsMap.put(method.getName(), methodItem);
				}
			}//~ if
		}
		
	}
	
	
	
	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
		
		String m = request.getParameter("m");
		String callback = request.getParameter("callback");  
		//System.out.println("M:"+m);
		buildMethods();
		if(methodsMap.containsKey(m)){
			System.out.println("M2:"+m);
			
			
			
			//support jsonp
			
			PrintWriter out = response.getWriter();
			if(StringUtils.isNotBlank(callback)){
				out.print(callback + "(");
			}
			
			Method method =null;
			String[] methodItem =null;
			try {
				Class<?>[] list=  methodsList.get(m);
				 method = this.getClass().getDeclaredMethod(m, list);
				
				 methodItem = methodsMap.get(m);
				
				Object[] obj = new Object[list.length];
				
				for(int i=0;i<methodItem.length;i++){
					String key =methodItem[i];
					
					
					
					if(key.equals("req")||key.equals("request") || list[i].getSimpleName().equalsIgnoreCase("HttpServletRequest")){
						obj[i]=	request;
					}else if(key.equals("res")|| key.equals("resp")||key.equals("response")||list[i].getSimpleName().equalsIgnoreCase("HttpServletResponse")){
						obj[i]=	response;
					}else{
						if(request.getParameter(key)==null){
							System.out.println("[警告]"+key+",参数的值为空");
							continue;
						}
						
						 if(list[i].getSimpleName().equals("int")){
							 obj[i]= (int)Integer.parseInt(request.getParameter(key));
						 }else if(list[i].getSimpleName().equals("double")){
							 obj[i]=(double) Double.parseDouble(request.getParameter(key));
						 }else if(list[i].getSimpleName().equals("String")){
							 obj[i]= request.getParameter(key).toString();
						 }else{
							 obj[i]= request.getParameter(key);
						 }
						
						if(obj[i]==null){
							System.out.println(key+" null");
						}else{
							System.out.println(key+" "+obj[i].toString());
						}
					}
					
				}
				String result = "";
				if(obj.length<=0){
					result =method.invoke(this).toString();
					
				}else{
					result = method.invoke(this, obj).toString();
				}
				//System.out.println(result);
				out.print(result);
				
			} catch (NoSuchMethodException | SecurityException e) {
				ResultMsg msg = new ResultMsg();
				msg.setErrorCode(500);
				msg.setErrorMsg(e.getMessage());
				out.print(GsonHelper.getGson().toJson(msg));
				System.err.println("[异常],没有这样的方法，或者安全问题:"+m);
			} catch (IllegalAccessException e) {
				ResultMsg msg = new ResultMsg();
				msg.setErrorCode(500);
				msg.setErrorMsg(e.getMessage());
				out.print(GsonHelper.getGson().toJson(msg));
				System.err.println("[异常],方法调用出错:"+m);
				printKey(m,methodItem);
				printParams(request);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				ResultMsg msg = new ResultMsg();
				msg.setErrorCode(500);
				msg.setErrorMsg(e.getMessage());
				out.print(GsonHelper.getGson().toJson(msg));
				System.err.println("[异常],方法参数调用出错:"+m);
				printKey(m,methodItem);
				printParams(request);
				
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				ResultMsg msg = new ResultMsg();
				System.err.println("[警告],方法内部抛出异常:"+m);
				printParams(request);
				try{
					ThrowError err = (ThrowError)e.getTargetException();
					msg.setErrorCode(err.getErrorCode());
					msg.setErrorMsg(err.getMessage());
					out.print(GsonHelper.getGson().toJson(msg));
				}catch(Exception ex){
					
					msg.setErrorCode(500);
					msg.setErrorMsg(e.getMessage());
					out.print(GsonHelper.getGson().toJson(msg));
				}
				
			} catch(Exception e){
				e.printStackTrace();
				System.err.println("[异常],未知出错:"+m);
				printParams(request);
				ResultMsg msg = new ResultMsg();
				
				msg.setErrorMsg(e.getMessage());
				out.print(GsonHelper.getGson().toJson(msg));
			}finally{
				if(StringUtils.isNotBlank(callback)){
					out.print(")");
					response.setContentType("text/javascript"); 
				}
				
				out.close();
			}
		}else{
			System.out.println("[警告],不存在方法:"+m);
		}
	}
	
	private void printParams(HttpServletRequest request) throws UnsupportedEncodingException{
		Enumeration<String> paramNames = request.getParameterNames();
		StringBuilder buffer = new StringBuilder();
		String paramName = null;
        buffer.append("Param:");
        while (paramNames.hasMoreElements()) {
            paramName = paramNames.nextElement();
            buffer.append("[");
            buffer.append(paramName);
            buffer.append(":");
            String encoding = GConvert.getEncoding(request.getParameter(paramName));
            if (encoding.equals("ISO-8859-1")) {
                buffer.append(new String(request.getParameter(paramName).getBytes(encoding), "utf-8"));
            } else {
                buffer.append(request.getParameter(paramName));
            }
            //buffer.append(new String(request.getParameter(paramName).getBytes(),"iso8859-1")).append("\n");
            // buffer.append(new String(request.getParameter(paramName).getBytes(),"utf-8")).append("\n");
            // buffer.append(new String(request.getParameter(paramName).getBytes(),"gb2312")).append("\n");
            //  buffer.append(new String(request.getParameter(paramName).getBytes("iso8859-1"),"utf-8"));
            //  buffer.append(new String(request.getParameter(paramName).getBytes("utf-8"),"utf-8")).append("\n");
            buffer.append(":=>");
            buffer.append(encoding);
            buffer.append("]");
        }
        System.out.println(buffer.toString());
	}
	
	private void printKey(String medthodName,String[] methodItem){
		
		
		System.out.println("方法:"+medthodName+",参数:["+StringUtils.join(methodItem, ",")+"]");
	}

	protected String getJson(ResultMsg msg) {
	        //Output json;
	        return GsonHelper.getGson().toJson(msg);
	}
}// ~
