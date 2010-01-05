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
package org.seasar.dolteng.projects.model.dicon;

import static org.seasar.dolteng.projects.Constants.ATTR_COMPONENT_CLASS;
import static org.seasar.dolteng.projects.Constants.ATTR_COMPONENT_NAME;
import static org.seasar.dolteng.projects.Constants.TAG_COMPONENT;

import org.eclipse.core.runtime.IProgressMonitor;
import org.seasar.dolteng.eclipse.util.ProgressMonitorUtil;
import org.seasar.framework.util.ArrayMap;

/**
 * diconファイルのモデル
 * 
 * @author daisuke
 */
@SuppressWarnings("serial")
public class DiconModel extends DiconElement {

    private final String DICON_OPEN = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + NL
            + "<!DOCTYPE components PUBLIC \"-//SEASAR//DTD S2Container 2.4//EN\" "
            + NL
            + "\t\"http://www.seasar.org/dtd/components24.dtd\">"
            + NL
            + "<components>";

    private final String DICON_CLOSE = NL + "</components>" + NL;

    @SuppressWarnings("unused")
    private String diconName;

    public DiconModel(String diconName) {
        super("components", null, null);
        this.diconName = diconName;
    }

    @SuppressWarnings("unchecked")
    public DiconElement getComponent(final String componentName,
            final String clazz) {
        for (DiconElement child : children) {
            if (TAG_COMPONENT.equals(child.getTag())
                    && componentName.equals(child.getAttributeMap().get(
                            ATTR_COMPONENT_NAME))) {
                return child;
            }
        }

        ArrayMap map = new ArrayMap();
        map.put(ATTR_COMPONENT_NAME, componentName);
        map.put(ATTR_COMPONENT_CLASS, clazz);
        
        DiconElement created = new DiconElement("component", map);
        appendChild(created);
        return created;
    }

    @Override
    public String buildElement(int indent, IProgressMonitor monitor) {
        StringBuilder sb = new StringBuilder();
        sb.append(DICON_OPEN);

        for (DiconElement component : children) {
            sb.append(component.buildElement(indent + 1, monitor));
            ProgressMonitorUtil.isCanceled(monitor, 1);
        }

        sb.append(DICON_CLOSE);
        return sb.toString();
    }

    @Override
    public int compareTo(DiconElement o) {
        if (o instanceof DiconModel) {
            throw new RuntimeException();
        }
        return super.compareTo(o);
    }

    @Override
    public String toString() {
        return children.toString();
    }
}
