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
package org.seasar.dolteng.eclipse.wigets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.seasar.dolteng.eclipse.DoltengCore;
import org.seasar.dolteng.eclipse.model.ScaffoldDisplay;
import org.seasar.dolteng.eclipse.model.TreeContent;
import org.seasar.dolteng.eclipse.nls.Labels;
import org.seasar.dolteng.eclipse.nls.Messages;
import org.seasar.dolteng.eclipse.part.DatabaseView;
import org.seasar.dolteng.eclipse.preferences.DoltengPreferences;
import org.seasar.dolteng.eclipse.scaffold.ScaffoldConfig;
import org.seasar.dolteng.eclipse.scaffold.ScaffoldConfigResolver;
import org.seasar.dolteng.eclipse.scaffold.ScaffoldHeadMeisaiConfig;
import org.seasar.dolteng.eclipse.scaffold.ScaffoldHeadMeisaiConfigResolver;
import org.seasar.dolteng.eclipse.util.ProjectUtil;
import org.seasar.framework.util.StringUtil;

import org.seasar.dolteng.eclipse.model.impl.ScaffoldModel;
import org.seasar.dolteng.eclipse.model.impl.TableNode;
import org.seasar.dolteng.core.dao.impl.BasicDatabaseMetadataDao;
import org.seasar.dolteng.core.entity.ColumnMetaData;

/**
 * @author seiichi
 * 
 */
public class OutputLocationHeadMeisaiDialog extends TitleAreaDialog {

    private IJavaProject javap;

    private IPackageFragmentRoot rootPkg;

    private String rootPkgName;

    private ScaffoldHeadMeisaiConfigResolver resolver;

    private ScaffoldDisplay[] displaies;

    private Map<Integer, String> index2id;

    private ScaffoldHeadMeisaiConfig selectedConfig;
    
    // 起動元のデータベースビューのカレントノード（テーブル情報）
    private TableNode current;
    
    // 現在のテーブル（起動元のデータベースビューのカレントノード）の列情報を表示するリストコンポーネント
    private org.eclipse.swt.widgets.List currentTableColumnsList = null;

    // ヘッダ明細のうち明細に指定するテーブルの候補を表示するリストコンポーネント
    private org.eclipse.swt.widgets.List meisaiTableList = null;

    
    
    
    
    // テーブルの選択されている列情報
    // Map<i, String[0]> に列名が格納されています。
    // Map<i, String[1]> に型（データベースのカラムタイプ）が格納されています。
    private Map<Integer, String[]> selectedColumns;
    
    
    // 明細テーブルの名前
    private String meisaiTableName;
    
    // 明細テーブルの列情報
    // Map<i, String[0]> に列名が格納されています。
    // Map<i, String[1]> に型（データベースのカラムタイプ）が格納されています。
    private Map<Integer, String[]> meisaiColumns;
    
    
    
    
    
    
    
    public OutputLocationHeadMeisaiDialog(Shell parentShell, IJavaProject javap, TableNode current) {
        super(parentShell);
        this.javap = javap;
        this.rootPkg = ProjectUtil.getDefaultSrcPackageFragmentRoot(javap);
        this.current = current;

        DoltengPreferences pref = DoltengCore.getPreferences(javap);
        if (pref != null) {
            this.rootPkgName = pref.getDefaultRootPackageName();
        }
        
        this.resolver = new ScaffoldHeadMeisaiConfigResolver(this.javap.getProject());
        this.resolver.initialize();
        this.displaies = this.resolver.getScaffolds();
        this.index2id = new HashMap<Integer, String>(this.displaies.length);
        for (int i = 0; i < this.displaies.length; i++) {
            String id = this.displaies[i].getId();
            if (i == 0) {
                this.selectedConfig = this.resolver.getConfig(id);
            }
            this.index2id.put(new Integer(i), id);
        }
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = createMainLayout((Composite) super
                .createDialogArea(parent));

        setTitle(Messages.GENERATE_HEAD_MEISAI_CODES);
                
        createLabel(composite, Labels.PACKAGEFRAGMENT_ROOT);
        final Combo fragment = createCombo(composite, getPkgFragmentRoot());
        fragment.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String path = fragment.getText();
                IResource r = javap.getProject().findMember(path);
                if (r != null && r.exists()) {
                    IPackageFragmentRoot root = javap.getPackageFragmentRoot(r);
                    if (root != null && root.exists()) {
                        rootPkg = root;
                    }
                }
            }
        });
        fragment.setText(rootPkg.getResource().getProjectRelativePath()
                .toString());

        createLabel(composite, Labels.ROOT_PKG);
        final Combo rootpkg = createCombo(composite, getRootPkgs());
        rootpkg.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String name = rootpkg.getText();
                if (StringUtil.isEmpty(name) == false) {
                    rootPkgName = name;
                }
            }
        });
        rootpkg.setText(rootPkgName);

        createLabel(composite, Labels.SCAFFOLD_TYPE);
        final Combo scaffolds = createCombo(composite, getScaffoldLabels());
        scaffolds.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                selectedConfig = resolver.getConfig(index2id.get(new Integer(
                        scaffolds.getSelectionIndex())));
            }
        });
        
        // 起動元のデータベースビューのカレントノード（テーブル情報）
        TreeContent[] currentTable = current.getChildren();
        String[] dbcolumns = new String[currentTable.length];
        int i = 0;
        for (TreeContent column : currentTable) {
            dbcolumns[i++] = column.getText();
        }
        
        // Scaffold対象のテーブルの列情報を、リストボックスにて表示します。
        // current.getText() でヘッダのテーブル名が取得できます。
        createLabel(composite, current.getText() + "\nRetrieval\nCondition :");
        currentTableColumnsList = new org.eclipse.swt.widgets.List(
                composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
        currentTableColumnsList.setSize(10, 10);
        currentTableColumnsList.setLayoutData(new GridData(SWT.LEFT));
        currentTableColumnsList.setItems(dbcolumns);
        currentTableColumnsList.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                String[] tmp = currentTableColumnsList.getSelection();

                selectedColumns = new HashMap<Integer, String[]>(tmp.length);
                for (int i = 0; i < tmp.length; i++) {
                    selectedColumns.put(i, tmp[i].split(":"));
                    selectedColumns.get(i)[0] = selectedColumns.get(i)[0].trim();
                    selectedColumns.get(i)[1] = selectedColumns.get(i)[1].trim();
                }
            }
        });
        //--------------------------------------------------------------------------
        //--------------------------------------------------------------------------

        // ヘッダ明細のうち明細に指定するテーブルの候補を、リストボックスにて表示します。
        createLabel(composite, "Detail\nTable:");
        meisaiTableList = new org.eclipse.swt.widgets.List(
                composite, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
        meisaiTableList.setSize(10, 10);
        meisaiTableList.setLayoutData(new GridData(SWT.LEFT));
        meisaiTableList.setItems(DatabaseView.getAllTables());
        meisaiTableList.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                String[] tmp = meisaiTableList.getSelection();
                meisaiTableName = tmp[0];
            }
        });
        //--------------------------------------------------------------------------
        //--------------------------------------------------------------------------
        
        scaffolds.select(0);
        return composite;
    }
    
    /**
     * テーブル上の選択された列情報を取得します。
     * Map<i, String[0]> に列名が格納されています。
     * Map<i, String[1]> に型（データベースのカラムタイプ）が格納されています。
     * 
     * @return テーブル上の選択された列情報
     */
    public Map<Integer, String[]> getSelectedColumns() {
        return selectedColumns;
    }
    
    /**
     * 明細テーブルの名前を取得します。
     * @return
     */
    public String getMeisaiTableName() {
        return meisaiTableName;
    }
    
    /**
     * 明細テーブルの列情報を取得します。
     * Map<i, String[0]> に列名が格納されています。
     * Map<i, String[1]> に型（データベースのカラムタイプ）が格納されています。
     * 
     * @return
     */
    public Map<Integer, String[]> getMeisaiColumns() {
        return meisaiColumns;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    @Override
    protected Control createButtonBar(Composite parent) {
        Control c = super.createButtonBar(parent);
        Button ok = getButton(IDialogConstants.OK_ID);
        ok.setEnabled(0 < this.displaies.length);
        return c;
    }

    private Composite createMainLayout(Composite rootComposite) {
        Composite composite = new Composite(rootComposite, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        return composite;
    }

    protected Label createLabel(Composite parent, String s) {
        Label label = new Label(parent, SWT.NONE);
        label.setText(s);
        return label;
    }

    protected Combo createCombo(Composite parent, String[] elements) {
        Combo combo = new Combo(parent, SWT.BORDER | SWT.READ_ONLY);
        combo.setItems(elements);
        return combo;
    }

    private String[] getPkgFragmentRoot() {
        List<String> results = new ArrayList<String>();
        try {
            IPackageFragmentRoot[] roots = this.javap.getPackageFragmentRoots();
            for (int i = 0; i < roots.length; i++) {
                IPackageFragmentRoot root = roots[i];
                if (IPackageFragmentRoot.K_SOURCE == root.getKind()) {
                    results.add(root.getResource().getProjectRelativePath()
                            .toString());
                }
            }
        } catch (JavaModelException e) {
            DoltengCore.log(e);
        }
        return results.toArray(new String[results.size()]);
    }

    private String[] getRootPkgs() {
        DoltengPreferences pref = DoltengCore.getPreferences(javap);
        return pref.getNamingConvention().getRootPackageNames();
    }

    private String[] getScaffoldLabels() {
        List<String> labels = new ArrayList<String>(this.displaies.length);
        for (int i = 0; i < this.displaies.length; i++) {
            labels.add(this.displaies[i].getName());
        }
        return labels.toArray(new String[labels.size()]);
    }

    public String getRootPkg() {
        return this.rootPkg.getResource().getProjectRelativePath().toString();
    }

    public String getRootPkgName() {
        return this.rootPkgName;
    }

    public ScaffoldHeadMeisaiConfig getSelectedConfig() {
        return this.selectedConfig;
    }
}
