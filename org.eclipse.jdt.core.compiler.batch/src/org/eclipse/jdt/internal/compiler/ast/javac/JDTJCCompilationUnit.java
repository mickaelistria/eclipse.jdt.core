package org.eclipse.jdt.internal.compiler.ast.javac;

import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;

import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;

public class JDTJCCompilationUnit extends JCCompilationUnit {
	private final Context context;

	public JDTJCCompilationUnit(CompilationUnitDeclaration jdt, Context context) {
		super(List.nil());
		this.context = context;
		if (jdt.moduleDeclaration != null) {
			this.defs = this.defs.append(new JDTJCModuleDecl(jdt.moduleDeclaration));
		}
		if (jdt.currentPackage != null) {
			this.defs = this.defs.append(new JDTJCPackageDecl(jdt.currentPackage, this.context));
		}
		for (TypeDeclaration jdtType : jdt.types) {
			this.defs = this.defs.append(new JDTJCTypeDecl(jdtType, this.context));
		}
	}
	
}
