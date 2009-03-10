package org.seasar.dolteng.projects.model.maven;

import java.util.ArrayList;
import java.util.List;

public class DependencyModel {

    public String groupId;

    public String artifactId;

    public String version;

    public String scope;

    public List<DependencyModel> exclusions = new ArrayList<DependencyModel>();

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("{");
        buf.append(groupId);
        buf.append(",");
        buf.append(artifactId);
        buf.append(",");
        buf.append(version);
        buf.append(",");
        buf.append(scope);
        buf.append(",");
        buf.append(exclusions);
        buf.append("}");
        return buf.toString();
    }
}
