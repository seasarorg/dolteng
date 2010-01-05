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
package org.seasar.dolteng.console.linker;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.ui.console.IHyperlink;
import org.osgi.framework.BundleContext;
import org.seasar.eclipse.common.util.ExtensionAcceptor;
import org.seasar.eclipse.common.util.LogUtil;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.seasar.dolteng.console.linker";

	// The shared instance
	private static Activator plugin;

	public static final String IDBASEDURL = "messageidbasedurl";
	protected Map<String, String> idbase = new HashMap<String, String>();

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		ExtensionAcceptor.accept(context.getBundle().getSymbolicName(),
				IDBASEDURL,
				new ExtensionAcceptor.ExtensionVisitor() {
					public void visit(IConfigurationElement e) {
						if ("systemToUrl".equals(e.getName())) {
							idbase.put(e.getAttribute("systemName"), e
									.getAttribute("baseUrl"));
						}
					}
				});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	public static void log(Throwable t) {
		LogUtil.log(getDefault(), t);
	}

	public static IHyperlink create(String base, String id) {
		try {
			String urlBase = getDefault().idbase.get(base);
			if (urlBase != null && 0 < urlBase.length()) {
				URL url = new URL(urlBase + id);
				return new URLHyperLink(url);
			}
		} catch (Exception e) {
			log(e);
		}
		return null;
	}
}
