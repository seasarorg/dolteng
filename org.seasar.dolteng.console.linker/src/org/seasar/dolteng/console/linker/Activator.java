package org.seasar.dolteng.console.linker;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.ui.console.IHyperlink;
import org.osgi.framework.BundleContext;
import org.seasar.dolteng.eclipse.util.LogUtil;
import org.seasar.eclipse.common.util.ExtensionAcceptor;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.seasar.dolteng.console.anchor";

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
		ExtensionAcceptor.accept(PLUGIN_ID, IDBASEDURL,
				new ExtensionAcceptor.ExtensionVisitor() {
					@Override
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
