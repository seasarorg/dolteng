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

import org.seasar.framework.util.StringUtil;

/**
 * @author taichi
 * 
 */
public class NameConverter {

    public static String toCamelCase(String name) {
        String s = name.toLowerCase();
        StringBuffer stb = new StringBuffer();
        String[] ary = s.split("_");
        for (String element : ary) {
            stb.append(StringUtil.capitalize(element));
        }
        return stb.toString();

    }

    /**
     * text に指定された文字列をパスカル形式に変換します。
     * @param text 変換対象の文字列
     * @return パスカル形式に変換された文字列
     */
    public static String camelize(String text) {
        int length = text.length();
        StringBuffer sb = new StringBuffer();
        boolean isFirstChar = true;
        for(int i = 0; i < length; i++) {
            if(isFirstChar) {
                sb.append(Character.toUpperCase(text.charAt(i)));
                isFirstChar = false;
            } else {
                if(text.charAt(i) == '-' || text.charAt(i) == '_') {
                    isFirstChar = true;
                } else {
                    sb.append(text.charAt(i));
                }
            }
        }
        return sb.toString();
    }
}
