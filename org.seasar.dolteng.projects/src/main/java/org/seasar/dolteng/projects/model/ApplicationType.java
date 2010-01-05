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
package org.seasar.dolteng.projects.model;

import java.util.ArrayList;
import java.util.List;

/**
 * アプリケーションタイプ情報
 * 
 * @author daisuke
 */
public class ApplicationType {

    private String id;

    private String name;
    
    private String packaging;

    private List<String> firstFacets = new ArrayList<String>();

    private List<String> lastFacets = new ArrayList<String>();

    private List<String> defaultFacets = new ArrayList<String>();

    /** このタイプで無効になるカテゴリのリスト */
    private List<String> disableCategories = new ArrayList<String>();

    /** このタイプで無効になるファセットのリスト */
    private List<String> disableFacets = new ArrayList<String>();

    public ApplicationType(String id, String name, String packaging) {
        this.id = id;
        this.name = name;
        this.packaging = packaging;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPackaging() {
        return packaging;
    }

    public List<String> getFirstFacets() {
        return firstFacets;
    }

    public List<String> getLastFacets() {
        return lastFacets;
    }

    public String[] getDefaultFacets() {
        return defaultFacets.toArray(new String[defaultFacets.size()]);
    }

    /**
     * 指定したカテゴリが有効になっているか調べる
     * 
     * @param category
     *            対象カテゴリ
     * @return 無効な場合<code>true</code>
     */
    public boolean isDisabled(FacetCategory category) {
        return disableCategories.contains(category.getId());
    }

    public boolean isDisabled(FacetConfig fc) {
        return disableFacets.contains(fc.getId());
    }

    public void addFirst(String firstFacet) {
        firstFacets.add(firstFacet);
    }

    public void addLast(String lastFacet) {
        lastFacets.add(lastFacet);
    }

    public void addDefaultFacet(String facetId) {
        defaultFacets.add(facetId);
    }

    public void disableCategory(String category) {
        if (category != null) {
            disableCategories.add(category);
        }
    }

    public void disableFacet(String facet) {
        if (facet != null) {
            disableFacets.add(facet);
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
