package org.eclipse.jdt.internal.compiler.ast.javac;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Queue;

import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;

import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.batch.CompilationUnit;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.parser.Parser;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;

import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.Modules;
import com.sun.tools.javac.comp.Todo;
import com.sun.tools.javac.file.JavacFileManager;
import com.sun.tools.javac.main.JavaCompiler;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Pair;

public class JDTJCCompiler {

	public static void main(String... args) {
		ICompilationUnit unit = new CompilationUnit("""
			//package lol;
			public class A {
				public int return42() {
					return 42;
				}
				public static void main(String[] args) {
					System.err.println(new A().return42());
				}
			}
			""".toCharArray(), "A.java", StandardCharsets.UTF_8.toString(), null, false, null);
		ProblemReporter problemReporter = new ProblemReporter(DefaultErrorHandlingPolicies.proceedWithAllProblems(), new CompilerOptions(), new DefaultProblemFactory());
		CompilationResult compilationResult = new CompilationResult(unit, 0, 0, Integer.MAX_VALUE);
		Parser parser = new Parser(problemReporter, true);
		Context context = new Context();
		context.put(JavaFileManager.class, new JavacFileManager(context, true, Charset.defaultCharset()));
		JavaCompiler javac = new com.sun.tools.javac.main.JavaCompiler(context);
		CompilationUnitDeclaration jdtUnit = parser.parse(unit, compilationResult);
		JDTJCCompilationUnit jdtJcCompilationUnit = new JDTJCCompilationUnit(jdtUnit, context);
		jdtJcCompilationUnit.sourcefile = new SimpleJavaFileObject(new File(System.getProperty("java.io.tmpdir"), "A.java").toURI(), JavaFileObject.Kind.SOURCE) {
			@Override
			public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
				return new String(unit.getContents());
			}
		};
		Modules.instance(context).initModules(List.of(jdtJcCompilationUnit));
		Todo todo = Todo.instance(context);
		System.out.println("Enter...");
		javac.enterTrees(List.of(jdtJcCompilationUnit));
		System.out.println("<Annotation processing should be plugged in here and looping with Enter until it doesn't generate change>");
		System.out.println("Attribute...");
		Queue<Env<AttrContext>> attribute = javac.attribute(new LinkedList<>(List.of(todo.peek())));
		System.out.println("Flow...");
		Queue<Env<AttrContext>> flow = javac.flow(attribute);
		System.out.println("Desugar...");
		Queue<Pair<Env<AttrContext>, JCClassDecl>> desugar = javac.desugar(flow);
		System.out.println("Gen...");
		javac.generate(desugar);
		System.out.println("Done!");
		// TODO verify with javap
	}
}
