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
package org.seasar.dolteng.projects.handler.impl;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.seasar.dolteng.eclipse.Constants;
import org.seasar.dolteng.eclipse.DoltengCore;
import org.seasar.dolteng.eclipse.nls.Messages;
import org.seasar.dolteng.eclipse.util.ProgressMonitorUtil;
import org.seasar.dolteng.eclipse.util.ProjectUtil;
import org.seasar.dolteng.projects.ProjectBuilder;

/**
 * TomcatHandlerの効果を打ち消す。
 * 
 * @author daisuke
 */
@SuppressWarnings("serial")
public class TomcatCounteractHandler extends DefaultHandler {

    public TomcatCounteractHandler() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.dolteng.eclipse.template.DefaultHandler#getType()
     */
    @Override
    public String getType() {
        return "tomcatCounteract";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.dolteng.eclipse.template.DefaultHandler#handle(org.seasar.dolteng.eclipse.template.ProjectBuilder,
     *      org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void handle(ProjectBuilder builder, IProgressMonitor monitor) {
        try {
            super.handle(builder, monitor);
            monitor.setTaskName(Messages.bind(Messages.REMOVE_NATURE_OF,
                    "Tomcat"));
            if (Platform.getBundle(Constants.ID_TOMCAT_PLUGIN) != null) {
                ProjectUtil.removeNature(builder.getProjectHandle(),
                        Constants.ID_TOMCAT_NATURE);
            }
            ProgressMonitorUtil.isCanceled(monitor, 1);
        } catch (CoreException e) {
            DoltengCore.log(e);
        }
    }
}
