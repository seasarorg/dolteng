package ${configs.rootpackagename}.${configs.subapplicationrootpackagename}.${configs.table};

${getImports()}
<#if isTigerResource() = true>
import org.seasar.teeda.extension.annotation.convert.DateTimeConverter;
import org.seasar.teeda.extension.annotation.takeover.TakeOver;
import org.seasar.teeda.extension.annotation.takeover.TakeOverType;
import org.seasar.teeda.extension.annotation.validator.Required;
</#if>
import org.seasar.teeda.core.exception.AppFacesException;
import org.seasar.teeda.extension.util.LabelHelper;

import ${configs.rootpackagename}.${configs.entitypackagename}.${configs.table_capitalize};
import ${configs.rootpackagename}.${configs.entitypackagename}.${configs.meisaitable_capitalize};
import ${configs.rootpackagename}.${configs.dtopackagename}.${configs.meisaitable_capitalize}Dto;
import ${configs.rootpackagename}.${configs.subapplicationrootpackagename}.CrudType;

public class ${configs.table_capitalize}Confirm${configs.pagesuffix} extends Abstract${configs.table_capitalize}${configs.pagesuffix} {
	
	// detail information
	public int ${configs.meisaitable?uncap_first}Index;
	public ${configs.meisaitable_capitalize}Dto[] ${configs.meisaitable?uncap_first}Items;
	
	public LabelHelper labelHelper;
	
	public ${configs.table_capitalize}Confirm${configs.pagesuffix}() {
	}
	
	public Class initialize() {
		if(isComeFromList()) {
			${configs.table_capitalize} data = ${configs.table}${configs.daosuffix}.selectById(${createPkeyMethodCallArgsCopy()});
			if(data == null) {
				throw new AppFacesException("E0000001");
			}
			${configs.table}${configs.dxosuffix}.convert(data ,this);
		}
		
		if (super.crudType != CrudType.CREATE && super.crudType != CrudType.UPDATE && ${createHeadPkeyName()} != null) {
			${configs.meisaitable_capitalize}[] meisai =
				${configs.meisaitable_capitalize}Dao.selectBy${createHeadMeisaiPkeyByName()?cap_first}(${createHeadPkeyName()});
			${configs.meisaitable?uncap_first}Items = ${configs.meisaitable_capitalize}Dto.convert(meisai);
		}
		
		return null;
	}
	
	public Class prerender() {
		return null;
	}

<#if isTigerResource() = true>
	@TakeOver(type = TakeOverType.NEVER)
<#else>
	public static final String doFinish_TAKE_OVER = "type=never";
</#if>
	public Class doFinish() {
		switch(super.crudType) {
			case CrudType.CREATE:
				${configs.table}${configs.daosuffix}.insert(${configs.table}${configs.dxosuffix}.convert(this));
				for (int i = 0; i < ${configs.meisaitable?uncap_first}Items.length; i++) {
					${configs.meisaitable?uncap_first}Items[i].meisai${createHeadMeisaiPkeyByName()?cap_first} = ${createHeadPkeyName()};
					${configs.meisaitable_capitalize}Dao.insert(${configs.meisaitable?uncap_first}Items[i].convert());
				}
				break;
			case CrudType.UPDATE:
				${configs.table}${configs.daosuffix}.update(${configs.table}${configs.dxosuffix}.convert(this));
				for (int i = 0; i < ${configs.meisaitable?uncap_first}Items.length; i++) {
					${configs.meisaitable?uncap_first}Items[i].meisai${createHeadMeisaiPkeyByName()?cap_first} = ${createHeadPkeyName()};
					${configs.meisaitable_capitalize}Dao.update(${configs.meisaitable?uncap_first}Items[i].convert());
				}
				break;
			case CrudType.DELETE:
				${configs.table}${configs.daosuffix}.delete(${configs.table}${configs.dxosuffix}.convert(this));
				for (int i = 0; i < ${configs.meisaitable?uncap_first}Items.length; i++) {
					${configs.meisaitable?uncap_first}Items[i].meisai${createHeadMeisaiPkeyByName()?cap_first} = ${createHeadPkeyName()};
					${configs.meisaitable_capitalize}Dao.delete(${configs.meisaitable?uncap_first}Items[i].convert());
				}
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