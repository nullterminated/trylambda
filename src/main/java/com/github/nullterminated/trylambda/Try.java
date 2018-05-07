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

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A utility class for handling try blocks using lambda expressions.
 *
 * @author Ramsey Gurley
 */
public final class Try {
	/**
	 * Private constructor.
	 */
	private Try() {
	}

	/**
	 * A method to use auto closing try with resources with lambda parameters. The supplier argument supplies the
	 * AutoCloseable resource of the try block, which is passed to the function argument in the body of the try block.
	 * The result of the function argument is rewrapped in a new supplier so that calls to this method may be chained.
	 * If a {@link WrappedException} is thrown by the supplier or function arguments, it is rethrown. All other
	 * exceptions are passed to the errorHandler argument for handling.
	 *
	 * @param <T>          the supplied AutoCloseable type
	 * @param <R>          the supplied result type
	 * @param supplier     the AutoCloseable supplier
	 * @param errorHandler a function to handle exceptions thrown by the supplier or function arguments
	 * @param function     a function that accepts the supplied AutoCloseable type and returns a supplier of the result
	 *                     type
	 * @return a supplier of the result type
	 */
	public static <T extends AutoCloseable, R> CheckedSupplier<R> trys(
			final CheckedSupplier<T> supplier,
			final Function<Exception, CheckedSupplier<R>> errorHandler,
			final Function<T, CheckedSupplier<R>> function) {
		CheckedSupplier<R> result;
		try (final T resource = supplier.gets()) {
			final R value = function.apply(resource).gets();
			result = () -> value;
		} catch (final WrappedException e) {
			throw e;
		} catch (final Exception e) {
			result = errorHandler.apply(e);
		}
		return result;
	}

	/**
	 * Calls the three argument form of this method with an error handler which simply wraps any exception in a
	 * {@link WrappedException}.
	 *
	 * @param <T>      the supplied AutoCloseable type
	 * @param <R>      the supplied result type
	 * @param supplier the AutoCloseable supplier
	 * @param function a function that accepts the supplied AutoCloseable type and returns a supplier of the result type
	 * @return a supplier of the result type
	 */
	public static <T extends AutoCloseable, R> CheckedSupplier<R> trys(
			final CheckedSupplier<T> supplier,
			final Function<T, CheckedSupplier<R>> function) {
		return trys(supplier, e -> {
			throw WrappedException.wrap(e);
		}, function);
	}

	/**
	 * Wraps the call to {@link java.util.function.Supplier#get() get} in a try block. Either an exception thrown by the
	 * supplier or the supplied value is returned. If a {@link WrappedException} is thrown, its cause is returned.
	 *
	 * @param <R>      the supplied value type
	 * @param supplier the value supplier
	 * @return either an exception thrown by the supplier or the supplied value
	 */
	public static <R> Either<Exception, R> either(Supplier<R> supplier) {
		Either<Exception, R> result;
		try {
			result = Either.right(supplier.get());
		} catch (WrappedException e) {
			result = Either.left(((WrappedException) e).getCause());
		} catch (Exception e) {
			result = Either.left(e);
		}
		return result;
	}

	/**
	 * This method exists to allow usage of {@link #either(java.util.function.Supplier) either} with checked suppliers
	 * without casting or needing to declare the checked supplier explicitly.
	 *
	 * @param <R>      the supplied value type
	 * @param supplier the supplier function
	 * @return either an exception or the supplied value
	 */
	public static <R> Either<Exception, R> either(CheckedSupplier<R> supplier) {
		return either((Supplier<R>) supplier);
	}
}
