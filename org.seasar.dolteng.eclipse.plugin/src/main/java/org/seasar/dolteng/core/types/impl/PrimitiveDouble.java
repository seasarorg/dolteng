/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.dolteng.core.types.impl;

import java.sql.Types;

import org.seasar.dolteng.core.types.TypeMapping;

/**
 * @author taichi
 * 
 */
public class PrimitiveDouble implements TypeMapping {

    private static final int[] SQL_TYPES = new int[] { Types.DOUBLE };

    private static final String[] SQL_TYPENAMES = { "DOUBLE" };

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.dolteng.core.types.TypeMapping#isPrimitive()
     */
    public boolean isPrimitive() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.dolteng.core.types.TypeMapping#getSqlType()
     */
    public int[] getSqlType() {
        return SQL_TYPES;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.dolteng.core.types.TypeMapping#getSqlTypeName()
     */
    public String[] getSqlTypeName() {
        return SQL_TYPENAMES;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.dolteng.core.types.TypeMapping#getJavaClass()
     */
    public Class getJavaClass() {
        return double.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.dolteng.core.types.TypeMapping#getJavaClassName()
     */
    public String getJavaClassName() {
        return getJavaClass().getName();
    }

}
