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

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A Java implementation of the Either monad.
 *
 * The Either type represents values with two possibilities: a value of type
 * Either a b is either Left a or Right b.
 *
 * The Either type is sometimes used to represent a value which is either
 * correct or an error; by convention, the Left constructor is used to hold an
 * error value and the Right constructor is used to hold a correct value
 * (mnemonic: "right" also means "correct").
 *
 * Pattern matching is accomplished using polymorphism.
 *
 * @param <A> the left value type
 * @param <B> the right value type
 * @author Ramsey Gurley
 */
public abstract class Either<A, B> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Private constructor to limit subclasses to inner classes.
	 */
	private Either() {
	}

	/**
	 * Factory method for constructing lefts.
	 *
	 * @param <A>  the left type
	 * @param <B>  the right type
	 * @param left the left value
	 * @return a new left
	 */
	public static <A, B> Either<A, B> left(final A left) {
		return new Left<>(left);
	}

	/**
	 * Factory method for constructing rights.
	 *
	 * @param <A>   the left type
	 * @param <B>   the right type
	 * @param right the right value
	 * @return a new right
	 */
	public static <A, B> Either<A, B> right(final B right) {
		return new Right<>(right);
	}

	/**
	 *
	 * @return true if left
	 */
	public final boolean isLeft() {
		return this instanceof Left;
	}

	/**
	 *
	 * @return true if right
	 */
	public final boolean isRight() {
		return this instanceof Right;
	}

	/**
	 *
	 * @return the left value
	 * @throws UnsupportedOperationException if the receiver is right
	 */
	public abstract A getLeft();

	/**
	 *
	 * @return the right value
	 * @throws UnsupportedOperationException if the receiver is left
	 */
	public abstract B getRight();

	/**
	 * Pass the value of this either to a consumer.
	 *
	 * @param leftConsumer  the consumer for lefts
	 * @param rightConsumer the consumer for rights
	 */
	public abstract void use(Consumer<A> leftConsumer, Consumer<B> rightConsumer);

	/**
	 * Map this Either&lt;A,B&gt; to a new Either&lt;X,Y&gt;.
	 *
	 * @param <X>           the new left type
	 * @param <Y>           the new right type
	 * @param leftFunction  function to convert A to X
	 * @param rightFunction function to convert B to Y
	 * @return a new Either&lt;X,Y&gt;
	 */
	public abstract <X, Y> Either<X, Y> map(Function<A, X> leftFunction, Function<B, Y> rightFunction);

	/**
	 * Reduce an Either&lt;A,B&gt; to a single value type T.
	 *
	 * @param <T>           the result type
	 * @param leftFunction  function to convert A to T
	 * @param rightFunction function to convert B to T
	 * @return a value typed T
	 */
	public abstract <T> T reduce(Function<A, T> leftFunction, Function<B, T> rightFunction);

	/**
	 * Flip an Either&lt;A,B&gt; to a Either&lt;B,A&gt;.
	 *
	 * @return a new either with types flipped
	 */
	public abstract Either<B, A> flip();

	public static final class Left<A, B> extends Either<A, B> {

		private static final long serialVersionUID = 1L;
		private final A left;

		private Left(final A left) {
			if (left == null) {
				throw new IllegalArgumentException("left is null");
			}
			this.left = left;
		}

		@Override
		public A getLeft() {
			return left;
		}

		@Override
		public B getRight() {
			throw new UnsupportedOperationException("Cannot getRight from Left.");
		}

		@Override
		public int hashCode() {
			final int hash = 7;
			return 17 * hash + Objects.hashCode(left);
		}

		@Override
		public boolean equals(final Object obj) {
			return this == obj || obj != null && Objects.equals(getClass(), obj.getClass())
					&& Objects.equals(left, ((Left<?, ?>) obj).left);
		}

		@Override
		public String toString() {
			return "Left{" + "left=" + left + '}';
		}

		@Override
		public void use(final Consumer<A> leftConsumer, final Consumer<B> rightConsumer) {
			leftConsumer.accept(left);
		}

		@Override
		public <X, Y> Either<X, Y> map(final Function<A, X> leftFunction, final Function<B, Y> rightFunction) {
			return new Left<>(leftFunction.apply(left));
		}

		@Override
		public <T> T reduce(final Function<A, T> leftFunction, final Function<B, T> rightFunction) {
			return leftFunction.apply(left);
		}

		@Override
		public Either<B, A> flip() {
			return new Right<>(left);
		}
	}

	public static final class Right<A, B> extends Either<A, B> {

		private static final long serialVersionUID = 1L;

		private final B right;

		private Right(final B right) {
			if (right == null) {
				throw new IllegalArgumentException("right is null");
			}
			this.right = right;
		}

		@Override
		public A getLeft() {
			throw new UnsupportedOperationException("Cannot getLeft from Right.");
		}

		@Override
		public B getRight() {
			return right;
		}

		@Override
		public int hashCode() {
			final int hash = 3;
			return 41 * hash + Objects.hashCode(right);
		}

		@Override
		public boolean equals(final Object obj) {
			return this == obj || obj != null && Objects.equals(getClass(), obj.getClass())
					&& Objects.equals(right, ((Right<?, ?>) obj).right);
		}

		@Override
		public String toString() {
			return "Right{" + "right=" + right + '}';
		}

		@Override
		public void use(final Consumer<A> leftConsumer, final Consumer<B> rightConsumer) {
			rightConsumer.accept(right);
		}

		@Override
		public <X, Y> Either<X, Y> map(final Function<A, X> leftFunction, final Function<B, Y> rightFunction) {
			return new Right<>(rightFunction.apply(right));
		}

		@Override
		public <T> T reduce(final Function<A, T> leftFunction, final Function<B, T> rightFunction) {
			return rightFunction.apply(right);
		}

		@Override
		public Either<B, A> flip() {
			return new Left<>(right);
		}
	}
}
