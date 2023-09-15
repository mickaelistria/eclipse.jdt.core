package org.eclipse.jdt.internal.compiler.ast.javac;

import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Annotation;
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;

public class JDTJCTypeDecl extends JCClassDecl {

	public JDTJCTypeDecl(TypeDeclaration jdt, Context context) {
		super(toJCModifiers(jdt.modifiers, jdt.annotations, context), //
			Names.instance(context).fromString(new String(jdt.name)), //
			/*TODO typarams*/ List.nil(), //
			/*extending*/ null, //
			/*TODO implementing*/ List.nil(), //
			/*TODO permitting*/ List.nil(),
			/*TODO defs*/ toDefs(jdt.methods, context), //
			/*ClassSymbol sym*/ null);
		Utils.applyCommon(this, jdt);
	}

	static JCModifiers toJCModifiers(long modifiers, Annotation[] annotations, Context context) {
		return TreeMaker.instance(context).Modifiers(modifiers,
				Utils.toJCAnnotationList(annotations));
	}

	private static List<JCTree> toDefs(AbstractMethodDeclaration[] methods, Context context) {
		List<JCTree> res = List.nil();
		for (AbstractMethodDeclaration aMethod : methods) {
			if (!aMethod.isDefaultConstructor() && aMethod instanceof MethodDeclaration method) {
				res = res.append(new JDTJCMethodDecl(method, context));
			}
		}
		return res;
	}
}
