package ${configs.rootpackagename}.${configs.servicepackagename};

import org.seasar.extension.jdbc.service.S2AbstractService;

import ${configs.rootpackagename}.${configs.entitypackagename}.${configs.table_capitalize};

public class ${configs.table_capitalize}${configs.servicesuffix} extends AbstractService<${configs.table_capitalize}> {

    public ${configs.table_capitalize} findById(${createPkeyMethodArgs()}) {
        return select().id(${createPkeyMethodCallArgsCopy()}).getSingleResult();
    }
}