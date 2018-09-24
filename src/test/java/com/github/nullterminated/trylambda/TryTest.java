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

import static com.github.nullterminated.trylambda.Try.either;
import static com.github.nullterminated.trylambda.Try.trys;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.function.Function;
import java.util.function.Supplier;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Ramsey Gurley
 */
public class TryTest {

	private static final RuntimeException EX = new RuntimeException();
	private final CheckedSupplier<ByteArrayOutputStream> out = ByteArrayOutputStream::new;
	private final CheckedSupplier<ByteArrayOutputStream> nil = () -> null;
	private final CheckedSupplier<ByteArrayOutputStream> thr = () -> {
		throw EX;
	};
	private final CheckedSupplier<ByteArrayOutputStream> clo = () -> {
		return new ByteArrayOutputStream() {
			@Override
			public void close() throws IOException {
				throw EX;
			}
		};
	};
	private final Function<Exception, CheckedSupplier<Integer>> handler = e -> () -> 1;
	private final Function<Exception, CheckedSupplier<Integer>> thrHandler = e -> {
		throw (RuntimeException) e;
	};

	public TryTest() {
	}

	@Test
	public void testUtilClass() throws Throwable {
		final Class<?> clazz = Try.class;
		assertTrue(Modifier.isFinal(clazz.getModifiers()));
		assertEquals(1, clazz.getDeclaredConstructors().length);
		final Constructor<?> cons = clazz.getDeclaredConstructor();
		assertTrue(Modifier.isPrivate(cons.getModifiers()));
	}

	static Integer doIt(OutputStream os1, ByteArrayOutputStream os2) {
		return 1;
	}

	static Integer doEx(OutputStream os1, ByteArrayOutputStream os2) {
		throw EX;
	}

	/**
	 * Test of trys method, of class Try.
	 */
	@Test
	public void testTrys() {
		assertEquals(Integer.valueOf(1), trys(out, os1 -> trys(out, os2 -> () -> doIt(os1, os2))).get());
		assertEquals(Integer.valueOf(1), trys(out, os1 -> trys(nil, os2 -> () -> doIt(os1, os2))).get());
		assertEquals(Integer.valueOf(1), trys(out, os1 -> trys(thr, handler, os2 -> () -> doIt(os1, os2))).get());
		assertThrows(RuntimeException.class,
				() -> trys(out, thrHandler, os1 -> trys(nil, thrHandler, os2 -> () -> doEx(os1, os2))).get());
		assertThrows(WrappedException.class, () -> trys(out, os1 -> trys(thr, os2 -> () -> doIt(os1, os2))).get());
		assertThrows(WrappedException.class, () -> trys(out, os1 -> trys(clo, os2 -> () -> doIt(os1, os2))).get());
		assertThrows(WrappedException.class, () -> trys(out, os1 -> trys(out, os2 -> () -> doEx(os1, os2))).get());
		assertThrows(WrappedException.class, () -> trys(out, os1 -> trys(nil, os2 -> () -> doEx(os1, os2))).get());
		assertThrows(WrappedException.class, () -> trys(out, os1 -> trys(clo, os2 -> () -> doEx(os1, os2))).get());
	}

	/**
	 * Test of either method, of class Try.
	 */
	@Test
	public void testEither() {
		final Exception ex = new Exception();
		final Supplier<Integer> supplier = () -> {
			throw EX;
		};
		assertEquals(Either.right(1), either(() -> 1));
		assertEquals(Either.left(ex), either(() -> {
			throw ex;
		}));
		assertEquals(Either.left(EX), either(supplier));
	}
}
