package ${configs.rootpackagename}.${configs.dtopackagename};

import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.Date;

public class ${configs.table_capitalize}Dto {

<#list mappings as mapping>
	private ${getJavaClassName(mapping)} ${mapping.javaFieldName}_${getDtoSuffix("${getJavaClassName(mapping)}")};

</#list>
	
	private int firstResult;
	
	private int maxResults;
	
	public ${configs.table_capitalize}Dto() {
	}

<#list mappings as mapping>
	public ${getJavaClassName(mapping)} get${mapping.javaFieldName?cap_first}_${getDtoSuffix("${getJavaClassName(mapping)}")}() {
		return this.${mapping.javaFieldName}_${getDtoSuffix("${getJavaClassName(mapping)}")};
	}

	public void set${mapping.javaFieldName?cap_first}_${getDtoSuffix("${getJavaClassName(mapping)}")}(${getJavaClassName(mapping)} ${mapping.javaFieldName?lower_case}) {
		this.${mapping.javaFieldName}_${getDtoSuffix("${getJavaClassName(mapping)}")} = ${mapping.javaFieldName?lower_case};
	}
</#list>

	public int getFirstResult() {
		return firstResult;
	}
	
	public void setFirstResult(int firstResult) {
		this.firstResult = firstResult;
	}
	
	public int getMaxResults() {
		return maxResults;
	}
	
	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}
}