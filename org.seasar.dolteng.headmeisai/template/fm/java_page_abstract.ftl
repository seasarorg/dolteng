package ${configs.rootpackagename}.${configs.subapplicationrootpackagename}.${configs.table};

${getImports()}
${getImportsInMeisai()}
import ${configs.rootpackagename}.${configs.daopackagename}.${configs.table_capitalize}Dao;
import ${configs.rootpackagename}.${configs.subapplicationrootpackagename}.AbstractCrudPage;
import ${configs.rootpackagename}.${configs.daopackagename}.${configs.meisaitable_capitalize}Dao;

public abstract class Abstract${configs.table_capitalize}${configs.pagesuffix} extends AbstractCrud${configs.pagesuffix} {

	protected ${configs.table_capitalize}${configs.daosuffix} ${configs.table}${configs.daosuffix};
	
	protected ${configs.table_capitalize}${configs.dxosuffix} ${configs.table}${configs.dxosuffix};

	protected ${configs.meisaitable_capitalize}${configs.daosuffix} ${configs.meisaitable?cap_first}${configs.daosuffix};
	
<#list mappings as mapping>
	private ${getJavaClassName(mapping)} ${mapping.javaFieldName};

</#list>

<#list meisaiColumnsMappings as mapping>
	private ${getMeisaiJavaClassName(mapping)} meisai${mapping.javaFieldName?cap_first};
</#list>

	public Abstract${configs.table_capitalize}${configs.pagesuffix}() {
	}

<#list mappings as mapping>
	public ${getJavaClassName(mapping)} get${mapping.javaFieldName?cap_first}() {
		return this.${mapping.javaFieldName};
	}

	public void set${mapping.javaFieldName?cap_first}(${getJavaClassName(mapping)} ${mapping.javaFieldName}) {
		this.${mapping.javaFieldName} = ${mapping.javaFieldName};
	}

</#list>

<#list meisaiColumnsMappings as mapping>
	public ${getMeisaiJavaClassName(mapping)} getMeisai${mapping.javaFieldName?cap_first}() {
		return this.meisai${mapping.javaFieldName?cap_first};
	}

	public void setMeisai${mapping.javaFieldName?cap_first}(${getMeisaiJavaClassName(mapping)} ${mapping.javaFieldName}) {
		this.meisai${mapping.javaFieldName?cap_first} = ${mapping.javaFieldName};
	}

</#list>

	public ${configs.table_capitalize}${configs.daosuffix} get${configs.table_capitalize}${configs.daosuffix}() {
		return this.${configs.table}${configs.daosuffix};
	}

	public void set${configs.table_capitalize}${configs.daosuffix}(${configs.table_capitalize}${configs.daosuffix} ${configs.table}${configs.daosuffix}) {
		this.${configs.table}${configs.daosuffix} = ${configs.table}${configs.daosuffix};
	}

	public ${configs.table_capitalize}${configs.dxosuffix} get${configs.table_capitalize}${configs.dxosuffix}() {
		return this.${configs.table}${configs.dxosuffix};
	}

	public void set${configs.table_capitalize}${configs.dxosuffix}(${configs.table_capitalize}${configs.dxosuffix} ${configs.table}${configs.dxosuffix}) {
		this.${configs.table}${configs.dxosuffix} = ${configs.table}${configs.dxosuffix};
	}

	public ${configs.meisaitable_capitalize}${configs.daosuffix} get${configs.meisaitable_capitalize}${configs.daosuffix}() {
		return this.${configs.meisaitable_capitalize}${configs.daosuffix};
	}

	public void set${configs.meisaitable_capitalize}${configs.daosuffix}(${configs.meisaitable_capitalize}${configs.daosuffix} ${configs.meisaitable}${configs.daosuffix}) {
		this.${configs.meisaitable_capitalize}${configs.daosuffix} = ${configs.meisaitable}${configs.daosuffix};
	}
}