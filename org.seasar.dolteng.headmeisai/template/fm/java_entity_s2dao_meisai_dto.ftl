package ${configs.rootpackagename}.${configs.dtopackagename};

<#if isTigerResource() = true>
import org.seasar.dao.annotation.tiger.Bean;
import org.seasar.dao.annotation.tiger.Id;
import org.seasar.dao.annotation.tiger.IdType;
</#if>
${getImportsInMeisai()}

import java.io.Serializable;
import ${configs.rootpackagename}.${configs.entitypackagename}.${configs.meisaitable_capitalize};


public class ${configs.meisaitable_capitalize}Dto implements Serializable {
<#list meisaiColumnsMappings as mapping>
	private ${getMeisaiJavaClassName(mapping)} meisai${mapping.javaFieldName?cap_first};

</#list>
	public ${configs.meisaitable_capitalize} convert() {
		${configs.meisaitable_capitalize} meisai = new ${configs.meisaitable_capitalize}();
		
		<#list meisaiColumnsMappings as mapping>
		meisai.set${mapping.javaFieldName?cap_first}(meisai${mapping.javaFieldName?cap_first});
		</#list>
		
		return meisai;
	}
	
	public static ${configs.meisaitable_capitalize}Dto[] convert(${configs.meisaitable_capitalize}[] meisai) {
		${configs.meisaitable_capitalize}Dto[] dto = new ${configs.meisaitable_capitalize}Dto[meisai.length];
		for (int i = 0; i < meisai.length; i++) {
			${configs.meisaitable_capitalize}Dto tmp = new ${configs.meisaitable_capitalize}Dto();
			<#list meisaiColumnsMappings as mapping>
			tmp.meisai${mapping.javaFieldName?cap_first} = meisai[i].get${mapping.javaFieldName?cap_first}();
			</#list>
			dto[i] = tmp;
		}
		return dto;
	}

<#list meisaiColumnsMappings as mapping>
	public ${getMeisaiJavaClassName(mapping)} getMeisai${mapping.javaFieldName?cap_first}() {
		return this.meisai${mapping.javaFieldName?cap_first};
	}

	public void setMeisai${mapping.javaFieldName?cap_first}(${getMeisaiJavaClassName(mapping)} ${mapping.javaFieldName}) {
		this.meisai${mapping.javaFieldName?cap_first} = ${mapping.javaFieldName};
	}

</#list>
}
