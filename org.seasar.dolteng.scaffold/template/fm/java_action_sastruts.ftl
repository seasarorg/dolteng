package ${configs.rootpackagename}.action;

${getImports()}

import javax.annotation.Resource;

import org.apache.struts.action.ActionMessages;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import java.util.List;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.where.SimpleWhere;

import ${configs.rootpackagename}.${configs.entitypackagename}.${configs.table_capitalize};
import ${configs.rootpackagename}.${configs.servicepackagename}.${configs.table_capitalize}${configs.servicesuffix};
import ${configs.rootpackagename}.form.${configs.table_capitalize}Form;

public class ${configs.table_capitalize}${configs.actionsuffix} {

<#if isSelectedExisted() = true>
    private Integer limit = 5;

    private Integer count;
</#if>

    public List<${configs.table_capitalize}> ${configs.table}Items;
    
    @ActionForm
    @Resource
    protected ${configs.table_capitalize}Form ${configs.table}Form;

    @Resource
    protected ${configs.table_capitalize}${configs.servicesuffix} ${configs.table}${configs.servicesuffix};

    public JdbcManager jdbcManager;

    @Execute(validator = false)
    public String index() {
<#if isSelectedExisted() = false>
        ${configs.table}Items = ${configs.table}${configs.servicesuffix}.findAll();
<#else>
        SimpleWhere swh = new SimpleWhere()
        <#list selectedColumnsMappings as condition>
          <#if getJavaClassName(condition) = "String">
            .like("${condition.javaFieldName}", ${configs.table}Form.${condition.javaFieldName}+"%")
          <#elseif getJavaClassName(condition) = "Integer" || getJavaClassName(condition) = "BigDecimal">
            .ge("${condition.javaFieldName}", ${configs.table}Form.${condition.javaFieldName} != null && ${configs.table}Form.${condition.javaFieldName}.length() > 0 ? ${configs.table}Form.${condition.javaFieldName} : 0)
          <#elseif getJavaClassName(condition) = "Date" || getJavaClassName(condition) = "Timestamp">
            .ge("${condition.javaFieldName}", ${configs.table}Form.${condition.javaFieldName} != null && ${configs.table}Form.${condition.javaFieldName}.length() > 0 ? ${configs.table}Form.${condition.javaFieldName} : "1900/01/01")
          </#if>
        </#list>
        ;
        
        ${configs.table}Items = jdbcManager.from(${configs.table_capitalize}.class).where(swh)
                                  .orderBy("${orderbyStringColumn}")
                                  .getResultList();
        count = ${configs.table}Items.size();
        ${configs.table}Form.count = count.toString();
        ${configs.table}Form.totalNumber = count.toString();
        ${configs.table}Form.totalPageIndex = String.valueOf(count/limit);
        if (Integer.valueOf(${configs.table}Form.totalNumber)%limit != 0)
        	${configs.table}Form.totalPageIndex = String.valueOf(Integer.valueOf(${configs.table}Form.totalPageIndex)+1);
        ${configs.table}Form.currentPageIndex = String.valueOf(Integer.valueOf(${configs.table}Form.offset)/limit+1);
        
        ${configs.table}Items = jdbcManager.from(${configs.table_capitalize}.class).where(swh)
                                  .orderBy("${orderbyStringColumn}")
                                  .limit(limit).offset(Integer.valueOf(${configs.table}Form.offset))
                                  .getResultList();        
        
        if (Long.valueOf(${configs.table}Form.offset) + limit < count) {
          ${configs.table}Form.isNextPage = "true";
        } else {
          ${configs.table}Form.isNextPage = "false";
        }
        if (0 <= Long.valueOf(${configs.table}Form.offset) - limit) {
          ${configs.table}Form.isPrevPage = "true";
        } else {
          ${configs.table}Form.isPrevPage = "false";
        }
</#if>

        return "list.${configs.viewtemplateextension}";
    }

<#if isSelectedExisted() = true>
    @Execute(validator = false)
    public String retrieve() {
        return index();
    }

    @Execute(validator = false)
    public String nextPage() {
        Integer loffset = Integer.valueOf(${configs.table}Form.offset);
        loffset += limit;
        ${configs.table}Form.offset = loffset.toString();
        return index();
    }

    @Execute(validator = false)
    public String lastPage() {
        count = Integer.valueOf(${configs.table}Form.count);
        Integer loffset;
        if (count%limit == 0) {
        	loffset = count/limit*limit - limit;
        } else {
        	loffset = count/limit*limit;
        }
        ${configs.table}Form.offset = loffset.toString();
        return index();
    }

    @Execute(validator = false)
    public String prevPage() {
        Integer loffset = Integer.valueOf(${configs.table}Form.offset);
        loffset -= limit;
        ${configs.table}Form.offset = loffset.toString();
        return index();
    }
    
    @Execute(validator = false)
    public String firsPage() {
        ${configs.table}Form.offset = "0";
        return index();
    }
</#if>




    @Execute(validator = false, urlPattern = "show<#list mappings as mapping><#if mapping.isPrimaryKey() = true><#noparse>/{</#noparse>${mapping.javaFieldName}<#noparse>}</#noparse></#if></#list>")
    public String show() {
        ${configs.table_capitalize} entity = ${configs.table}${configs.servicesuffix}.findById(${createFormPkeyMethodCallArgsCopy("${configs.table}Form")});
        Beans.copy(entity, ${configs.table}Form).dateConverter("yyyy-MM-dd").execute();
        return "show.${configs.viewtemplateextension}";
    }

    @Execute(validator = false, urlPattern = "edit<#list mappings as mapping><#if mapping.isPrimaryKey() = true><#noparse>/{</#noparse>${mapping.javaFieldName}<#noparse>}</#noparse></#if></#list>")
    public String edit() {
        ${configs.table_capitalize} entity = ${configs.table}${configs.servicesuffix}.findById(${createFormPkeyMethodCallArgsCopy("${configs.table}Form")});
        Beans.copy(entity, ${configs.table}Form).dateConverter("yyyy-MM-dd").execute();
        return "edit.${configs.viewtemplateextension}";
    }

    @Execute(validator = false)
    public String create() {
        return "create.${configs.viewtemplateextension}";
    }

    @Execute(validator = false, urlPattern = "delete<#list mappings as mapping><#if mapping.isPrimaryKey() = true><#noparse>/{</#noparse>${mapping.javaFieldName}<#noparse>}</#noparse></#if><#if isVersionColumn(mapping) = true><#noparse>/{</#noparse>${mapping.javaFieldName}<#noparse>}</#noparse></#if></#list>", redirect = true)
    public String delete() {
        ${configs.table_capitalize} entity = Beans.createAndCopy(${configs.table_capitalize}.class, ${configs.table}Form).dateConverter("yyyy-MM-dd").execute();
        ${configs.table}${configs.servicesuffix}.delete(entity);
        return "/${configs.table}/";
    }

    @Execute(input = "create.${configs.viewtemplateextension}", redirect = true)
    public String insert() {
        ${configs.table_capitalize} entity = Beans.createAndCopy(${configs.table_capitalize}.class, ${configs.table}Form).dateConverter("yyyy-MM-dd").execute();
        ${configs.table}${configs.servicesuffix}.insert(entity);
        return "/${configs.table}/";
    }

    @Execute(input = "edit.${configs.viewtemplateextension}", redirect = true)
    public String update() {
        ${configs.table_capitalize} entity = Beans.createAndCopy(${configs.table_capitalize}.class, ${configs.table}Form).dateConverter("yyyy-MM-dd").execute();
        ${configs.table}${configs.servicesuffix}.update(entity);
        return "/${configs.table}/";
    }
}