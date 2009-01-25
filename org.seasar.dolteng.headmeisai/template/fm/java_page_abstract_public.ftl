package ${configs.rootpackagename}.${configs.subapplicationrootpackagename}.${configs.table};

${getImports()}
${getImportsInMeisai()}
import ${configs.rootpackagename}.${configs.daopackagename}.${configs.table_capitalize}Dao;
import ${configs.rootpackagename}.${configs.subapplicationrootpackagename}.AbstractCrudPage;
import ${configs.rootpackagename}.${configs.daopackagename}.${configs.meisaitable_capitalize}Dao;

public abstract class Abstract${configs.table_capitalize}${configs.pagesuffix} extends AbstractCrud${configs.pagesuffix} {

	public ${configs.table_capitalize}${configs.daosuffix} ${configs.table}${configs.daosuffix};
	
	public ${configs.table_capitalize}${configs.dxosuffix} ${configs.table}${configs.dxosuffix};

	public ${configs.meisaitable_capitalize}${configs.daosuffix} ${configs.meisaitable?cap_first}${configs.daosuffix};
	
<#list mappings as mapping>
	public ${getJavaClassName(mapping)} ${mapping.javaFieldName};

</#list>



<#list meisaiColumnsMappings as mapping>
	public ${getJavaClassName(mapping)} meisai${mapping.javaFieldName?cap_first};
</#list>



	public Abstract${configs.table_capitalize}${configs.pagesuffix}() {
	}
}