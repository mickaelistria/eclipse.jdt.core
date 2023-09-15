package org.eclipse.jdt.internal.compiler.ast.javac;

import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;

import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;

/**
 *
 */

public class JDTJCMethodDecl extends JCMethodDecl {

	protected JDTJCMethodDecl(MethodDeclaration jdt, Context context) {
		super(JDTJCTypeDecl.toJCModifiers(jdt.modifiers, jdt.annotations, context),
			Names.instance(context).fromString(new String(jdt.selector)),
			JDTJCBlock.toJCExpression(jdt.returnType, context),
			JDTJCTypeParam.formArray(jdt.typeParameters, context),
			null,
			JDTJCVariableDecl.fromArray(jdt.arguments, context),
			List.nil(),
			new JDTJCBlock(jdt.statements, context),
			null,
			null);
		Utils.applyCommon(this, jdt);
	}
//
//	private static List<JCVariableDecl> flagParameters(List<JCVariableDecl> fromArray) {
//		fromArray.forEach(variableDecl -> variableDecl.mods.flags |= Flags.PARAMETER);
//		return fromArray;
//	}

//	public static Type toJCType(TypeReference jdt, Context context) {
//		Symtab table = Symtab.instance(context);
//		Types types = Types.instance(context);
//		if (jdt instanceof SingleTypeReference singleType) {
//			Type t = null;
//			if (singleType.isBaseTypeReference()) {
//				String token = new String(singleType.token);
//				if (table.intType.tsym.name.toString().equals(token)) {
//					t = table.intType;
//				} else if (table.voidType.tsym.name.toString().equals(token)) {
//					t = table.voidType;
//				}
//			} else {
////				table.stringType
//			}
//			if (singleType instanceof ArrayTypeReference arrayType) {
//				t = types.makeArrayType(t);
//			}
//			return t;
//		}
//		throw new UnsupportedOperationException("Type not translated to Javac: " + jdt);
//	}

}
