package ${configs.rootpackagename}.action;

${getImports()}

import javax.annotation.Resource;

import org.apache.struts.action.ActionMessages;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import java.util.List;

import ${configs.rootpackagename}.${configs.entitypackagename}.${configs.table_capitalize};
import ${configs.rootpackagename}.${configs.servicepackagename}.${configs.table_capitalize}${configs.servicesuffix};
import ${configs.rootpackagename}.form.${configs.table_capitalize}Form;

public class ${configs.table_capitalize}${configs.actionsuffix} {

    public List<${configs.table_capitalize}> ${configs.table}Items;
    
    @ActionForm
    @Resource
    protected ${configs.table_capitalize}Form ${configs.table}Form;

    @Resource
    protected ${configs.table_capitalize}${configs.servicesuffix} ${configs.table}${configs.servicesuffix};

    @Execute(validator = false)
    public String index() {
        ${configs.table}Items = ${configs.table}${configs.servicesuffix}.findAll();
        return "list.${configs.viewtemplateextension}";
    }

    @Execute(validator = false, urlPattern = "show<#list mappings as mapping><#if mapping.isPrimaryKey() = true><#noparse>/{</#noparse>${mapping.javaFieldName}<#noparse>}</#noparse></#if></#list>")
    public String show() {
        ${configs.table_capitalize} entity = ${configs.table}${configs.servicesuffix}.findById(${createFormPkeyMethodCallArgsCopy("${configs.table}Form")});
        Beans.copy(entity, ${configs.table}Form).execute();
        return "show.${configs.viewtemplateextension}";
    }

    @Execute(validator = false, urlPattern = "edit<#list mappings as mapping><#if mapping.isPrimaryKey() = true><#noparse>/{</#noparse>${mapping.javaFieldName}<#noparse>}</#noparse></#if></#list>")
    public String edit() {
        ${configs.table_capitalize} entity = ${configs.table}${configs.servicesuffix}.findById(${createFormPkeyMethodCallArgsCopy("${configs.table}Form")});
        Beans.copy(entity, ${configs.table}Form).execute();
        return "edit.${configs.viewtemplateextension}";
    }

    @Execute(validator = false)
    public String create() {
        return "create.${configs.viewtemplateextension}";
    }

    @Execute(validator = false, urlPattern = "delete<#list mappings as mapping><#if mapping.isPrimaryKey() = true><#noparse>/{</#noparse>${mapping.javaFieldName}<#noparse>}</#noparse></#if><#if isVersionColumn(mapping) = true><#noparse>/{</#noparse>${mapping.javaFieldName}<#noparse>}</#noparse></#if></#list>", redirect = true)
    public String delete() {
        ${configs.table_capitalize} entity = Beans.createAndCopy(${configs.table_capitalize}.class, ${configs.table}Form).execute();
        ${configs.table}${configs.servicesuffix}.delete(entity);
        return "/${configs.table}/";
    }

    @Execute(input = "create.${configs.viewtemplateextension}", redirect = true)
    public String insert() {
        ${configs.table_capitalize} entity = Beans.createAndCopy(${configs.table_capitalize}.class, ${configs.table}Form).execute();
        ${configs.table}${configs.servicesuffix}.insert(entity);
        return "/${configs.table}/";
    }

    @Execute(input = "edit.${configs.viewtemplateextension}", redirect = true)
    public String update() {
        ${configs.table_capitalize} entity = Beans.createAndCopy(${configs.table_capitalize}.class, ${configs.table}Form).execute();
        ${configs.table}${configs.servicesuffix}.update(entity);
        return "/${configs.table}/";
    }
}