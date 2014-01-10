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
package org.eclipse.riena.internal.communication.hessian;

import java.io.IOException;
import java.security.Principal;

import javax.security.auth.kerberos.KerberosPrincipal;
import javax.security.auth.login.LoginException;
import javax.security.auth.x500.X500Principal;

import com.sun.security.auth.NTDomainPrincipal;
import com.sun.security.auth.NTSid;
import com.sun.security.auth.NTSidDomainPrincipal;
import com.sun.security.auth.NTSidGroupPrincipal;
import com.sun.security.auth.NTSidPrimaryGroupPrincipal;
import com.sun.security.auth.NTSidUserPrincipal;

import org.eclipse.riena.internal.communication.factory.hessian.serializer.PrincipalSerializerFactory;
import org.eclipse.riena.security.common.authentication.SimplePrincipal;

/**
 * Test the {@code PrincipalSerializerFactory} class.
 */
public class PrincipalSerializerFactoryTest extends AbstractSerializerFactoryTestCase {

	public void testPrincipalFail() {
		assertFalse(isBackAndForthOk(new SimplePrincipal("Chef"), HessianSerializerVersion.Two, Principal.class));
	}

	public void testPrincipal1() {
		assertTrue(isBackAndForthOk(new SimplePrincipal("Chef"), HessianSerializerVersion.Two, null,
				new PrincipalSerializerFactory()));
	}

	public void testPrincipal2() {
		assertTrue(isBackAndForthOk(new SimplePrincipal("Chef"), HessianSerializerVersion.Two, Principal.class,
				new PrincipalSerializerFactory()));
	}

	public void testPrincipal3() {
		assertTrue(isBackAndForthOk(new SimplePrincipal("Chef"), HessianSerializerVersion.Two, SimplePrincipal.class,
				new PrincipalSerializerFactory()));
	}

	public void testNTDomainPrincipal1() {
		assertTrue(isBackAndForthOk(new NTDomainPrincipal("Boss"), HessianSerializerVersion.Two, null,
				new PrincipalSerializerFactory()));
	}

	public void testNTSidFail() throws LoginException, IOException {
		assertFalse(isBackAndForthOk(new NTSid("A"), HessianSerializerVersion.Two, null));
	}

	public void testNTSid1() throws LoginException, IOException {
		assertTrue(isBackAndForthOk(new NTSid("A"), HessianSerializerVersion.Two, null,
				new PrincipalSerializerFactory()));
	}

	public void testNTSid2() throws LoginException, IOException {
		assertTrue(isBackAndForthOk(new NTSid("A"), HessianSerializerVersion.Two, Principal.class,
				new PrincipalSerializerFactory()));
	}

	public void testNTSid3() throws LoginException, IOException {
		assertTrue(isBackAndForthOk(new NTSid("A"), HessianSerializerVersion.Two, NTSid.class,
				new PrincipalSerializerFactory()));
	}

	public void testNTSidDomainPrincipal() throws LoginException, IOException {
		assertTrue(isBackAndForthOk(new NTSidDomainPrincipal("DAE"), HessianSerializerVersion.Two, null,
				new PrincipalSerializerFactory()));
	}

	public void testNTSidGroupPrincipal() throws LoginException, IOException {
		assertTrue(isBackAndForthOk(new NTSidGroupPrincipal("DAE"), HessianSerializerVersion.Two, null,
				new PrincipalSerializerFactory()));
	}

	public void testNTSidPrimaryGroupPrincipal() throws LoginException, IOException {
		assertTrue(isBackAndForthOk(new NTSidPrimaryGroupPrincipal("DAE"), HessianSerializerVersion.Two, null,
				new PrincipalSerializerFactory()));
	}

	public void testNTSidUserPrincipalReturnPrincipal() throws LoginException, IOException {
		assertTrue(isBackAndForthOk(new NTSidUserPrincipal("DAE"), HessianSerializerVersion.Two, Principal.class,
				new PrincipalSerializerFactory()));
	}

	public void testNTSidUserPrincipal() throws LoginException, IOException {
		assertTrue(isBackAndForthOk(new NTSidUserPrincipal("DAE"), HessianSerializerVersion.Two, null,
				new PrincipalSerializerFactory()));
	}

	public void testKerberosPrincipalFailsBecauseTheGenericPrincipalSerializerFactoryCanNotHandleIt()
			throws LoginException, IOException {
		assertFalse(isBackAndForthOk(new KerberosPrincipal("duke@FOO.COM", KerberosPrincipal.KRB_NT_PRINCIPAL),
				HessianSerializerVersion.Two, null, new PrincipalSerializerFactory()));
	}

	public void testX500PrincipalFailsBecauseTheGenericPrincipalSerializerFactoryCanNotHandleIt()
			throws LoginException, IOException {
		assertFalse(isBackAndForthOk(new X500Principal("cn=hans"), HessianSerializerVersion.Two, null,
				new PrincipalSerializerFactory()));
	}
}
