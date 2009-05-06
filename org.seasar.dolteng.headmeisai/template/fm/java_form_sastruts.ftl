package ${configs.rootpackagename}.form;

${getImports()}
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ${configs.rootpackagename}.${configs.entitypackagename}.${configs.table_capitalize};
import ${configs.rootpackagename}.${configs.entitypackagename}.${configs.meisaitable_capitalize};

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















	public List<Map<String, Object>> ${configs.meisaitable}Items = new ArrayList<Map<String, Object>>();
	
	public void initializeMeisai() {
		for (int i = 0; i < 5; i++) {
			Map<String, Object> m = new HashMap<String, Object>();
<#list meisaiColumnsMappings as mapping>
			m.put("${mapping.javaFieldName}", "");
</#list>
			${configs.meisaitable}Items.add(m);
		}
	}
	
	public void initializeMeisai(List<${configs.meisaitable_capitalize}> ${configs.meisaitable}List) {
		for (int i = 0; i < ${configs.meisaitable}List.size(); i++) {
			Map<String, Object> m = new HashMap<String, Object>();
<#list meisaiColumnsMappings as mapping>
<#if mapping.isDate() = true>
			if (${configs.meisaitable}List.get(i).${mapping.javaFieldName} != null) {
				m.put("${mapping.javaFieldName}", ${configs.meisaitable}List.get(i).${mapping.javaFieldName}.
						toString().replace('-', '/'));
			}
<#else>
			m.put("${mapping.javaFieldName}", ${configs.meisaitable}List.get(i).${mapping.javaFieldName});
</#if>
</#list>
			${configs.meisaitable}Items.add(m);
		}
	}
}