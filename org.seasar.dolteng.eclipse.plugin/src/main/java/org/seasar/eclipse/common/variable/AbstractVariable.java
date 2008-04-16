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
package org.seasar.eclipse.common.variable;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ClasspathVariableInitializer;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

/**
 * @author taichi
 * 
 */
public abstract class AbstractVariable extends ClasspathVariableInitializer {

	/**
	 * 
	 */
	public AbstractVariable() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.ClasspathVariableInitializer#initialize(java.lang.String)
	 */
	@Override
    public void initialize(String variable) {
		URL installLocation = getInstallLocation();
		URL local = null;
		try {
			local = FileLocator.toFileURL(installLocation);
		} catch (IOException e) {
			JavaCore.removeClasspathVariable(variable, null);
			return;
		}
		try {
			String fullPath = new File(local.getPath()).getAbsolutePath();
			JavaCore.setClasspathVariable(variable,
					Path.fromOSString(fullPath), null);
		} catch (JavaModelException e1) {
			JavaCore.removeClasspathVariable(variable, null);
		}

	}

	protected abstract URL getInstallLocation();
}
