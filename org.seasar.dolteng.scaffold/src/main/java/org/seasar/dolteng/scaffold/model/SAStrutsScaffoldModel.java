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
package org.seasar.dolteng.scaffold.model;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.seasar.dolteng.eclipse.DoltengCore;
import org.seasar.dolteng.eclipse.model.impl.ProjectNode;
import org.seasar.dolteng.eclipse.model.impl.ScaffoldModel;
import org.seasar.dolteng.eclipse.model.impl.TableNode;
import org.seasar.framework.util.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * SAStruts専用のScaffoldModel
 * 
 * @author newta
 */
public class SAStrutsScaffoldModel extends ScaffoldModel {

	public static final String WEB_INF_WEB_XML = "/WEB-INF/web.xml";

	public static final String CONTEXT_PARAM = "context-param";

	public static final String SASTRUTS_VIEW_PREFIX = "sastruts.VIEW_PREFIX";

	public static final String PARAM_NAME = "param-name";

	public static final String PARAM_VALUE = "param-value";

	/*
	 * @see org.seasar.dolteng.eclipse.model.impl.ScaffoldModel
	 */
	public SAStrutsScaffoldModel(Map<String, String> configs, TableNode node,
			String viewTemplateExtension, Map<Integer, String[]> selectedColumns) {
		super(configs, node, selectedColumns);

		appendConfig(configs, node, viewTemplateExtension);

	}

	/**
	 * コンフィグにSAStruts専用の設定を追加する
	 * 
	 * @param configs
	 * @param node
	 * @param viewTemplateExtension
	 */
	protected void appendConfig(Map<String, String> configs, TableNode node,
			String viewTemplateExtension) {
		ProjectNode n = (ProjectNode) node.getRoot();
		IJavaProject project = n.getJavaProject();

		// NamingConventionに追加されるようならば削除する
		configs.put(convertKey("FormPackageName"), "form");
		configs.put(convertKey("FormSuffix"), "Form");
		configs.put(convertKey("ActionPackageName"), "action");
		
		// ビューテンプレートの拡張子
		configs.put(convertKey("ViewTemplateExtension"), viewTemplateExtension);

		// ここはSAStruts独特の処理
		String webRootViewPrefix = getWebRootViewPrefix(project.getProject(),
				configs.get("webcontentsroot"));

		if (!StringUtil.isEmpty(webRootViewPrefix)) {
			if (webRootViewPrefix.startsWith("/")) {
				webRootViewPrefix = webRootViewPrefix.substring(1);
			}
			configs.put(convertKey("ViewRootPath"), webRootViewPrefix);
		}
	}

	// sa-struts-plugin参照
	protected String getWebRootViewPrefix(IProject project, String webRoot) {
		File webXmlFile = ((Path) project.getFile(webRoot + WEB_INF_WEB_XML)
				.getLocation()).toFile();
		if (webXmlFile.exists()) {
			String viewPrefix = null;
			try {
				viewPrefix = getViewPrefix(webXmlFile);
			} catch (ParserConfigurationException e) {
				DoltengCore.log(e);
				return null;
			} catch (SAXException e) {
				DoltengCore.log(e);
				return null;
			} catch (IOException e) {
				DoltengCore.log(e);
				return null;
			}
			if (!StringUtil.isEmpty(viewPrefix)) {
				return viewPrefix;
			}
			return null;
		} else {
			DoltengCore.log(webRoot + WEB_INF_WEB_XML + " not found");
			return null;
		}
	}

	// sa-struts-plugin参照
	protected String getViewPrefix(File webXmlFile) throws SAXException,
			IOException, ParserConfigurationException {
		DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
		dbfactory.setNamespaceAware(true);
		DocumentBuilder builder = dbfactory.newDocumentBuilder();
		Document doc = builder.parse(webXmlFile);
		Element element = doc.getDocumentElement();
		NodeList contextParamNodeList = element
				.getElementsByTagName(CONTEXT_PARAM);
		if (contextParamNodeList.getLength() == 1
				&& contextParamNodeList.item(0) instanceof Element) {
			Element contextParamElement = (Element) contextParamNodeList
					.item(0);
			NodeList paramNameNodeList = contextParamElement
					.getElementsByTagName(PARAM_NAME);
			if (paramNameNodeList.getLength() == 1) {
				if (((Node) paramNameNodeList.item(0)).getTextContent().equals(
						SASTRUTS_VIEW_PREFIX)) {
					NodeList paramValueNodeList = contextParamElement
							.getElementsByTagName(PARAM_VALUE);
					if (paramValueNodeList.getLength() == 1) {
						return ((Node) paramValueNodeList.item(0))
								.getTextContent();
					}
				}
			}
		}
		return null;
	}

	private static String convertKey(String key) {
		return key.toLowerCase();
	}

}
