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
package org.seasar.dolteng.eclipse.model.impl;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.seasar.dolteng.eclipse.model.ColumnDescriptor;
import org.seasar.dolteng.eclipse.model.PageMappingRow;
import org.seasar.dolteng.eclipse.nls.Labels;
import org.seasar.framework.util.ClassUtil;

/**
 * @author taichi
 * 
 */
public class SrcFieldNameColumn implements ColumnDescriptor {

    private static final String NAME = ClassUtil
            .getShortClassName(SrcFieldNameColumn.class);

    private CellEditor editor;

    public SrcFieldNameColumn(Table table) {
        this.editor = new TextCellEditor(table);
        TableColumn column = new TableColumn(table, SWT.NONE);
        column.setText(Labels.COLUMN_SRC_FIELD);
        column.setWidth(100);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.dolteng.ui.eclipse.models.ColumnDescriptor#getName()
     */
    public String getName() {
        return NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.dolteng.ui.eclipse.models.ColumnDescriptor#getCellEditor()
     */
    public CellEditor getCellEditor() {
        return this.editor;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.dolteng.ui.eclipse.models.ColumnDescriptor#getText(java.lang.Object)
     */
    public String getText(Object element) {
        if (element instanceof PageMappingRow) {
            PageMappingRow row = (PageMappingRow) element;
            return row.getSrcFieldName();
        }
        return "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.dolteng.ui.eclipse.models.ColumnDescriptor#getImage(java.lang.Object)
     */
    public Image getImage(Object element) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.dolteng.ui.eclipse.models.ColumnDescriptor#getValue(java.lang.Object)
     */
    public Object getValue(Object element) {
        return getText(element);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.dolteng.ui.eclipse.models.ColumnDescriptor#setValue(java.lang.Object,
     *      java.lang.Object)
     */
    public void setValue(Object element, Object value) {
        if (element instanceof PageMappingRow && value != null) {
            PageMappingRow row = (PageMappingRow) element;
            row.setSrcFieldName(value.toString());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.dolteng.ui.eclipse.models.ColumnDescriptor#canModify()
     */
    public boolean canModify() {
        return false;
    }

}
