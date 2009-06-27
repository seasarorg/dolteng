package ${configs.rootpackagename}.${configs.subapplicationrootpackagename}.${configs.table};

${getDateAndNullableAndPrimaryKeyImports()}

<#if isTigerResource() = true>
import org.seasar.teeda.extension.annotation.takeover.TakeOver;
import org.seasar.teeda.extension.annotation.takeover.TakeOverType;
	<#if isMappingsContainsDate() = true>
import org.seasar.teeda.extension.annotation.convert.DateTimeConverter;
	</#if>
	<#if isMappingsContainsRequired() = true>
import org.seasar.teeda.extension.annotation.validator.Required;
	</#if>
</#if>
import org.seasar.teeda.core.exception.AppFacesException;
import org.seasar.teeda.extension.util.LabelHelper;

import ${configs.rootpackagename}.${configs.entitypackagename}.${configs.table_capitalize};
import ${configs.rootpackagename}.${configs.subapplicationrootpackagename}.CrudType;

public class ${configs.table_capitalize}Confirm${configs.pagesuffix} extends Abstract${configs.table_capitalize}${configs.pagesuffix} {
	
	public LabelHelper labelHelper;
	
	public ${configs.table_capitalize}Confirm${configs.pagesuffix}() {
	}
	
<#if isTigerResource() = true>
	public Class<?> initialize() {
<#else>
	public Class initialize() {
</#if>
		if(isComeFromList()) {
			${configs.table_capitalize} data = ${configs.table}${configs.daosuffix}.selectById(${createPkeyMethodCallArgsCopy()});
			if(data == null) {
				throw new AppFacesException("E0000001");
			}
			${configs.table}${configs.dxosuffix}.convert(data ,this);
		}
		return null;
	}
	
<#if isTigerResource() = true>
	public Class<?> prerender() {
<#else>
	public Class prerender() {
</#if>
		return null;
	}

<#if isTigerResource() = true>
	@TakeOver(type = TakeOverType.NEVER)
	public Class<?> doFinish() {
<#else>
	public static final String doFinish_TAKE_OVER = "type=never";
	public Class doFinish() {
</#if>
		switch(super.crudType) {
			case CrudType.CREATE:
				${configs.table}${configs.daosuffix}.insert(${configs.table}${configs.dxosuffix}.convert(this));
				break;
			case CrudType.UPDATE:
				${configs.table}${configs.daosuffix}.update(${configs.table}${configs.dxosuffix}.convert(this));
				break;
			case CrudType.DELETE:
				${configs.table}${configs.daosuffix}.delete(${configs.table}${configs.dxosuffix}.convert(this));
				break;
			default:
				break;
		}
		return ${configs.table_capitalize}List${configs.pagesuffix}.class;
	}
	
	public boolean isComeFromList() {
		return super.crudType == CrudType.READ || super.crudType == CrudType.DELETE;
	}

<#list mappings as mapping>
<#if mapping.isNullable() = false && mapping.isPrimaryKey() = false>
<#if isTigerResource() = true>
	@Required
<#else>
	public static final String ${mapping.javaFieldName}_TRequiredValidator = null;
</#if>
	public void set${mapping.javaFieldName?cap_first}(${getJavaClassName(mapping)} ${mapping.javaFieldName}) {
		super.${mapping.javaFieldName} = ${mapping.javaFieldName};
	}

</#if>
<#if mapping.isDate() = true>
<#if isTigerResource() = true>
	@DateTimeConverter
<#else>
	public static final String ${mapping.javaFieldName}_TDateTimeConverter = null;
</#if>
	public ${getJavaClassName(mapping)} get${mapping.javaFieldName?cap_first}() {
		return ${mapping.javaFieldName};
	}

</#if>
</#list>
	public String getJump${configs.table_capitalize}EditStyle() {
		return isComeFromList() ? "display: none;" : "";
	}

	public String getDoFinishValue() {
		return labelHelper.getLabelValue(CrudType.toString(super.crudType));
	}
}