/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.eclipse.common.util;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.seasar.eclipse.common.util.LogUtil;
import org.seasar.framework.util.StringUtil;

/**
 * @author taichi
 * 
 */
public class LaunchUtil {

    public static IProject getProject(ILaunch launch) {
        IProject result = null;
        try {
            ILaunchConfiguration config = launch.getLaunchConfiguration();
            String name = config.getAttribute(
                    IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, "");
            if (StringUtil.isEmpty(name) == false) {
                IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
                result = root.getProject(name);
            }
        } catch (CoreException e) {
            LogUtil.log(ResourcesPlugin.getPlugin(), e);
        }
        return result;
    }
}
