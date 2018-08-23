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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 *
 * @author Ramsey Gurley
 */
public class EitherTest {

	public EitherTest() {
	}

	static class Holder<T> {

		T value;
	}

	/**
	 * Test of use method, of class Either.
	 */
	@Test
	public void testUse() {
		final List<Either<Integer, String>> eithers = Arrays.asList(
				Either.left(1),
				Either.right("hello"),
				Either.right("world"),
				Either.left(3));
		final Holder<Integer> sum = new Holder<>();
		sum.value = 0;
		eithers.stream().forEach(either -> either.use(
				left -> sum.value += left,
				right -> sum.value += right.length()));
		assertEquals(Integer.valueOf(14), sum.value);
	}

	/**
	 * Test of reduce method, of class Either.
	 */
	@Test
	public void testReduce() {
		final List<Either<Integer, String>> eithers = Arrays.asList(
				Either.left(1),
				Either.right("hello"),
				Either.right("world"),
				Either.left(3));
		final Holder<Integer> sum = new Holder<>();
		sum.value = 0;
		eithers.forEach(either -> sum.value += either.reduce(
				left -> left,
				right -> right.length()));
		assertEquals(14, (int) sum.value);
	}

	/**
	 * Test of isLeft method, of class Either.
	 */
	@Test
	public void testIsLeft() {
		final List<Either<Integer, String>> eithers = Arrays.asList(
				Either.left(1),
				Either.right("hello"),
				Either.right("world"),
				Either.right("!"),
				Either.left(3));
		final long lefts = eithers.stream()
				.filter(Either::isLeft)
				.count();
		assertEquals(2l, lefts);
	}

	/**
	 * Test of isRight method, of class Either.
	 */
	@Test
	public void testIsRight() {
		final List<Either<Integer, String>> eithers = Arrays.asList(
				Either.left(1),
				Either.right("hello"),
				Either.right("world"),
				Either.right("!"),
				Either.left(3));
		final long rights = eithers.stream()
				.filter(Either::isRight)
				.count();
		assertEquals(3l, rights);
	}

	/**
	 * Test of getLeft method, of class Either.
	 */
	@Test
	public void testGetLeft() {
		final List<Either<Integer, String>> eithers = Arrays.asList(
				Either.left(1),
				Either.right("hello"),
				Either.right("world"),
				Either.right("!"),
				Either.left(3));
		final long lefts = eithers.stream()
				.filter(Either::isLeft)
				.map(Either::getLeft)
				.count();
		assertEquals(2l, lefts);
		assertThrows(UnsupportedOperationException.class, () -> Either.right("test").getLeft());
	}

	/**
	 * Test of getRight method, of class Either.
	 */
	@Test
	public void testGetRight() {
		final List<Either<Integer, String>> eithers = Arrays.asList(
				Either.left(1),
				Either.right("hello"),
				Either.right("world"),
				Either.right("!"),
				Either.left(3));
		final long rights = eithers.stream()
				.filter(Either::isRight)
				.map(Either::getRight)
				.count();
		assertEquals(3l, rights);
		assertThrows(UnsupportedOperationException.class, () -> Either.left("test").getRight());
	}

	@Test
	public void testEquals() {
		final Either<String, String> right = Either.right("test");
		final Either<String, String> left = Either.left("test");
		assertTrue(right.equals(right));
		assertTrue(left.equals(left));
		assertTrue(right.equals(Either.right("test")));
		assertTrue(left.equals(Either.left("test")));
		assertFalse(right.equals(Either.right("test1")));
		assertFalse(left.equals(Either.left("test1")));
		assertFalse(left.equals(right));
		assertFalse(right.equals(left));
		assertFalse(left.equals(null));
		assertFalse(right.equals(null));
	}

	@Test
	public void testHashCode() {
		assertEquals(3556617, Either.left("test").hashCode());
		assertEquals(3556621, Either.right("test").hashCode());
	}

	@Test
	public void testToString() {
		assertEquals("Left{left=test}", Either.left("test").toString());
		assertEquals("Right{right=test}", Either.right("test").toString());
	}

	/**
	 * Test of left method, of class Either.
	 */
	@Test
	public void testLeft() {
		final Object left = "left";
		final Either<Object, Object> expResult = Either.left("left");
		final Either<Object, Object> result = Either.left(left);
		assertEquals(expResult, result);
		assertThrows(IllegalArgumentException.class, () -> Either.left(null));
	}

	/**
	 * Test of right method, of class Either.
	 */
	@Test
	public void testRight() {
		final Object right = "right";
		final Either<Object, Object> expResult = Either.right("right");
		final Either<Object, Object> result = Either.right(right);
		assertEquals(expResult, result);
		assertThrows(IllegalArgumentException.class, () -> Either.right(null));
	}

	/**
	 * Test of map method, of class Either.
	 */
	@Test
	public void testMap() {
		final Either<Integer, Object> instance = Either.left(1);
		final Either<String, Object> expResult = Either.left("1");
		final Either<String, Object> result = instance.map(Object::toString, null);
		assertEquals(expResult, result);

		final Either<Object, Integer> instance2 = Either.right(1);
		final Either<Object, String> expResult2 = Either.right("1");
		final Either<Object, String> result2 = instance2.map(null, Object::toString);
		assertEquals(expResult2, result2);
	}

	/**
	 * Test of flip method, of class Either.
	 */
	@Test
	public void testFlip() {
		assertEquals(Either.right(1), Either.left(1).flip());
		assertEquals(Either.left(1), Either.right(1).flip());
	}

}
