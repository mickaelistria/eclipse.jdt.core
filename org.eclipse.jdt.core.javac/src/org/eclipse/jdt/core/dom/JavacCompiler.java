/*******************************************************************************
 * Copyright (c) 2024 Red Hat, Inc. and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.jdt.core.dom;

import java.util.Arrays;

import javax.tools.JavaFileObject;

import org.eclipse.core.runtime.ILog;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.Compiler;
import org.eclipse.jdt.internal.compiler.ICompilerRequestor;
import org.eclipse.jdt.internal.compiler.IErrorHandlingPolicy;
import org.eclipse.jdt.internal.compiler.IProblemFactory;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;

import com.sun.tools.javac.main.JavaCompiler;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;

public class JavacCompiler extends Compiler {

	public JavacCompiler(INameEnvironment environment, IErrorHandlingPolicy policy, CompilerOptions options,
			ICompilerRequestor requestor, IProblemFactory problemFactory) {
		super(environment, policy, options, requestor, problemFactory);
		//TODO Auto-generated constructor stub
	}

	@Override
	public void compile(ICompilationUnit[] sourceUnits) {
		Context javacContext = createJavacContext();
		JavaCompiler javac = JavaCompiler.instance(javacContext);
		try {
			javac.compile(List.from(Arrays.stream(sourceUnits).map(this::toJavaFileObject).toList()));
		} catch (Throwable e) {
			ILog.get().error(e.getMessage(), e);
//			problemReporter.
		}
	}

	private Context createJavacContext() {
		throw new UnsupportedOperationException("Unimplemented method 'createJavacContext'");
	}

	private JavaFileObject toJavaFileObject(ICompilationUnit icompilationunit1) {
		throw new UnsupportedOperationException("Unimplemented method");
	}

}
