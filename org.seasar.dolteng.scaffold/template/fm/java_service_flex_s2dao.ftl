package ${configs.rootpackagename}.${configs.subapplicationrootpackagename}.${configs.table};

import ${configs.rootpackagename}.${configs.entitypackagename}.${configs.table_capitalize};

public interface ${configs.table_capitalize}${configs.servicesuffix} {

	public ${configs.table_capitalize}[] getAllItems();
	
	public ${configs.table_capitalize} selectById(${createPkeyMethodArgs()});
	
	public void insertItem(${configs.table_capitalize} ${configs.table});

	public void updateItem(${configs.table_capitalize} ${configs.table});
	
	public void deleteItem(${createPkeyMethodArgs()});
	
}