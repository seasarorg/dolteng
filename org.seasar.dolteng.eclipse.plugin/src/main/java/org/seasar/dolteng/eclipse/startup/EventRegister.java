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
package org.seasar.dolteng.eclipse.startup;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.seasar.dolteng.eclipse.marker.DIMapper;
import org.seasar.dolteng.eclipse.marker.HtmlMapper;
import org.seasar.dolteng.eclipse.marker.KuinaDaoErrorReporter;
import org.seasar.dolteng.eclipse.marker.PageMapper;
import org.seasar.dolteng.eclipse.marker.SqlMapper;
import org.seasar.dolteng.eclipse.preferences.ConventionChangeListener;

/**
 * @author taichi
 * 
 */
public class EventRegister implements IStartup {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IStartup#earlyStartup()
     */
    public void earlyStartup() {
        final IWorkbench workbench = PlatformUI.getWorkbench();
        workbench.getDisplay().asyncExec(new Runnable() {
            public void run() {
                IWorkspace workspace = ResourcesPlugin.getWorkspace();
                workspace.addResourceChangeListener(
                        new ConventionChangeListener(),
                        IResourceChangeEvent.POST_BUILD);
                workspace.addResourceChangeListener(new PageMapper(),
                        IResourceChangeEvent.POST_CHANGE);
                JavaCore.addElementChangedListener(new HtmlMapper(),
                        ElementChangedEvent.POST_CHANGE);
                JavaCore.addElementChangedListener(new DIMapper(),
                        ElementChangedEvent.POST_CHANGE);
                JavaCore.addElementChangedListener(new KuinaDaoErrorReporter(),
                        ElementChangedEvent.POST_CHANGE);
                SqlMapper sqlMapper = new SqlMapper();
                workspace.addResourceChangeListener(sqlMapper);
                JavaCore.addElementChangedListener(sqlMapper);
            }
        });
    }
}
