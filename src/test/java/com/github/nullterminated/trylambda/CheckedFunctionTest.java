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
public class CheckedFunctionTest {

	public CheckedFunctionTest() {
	}

	/**
	 * Test of apply method, of class CheckedFunction.
	 */
	@Test
	public void testApply() {
		final Exception ex = new Exception();
		final CheckedFunction<Integer, Integer> fun1 = i -> i;
		final CheckedFunction<Integer, Integer> fun2 = i -> {
			throw ex;
		};
		assertEquals(Integer.valueOf(1), fun1.apply(1));
		assertThrows(WrappedException.class, () -> fun2.apply(1));
	}
}
