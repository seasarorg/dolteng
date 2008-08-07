package ${configs.rootpackagename}.${configs.servicepackagename};

import org.seasar.extension.jdbc.service.S2AbstractService;

public abstract class AbstractService<ENTITY> extends S2AbstractService<ENTITY> {

    public ENTITY findById(Object... idProperties) {
        return select().id(idProperties).getSingleResult();
    }
}