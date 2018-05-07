/*
 * Copyright (C) 2018 Ramsey Gurley
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.nullterminated.trylambda;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 *
 * @author Ramsey Gurley
 */
public class CheckedConsumerTest {

	public CheckedConsumerTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	private static class Holder {
		int value = 0;
	}

	/**
	 * Test of accept method, of class CheckedConsumer.
	 */
	@Test
	public void testAccept() {
		final Holder holds = new Holder();
		final Exception ex = new Exception();
		final WrappedException wrap = WrappedException.wrap(ex);
		final CheckedConsumer<Integer> cons1 = i -> {
			holds.value = i;
		};
		final CheckedConsumer<Integer> cons2 = i -> {
			throw ex;
		};
		final CheckedConsumer<Integer> cons3 = i -> {
			throw wrap;
		};
		cons1.accept(1);
		assertEquals(1, holds.value);
		assertThrows(WrappedException.class, () -> cons2.accept(1));
		assertThrows(WrappedException.class, () -> cons3.accept(1));
	}
}
