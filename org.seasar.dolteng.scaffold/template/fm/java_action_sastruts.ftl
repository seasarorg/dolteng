package ${configs.rootpackagename}.action;

${getImports()}

import javax.persistence.NoResultException;
import org.seasar.extension.jdbc.exception.SNonUniqueResultException;

import org.apache.struts.action.ActionMessages;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.extension.jdbc.SqlLogRegistryLocator;
import org.seasar.struts.annotation.Execute;
import org.seasar.framework.beans.util.BeanMap;
import java.util.ArrayList;
import java.util.List;

import ${configs.rootpackagename}.${configs.entitypackagename}.${configs.table_capitalize};
import ${configs.rootpackagename}.${configs.servicepackagename}.${configs.table_capitalize}${configs.servicesuffix};
import ${configs.rootpackagename}.${configs.dtopackagename}.${configs.table_capitalize}${configs.dtosuffix};

public class ${configs.table_capitalize}${configs.actionsuffix} {

	@ActionForm
	public ${configs.table_capitalize}${configs.dtosuffix} ${configs.table}${configs.dtosuffix};
	
	public ${configs.table_capitalize}${configs.servicesuffix} ${configs.table}${configs.servicesuffix};
	

	@Execute(validator=false)
	public String index(){
	
		return list();
	}

	@Execute(validator=false)
	public String list(){
	
		List<${configs.table_capitalize}> list = ${configs.table}${configs.servicesuffix}.findAll();

		${configs.table}${configs.dtosuffix}.recordList = new ArrayList<${configs.table_capitalize}${configs.dtosuffix}>();
	
		for(${configs.table_capitalize} entity : list){
			
			${configs.table_capitalize}${configs.dtosuffix} dto 
					= Beans.createAndCopy(${configs.table_capitalize}${configs.dtosuffix}.class, entity)
							.dateConverter("yyyy/MM/dd",new String[]{
<#list mappings as mapping>
	<#if mapping.isDate() = true>
								"${mapping.javaFieldName}",
	</#if>						
</#list>
								})
							.execute();
							
			${configs.table}${configs.dtosuffix}.recordList.add(dto);
		}
	
		return "list.jsp";
	}
	
	@Execute(validator=false)
	public String show(){
	
		loadEntity();
	
		return "show.jsp";
	}
	
	@Execute(validator=false)
	public String edit(){
	
		loadEntity();
		
		return "edit.jsp";
	}
	
	@Execute(validator=false)
	public String create(){
		
		return "create.jsp";
	}
	
	@Execute(validator=false)
	public String delete(){
	
		${configs.table_capitalize} entity = Beans.createAndCopy(${configs.table_capitalize}.class, ${configs.table}${configs.dtosuffix})
			.execute();
		
		${configs.table}${configs.servicesuffix}.delete(entity);
		
		return list();
	}
	
	
	private void loadEntity(){
	
		BeanMap conditions = new BeanMap();
		
<#list mappings as mapping>
	<#if mapping.isPrimaryKey() = true>
		conditions.put("${mapping.javaFieldName}", ${configs.table}${configs.dtosuffix}.${mapping.javaFieldName});
	</#if>
</#list>

		List<${configs.table_capitalize}> resultList = ${configs.table}${configs.servicesuffix}.findByCondition(conditions);

		if(resultList.size() == 0){
			
			throw new NoResultException(); 
		}else if(resultList.size() > 1){
			throw new SNonUniqueResultException(SqlLogRegistryLocator.getInstance().getLast().getCompleteSql()); 
		}

		${configs.table_capitalize} entity = resultList.get(0);
		
		Beans.copy(entity, ${configs.table}${configs.dtosuffix})
			.dateConverter("yyyy/MM/dd",new String[]{
<#list mappings as mapping>
	<#if mapping.isDate() = true>
				"${mapping.javaFieldName}",
	</#if>						
</#list>
				})
			.execute();
	}
	
	
	@Execute(input="create.jsp", validate="validateInsert")
	public String insert(){
		
		${configs.table_capitalize} entity = Beans.createAndCopy(${configs.table_capitalize}.class, ${configs.table}${configs.dtosuffix})
			.execute();
		
		${configs.table}${configs.servicesuffix}.insert(entity);
		
		return list();
	}
	
	@Execute(input="create.jsp", validate="validateUpdate")
	public String update(){
		
		${configs.table_capitalize} entity = Beans.createAndCopy(${configs.table_capitalize}.class, ${configs.table}${configs.dtosuffix})
			.execute();
		
		${configs.table}${configs.servicesuffix}.update(entity);
		
		return list();
	}
	
	
	public ActionMessages validateInsert() {
	    ActionMessages errors = new ActionMessages();
	    return errors;
	}
	
	public ActionMessages validateUpdate() {
	    ActionMessages errors = new ActionMessages();
	    return errors;
	}
}