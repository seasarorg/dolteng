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
package org.seasar.dolteng.projects.model;

/**
 * ファセットカテゴリ情報
 * 
 * @author daisuke
 */
public class FacetCategory {

    /** カテゴリID */
    private String id;

    /** カテゴリKey */
    private String key;

    /** カテゴリ名 */
    private String name;

    /**
     * コンストラクタ。
     * 
     * @param id
     *            カテゴリID
     * @param key
     *            カテゴリKeyは必須で、<code>null</code>であってはいいけません。アルファベット2文字である必要があります。そうでない場合は、<code>IllegalArgumentException</code>をスローします。
     * @param name
     *            カテゴリ名
     * @category instance creation
     */
    public FacetCategory(String id, String key, String name) {
        super();
        if (key == null || key.length() != 2) {
            throw new IllegalArgumentException("key is null.");
        }
        this.id = id;
        this.key = key;
        this.name = name;
    }

    public FacetCategory(String key) {
        this(null, key, null);
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FacetCategory other = (FacetCategory) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return key;
    }
}
