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

/**
 * A function interface that automatically wraps any exceptions.
 *
 * @param <T> the function argument type
 * @param <R> the function result type
 * @author Ramsey Gurley
 */
public interface CheckedFunction<T, R> extends Function<T, R> {
	@Override
	default R apply(final T arg) {
		try {
			return applies(arg);
		} catch (final Exception e) {
			throw WrappedException.wrap(e);
		}
	}

	/**
	 * Same as {@link java.util.function.Function#apply(java.lang.Object) apply}
	 * except Exceptions may be thrown.
	 *
	 * @param arg the function argument
	 * @return the function result
	 * @throws Exception an exception
	 */
	R applies(T arg) throws Exception;
}
