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
package org.seasar.dolteng.eclipse.util;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.seasar.dolteng.eclipse.DoltengCore;
import org.seasar.framework.convention.NamingConvention;

/**
 * @author taichi
 * 
 */
public class NamingConventionUtil {

    public static IType fromComponentNameToType(NamingConvention nc,
            String componentName, IJavaProject project) {
        ClassLoader current = Thread.currentThread().getContextClassLoader();
        try {
            ClassLoader loader = new JavaProjectClassLoader(project);
            Thread.currentThread().setContextClassLoader(loader);
            Class clazz = nc.fromComponentNameToClass(componentName);
            if (clazz != null) {
                String name = clazz.getName();
                return project.findType(nc.toInterfaceClassName(name));
            }
        } catch (CoreException e) {
            DoltengCore.log(e);
        } finally {
            Thread.currentThread().setContextClassLoader(current);
        }
        return null;
    }

}
