package org.eclipse.jdt.internal.compiler.ast.javac;

import java.util.Arrays;

import org.eclipse.jdt.internal.compiler.ast.ImportReference;

import com.sun.tools.javac.tree.JCTree.JCPackageDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;

/**
 * 
 */

public class JDTJCPackageDecl extends JCPackageDecl {

	public JDTJCPackageDecl(final ImportReference packageDecl, Context context) {
		super(packageDecl.annotations == null ? List.nil() : List.from(Arrays.stream(packageDecl.annotations).map(JDTJCAnnotation::new).toArray(JCAnnotation[]::new)),
			TreeMaker.instance(context).Ident(Names.instance(context).fromString(new String(packageDecl.getSimpleName()))));
	}

}
