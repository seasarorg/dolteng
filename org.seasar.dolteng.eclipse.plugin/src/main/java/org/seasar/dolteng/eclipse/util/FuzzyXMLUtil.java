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
package org.seasar.dolteng.eclipse.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.aonir.fuzzyxml.FuzzyXMLAttribute;
import jp.aonir.fuzzyxml.FuzzyXMLDocument;
import jp.aonir.fuzzyxml.FuzzyXMLElement;
import jp.aonir.fuzzyxml.FuzzyXMLNode;
import jp.aonir.fuzzyxml.FuzzyXMLParser;
import jp.aonir.fuzzyxml.XPath;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.seasar.framework.util.InputStreamUtil;

/**
 * @author taichi
 * 
 */
public class FuzzyXMLUtil {

    protected static Pattern encoding = Pattern
            .compile("<\\?xml\\s+[^\\?>]*?encoding\\s*=\\s*\"(.*?)\"[^\\?>]*?\\?>");

    public static String getChildText(FuzzyXMLElement e) {
        return e.getValue().replaceAll("\"|\r|\n", "");
    }

    public static FuzzyXMLNode[] selectNodes(IFile file, String query)
            throws IOException, CoreException {
        FuzzyXMLDocument doc = parse(file);
        FuzzyXMLNode[] list = XPath
                .selectNodes(doc.getDocumentElement(), query);
        return list;
    }

    public static FuzzyXMLDocument parse(IFile file) throws IOException,
            CoreException {
        byte[] bytes = InputStreamUtil.getBytes(file.getContents(true));
        String encoding = getEncoding(bytes);
        FuzzyXMLParser parser = new FuzzyXMLParser();
        FuzzyXMLDocument doc = parser.parse(new String(bytes, encoding));
        return doc;
    }

    public static String getAttribute(FuzzyXMLElement e, String name) {
        String result = "";
        FuzzyXMLAttribute a = e.getAttributeNode(name);
        if (a != null) {
            result = a.getValue();
        }
        return result;
    }

    public static FuzzyXMLElement getFirstChild(FuzzyXMLElement element) {
        for (FuzzyXMLNode kid : element.getChildren()) {
            if (kid instanceof FuzzyXMLElement) {
                return (FuzzyXMLElement) kid;
            }
        }
        return null;
    }

    public static String getEncoding(byte[] bytes) {
        // from BOM
        if (starts(bytes, new int[] { 0x00, 0x00, 0xFE, 0xFF })) {
            return "UTF-32BE";
        } else if (starts(bytes, new int[] { 0xFF, 0xFE, 0x00, 0x00 })) {
            return "UTF-32LE";
        } else if (starts(bytes, new int[] { 0xFE, 0xFF })) {
            return "UTF-16BE";
        } else if (starts(bytes, new int[] { 0xFF, 0xFE })) {
            return "UTF-16LE";
        } else if (starts(bytes, new int[] { 0xEF, 0xBB, 0xBF })) {
            return "UTF-8";
        }
        // from '<'
        if (starts(bytes, new int[] { 0x00, 0x00, 0x00, 0x3C })) {
            return "UTF-32BE";
        } else if (starts(bytes, new int[] { 0x3C, 0x00, 0x00, 0x00 })) {
            return "UTF-32LE";
        } else if (starts(bytes, new int[] { 0x00, 0x3C })) {
            return "UTF-16BE";
        } else if (starts(bytes, new int[] { 0x3C, 0x00 })) {
            return "UTF-16LE";
        }
        // from XML Declaration
        String str;
        try {
            str = new String(bytes, "ASCII");
            Matcher matcher = encoding.matcher(str);
            if (matcher.find()) {
                return matcher.group(1);
            }
        } catch (UnsupportedEncodingException ignore) {
        }
        // default
        return "UTF-8";
    }

    protected static boolean starts(byte[] array, int[] expected) {
        if (array.length < expected.length) {
            return false;
        }
        for (int i = 0; i < expected.length; ++i) {
            if (array[i] != expected[i]) {
                return false;
            }
        }
        return true;
    }

}
