package ${configs.rootpackagename}.${configs.subapplicationrootpackagename}.${configs.table};

import ${configs.rootpackagename}.${configs.daopackagename}.${configs.table_capitalize}Dao;
import ${configs.rootpackagename}.${configs.entitypackagename}.${configs.table_capitalize};

public class ${configs.table_capitalize}${configs.servicesuffix}  {

	private ${configs.table_capitalize}${configs.daosuffix} ${configs.table}${configs.daosuffix};

	public ${configs.table_capitalize}${configs.servicesuffix}() {
	}

	public ${configs.table_capitalize}[] selectAll() {
		return get${configs.table_capitalize}${configs.daosuffix}().selectAll();
	}
	
	public ${configs.table_capitalize} selectById(${createPkeyMethodArgs()}) {
		return get${configs.table_capitalize}${configs.daosuffix}().selectById(${createPkeyMethodCallArgsCopy()});
	}
	
	public void insert(${configs.table_capitalize} ${configs.table}) {
		get${configs.table_capitalize}${configs.daosuffix}().insert(${configs.table});
	}

	public int update(${configs.table_capitalize} ${configs.table}) {
		return get${configs.table_capitalize}${configs.daosuffix}().update(${configs.table});
	}
	
	public void remove(${createPkeyMethodArgs()}) {
		${configs.table_capitalize} ${configs.table} = selectById(${createPkeyMethodCallArgsCopy()});
		get${configs.table_capitalize}${configs.daosuffix}().delete(${configs.table});
	}

	public ${configs.table_capitalize}${configs.daosuffix} get${configs.table_capitalize}${configs.daosuffix}() {
		return this.${configs.table}${configs.daosuffix};
	}

	public void set${configs.table_capitalize}${configs.daosuffix}(${configs.table_capitalize}${configs.daosuffix} ${configs.table}${configs.daosuffix}) {
		this.${configs.table}${configs.daosuffix} = ${configs.table}${configs.daosuffix};
	}

}