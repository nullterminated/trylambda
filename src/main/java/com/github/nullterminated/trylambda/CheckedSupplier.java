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

import java.util.function.Supplier;

/**
 * A supplier interface that automatically wraps any exceptions.
 *
 * @author Ramsey Gurley
 * @param <T> the supplier return type
 */
@FunctionalInterface
public interface CheckedSupplier<T> extends Supplier<T> {
	@Override
	default T get() {
		try {
			return gets();
		} catch (final WrappedException e) {
			throw e;
		} catch (final Exception e) {
			throw WrappedException.wrap(e);
		}
	}

	/**
	 * Same as {@link java.util.function.Supplier#get() get} except Exceptions may
	 * be thrown.
	 *
	 * @return the supplied object
	 * @throws Exception an exception
	 */
	T gets() throws Exception;
}
