package org.eclipse.jdt.internal.compiler.ast.javac;

import java.util.Arrays;

import org.eclipse.jdt.internal.compiler.ast.Argument;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;

public class JDTJCVariableDecl extends JCVariableDecl {

	public JDTJCVariableDecl(Argument param, Context context) {
		super(JDTJCTypeDecl.toJCModifiers(param.modifiers | Flags.PARAMETER, param.annotations, context),
			Names.instance(context).fromString(new String(param.name)),
			JDTJCBlock.toJCExpression(param.type, context),
			null,
			null);
		Utils.applyCommon(this, param);
	}

	public static List<JCVariableDecl> fromArray(Argument[] arguments, Context context) {
		return arguments == null ? List.nil() : //
			List.from(Arrays.stream(arguments).map(param -> new JDTJCVariableDecl(param, context)).toList());
	}
	

}
