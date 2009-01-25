package ${configs.rootpackagename}.${configs.subapplicationrootpackagename}.${configs.table};

${getImports()}
<#if isTigerResource() = true>
import org.seasar.teeda.extension.annotation.validator.Required;

</#if>
import org.seasar.teeda.core.exception.AppFacesException;

import ${configs.rootpackagename}.${configs.entitypackagename}.${configs.table_capitalize};
import ${configs.rootpackagename}.${configs.entitypackagename}.${configs.meisaitable_capitalize};
import ${configs.rootpackagename}.${configs.dtopackagename}.${configs.meisaitable_capitalize}Dto;
import ${configs.rootpackagename}.${configs.subapplicationrootpackagename}.CrudType;

public class ${configs.table_capitalize}Edit${configs.pagesuffix} extends Abstract${configs.table_capitalize}${configs.pagesuffix} {

	// detail information
	public int ${configs.meisaitable?uncap_first}Index;
	public ${configs.meisaitable_capitalize}Dto[] ${configs.meisaitable?uncap_first}Items;

	public ${configs.table_capitalize}Edit${configs.pagesuffix}() {
	}
	
	public Class initialize() {
		if(super.crudType == CrudType.UPDATE) {
			${configs.table_capitalize} data = ${configs.table}${configs.daosuffix}.selectById(${createPkeyMethodCallArgsCopy()});
			if(data == null) {
				throw new AppFacesException("E0000001");
			}
			${configs.table}${configs.dxosuffix}.convert(data ,this);
		}
		
		if (${createHeadPkeyName()} != null) {
			${configs.meisaitable_capitalize}[] meisai =
				${configs.meisaitable_capitalize}Dao.selectBy${createHeadMeisaiPkeyByName()?cap_first}(${createHeadPkeyName()});
			${configs.meisaitable?uncap_first}Items = ${configs.meisaitable_capitalize}Dto.convert(meisai);
		} else {
			${configs.meisaitable?uncap_first}Items = new ${configs.meisaitable?cap_first}Dto[5];
			for (int i = 0; i < ${configs.meisaitable?uncap_first}Items.length; i++) {
				${configs.meisaitable?cap_first}Dto dto = new ${configs.meisaitable?cap_first}Dto();
				//dto.meisai${createMeisaiPkeyName()?cap_first} = i + 1;
				dto.meisai${createMeisaiPkeyName()?cap_first} = ${castFromMeisaiToHead()};
				${configs.meisaitable?uncap_first}Items[i] = dto;
			}
		}
		
		return null;
	}
	
	public Class prerender() {
		return null;
	}

<#list mappings as mapping>
<#if mapping.isNullable() = false && mapping.isPrimaryKey() = false>
<#if isTigerResource() = true>
	@Required
<#else>
	public static final String ${mapping.javaFieldName}_TRequiredValidator = null;
</#if>
	public void set${mapping.javaFieldName?cap_first}(${getJavaClassName(mapping)} ${mapping.javaFieldName?lower_case}) {
		super.${mapping.javaFieldName} = ${mapping.javaFieldName?lower_case};
	}

</#if>
</#list>
	public String getIsNotCreateStyle() {
		return super.crudType == CrudType.CREATE ? "display: none;" : null;
	}
}