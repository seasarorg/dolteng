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
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

/**
 * @author taichi
 * 
 */
public class PreferenceStoreUtil {
	public static ScopedPreferenceStore getCascadingStore(String qualifier,
			IProject project) {
		IScopeContext[] ctxs = null;
		if (project != null) {
			ctxs = new IScopeContext[2];
			ctxs[0] = new ProjectScope(project);
			ctxs[1] = new InstanceScope();
		} else {
			ctxs = new IScopeContext[1];
			ctxs[0] = new InstanceScope();
		}

		ScopedPreferenceStore store = new ScopedPreferenceStore(ctxs[0],
				qualifier);
		store.setSearchContexts(ctxs);
		return store;
	}
}
