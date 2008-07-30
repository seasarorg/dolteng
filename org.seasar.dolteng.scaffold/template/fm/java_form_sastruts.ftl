package ${configs.rootpackagename}.${configs.dtopackagename};


${getImports()}
import java.util.List;
import ${configs.rootpackagename}.${configs.entitypackagename}.${configs.table_capitalize};

public class ${configs.table_capitalize}${configs.dtosuffix} {

	public List<${configs.table_capitalize}${configs.dtosuffix}> recordList;
	
<#list mappings as mapping>
	public String ${mapping.javaFieldName};
	
</#list>
}