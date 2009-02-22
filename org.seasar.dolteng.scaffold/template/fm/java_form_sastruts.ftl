package ${configs.rootpackagename}.form;


${getImports()}
import java.util.List;
import ${configs.rootpackagename}.${configs.entitypackagename}.${configs.table_capitalize};

public class ${configs.table_capitalize}Form {
	
<#list mappings as mapping>
	public String ${mapping.javaFieldName} = "";

</#list>

<#if isSelectedExisted() = true>	
	public String offset = "0";
	
	public String count = "0";
	
	public String isNextPage = "true";
	
	public String isPrevPage = "true";

	public String totalNumber = "0";
	
	public String currentPageIndex = "0";
	
	public String totalPageIndex = "0";
</#if>
}