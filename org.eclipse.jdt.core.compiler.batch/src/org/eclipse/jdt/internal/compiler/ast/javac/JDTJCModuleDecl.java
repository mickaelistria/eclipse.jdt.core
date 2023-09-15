package org.eclipse.jdt.internal.compiler.ast.javac;

import org.eclipse.jdt.internal.compiler.ast.ModuleDeclaration;

import com.sun.tools.javac.tree.JCTree.JCModuleDecl;

/**
 * 
 */

public class JDTJCModuleDecl extends JCModuleDecl {


	public JDTJCModuleDecl(ModuleDeclaration jdt) {
		super(null, null, null, null);
		throw new UnsupportedOperationException();
	}
}
