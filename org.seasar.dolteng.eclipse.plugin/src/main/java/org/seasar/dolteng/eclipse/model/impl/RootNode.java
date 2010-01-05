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

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.swt.graphics.Image;
import org.seasar.dolteng.eclipse.action.ActionRegistry;
import org.seasar.dolteng.eclipse.action.ConnectionConfigAction;
import org.seasar.dolteng.eclipse.action.DeleteConnectionConfigAction;
import org.seasar.dolteng.eclipse.nls.Images;
import org.seasar.dolteng.eclipse.nls.Labels;

/**
 * @author taichi
 * 
 */
public class RootNode extends AbstractNode {

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.dolteng.ui.eclipse.models.ContentDescriptor#getText()
     */
    public String getText() {
        return Labels.PLUGIN_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.dolteng.ui.eclipse.models.ContentDescriptor#getImage()
     */
    public Image getImage() {
        return Images.PLUGIN_IMAGE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.dolteng.ui.eclipse.models.impl.AbstractLeaf#doubleClick(org.seasar.dolteng.ui.eclipse.actions.ActionRegistry)
     */
    @Override
    public void doubleClick(ActionRegistry registry) {
        registry.run(ConnectionConfigAction.ID);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.dolteng.ui.eclipse.models.impl.AbstractLeaf#fillContextMenu(org.eclipse.jface.action.IMenuManager,
     *      org.seasar.dolteng.ui.eclipse.actions.ActionRegistry)
     */
    @Override
    public void fillContextMenu(IMenuManager manager, ActionRegistry registry) {
        manager.add(registry.find(ConnectionConfigAction.ID));
        manager.add(registry.find(DeleteConnectionConfigAction.ID));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.dolteng.ui.eclipse.models.impl.AbstractNode#expanded(org.seasar.dolteng.ui.eclipse.actions.ActionRegistry)
     */
    @Override
    public void expanded(AbstractTreeViewer viewer, ActionRegistry registry) {
    }

}
