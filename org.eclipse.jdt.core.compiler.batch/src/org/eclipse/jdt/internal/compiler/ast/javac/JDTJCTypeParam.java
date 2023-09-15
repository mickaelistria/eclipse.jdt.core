package org.eclipse.jdt.internal.compiler.ast.javac;

import java.util.Arrays;

import org.eclipse.jdt.internal.compiler.ast.TypeParameter;

import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;

/**
 * 
 */

public class JDTJCTypeParam extends JCTypeParameter {

	public static List<JCTypeParameter> formArray(TypeParameter[] jdt, Context context) {
		return jdt == null ? List.nil() : //
			List.from(Arrays.stream(jdt).map(typeParam -> new JDTJCTypeParam(typeParam, context)).toList());
	}

	public JDTJCTypeParam(TypeParameter jdt, Context context) {
		super(Names.instance(context).fromString(new String(jdt.name)),
			List.nil(),
			Utils.toJCAnnotationList(jdt.annotations));
	}

//	public JDTJCTypeParam(TypeReference jdt, Context context) {
//		super(Names.instance(context).fromString(new String(jdt.)),
//			List.nil(),
//			Utils.toJCAnnotationList(jdt.annotations));
//	}
}
