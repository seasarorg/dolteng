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

package org.seasar.eclipse.common.preference;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.dialogs.PropertyPage;

/**
 * @author taichi
 * 
 */
public abstract class AbstractPreferencePage extends PropertyPage implements
		IWorkbenchPreferencePage {

	protected IWorkbench workbench;

	public AbstractPreferencePage() {
		super();
	}

	public void init(IWorkbench workbench) {
		this.workbench = workbench;
	}

	public boolean isProjectPage() {
		return getProject() != null;
	}

	public IProject getProject() {
		IProject result = null;
		Object o = getElement();
		if (o instanceof IProject) {
			result = (IProject) o;
		} else if (o instanceof IAdaptable) {
			IAdaptable a = (IAdaptable) o;
			result = (IProject) a.getAdapter(IProject.class);
		}
		return result;
	}
}
