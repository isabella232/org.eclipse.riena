/** Copyright 2004, 2005 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Addendum:
 * The original version of this file belongs to the retired Apache HiveMind 
 * project: http://hivemind.apache.org/hivemind1/index.html
 * The original files can be found in hivemind-1.1.jar in package org.apache.hivemind.order
 * 
 * The original version has been modified entirely by Riena committers such:
 * - removed dependencies from HiveMind
 * - added generics, uses extended for loops, ..
 * - adapted to Riena/Eclipse coding conventions 
 */
package org.eclipse.riena.core.util;

import java.util.List;

import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.NonUITestCase;
import org.eclipse.riena.core.util.Orderer.OrdererFailure;

/**
 * Tests for the {@link org.apache.hivemind.order.Orderer}.
 * 
 * @author Howard Lewis Ship
 */
@NonUITestCase
public class OrdererTest extends RienaTestCase {

	public void testNoDependencies() {
		final Orderer<String> o = new Orderer<String>();

		o.add("FRED", "fred", null, null);
		o.add("BARNEY", "barney", null, null);
		o.add("WILMA", "wilma", null, null);
		o.add("BETTY", "betty", null, null);

		final List<String> l = o.getOrderedObjects();

		assertEquals(Literal.list("FRED").list("BARNEY").list("WILMA").list("BETTY"), l);
	}

	public void testPrereq() {
		final Orderer<String> o = new Orderer<String>();

		o.add("FRED", "fred", "wilma", null);
		o.add("BARNEY", "barney", "betty", null);
		o.add("BETTY", "betty", null, null);
		o.add("WILMA", "wilma", null, null);

		final List<String> l = o.getOrderedObjects();

		assertEquals(Literal.list("WILMA").list("FRED").list("BETTY").list("BARNEY"), l);
	}

	public void testPostreq() {
		final Orderer<String> o = new Orderer<String>();

		o.add("FRED", "fred", null, "barney,wilma");
		o.add("BARNEY", "barney", null, "betty");
		o.add("BETTY", "betty", null, null);
		o.add("WILMA", "wilma", null, null);

		final List<String> l = o.getOrderedObjects();

		assertEquals(Literal.list("FRED").list("BARNEY").list("BETTY").list("WILMA"), l);
	}

	public void testPrePostreq() {
		final Orderer<String> o = new Orderer<String>();

		o.add("FRED", "fred", null, "barney,wilma");
		o.add("BARNEY", "barney", "wilma", "betty");
		o.add("BETTY", "betty", null, null);
		o.add("WILMA", "wilma", null, null);

		final List<String> l = o.getOrderedObjects();

		assertEquals(Literal.list("FRED").list("WILMA").list("BARNEY").list("BETTY"), l);
	}

	public void testUnknownPrereq() {
		final Orderer<String> o = new Orderer<String>();

		o.add("FRED", "fred", "charlie", "barney,wilma");
		o.add("BARNEY", "barney", "wilma", "betty");
		o.add("BETTY", "betty", null, null);
		o.add("WILMA", "wilma", null, null);

		final List<String> l = o.getOrderedObjects();

		assertEquals(Literal.list("FRED").list("WILMA").list("BARNEY").list("BETTY"), l);
		// TODO check logging: expect logging for charlie -> fred
	}

	public void testUnknownPostreq() {
		final Orderer<String> o = new Orderer<String>();

		o.add("FRED", "fred", null, "barney,wilma");
		o.add("BARNEY", "barney", "wilma", "betty");
		o.add("BETTY", "betty", null, "dino");
		o.add("WILMA", "wilma", null, null);

		final List<String> l = o.getOrderedObjects();

		assertEquals(Literal.list("FRED").list("WILMA").list("BARNEY").list("BETTY"), l);

		// TODO Check logging: Unknown cartoon character dependency 'dino' (for 'betty').", null, null);
	}

	public void testCyclePre() {
		try {
			final Orderer<String> o = new Orderer<String>();

			o.add("FRED", "fred", "wilma", null);
			o.add("BARNEY", "barney", "betty", null);
			o.add("BETTY", "betty", "fred", null);
			o.add("WILMA", "wilma", "barney", null);

			final List<String> l = o.getOrderedObjects();
			fail();
		} catch (final OrdererFailure e) {
			assertTrue(e.getMessage().contains("between 'wilma'"));
		}
		//		assertListsEqual(new Object[] { "WILMA", "FRED", "BETTY", "BARNEY" }, l);
	}

	public void testCyclePost() {
		try {
			final Orderer<String> o = new Orderer<String>();

			o.add("WILMA", "wilma", null, "betty");
			o.add("FRED", "fred", null, "barney");
			o.add("BARNEY", "barney", null, "wilma");
			o.add("BETTY", "betty", null, "fred");

			final List<String> l = o.getOrderedObjects();
			fail();
		} catch (final OrdererFailure e) {
			assertTrue(e.getMessage().contains("between 'fred' and 'betty'"));
		}
		//		assertListsEqual(new Object[] { "FRED", "BARNEY", "WILMA", "BETTY" }, l);
	}

	public void testDupe() {
		final Orderer<String> o = new Orderer<String>();

		o.add("FRED", "flintstone", null, null);
		o.add("BARNEY", "rubble", null, null);

		//		interceptLogging();
		try {
			o.add("WILMA", "flintstone", null, null);
			fail();
		} catch (final OrdererFailure e) {
			assertTrue(e.getMessage().contains("'flintstone'"));
		}

		//		List<String> l = o.getOrderedObjects();
		//
		//		assertListsEqual(new Object[] { "FRED", "BARNEY" }, l);
	}

	public void testPreStar() {
		final Orderer<String> o = new Orderer<String>();

		o.add("FRED", "fred", "*", null);
		o.add("BARNEY", "barney", "betty", null);
		o.add("WILMA", "wilma", "betty", null);
		o.add("BETTY", "betty", null, null);

		final List<String> l = o.getOrderedObjects();

		assertEquals(Literal.list("BETTY").list("BARNEY").list("WILMA").list("FRED"), l);
	}

	public void testPreStartDupe() {
		final Orderer<String> o = new Orderer<String>();

		o.add("FRED", "fred", "*", null);
		o.add("BARNEY", "barney", "*", null);
		o.add("WILMA", "wilma", "betty", null);
		o.add("BETTY", "betty", null, null);

		try {
			final List<String> l = o.getOrderedObjects();
			assertEquals(Literal.list("BARNEY").list("BETTY").list("WILMA").list("FRED"), l);
			fail();
		} catch (final OrdererFailure e) {
			assertTrue(e.getMessage().contains(
					"More than one trailer. Conflicting 'fred' (ordered unknown) and 'barney' (last)"));
		}

	}

	public void testPostStar() {
		final Orderer<String> o = new Orderer<String>();

		o.add("FRED", "fred", null, "wilma");
		o.add("BARNEY", "barney", null, "*");
		o.add("WILMA", "wilma", null, "betty");
		o.add("BETTY", "betty", null, null);

		final List<String> l = o.getOrderedObjects();

		assertEquals(Literal.list("BARNEY").list("FRED").list("WILMA").list("BETTY"), l);
	}

	public void testPostStarDupe() {
		final Orderer<String> o = new Orderer<String>();

		o.add("FRED", "fred", null, "wilma");
		o.add("BARNEY", "barney", null, "*");
		o.add("WILMA", "wilma", null, "*");
		o.add("BETTY", "betty", null, null);

		try {
			final List<String> l = o.getOrderedObjects();
			assertEquals(Literal.list("BARNEY").list("FRED").list("WILMA").list("BETTY"), l);
			fail();
		} catch (final OrdererFailure e) {
			assertTrue(e.getMessage().contains(
					"More than one leader. Conflicting 'barney' (ordered unknown) and 'wilma' (first)"));
		}

	}

	public void testNoObjects() {
		final Orderer<String> o = new Orderer<String>();

		final List<String> l = o.getOrderedObjects();

		assertEquals(0, l.size());
	}

}