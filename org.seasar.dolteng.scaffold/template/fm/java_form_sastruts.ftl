package ${configs.rootpackagename}.form;


${getImports()}
import java.util.List;
import ${configs.rootpackagename}.${configs.entitypackagename}.${configs.table_capitalize};

public class ${configs.table_capitalize}Form {
	
<#list mappings as mapping>
	public String ${mapping.javaFieldName};
	
</#list>
}