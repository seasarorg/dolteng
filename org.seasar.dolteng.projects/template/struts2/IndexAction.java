package ${packageName}.action;

import org.apache.struts2.config.Result;
import org.apache.struts2.views.freemarker.FreemarkerResult;

import com.opensymphony.xwork2.ActionSupport;

@Result(type = FreemarkerResult.class, value = "index.ftl")
public class IndexAction extends ActionSupport {
	public String getMessage(){
		return "Seasar2";
	}
}
