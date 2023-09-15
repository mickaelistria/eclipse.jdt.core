package org.eclipse.jdt.internal.compiler.ast.javac;

import java.util.Arrays;

import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.Annotation;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.util.List;

public class Utils {

	public static void applyCommon(JCTree javac, ASTNode jdt) {
		javac.pos = jdt.sourceEnd;
	}

	public static List<JCAnnotation> toJCAnnotationList(Annotation[] annotations) {
		return annotations == null ?
			List.nil() : //
			List.from(Arrays.stream(annotations).map(JDTJCAnnotation::new).toArray(JCAnnotation[]::new));
	}
}
