package org.eclipse.jdt.internal.compiler.ast.javac;

import org.eclipse.jdt.internal.compiler.ast.Annotation;

import com.sun.tools.javac.tree.JCTree.JCAnnotation;

/**
 * 
 */

public class JDTJCAnnotation extends JCAnnotation {

	public JDTJCAnnotation(Annotation jdt) {
		super(null, null, null);
		throw new UnsupportedOperationException();
	}
}
