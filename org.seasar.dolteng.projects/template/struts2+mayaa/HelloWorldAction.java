package ${packageName}.action;

import org.apache.struts2.config.Result;
import org.seasar.mayaa.struts2.MayaaResult;

import com.opensymphony.xwork2.ActionSupport;

@Result(type = MayaaResult.class, value = "helloWorld.html")
public class HelloWorldAction extends ActionSupport {
	public String getMessage(){
		return "Seasar2";
	}
}
