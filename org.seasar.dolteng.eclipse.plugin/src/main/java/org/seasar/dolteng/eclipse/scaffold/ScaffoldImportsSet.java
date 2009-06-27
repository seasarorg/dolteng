/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.dolteng.eclipse.scaffold;

import java.util.HashSet;
import java.util.Set;

import org.seasar.dolteng.eclipse.convention.NamingUtil;
import org.seasar.dolteng.eclipse.model.EntityMappingRow;

/**
 * @author r_ikeda
 */
public class ScaffoldImportsSet {

    private final Set<String> imports = new HashSet<String>();

    public void addJpaAnnotaion(EntityMappingRow[] mappings) {
        for (EntityMappingRow row : mappings) {
            if (row.getSqlColumnName().equalsIgnoreCase(row.getJavaFieldName()) == false) {
                imports.add("javax.persistence.Column");
            }
            if (row.isPrimaryKey()) {
                imports.add("javax.persistence.Id");
                imports.add("javax.persistence.GeneratedValue");
            }
            if (isVersionColumn(row)) {
                imports.add("javax.persistence.Version");
            }
            if (row.isDate()) {
                imports.add("javax.persistence.Temporal");
                imports.add("javax.persistence.TemporalType");
            }
        }
    }

    public void addRequired(EntityMappingRow[] mappings) {
        for (EntityMappingRow row : mappings) {
            if (row.isPrimaryKey() || isVersionColumn(row)
                    || row.isNullable() == false) {
                add(row);
            }
        }
    }

    public void addDate(EntityMappingRow[] mappings) {
        for (EntityMappingRow row : mappings) {
            if (row.isDate()) {
                add(row);
            }
        }
    }

    public void addAll(EntityMappingRow[] mappings) {
        for (EntityMappingRow row : mappings) {
            add(row);
        }
    }

    public void add(EntityMappingRow row) {
        if (row == null || row.isPrimitive()) {
            return;
        }
        add(row.getJavaClassName());
    }

    public void add(String className) {
        if (className == null || className.startsWith("java.lang")) {
            return;
        }
        imports.add(className);
    }

    public String getImportsString() {
        if (imports == null || imports.isEmpty()) {
            return "";
        }
        String separator = System.getProperty("line.separator", "\n");
        StringBuilder sb = new StringBuilder();
        for (String element : imports) {
            sb.append("import ");
            sb.append(element);
            sb.append(';');
            sb.append(separator);
        }
        return sb.toString();
    }

    private boolean isVersionColumn(EntityMappingRow row) {
        return NamingUtil.isVersionNo(row.getSqlColumnName());
    }

}
