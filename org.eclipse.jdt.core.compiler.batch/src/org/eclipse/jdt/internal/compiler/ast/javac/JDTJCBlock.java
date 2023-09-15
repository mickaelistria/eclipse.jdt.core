package org.eclipse.jdt.internal.compiler.ast.javac;

import java.util.Arrays;
import java.util.stream.Stream;

import org.eclipse.jdt.internal.compiler.ast.AllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.ArrayTypeReference;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.ast.IntLiteral;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.QualifiedNameReference;
import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;
import org.eclipse.jdt.internal.compiler.ast.SingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.SuperReference;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;

import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;

public class JDTJCBlock extends JCBlock {

	public JDTJCBlock(Statement[] statements, Context context) {
		super(0, List.from(Stream.of(statements).map(jdt -> toJCStatement(jdt, context)).toList()));
	}

	private static JCStatement toJCStatement(Statement jdt, Context context) {
		TreeMaker maker = TreeMaker.instance(context);
		if (jdt instanceof ReturnStatement returnStatement) {
			JCReturn res = maker.Return(toJCExpression(returnStatement.expression,context));
			Utils.applyCommon(res, jdt);
			return res;
		} else if (jdt instanceof Expression exp) {
			JCExpressionStatement res = maker.Exec(toJCExpression(exp, context));
			Utils.applyCommon(res, jdt);
			return res;
		}
		throw new UnsupportedOperationException("Not yet supported: " + jdt);
	}

	public static JCExpression toJCExpression(Expression jdt, Context context) {
		if (jdt == null) {
			return null;
		}
		TreeMaker maker = TreeMaker.instance(context);
		Names names = Names.instance(context);
		Symtab table = Symtab.instance(context);
		if (jdt instanceof IntLiteral intLiteral) {
			intLiteral.computeConstant();
			JCLiteral res = maker.Literal(TypeTag.INT, intLiteral.constant.intValue());
			Utils.applyCommon(res, jdt);
			return res;
		} else if (jdt instanceof FieldReference fieldReference) {
			JCFieldAccess res = maker.Select(toJCExpression(fieldReference.receiver, context), Names.instance(context).fromString(new String(fieldReference.token)));
			Utils.applyCommon(res, jdt);
			return res;
		} else if (jdt instanceof MessageSend messageSend) {
			JCMethodInvocation res = maker.Apply(List.nil(), maker.Select(toJCExpression(messageSend.receiver, context), names.fromString(new String(messageSend.selector))), messageSend.arguments() == null ? List.nil() : //
				List.from(Arrays.stream(messageSend.arguments()).map(arg -> toJCExpression(arg, context)).toList()));
			Utils.applyCommon(res, jdt);
			return res;
		} else if (jdt instanceof QualifiedNameReference qnameRef) {
			JCExpression current = maker.Ident(names.fromString(new String(qnameRef.tokens[0])));
			for (int i = 1; i < qnameRef.tokens.length; i++) {
				current = maker.Select(current, names.fromString(new String(qnameRef.tokens[i])));
			}
			return current;
		} else if (jdt instanceof SuperReference superRef) {
			return maker.Super(null, null);
		} else if (jdt instanceof ThisReference thisRef) {
			return maker.Ident(Names.instance(context).fromString("this"));
		} else if (jdt instanceof AllocationExpression newExp) {
			return maker.NewClass(newExp.enclosingInstance() != newExp ? toJCExpression(newExp.enclosingInstance(), context) : null,
			newExp.typeArguments == null ? List.nil() : List.from(Arrays.stream(newExp.typeArguments).map(type -> toJCExpression(type, context)).toList()),
			toJCExpression(newExp.type, context),
			toJCExpressions(newExp.arguments, context), null);
		} else if (jdt instanceof SingleTypeReference singleTypeRef) {
			// missing: annotations, varargs...
			if (singleTypeRef.isBaseTypeReference()) {
				String token = new String(singleTypeRef.token);
				Type t = null;
				if (table.intType.tsym.name.toString().equals(token)) {
					t = table.intType;
				} else if (table.voidType.tsym.name.toString().equals(token)) {
					t = table.voidType;
				}
				return maker.Type(t);
			} else if (singleTypeRef instanceof ArrayTypeReference arrayType) {
				return maker.TypeArray(maker.Ident(names.fromString(new String(singleTypeRef.token))));
			}
			return maker.Ident(names.fromString(new String(singleTypeRef.token)));
		}
		throw new UnsupportedOperationException("Not yet supported: " + jdt);
	}

	public static List<JCExpression> toJCExpressions(Expression[] jdt, Context context) {
		return jdt == null ? List.nil() : //
			List.from(Arrays.stream(jdt).map(exp -> toJCExpression(exp, context)).toList());
	}
}
