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
package org.seasar.dolteng.projects.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import com.thoughtworks.xstream.XStream;

/**
 * モデル to XML のシリアライザ
 * 
 * @author takezoe
 */
public final class XStreamSerializer {
	
	public static String serialize(Object obj, ClassLoader loader) {
		XStream xstream = new XStream();
		xstream.setClassLoader(loader);
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ xstream.toXML(obj);
	}
	
	public static InputStream serializeStream(Object obj, ClassLoader loader)
			throws UnsupportedEncodingException {
		
		String xml = serialize(obj, loader);
		return new ByteArrayInputStream(xml.getBytes("UTF-8"));
	}
	
	public static Object deserialize(String xml, ClassLoader loader) {
		XStream xstream = new XStream();
		xstream.setClassLoader(loader);
		return xstream.fromXML(xml);
	}
	
	public static Object deserialize(Reader reader, ClassLoader loader) {
		XStream xstream = new XStream();
		xstream.setClassLoader(loader);
		return xstream.fromXML(reader);
	}
	
	public static Object deserialize(InputStream in, ClassLoader loader)
			throws UnsupportedEncodingException {
		
		XStream xstream = new XStream();
		xstream.setClassLoader(loader);
		return xstream.fromXML(new InputStreamReader(in, "UTF-8"));
	}
	
	/**
	 * ユーティリティクラス
	 */
	private XStreamSerializer() {
	}
}
