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
public class WrappedExceptionTest {

	public WrappedExceptionTest() {
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

	/**
	 * Test of wrap method, of class WrappedException.
	 */
	@Test
	public void testWrap_String_Throwable() {
		String message1 = "1";
		String message2 = "2";
		Exception e = new Exception();
		WrappedException wrap1 = WrappedException.wrap(message1, e);
		WrappedException wrap2 = WrappedException.wrap(message2, wrap1);
		assertEquals(e, wrap1.getCause());
		assertEquals(message1, wrap1.getMessage());
		assertEquals(e, wrap2.getCause());
		assertEquals(message2, wrap2.getMessage());
		assertThrows(NullPointerException.class, () -> WrappedException.wrap("message", null));
	}

	/**
	 * Test of wrap method, of class WrappedException.
	 */
	@Test
	public void testWrap_Throwable() {
		Exception e = new Exception();
		WrappedException wrap1 = WrappedException.wrap(e);
		WrappedException wrap2 = WrappedException.wrap(wrap1);
		assertEquals(e, wrap1.getCause());
		assertEquals(e, wrap2.getCause());
		assertThrows(NullPointerException.class, () -> WrappedException.wrap(null));
	}

}
