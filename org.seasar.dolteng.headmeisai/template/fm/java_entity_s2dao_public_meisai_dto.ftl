package ${configs.rootpackagename}.${configs.dtopackagename};

<#if isTigerResource() = true>
import org.seasar.dao.annotation.tiger.Bean;
import org.seasar.dao.annotation.tiger.Id;
import org.seasar.dao.annotation.tiger.IdType;
</#if>
${getImports()}

import java.io.Serializable;
import ${configs.rootpackagename}.${configs.entitypackagename}.${configs.meisaitable_capitalize};


public class ${configs.meisaitable_capitalize}Dto implements Serializable {
<#list meisaiColumnsMappings as mapping>
	public ${getJavaClassName(mapping)} meisai${mapping.javaFieldName?cap_first};

</#list>
	public ${configs.meisaitable_capitalize} convert() {
		${configs.meisaitable_capitalize} meisai = new ${configs.meisaitable_capitalize}();
		
		<#list meisaiColumnsMappings as mapping>
			meisai.${mapping.javaFieldName} = meisai${mapping.javaFieldName?cap_first};
		</#list>
		
		return meisai;
	}
	
	public static ${configs.meisaitable_capitalize}Dto[] convert(${configs.meisaitable_capitalize}[] meisai) {
		${configs.meisaitable_capitalize}Dto[] dto = new ${configs.meisaitable_capitalize}Dto[meisai.length];
		for (int i = 0; i < meisai.length; i++) {
			${configs.meisaitable_capitalize}Dto tmp = new ${configs.meisaitable_capitalize}Dto();
			<#list meisaiColumnsMappings as mapping>
			tmp.meisai${mapping.javaFieldName?cap_first} = meisai[i].${mapping.javaFieldName};
			</#list>
			dto[i] = tmp;
		}
		return dto;
	}
}