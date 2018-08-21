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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 *
 * @author Ramsey Gurley
 */
public class CheckedSupplierTest {

	public CheckedSupplierTest() {
	}

	/**
	 * Test of get method, of class CheckedSupplier.
	 */
	@Test
	public void testGet() {
		final Exception ex = new Exception();
		final WrappedException wrap = WrappedException.wrap(ex);
		final CheckedSupplier<Integer> supplier1 = () -> {
			throw ex;
		};
		final CheckedSupplier<Integer> supplier2 = () -> 2;
		final CheckedSupplier<Integer> supplier3 = () -> {
			throw wrap;
		};
		assertEquals(Integer.valueOf(2), supplier2.get());
		assertThrows(WrappedException.class, () -> supplier1.get());
		assertThrows(WrappedException.class, () -> supplier3.get());
	}
}
