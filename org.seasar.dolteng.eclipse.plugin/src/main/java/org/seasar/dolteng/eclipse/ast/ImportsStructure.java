/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.seasar.dolteng.eclipse.ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IImportContainer;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.WildcardType;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.TypeNameRequestor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.DocumentRewriteSession;
import org.eclipse.jface.text.DocumentRewriteSessionType;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension4;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.TextUtilities;
import org.eclipse.text.edits.DeleteEdit;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.TextEdit;
import org.seasar.dolteng.eclipse.DoltengCore;
import org.seasar.dolteng.eclipse.nls.Messages;
import org.seasar.dolteng.eclipse.preferences.JavaPreferencesSettings;
import org.seasar.dolteng.eclipse.util.Resources;
import org.seasar.dolteng.eclipse.util.StatusUtil;
import org.seasar.framework.util.ClassUtil;

/**
 * Created on a Compilation unit, the ImportsStructure allows to add Import
 * Declarations that are added next to the existing import that has the best
 * match.
 */
public final class ImportsStructure {

    private ICompilationUnit fCompilationUnit;

    private List<PackageEntry> fPackageEntries;

    private int fImportOnDemandThreshold;

    private boolean fFilterImplicitImports;

    private boolean fFindAmbiguousImports;

    private List<String> fImportsCreated;

    private List<String> fStaticImportsCreated;

    private boolean fHasChanges = false;

    private IRegion fReplaceRange;

    private static final String JAVA_LANG = "java.lang"; //$NON-NLS-1$

    public ImportsStructure(ICompilationUnit cu) throws CoreException {
        this(cu, JavaPreferencesSettings.getImportOrderPreference(cu
                .getJavaProject()), JavaPreferencesSettings
                .getImportNumberThreshold(cu.getJavaProject()), true);
    }

    /**
     * Creates an ImportsStructure for a compilation unit. New imports are added
     * next to the existing import that is matching best.
     * 
     * @param cu
     *            The compilation unit
     * @param preferenceOrder
     *            Defines the preferred order of imports.
     * @param importThreshold
     *            Defines the number of imports in a package needed to introduce
     *            a import on demand instead (e.g. java.util.*).
     * @param restoreExistingImports
     *            If set, existing imports are kept. No imports are deleted,
     *            only new added.
     * @throws CoreException
     */
    public ImportsStructure(ICompilationUnit cu, String[] preferenceOrder,
            int importThreshold, boolean restoreExistingImports)
            throws CoreException {
        fCompilationUnit = cu;
        reconcile(cu);

        IImportContainer container = cu.getImportContainer();

        fImportOnDemandThreshold = importThreshold;
        fFilterImplicitImports = true;
        fFindAmbiguousImports = true; // !restoreExistingImports;

        fPackageEntries = new ArrayList<PackageEntry>(20);
        fImportsCreated = null; // initialized on 'create'
        fStaticImportsCreated = null;

        IProgressMonitor monitor = new NullProgressMonitor();
        IDocument document = null;
        try {
            document = aquireDocument(monitor);
            fReplaceRange = evaluateReplaceRange(document);
            if (restoreExistingImports && container.exists()) {
                addExistingImports(document, cu.getImports(), fReplaceRange);
            }
        } catch (BadLocationException e) {
            throw new CoreException(StatusUtil.createError(DoltengCore
                    .getDefault(), 0, e));
        } finally {
            if (document != null) {
                releaseDocument(document, monitor);
            }
        }
        PackageEntry[] order = new PackageEntry[preferenceOrder.length];
        for (int i = 0; i < order.length; i++) {
            String curr = preferenceOrder[i];
            if (curr.length() > 0 && curr.charAt(0) == '#') {
                curr = curr.substring(1);
                order[i] = new PackageEntry(curr, curr, true); // static import
                // group
            } else {
                order[i] = new PackageEntry(curr, curr, false); // normal import
                // group
            }
        }

        addPreferenceOrderHolders(order);

        fHasChanges = false;
    }

    private void addPreferenceOrderHolders(PackageEntry[] preferenceOrder) {
        if (fPackageEntries.isEmpty()) {
            // all new: copy the elements
            for (PackageEntry entry : preferenceOrder) {
                fPackageEntries.add(entry);
            }
        } else {
            // match the preference order entries to existing imports
            // entries not found are appended after the last successfully
            // matched entry

            PackageEntry[] lastAssigned = new PackageEntry[preferenceOrder.length];

            // find an existing package entry that matches most
            for (int k = 0; k < fPackageEntries.size(); k++) {
                PackageEntry entry = fPackageEntries.get(k);
                if (!entry.isComment()) {
                    String currName = entry.getName();
                    int currNameLen = currName.length();
                    int bestGroupIndex = -1;
                    int bestGroupLen = -1;
                    for (int i = 0; i < preferenceOrder.length; i++) {
                        boolean currPrevStatic = preferenceOrder[i].isStatic();
                        if (currPrevStatic == entry.isStatic()) {
                            String currPrefEntry = preferenceOrder[i].getName();
                            int currPrefLen = currPrefEntry.length();
                            if (currName.startsWith(currPrefEntry)
                                    && currPrefLen >= bestGroupLen) {
                                if (currPrefLen == currNameLen
                                        || currName.charAt(currPrefLen) == '.') {
                                    if (bestGroupIndex == -1
                                            || currPrefLen > bestGroupLen) {
                                        bestGroupLen = currPrefLen;
                                        bestGroupIndex = i;
                                    }
                                }
                            }
                        }
                    }
                    if (bestGroupIndex != -1) {
                        entry.setGroupID(preferenceOrder[bestGroupIndex]
                                .getName());
                        lastAssigned[bestGroupIndex] = entry; // remember last
                        // entry
                    }
                }
            }
            // fill in not-assigned categories, keep partial order
            int currAppendIndex = 0;
            for (int i = 0; i < lastAssigned.length; i++) {
                PackageEntry entry = lastAssigned[i];
                if (entry == null) {
                    PackageEntry newEntry = preferenceOrder[i];
                    if (currAppendIndex == 0 && !newEntry.isStatic()) {
                        currAppendIndex = getIndexAfterStatics();
                    }
                    fPackageEntries.add(currAppendIndex, newEntry);
                    currAppendIndex++;
                } else {
                    currAppendIndex = fPackageEntries.indexOf(entry) + 1;
                }
            }
        }
    }

    private void addExistingImports(IDocument document,
            IImportDeclaration[] decls, IRegion replaceRange)
            throws JavaModelException, BadLocationException {
        if (decls.length == 0) {
            return;
        }
        PackageEntry currPackage = null;

        IImportDeclaration curr = decls[0];
        ISourceRange sourceRange = curr.getSourceRange();
        int currOffset = sourceRange.getOffset();
        int currLength = sourceRange.getLength();
        int currEndLine = document.getLineOfOffset(currOffset + currLength);

        for (int i = 1; i < decls.length; i++) {
            String name = curr.getElementName();
            boolean isStatic = Flags.isStatic(curr.getFlags());

            String packName = Signature.getQualifier(name);
            if (currPackage == null
                    || currPackage.compareTo(packName, isStatic) != 0) {
                currPackage = new PackageEntry(packName, null, isStatic);
                fPackageEntries.add(currPackage);
            }

            IImportDeclaration next = decls[i];
            sourceRange = next.getSourceRange();
            int nextOffset = sourceRange.getOffset();
            int nextLength = sourceRange.getLength();
            int nextOffsetLine = document.getLineOfOffset(nextOffset);

            // if next import is on a different line, modify the end position to
            // the next line begin offset
            if (currEndLine < nextOffsetLine) {
                currEndLine++;
                nextOffset = document.getLineInformation(currEndLine)
                        .getOffset();
            }
            currPackage.add(new ImportDeclEntry(name, isStatic, new Region(
                    currOffset, nextOffset - currOffset)));
            currOffset = nextOffset;
            curr = next;

            // add a comment entry for spacing between imports
            if (currEndLine < nextOffsetLine) {
                nextOffset = document.getLineInformation(nextOffsetLine)
                        .getOffset();

                currPackage = new PackageEntry(); // create a comment package
                // entry for this
                fPackageEntries.add(currPackage);
                currPackage.add(new ImportDeclEntry(null, false, new Region(
                        currOffset, nextOffset - currOffset)));

                currOffset = nextOffset;
            }
            currEndLine = document.getLineOfOffset(nextOffset + nextLength);
        }

        String name = curr.getElementName();
        boolean isStatic = Flags.isStatic(curr.getFlags());
        String packName = Signature.getQualifier(name);
        if (currPackage == null
                || currPackage.compareTo(packName, isStatic) != 0) {
            currPackage = new PackageEntry(packName, null, isStatic);
            fPackageEntries.add(currPackage);
        }
        ISourceRange range = curr.getSourceRange();
        int length = replaceRange.getOffset() + replaceRange.getLength()
                - range.getOffset();
        currPackage.add(new ImportDeclEntry(name, isStatic, new Region(range
                .getOffset(), length)));
    }

    /**
     * @return Returns the compilation unit of this import structure.
     */
    public ICompilationUnit getCompilationUnit() {
        return fCompilationUnit;
    }

    /**
     * Sets that implicit imports (types in default package, cu- package and
     * 'java.lang') should not be created. Note that this is a heuristic filter
     * and can lead to missing imports, e.g. in cases where a type is forced to
     * be specified due to a name conflict. By default, the filter is enabled.
     * 
     * @param filterImplicitImports
     *            The filterImplicitImports to set
     */
    public void setFilterImplicitImports(boolean filterImplicitImports) {
        fFilterImplicitImports = filterImplicitImports;
    }

    /**
     * When set searches for imports that can not be folded into on-demand
     * imports but must be specified explicitly
     * 
     * @param findAmbiguousImports
     *            The new value
     */
    public void setFindAmbiguousImports(boolean findAmbiguousImports) {
        fFindAmbiguousImports = findAmbiguousImports;
    }

    private static class PackageMatcher {
        private String fNewName;

        private String fBestName;

        private int fBestMatchLen;

        public PackageMatcher() {
        }

        public void initialize(String newName, String bestName) {
            fNewName = newName;
            fBestName = bestName;
            fBestMatchLen = getCommonPrefixLength(bestName, fNewName);
        }

        public boolean isBetterMatch(String currName, boolean preferCurr) {
            boolean isBetter;
            int currMatchLen = getCommonPrefixLength(currName, fNewName);
            int matchDiff = currMatchLen - fBestMatchLen;
            if (matchDiff == 0) {
                if (currMatchLen == fNewName.length()
                        && currMatchLen == currName.length()
                        && currMatchLen == fBestName.length()) {
                    // duplicate entry and complete match
                    isBetter = preferCurr;
                } else {
                    isBetter = sameMatchLenTest(currName);
                }
            } else {
                isBetter = (matchDiff > 0); // curr has longer match
            }
            if (isBetter) {
                fBestName = currName;
                fBestMatchLen = currMatchLen;
            }
            return isBetter;
        }

        private boolean sameMatchLenTest(String currName) {
            int matchLen = fBestMatchLen;
            // known: bestName and currName differ from newName at position
            // 'matchLen'
            // currName and bestName dont have to differ at position 'matchLen'

            // determine the order and return true if currName is closer to
            // newName
            char newChar = getCharAt(fNewName, matchLen);
            char currChar = getCharAt(currName, matchLen);
            char bestChar = getCharAt(fBestName, matchLen);

            if (newChar < currChar) {
                if (bestChar < newChar) { // b < n < c
                    return (currChar - newChar) < (newChar - bestChar); // -> (c
                    // - n)
                    // < (n
                    // - b)
                } else { // n < b && n < c
                    if (currChar == bestChar) { // longer match between curr and
                        // best
                        return false; // keep curr and best together, new
                        // should be before both
                    } else {
                        return currChar < bestChar; // -> (c < b)
                    }
                }
            } else {
                if (bestChar > newChar) { // c < n < b
                    return (newChar - currChar) < (bestChar - newChar); // -> (n
                    // - c)
                    // < (b
                    // - n)
                } else { // n > b && n > c
                    if (currChar == bestChar) { // longer match between curr and
                        // best
                        return true; // keep curr and best together, new
                        // should be ahead of both
                    } else {
                        return currChar > bestChar; // -> (c > b)
                    }
                }
            }
        }

    }

    private static int getCommonPrefixLength(String s, String t) {
        int len = Math.min(s.length(), t.length());
        for (int i = 0; i < len; i++) {
            if (s.charAt(i) != t.charAt(i)) {
                return i;
            }
        }
        return len;
    }

    private static char getCharAt(String str, int index) {
        if (str.length() > index) {
            return str.charAt(index);
        }
        return 0;
    }

    private PackageEntry findBestMatch(String newName, boolean isStatic) {
        if (fPackageEntries.isEmpty()) {
            return null;
        }
        String groupId = null;
        int longestPrefix = -1;
        // find the matching group
        for (int i = 0; i < fPackageEntries.size(); i++) {
            PackageEntry curr = fPackageEntries.get(i);
            if (isStatic == curr.isStatic()) {
                String currGroup = curr.getGroupID();
                if (currGroup != null && newName.startsWith(currGroup)) {
                    int prefixLen = currGroup.length();
                    if (prefixLen == newName.length()) {
                        return curr; // perfect fit, use entry
                    }
                    if ((newName.charAt(prefixLen) == '.')
                            && prefixLen > longestPrefix) {
                        longestPrefix = prefixLen;
                        groupId = currGroup;
                    }
                }
            }
        }
        PackageEntry bestMatch = null;
        PackageMatcher matcher = new PackageMatcher();
        matcher.initialize(newName, ""); //$NON-NLS-1$
        for (int i = 0; i < fPackageEntries.size(); i++) { // find the best
            // match with the
            // same group
            PackageEntry curr = fPackageEntries.get(i);
            if (!curr.isComment() && curr.isStatic() == isStatic) {
                if (groupId == null || groupId.equals(curr.getGroupID())) {
                    boolean preferrCurr = (bestMatch == null)
                            || (curr.getNumberOfImports() > bestMatch
                                    .getNumberOfImports());
                    if (matcher.isBetterMatch(curr.getName(), preferrCurr)) {
                        bestMatch = curr;
                    }
                }
            }
        }
        return bestMatch;
    }

    public static boolean isImplicitImport(String qualifier, ICompilationUnit cu) {
        if (JAVA_LANG.equals(qualifier)) { //$NON-NLS-1$
            return true;
        }
        String packageName = cu.getParent().getElementName();
        if (qualifier.equals(packageName)) {
            return true;
        }
        String mainTypeName = ClassUtil.concatName(packageName, Signature
                .getQualifier(cu.getElementName()));
        return qualifier.equals(mainTypeName);
    }

    /**
     * Adds a new import declaration that is sorted in the structure using a
     * best match algorithm. If an import already exists, the import is not
     * added.
     * 
     * @param binding
     *            The type binding of the type to be added
     * @param ast
     *            The ast to create the type for
     * @return Returns the a new AST node that is either simple if the import
     *         was successful or fully qualified type name if the import could
     *         not be added due to a conflict.
     */
    // public Type addImport(ITypeBinding binding, AST ast) {
    // if (binding.isPrimitive()) {
    // return ast
    // .newPrimitiveType(PrimitiveType.toCode(binding.getName()));
    // }
    //
    // ITypeBinding normalizedBinding = Bindings.normalizeTypeBinding(binding);
    // if (normalizedBinding == null) {
    // return ast.newSimpleType(ast.newSimpleName("invalid")); //$NON-NLS-1$
    // }
    //
    // if (normalizedBinding.isTypeVariable()) {
    // // no import
    // return ast.newSimpleType(ast.newSimpleName(binding.getName()));
    // }
    // if (normalizedBinding.isWildcardType()) {
    // WildcardType wcType = ast.newWildcardType();
    // ITypeBinding bound = normalizedBinding.getBound();
    // if (bound != null && !bound.isWildcardType() && !bound.isCapture()) { //
    // bug
    // // 96942
    // Type boundType = addImport(bound, ast);
    // wcType.setBound(boundType, normalizedBinding.isUpperbound());
    // }
    // return wcType;
    // }
    //
    // if (normalizedBinding.isArray()) {
    // Type elementType = addImport(normalizedBinding.getElementType(),
    // ast);
    // return ast.newArrayType(elementType, normalizedBinding
    // .getDimensions());
    // }
    //
    // String qualifiedName = Bindings.getRawQualifiedName(normalizedBinding);
    // if (qualifiedName.length() > 0) {
    // String res = internalAddImport(qualifiedName);
    //
    // ITypeBinding[] typeArguments = normalizedBinding.getTypeArguments();
    // if (typeArguments.length > 0) {
    // Type erasureType = ast.newSimpleType(ASTNodeFactory.newName(
    // ast, res));
    // ParameterizedType paramType = ast
    // .newParameterizedType(erasureType);
    // List arguments = paramType.typeArguments();
    // for (int i = 0; i < typeArguments.length; i++) {
    // arguments.add(addImport(typeArguments[i], ast));
    // }
    // return paramType;
    // }
    // return ast.newSimpleType(ASTNodeFactory.newName(ast, res));
    // }
    // return ast.newSimpleType(ASTNodeFactory.newName(ast, Bindings
    // .getRawName(normalizedBinding)));
    // }
    /**
     * Adds a new import declaration that is sorted in the structure using a
     * best match algorithm. If an import already exists, the import is not
     * added.
     * 
     * @param typeSig
     *            The type in signature notation
     * @param ast
     *            The ast to create the type for
     * @return Returns the a new AST node that is either simple if the import
     *         was successful or fully qualified type name if the import could
     *         not be added due to a conflict.
     */
    @SuppressWarnings("unchecked")
    public Type addImportFromSignature(String typeSig, AST ast) {
        if (typeSig == null || typeSig.length() == 0) {
            throw new IllegalArgumentException(
                    "Invalid type signature: empty or null"); //$NON-NLS-1$
        }
        int sigKind = Signature.getTypeSignatureKind(typeSig);
        switch (sigKind) {
        case Signature.BASE_TYPE_SIGNATURE:
            return ast.newPrimitiveType(PrimitiveType.toCode(Signature
                    .toString(typeSig)));
        case Signature.ARRAY_TYPE_SIGNATURE:
            Type elementType = addImportFromSignature(Signature
                    .getElementType(typeSig), ast);
            return ast.newArrayType(elementType, Signature
                    .getArrayCount(typeSig));
        case Signature.CLASS_TYPE_SIGNATURE:
            String erasureSig = Signature.getTypeErasure(typeSig);

            String erasureName = Signature.toString(erasureSig);
            if (erasureSig.charAt(0) == Signature.C_RESOLVED) {
                erasureName = internalAddImport(erasureName);
            }
            Type baseType = ast.newSimpleType(ast.newName(erasureName));
            String[] typeArguments = Signature.getTypeArguments(typeSig);
            if (typeArguments.length > 0) {
                ParameterizedType type = ast.newParameterizedType(baseType);
                List<Type> argNodes = type.typeArguments();
                for (String typeArgument : typeArguments) {
                    argNodes.add(addImportFromSignature(typeArgument, ast));
                }
                return type;
            }
            return baseType;
        case Signature.TYPE_VARIABLE_SIGNATURE:
            return ast.newSimpleType(ast.newSimpleName(Signature
                    .toString(typeSig)));
        case Signature.WILDCARD_TYPE_SIGNATURE:
            WildcardType wildcardType = ast.newWildcardType();
            char ch = typeSig.charAt(0);
            if (ch != Signature.C_STAR) {
                Type bound = addImportFromSignature(typeSig.substring(1), ast);
                wildcardType.setBound(bound, ch == Signature.C_EXTENDS);
            }
            return wildcardType;
        case Signature.CAPTURE_TYPE_SIGNATURE:
            return addImportFromSignature(typeSig.substring(1), ast);
        default:
            DoltengCore.log("Unknown type signature kind: " + typeSig); //$NON-NLS-1$
        }
        return ast.newSimpleType(ast.newSimpleName("invalid")); //$NON-NLS-1$
    }

    /**
     * Adds a new import declaration that is sorted in the structure using a
     * best match algorithm. If an import already exists, the import is not
     * added.
     * 
     * @param binding
     *            The type binding of the type to be added
     * @return Returns the name to use in the code: Simple name if the import
     *         was added, fully qualified type name if the import could not be
     *         added due to a conflict.
     */
    // public String addImport(ITypeBinding binding) {
    //
    // if (binding.isPrimitive() || binding.isTypeVariable()) {
    // return binding.getName();
    // }
    //
    // ITypeBinding normalizedBinding = Bindings.normalizeTypeBinding(binding);
    // if (normalizedBinding == null) {
    // return "invalid"; //$NON-NLS-1$
    // }
    // if (normalizedBinding.isWildcardType()) {
    // StringBuffer res = new StringBuffer("?"); //$NON-NLS-1$
    // ITypeBinding bound = normalizedBinding.getBound();
    // if (bound != null && !bound.isWildcardType() && !bound.isCapture()) { //
    // bug
    // // 95942
    // if (normalizedBinding.isUpperbound()) {
    // res.append(" extends "); //$NON-NLS-1$
    // } else {
    // res.append(" super "); //$NON-NLS-1$
    // }
    // res.append(addImport(bound));
    // }
    // return res.toString();
    // }
    //
    // if (normalizedBinding.isArray()) {
    // StringBuffer res = new StringBuffer(addImport(normalizedBinding
    // .getElementType()));
    // for (int i = normalizedBinding.getDimensions(); i > 0; i--) {
    // res.append("[]"); //$NON-NLS-1$
    // }
    // return res.toString();
    // }
    //
    // String qualifiedName = Bindings.getRawQualifiedName(normalizedBinding);
    // if (qualifiedName.length() > 0) {
    // String str = internalAddImport(qualifiedName);
    //
    // ITypeBinding[] typeArguments = normalizedBinding.getTypeArguments();
    // if (typeArguments.length > 0) {
    // StringBuffer res = new StringBuffer(str);
    // res.append('<');
    // for (int i = 0; i < typeArguments.length; i++) {
    // if (i > 0) {
    // res.append(','); //$NON-NLS-1$
    // }
    // res.append(addImport(typeArguments[i]));
    // }
    // res.append('>');
    // return res.toString();
    // }
    // return str;
    // }
    // return Bindings.getRawName(normalizedBinding);
    // }
    /**
     * Adds a new import declaration that is sorted in the structure using a
     * best match algorithm. If an import already exists, the import is not
     * added.
     * 
     * @param qualifiedTypeName
     *            The fully qualified name of the type to import
     * @return Returns either the simple type name if the import was successful
     *         or else the qualified type name
     */
    public String addImport(String qualifiedTypeName) {
        int angleBracketOffset = qualifiedTypeName.indexOf('<');
        if (angleBracketOffset != -1) {
            return internalAddImport(qualifiedTypeName.substring(0,
                    angleBracketOffset))
                    + qualifiedTypeName.substring(angleBracketOffset);
        }
        int bracketOffset = qualifiedTypeName.indexOf('[');
        if (bracketOffset != -1) {
            return internalAddImport(qualifiedTypeName.substring(0,
                    bracketOffset))
                    + qualifiedTypeName.substring(bracketOffset);
        }
        return internalAddImport(qualifiedTypeName);
    }

    // public String addStaticImport(IBinding binding) {
    // if (binding instanceof IVariableBinding) {
    // ITypeBinding declaringType = ((IVariableBinding) binding)
    // .getDeclaringClass();
    // return addStaticImport(Bindings.getRawQualifiedName(declaringType),
    // binding.getName(), true);
    // } else if (binding instanceof IMethodBinding) {
    // ITypeBinding declaringType = ((IMethodBinding) binding)
    // .getDeclaringClass();
    // return addStaticImport(Bindings.getRawQualifiedName(declaringType),
    // binding.getName(), false);
    // }
    // return binding.getName();
    // }

    /**
     * Adds a new static import declaration that is sorted in the structure
     * using a best match algorithm. If an import already exists, the import is
     * not added.
     * 
     * @param declaringTypeName
     *            The qualified name of the static's member declaring type
     * @param simpleName
     * @param isField
     * @return Returns either the simple type name if the import was successful
     *         or else the qualified type name
     */
    public String addStaticImport(String declaringTypeName, String simpleName,
            boolean isField) {
        String containerName = Signature.getQualifier(declaringTypeName);
        String fullName = declaringTypeName + '.' + simpleName;

        if (containerName.length() == 0) {
            return declaringTypeName + '.' + simpleName;
        }
        if (!"*".equals(simpleName)) { //$NON-NLS-1$
            if (isField) {
                String existing = findStaticImport(null, simpleName);
                if (existing != null) {
                    if (existing.equals(fullName)) {
                        return simpleName;
                    }
                    return fullName;
                }
            } else {
                String existing = findStaticImport(declaringTypeName,
                        simpleName);
                if (existing != null) {
                    return simpleName;
                }
            }
        }
        ImportDeclEntry decl = new ImportDeclEntry(fullName, true, null);

        sortIn(declaringTypeName, decl, true);
        return simpleName;
    }

    private String internalAddImport(String fullTypeName) {
        int idx = fullTypeName.lastIndexOf('.');
        String typeContainerName, typeName;
        if (idx != -1) {
            typeContainerName = fullTypeName.substring(0, idx);
            typeName = fullTypeName.substring(idx + 1);
        } else {
            typeContainerName = ""; //$NON-NLS-1$
            typeName = fullTypeName;
        }

        if (typeContainerName.length() == 0
                && PrimitiveType.toCode(typeName) != null) {
            return fullTypeName;
        }

        if (!"*".equals(typeName)) { //$NON-NLS-1$
            String topLevelTypeName = Signature.getQualifier(fCompilationUnit
                    .getElementName());

            if (typeName.equals(topLevelTypeName)) {
                if (!typeContainerName.equals(fCompilationUnit.getParent()
                        .getElementName())) {
                    return fullTypeName;
                }
                return typeName;
            }
            String existing = findImport(typeName);
            if (existing != null) {
                if (fullTypeName.equals(existing)) {
                    return typeName;
                }
                return fullTypeName;
            }
        }

        ImportDeclEntry decl = new ImportDeclEntry(fullTypeName, false, null);

        sortIn(typeContainerName, decl, false);
        return typeName;
    }

    private int getIndexAfterStatics() {
        for (int i = 0; i < fPackageEntries.size(); i++) {
            if (!(fPackageEntries.get(i)).isStatic()) {
                return i;
            }
        }
        return fPackageEntries.size();
    }

    private void sortIn(String typeContainerName, ImportDeclEntry decl,
            boolean isStatic) {
        PackageEntry bestMatch = findBestMatch(typeContainerName, isStatic);
        if (bestMatch == null) {
            PackageEntry packEntry = new PackageEntry(typeContainerName, null,
                    isStatic);
            packEntry.add(decl);
            int insertPos = packEntry.isStatic() ? 0 : getIndexAfterStatics();
            fPackageEntries.add(insertPos, packEntry);
        } else {
            int cmp = typeContainerName.compareTo(bestMatch.getName());
            if (cmp == 0) {
                bestMatch.sortIn(decl);
            } else {
                // create a new package entry
                String group = bestMatch.getGroupID();
                if (group != null) {
                    if (!typeContainerName.startsWith(group)) {
                        group = null;
                    }
                }
                PackageEntry packEntry = new PackageEntry(typeContainerName,
                        group, isStatic);
                packEntry.add(decl);
                int index = fPackageEntries.indexOf(bestMatch);
                if (cmp < 0) { // insert ahead of best match
                    fPackageEntries.add(index, packEntry);
                } else { // insert after best match
                    fPackageEntries.add(index + 1, packEntry);
                }
            }
        }
        fHasChanges = true;
    }

    /**
     * Removes an import from the structure.
     * 
     * @param qualifiedName
     *            The qualified type name to remove from the imports
     * @return Returns <code>true</code> if the import was found and removed
     */
    public boolean removeImport(String qualifiedName) {
        String typeContainerName = Signature.getQualifier(qualifiedName);
        int bracketOffset = qualifiedName.indexOf('[');
        if (bracketOffset != -1) {
            qualifiedName = qualifiedName.substring(0, bracketOffset);
        }

        int nPackages = fPackageEntries.size();
        for (int i = 0; i < nPackages; i++) {
            PackageEntry entry = fPackageEntries.get(i);
            if (entry.compareTo(typeContainerName, false) == 0) {
                if (entry.remove(qualifiedName, false)) {
                    fHasChanges = true;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Removes an import from the structure.
     * 
     * @param qualifiedName
     *            The qualified member name to remove from the imports
     * @return Returns <code>true</code> if the import was found and removed
     */
    public boolean removeStaticImport(String qualifiedName) {
        String containerName = Signature.getQualifier(qualifiedName);

        int nPackages = fPackageEntries.size();
        for (int i = 0; i < nPackages; i++) {
            PackageEntry entry = fPackageEntries.get(i);
            if (entry.compareTo(containerName, true) == 0) {
                if (entry.remove(qualifiedName, true)) {
                    fHasChanges = true;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Removes an import from the structure.
     * 
     * @param binding
     *            The type to remove from the imports
     * @return Returns <code>true</code> if the import was found and removed
     */
    // public boolean removeImport(ITypeBinding binding) {
    // binding = Bindings.normalizeTypeBinding(binding);
    // if (binding == null) {
    // return false;
    // }
    // String qualifiedName = Bindings.getRawQualifiedName(binding);
    // if (qualifiedName.length() > 0) {
    // return removeImport(qualifiedName);
    // }
    // return false;
    // }
    /**
     * Looks if there already is single import for the given type name.
     * 
     * @param simpleName
     *            The simple name to find
     * @return Returns the qualified import name or <code>null</code>.
     */
    public String findImport(String simpleName) {
        for (PackageEntry entry : fPackageEntries) {
            if (!entry.isStatic()) {
                ImportDeclEntry found = entry.find(simpleName);
                if (found != null) {
                    return found.getElementName();
                }
            }
        }
        return null;
    }

    public String findStaticImport(String typeContainerName,
            String typeSimpleName) {
        for (PackageEntry entry : fPackageEntries) {
            if (entry.isStatic()) {
                if (typeContainerName == null
                        || entry.getName().equals(typeContainerName)) {
                    ImportDeclEntry found = entry.find(typeSimpleName);
                    if (found != null) {
                        return found.getElementName();
                    }
                }
            }
        }
        return null;
    }

    /**
     * Creates all new elements in the import structure.
     * 
     * @param save
     *            Save the CU after the import have been changed
     * @param monitor
     *            The progress monitor to use
     * @throws CoreException
     *             Thrown when the access to the CU failed
     */
    public void create(boolean save, IProgressMonitor monitor)
            throws CoreException {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        monitor.beginTask(Messages.ImportsStructure_operation_description, 4);

        IDocument document = null;
        DocumentRewriteSession session = null;
        try {
            document = aquireDocument(new SubProgressMonitor(monitor, 1));
            if (document instanceof IDocumentExtension4) {
                session = ((IDocumentExtension4) document)
                        .startRewriteSession(DocumentRewriteSessionType.UNRESTRICTED);
            }
            MultiTextEdit edit = getResultingEdits(document,
                    new SubProgressMonitor(monitor, 1));
            if (edit.hasChildren()) {
                if (save) {
                    commitDocument(document, edit, new SubProgressMonitor(
                            monitor, 1));
                } else {
                    edit.apply(document);
                }
            }
        } catch (BadLocationException e) {
            throw new CoreException(StatusUtil.createError(DoltengCore
                    .getDefault(), 10000, e));
        } finally {
            try {
                if (session != null) {
                    ((IDocumentExtension4) document)
                            .stopRewriteSession(session);
                }
            } finally {
                releaseDocument(document, new SubProgressMonitor(monitor, 1));
            }
            monitor.done();
        }
    }

    public static boolean isPrimary(ICompilationUnit cu) {
        return cu.getOwner() == null;
    }

    private IDocument aquireDocument(IProgressMonitor monitor)
            throws CoreException {
        if (isPrimary(fCompilationUnit)) {
            IFile file = (IFile) fCompilationUnit.getResource();
            if (file.exists()) {
                ITextFileBufferManager bufferManager = FileBuffers
                        .getTextFileBufferManager();
                IPath path = fCompilationUnit.getPath();
                bufferManager.connect(path, monitor);
                return bufferManager.getTextFileBuffer(path).getDocument();
            }
        }
        monitor.done();
        return new Document(fCompilationUnit.getSource());
    }

    private void releaseDocument(IDocument document, IProgressMonitor monitor)
            throws CoreException {
        if (isPrimary(fCompilationUnit)) {
            IFile file = (IFile) fCompilationUnit.getResource();
            if (file.exists()) {
                ITextFileBufferManager bufferManager = FileBuffers
                        .getTextFileBufferManager();
                bufferManager.disconnect(file.getFullPath(), monitor);
                return;
            }
        }
        fCompilationUnit.getBuffer().setContents(document.get());
        monitor.done();
    }

    private void commitDocument(IDocument document, MultiTextEdit edit,
            IProgressMonitor monitor) throws CoreException,
            MalformedTreeException, BadLocationException {
        if (isPrimary(fCompilationUnit)) {
            IFile file = (IFile) fCompilationUnit.getResource();
            if (file.exists()) {
                IStatus status = Resources.makeCommittable(file, null);
                if (!status.isOK()) {
                    throw new CoreException(status);
                }
                edit.apply(document); // apply after file is committable

                ITextFileBufferManager bufferManager = FileBuffers
                        .getTextFileBufferManager();
                bufferManager.getTextFileBuffer(file.getFullPath()).commit(
                        monitor, true);
                return;
            }
        }
        // no commit possible, make sure changes are in
        edit.apply(document);
    }

    public static void reconcile(ICompilationUnit unit)
            throws JavaModelException {
        synchronized (unit) {
            unit.reconcile(ICompilationUnit.NO_AST,
                    false /* don't force problem detection */,
                    null /* use primary owner */, null /*
                                                         * no progress monitor
                                                         */);
        }
    }

    public static boolean isLineDelimiterChar(char ch) {
        return ch == '\n' || ch == '\r';
    }

    private IRegion evaluateReplaceRange(IDocument document)
            throws JavaModelException, BadLocationException {
        reconcile(fCompilationUnit);

        IImportContainer container = fCompilationUnit.getImportContainer();
        if (container.exists()) {
            ISourceRange importSourceRange = container.getSourceRange();
            int startPos = importSourceRange.getOffset();
            int endPos = startPos + importSourceRange.getLength();
            if (!isLineDelimiterChar(document.getChar(endPos - 1))) {
                // if not already after a new line, go to beginning of next line
                // (if last char in new line -> import ends with a comment, see
                // 10557)
                int nextLine = document.getLineOfOffset(endPos) + 1;
                if (nextLine < document.getNumberOfLines()) {
                    int stopPos = document.getLineInformation(nextLine)
                            .getOffset();
                    // read to beginning of next character or beginning of next
                    // line
                    while (endPos < stopPos
                            && Character.isWhitespace(document.getChar(endPos))) {
                        endPos++;
                    }
                }
            }
            return new Region(startPos, endPos - startPos);
        }
        int start = getPackageStatementEndPos(document);
        return new Region(start, 0);
    }

    public MultiTextEdit getResultingEdits(IDocument document,
            IProgressMonitor monitor) throws JavaModelException,
            BadLocationException {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        try {
            fImportsCreated = new ArrayList<String>();
            fStaticImportsCreated = new ArrayList<String>();

            int importsStart = fReplaceRange.getOffset();
            int importsLen = fReplaceRange.getLength();

            String lineDelim = TextUtilities.getDefaultLineDelimiter(document);
            boolean useSpaceBetween = useSpaceBetweenGroups();

            int currPos = importsStart;
            MultiTextEdit resEdit = new MultiTextEdit();

            if (importsLen == 0) {
                // new import container
                resEdit.addChild(new InsertEdit(currPos, lineDelim)); // first
                // entry,
                // might
                // be
                // removed
                // later
            }

            PackageEntry lastPackage = null;

            Set<String> onDemandConflicts = null;
            if (fFindAmbiguousImports) {
                onDemandConflicts = evaluateStarImportConflicts(monitor);
            }

            ArrayList<String> stringsToInsert = new ArrayList<String>();

            for (PackageEntry pack : fPackageEntries) {
                int nImports = pack.getNumberOfImports();

                if (fFilterImplicitImports && !pack.isStatic()
                        && isImplicitImport(pack.getName(), fCompilationUnit)) {
                    pack.removeAllNew(onDemandConflicts);
                    nImports = pack.getNumberOfImports();
                }
                if (nImports == 0) {
                    continue;
                }

                if (useSpaceBetween) {
                    // add a space between two different groups by looking at
                    // the two adjacent imports
                    if (lastPackage != null && !pack.isComment()
                            && !pack.isSameGroup(lastPackage)) {
                        ImportDeclEntry last = lastPackage
                                .getImportAt(lastPackage.getNumberOfImports() - 1);
                        ImportDeclEntry first = pack.getImportAt(0);
                        if (!lastPackage.isComment()
                                && (last.isNew() || first.isNew())) {
                            stringsToInsert.add(lineDelim);
                        }
                    }
                }
                lastPackage = pack;

                boolean isStatic = pack.isStatic();

                boolean doStarImport = pack.hasStarImport(
                        fImportOnDemandThreshold, onDemandConflicts);
                if (doStarImport && (pack.find("*") == null)) { //$NON-NLS-1$
                    String starImportString = pack.getName() + ".*"; //$NON-NLS-1$
                    String str = getNewImportString(starImportString, isStatic,
                            lineDelim);
                    stringsToInsert.add(str);
                }

                for (int k = 0; k < nImports; k++) {
                    ImportDeclEntry currDecl = pack.getImportAt(k);
                    IRegion region = currDecl.getSourceRange();

                    if (region == null) { // new entry
                        if (!doStarImport
                                || currDecl.isOnDemand()
                                || (onDemandConflicts != null && onDemandConflicts
                                        .contains(currDecl.getSimpleName()))) {
                            String str = getNewImportString(currDecl
                                    .getElementName(), isStatic, lineDelim);
                            stringsToInsert.add(str);
                        }
                    } else {
                        if (!doStarImport
                                || currDecl.isOnDemand()
                                || onDemandConflicts == null
                                || onDemandConflicts.contains(currDecl
                                        .getSimpleName())) {
                            int offset = region.getOffset();
                            removeAndInsertNew(document, currPos, offset,
                                    stringsToInsert, resEdit);
                            stringsToInsert.clear();
                            currPos = offset + region.getLength();
                        }
                    }
                }
            }

            int end = importsStart + importsLen;
            removeAndInsertNew(document, currPos, end, stringsToInsert, resEdit);

            if (importsLen == 0) {
                if (!fImportsCreated.isEmpty()
                        || !fStaticImportsCreated.isEmpty()) { // new import
                    // container
                    if (fCompilationUnit.getPackageDeclarations().length == 0) { // no
                        // package
                        // statement
                        resEdit.removeChild(0);
                    }
                    // check if a space between import and first type is needed
                    IType[] types = fCompilationUnit.getTypes();
                    if (types.length > 0) {
                        if (types[0].getSourceRange().getOffset() == importsStart) {
                            resEdit
                                    .addChild(new InsertEdit(currPos, lineDelim));
                        }
                    }
                } else {
                    return new MultiTextEdit(); // no changes
                }
            }
            return resEdit;
        } finally {
            monitor.done();
        }
    }

    private void removeAndInsertNew(IDocument doc, int contentOffset,
            int contentEnd, List<String> stringsToInsert, MultiTextEdit resEdit)
            throws BadLocationException {
        int pos = contentOffset;
        for (String curr : stringsToInsert) {
            int idx = findInDocument(doc, curr, pos, contentEnd);
            if (idx != -1) {
                if (idx != pos) {
                    resEdit.addChild(new DeleteEdit(pos, idx - pos));
                }
                pos = idx + curr.length();
            } else {
                resEdit.addChild(new InsertEdit(pos, curr));
            }
        }
        if (pos < contentEnd) {
            resEdit.addChild(new DeleteEdit(pos, contentEnd - pos));
        }
    }

    private int findInDocument(IDocument doc, String str, int start, int end)
            throws BadLocationException {
        int pos = start;
        int len = str.length();
        if (pos + len > end || str.length() == 0) {
            return -1;
        }
        char first = str.charAt(0);
        int step = str.indexOf(first, 1);
        if (step == -1) {
            step = len;
        }
        while (pos + len <= end) {
            if (doc.getChar(pos) == first) {
                int k = 1;
                while (k < len && doc.getChar(pos + k) == str.charAt(k)) {
                    k++;
                }
                if (k == len) {
                    return pos; // found
                }
                if (k < step) {
                    pos += k;
                } else {
                    pos += step;
                }
            } else {
                pos++;
            }
        }
        return -1;
    }

    /**
     * @return Probes if the formatter allows spaces between imports
     */
    private boolean useSpaceBetweenGroups() {
        try {
            String sample = "import a.A;\n\n import b.B;\nclass C {}"; //$NON-NLS-1$
            TextEdit res = ToolFactory.createCodeFormatter(
                    fCompilationUnit.getJavaProject().getOptions(true)).format(
                    CodeFormatter.K_COMPILATION_UNIT, sample, 0,
                    sample.length(), 0, String.valueOf('\n'));
            Document doc = new Document(sample);
            res.apply(doc);
            int idx1 = doc.search(0, "import", true, true, false); //$NON-NLS-1$
            int line1 = doc.getLineOfOffset(idx1);
            int idx2 = doc.search(idx1 + 1, "import", true, true, false); //$NON-NLS-1$
            int line2 = doc.getLineOfOffset(idx2);
            return line2 - line1 > 1;
        } catch (BadLocationException e) {
            // should not happen
        }
        return true;
    }

    private Set<String> evaluateStarImportConflicts(IProgressMonitor monitor)
            throws JavaModelException {
        // long start= System.currentTimeMillis();

        final HashSet<String> onDemandConflicts = new HashSet<String>();

        IJavaSearchScope scope = SearchEngine
                .createJavaSearchScope(new IJavaElement[] { fCompilationUnit
                        .getJavaProject() });

        ArrayList<char[]> starImportPackages = new ArrayList<char[]>();
        ArrayList<char[]> simpleTypeNames = new ArrayList<char[]>();
        for (PackageEntry pack : fPackageEntries) {
            if (!pack.isStatic()
                    && pack.hasStarImport(fImportOnDemandThreshold, null)) {
                starImportPackages.add(pack.getName().toCharArray());
                for (int k = 0; k < pack.getNumberOfImports(); k++) {
                    ImportDeclEntry curr = pack.getImportAt(k);
                    if (!curr.isOnDemand() && !curr.isComment()) {
                        simpleTypeNames.add(curr.getSimpleName().toCharArray());
                    }
                }
            }
        }
        if (starImportPackages.isEmpty()) {
            return null;
        }

        starImportPackages.add(fCompilationUnit.getParent().getElementName()
                .toCharArray());
        starImportPackages.add(JAVA_LANG.toCharArray());

        char[][] allPackages = starImportPackages
                .toArray(new char[starImportPackages.size()][]);
        char[][] allTypes = simpleTypeNames
                .toArray(new char[simpleTypeNames.size()][]);

        TypeNameRequestor requestor = new TypeNameRequestor() {
            Map<String, String> foundTypes = new HashMap<String, String>();

            private String getTypeContainerName(char[] packageName,
                    char[][] enclosingTypeNames) {
                StringBuffer buf = new StringBuffer();
                buf.append(packageName);
                for (char[] enclosingTypeName : enclosingTypeNames) {
                    if (buf.length() > 0) {
                        buf.append('.');
                    }
                    buf.append(enclosingTypeName);
                }
                return buf.toString();
            }

            @Override
            public void acceptType(int flags, char[] packageName,
                    char[] simpleTypeName, char[][] enclosingTypeNames,
                    String path) {
                String name = new String(simpleTypeName);
                String containerName = getTypeContainerName(packageName,
                        enclosingTypeNames);

                String oldContainer = foundTypes.put(name,
                        containerName);
                if (oldContainer != null && !oldContainer.equals(containerName)) {
                    onDemandConflicts.add(name);
                }
            }
        };
        new SearchEngine().searchAllTypeNames(allPackages, allTypes, scope,
                requestor, IJavaSearchConstants.WAIT_UNTIL_READY_TO_SEARCH,
                monitor);
        return onDemandConflicts;
    }

    private String getNewImportString(String importName, boolean isStatic,
            String lineDelim) {
        StringBuffer buf = new StringBuffer();
        buf.append("import "); //$NON-NLS-1$
        if (isStatic) {
            buf.append("static "); //$NON-NLS-1$
        }
        buf.append(importName);
        buf.append(';');
        buf.append(lineDelim);
        // str= StubUtility.codeFormat(str, 0, lineDelim);

        if (isStatic) {
            fStaticImportsCreated.add(importName);
        } else {
            fImportsCreated.add(importName);
        }
        return buf.toString();
    }

    private int getPackageStatementEndPos(IDocument document)
            throws JavaModelException, BadLocationException {
        IPackageDeclaration[] packDecls = fCompilationUnit
                .getPackageDeclarations();
        if (packDecls != null && packDecls.length > 0) {
            ISourceRange range = packDecls[0].getSourceRange();
            int line = document.getLineOfOffset(range.getOffset()
                    + range.getLength());
            IRegion region = document.getLineInformation(line + 1);
            if (region != null) {
                IType[] types = fCompilationUnit.getTypes();
                if (types.length > 0) {
                    return Math.min(types[0].getSourceRange().getOffset(),
                            region.getOffset());
                }
                return region.getOffset();
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer("\n-----------------------\n"); //$NON-NLS-1$
        for (PackageEntry entry : fPackageEntries) {
            if (entry.isStatic()) {
                buf.append("static "); //$NON-NLS-1$
            }
            buf.append(entry.toString());
        }
        return buf.toString();
    }

    private static final class ImportDeclEntry {

        private String fElementName;

        private IRegion fSourceRange;

        private final boolean fIsStatic;

        public ImportDeclEntry(String elementName, boolean isStatic,
                IRegion sourceRange) {
            fElementName = elementName;
            fSourceRange = sourceRange;
            fIsStatic = isStatic;
        }

        public String getElementName() {
            return fElementName;
        }

        public int compareTo(String fullName, boolean isStatic) {
            int cmp = fElementName.compareTo(fullName);
            if (cmp == 0) {
                if (fIsStatic == isStatic) {
                    return 0;
                }
                return fIsStatic ? -1 : 1;
            }
            return cmp;
        }

        public String getSimpleName() {
            return Signature.getSimpleName(fElementName);
        }

        public boolean isOnDemand() {
            return fElementName != null && fElementName.endsWith(".*"); //$NON-NLS-1$
        }

        public boolean isStatic() {
            return fIsStatic;
        }

        public boolean isNew() {
            return fSourceRange == null;
        }

        public boolean isComment() {
            return fElementName == null;
        }

        public IRegion getSourceRange() {
            return fSourceRange;
        }

    }

    /*
     * Internal element for the import structure: A container for imports of all
     * types from the same package
     */
    private final static class PackageEntry {

        public static PackageEntry createOnPlaceholderEntry(
                String preferenceOrder) {
            if (preferenceOrder.length() > 0
                    && preferenceOrder.charAt(0) == '#') {
                String curr = preferenceOrder.substring(1);
                return new PackageEntry(curr, curr, true);
            }
            return new PackageEntry(preferenceOrder, preferenceOrder, false);
        }

        private String fName;

        private ArrayList<ImportDeclEntry> fImportEntries;

        private String fGroup;

        private boolean fIsStatic;

        /**
         * Comment package entry
         */
        public PackageEntry() {
            this("!", null, false); //$NON-NLS-1$
        }

        /**
         * @param name
         *            Name of the package entry. e.g. org.eclipse.jdt.ui,
         *            containing imports like org.eclipse.jdt.ui.JavaUI.
         * @param group
         *            The index of the preference order entry assigned different
         *            group id's will result in spacers between the entries
         * @param isStatic
         */
        public PackageEntry(String name, String group, boolean isStatic) {
            fName = name;
            fImportEntries = new ArrayList<ImportDeclEntry>(5);
            fGroup = group;
            fIsStatic = isStatic;
        }

        public boolean isStatic() {
            return fIsStatic;
        }

        public int compareTo(String name, boolean isStatic) {
            int cmp = fName.compareTo(name);
            if (cmp == 0) {
                if (fIsStatic == isStatic) {
                    return 0;
                }
                return fIsStatic ? -1 : 1;
            }
            return cmp;
        }

        public void sortIn(ImportDeclEntry imp) {
            String fullImportName = imp.getElementName();
            int insertPosition = -1;
            for (int i = 0; i < fImportEntries.size(); i++) {
                ImportDeclEntry curr = getImportAt(i);
                if (!curr.isComment()) {
                    int cmp = curr.compareTo(fullImportName, imp.isStatic());
                    if (cmp == 0) {
                        return; // exists already
                    } else if (cmp > 0 && insertPosition == -1) {
                        insertPosition = i;
                    }
                }
            }
            if (insertPosition == -1) {
                fImportEntries.add(imp);
            } else {
                fImportEntries.add(insertPosition, imp);
            }
        }

        public void add(ImportDeclEntry imp) {
            fImportEntries.add(imp);
        }

        public ImportDeclEntry find(String simpleName) {
            for (int i = 0; i < fImportEntries.size(); i++) {
                ImportDeclEntry curr = getImportAt(i);
                if (!curr.isComment()) {
                    String name = curr.getElementName();
                    if (name.endsWith(simpleName)) {
                        int dotPos = name.length() - simpleName.length() - 1;
                        if ((dotPos == -1)
                                || (dotPos > 0 && name.charAt(dotPos) == '.')) {
                            return curr;
                        }
                    }
                }
            }
            return null;
        }

        public boolean remove(String fullName, boolean isStaticImport) {
            for (int i = 0; i < fImportEntries.size(); i++) {
                ImportDeclEntry curr = getImportAt(i);
                if (!curr.isComment()
                        && curr.compareTo(fullName, isStaticImport) == 0) {
                    fImportEntries.remove(i);
                    return true;
                }
            }
            return false;
        }

        public void removeAllNew(Set<String> onDemandConflicts) {
            for (int i = fImportEntries.size() - 1; i >= 0; i--) {
                ImportDeclEntry curr = getImportAt(i);
                if (curr.isNew() /*
                                     * && (onDemandConflicts == null ||
                                     * onDemandConflicts.contains(curr.getSimpleName()))
                                     */) {
                    fImportEntries.remove(i);
                }
            }
        }

        public ImportDeclEntry getImportAt(int index) {
            return fImportEntries.get(index);
        }

        public boolean hasStarImport(int threshold, Set<String> explicitImports) {
            if (isComment() || isDefaultPackage()) { // can not star import
                // default package
                return false;
            }
            int count = 0;
            boolean containsNew = false;
            for (int i = 0; i < getNumberOfImports(); i++) {
                ImportDeclEntry curr = getImportAt(i);
                if (curr.isOnDemand()) {
                    return true;
                }
                if (!curr.isComment()) {
                    count++;
                    boolean isExplicit = !curr.isStatic()
                            && (explicitImports != null)
                            && explicitImports.contains(curr.getSimpleName());
                    containsNew |= curr.isNew() && !isExplicit;
                }
            }
            return (count >= threshold) && containsNew;
        }

        public int getNumberOfImports() {
            return fImportEntries.size();
        }

        public String getName() {
            return fName;
        }

        public String getGroupID() {
            return fGroup;
        }

        public void setGroupID(String groupID) {
            fGroup = groupID;
        }

        public boolean isSameGroup(PackageEntry other) {
            if (fGroup == null) {
                return other.getGroupID() == null;
            }
            return fGroup.equals(other.getGroupID())
                    && (fIsStatic == other.isStatic());
        }

        public ImportDeclEntry getLast() {
            int nImports = getNumberOfImports();
            if (nImports > 0) {
                return getImportAt(nImports - 1);
            }
            return null;
        }

        public boolean isComment() {
            return "!".equals(fName); //$NON-NLS-1$
        }

        public boolean isDefaultPackage() {
            return fName.length() == 0;
        }

        @Override
        public String toString() {
            StringBuffer buf = new StringBuffer();
            if (isComment()) {
                buf.append("comment\n"); //$NON-NLS-1$
            } else {
                buf.append(fName);
                buf.append(", groupId: ");buf.append(fGroup);buf.append("\n"); //$NON-NLS-1$ //$NON-NLS-2$
                for (int i = 0; i < getNumberOfImports(); i++) {
                    ImportDeclEntry curr = getImportAt(i);
                    buf.append("  "); //$NON-NLS-1$
                    if (curr.isStatic()) {
                        buf.append("static "); //$NON-NLS-1$
                    }
                    buf.append(curr.getSimpleName());
                    if (curr.isNew()) {
                        buf.append(" (new)"); //$NON-NLS-1$
                    }
                    buf.append("\n"); //$NON-NLS-1$
                }
            }
            return buf.toString();
        }
    }

    public String[] getCreatedImports() {
        if (fImportsCreated == null) {
            return new String[0];
        }
        return fImportsCreated.toArray(new String[fImportsCreated
                .size()]);
    }

    public String[] getCreatedStaticImports() {
        if (fStaticImportsCreated == null) {
            return new String[0];
        }
        return fStaticImportsCreated
                .toArray(new String[fStaticImportsCreated.size()]);
    }

    /**
     * Returns <code>true</code> if imports have been added or removed.
     * 
     * @return boolean
     */
    public boolean hasChanges() {
        return fHasChanges;
    }

}
