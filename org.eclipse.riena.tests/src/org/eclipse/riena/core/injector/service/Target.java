/*******************************************************************************
 * Copyright (c) 2007, 2014 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.core.injector.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The target for injecting
 */
public class Target {

	private final Map<String, List<Class<?>>> deps = new HashMap<String, List<Class<?>>>();
	private IRanking ranking;

	public void bind(final DepOne dep) {
		called("bind", DepOne.class, dep);
		add("bind", DepOne.class);
	}

	public void unbind(final DepOne dep) {
		called("unbind", DepOne.class, dep);
		remove("bind", DepOne.class);
	}

	public void bind(final DepTwo dep) {
		called("bind", DepTwo.class, dep);
		add("bind", DepTwo.class);
	}

	public void unbind(final DepTwo dep) {
		called("unbind", DepTwo.class, dep);
		remove("bind", DepTwo.class);
	}

	public void binde(final DepOne dep) {
		called("binde", DepOne.class, dep);
		add("binde", DepOne.class);
	}

	public void entbinde(final DepOne dep) {
		called("entbinde", DepOne.class, dep);
		remove("binde", DepOne.class);
	}

	public void bind(final IRanking dep) {
		called("bind", IRanking.class, dep);
		add("bind", IRanking.class);
		this.ranking = dep;
	}

	public void unbind(final IRanking dep) {
		called("unbind", IRanking.class, dep);
		remove("bind", IRanking.class);
	}

	/**
	 * @param string
	 * @param object
	 */
	private void called(final String methodName, final Class<?> type, final Object object) {
		System.out.println(methodName + "(" + type.getSimpleName() + ") -> " + object.getClass().getSimpleName());
	}

	public int count(final String method, final Class<?> clazz) {
		final List<Class<?>> deps4method = deps.get(key(method, clazz));
		if (deps4method != null) {
			return deps4method.size();
		}
		return 0;
	}

	public int getDepRanking() {
		return ranking.getRanking();
	}

	private void add(final String method, final Class<?> depClass) {
		List<Class<?>> deps4method = deps.get(key(method, depClass));
		if (deps4method == null) {
			deps4method = new ArrayList<Class<?>>();
			deps.put(key(method, depClass), deps4method);
		}
		deps4method.add(depClass);
	}

	private void remove(final String method, final Class<?> depClass) {
		final List<Class<?>> deps4method = deps.get(key(method, depClass));
		if (deps4method != null) {
			deps4method.remove(depClass);
		}
	}

	private String key(final String methodKey, final Class<?> type) {
		return methodKey + "(" + type.getName() + ")";
	}
}
