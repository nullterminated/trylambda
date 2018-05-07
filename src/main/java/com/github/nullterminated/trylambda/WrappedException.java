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

import java.util.Objects;

/**
 * A simple unchecked exception wrapper for wrapping checked {@link java.lang.Exception Exception} types. Throwable
 * types are not handled since Throwable includes system errors like
 * {@link java.lang.OutOfMemoryError OutOfMemoryError}.
 *
 * @author Ramsey Gurley
 */
public final class WrappedException extends RuntimeException {
	/**
	 * Private constructor to prevent multiply nested WrappedExceptions.
	 *
	 * @param message the exception message
	 * @param cause   the original exception
	 */
	private WrappedException(String message, Exception cause) {
		super(message, cause);
	}

	/**
	 * Private constructor to prevent multiply nested WrappedExceptions.
	 *
	 * @param cause the original exception
	 */
	private WrappedException(Exception cause) {
		super(cause);
	}

	/**
	 * Overridden to return Exception types instead of Throwable.
	 *
	 * @return an Exception
	 */
	@Override
	public Exception getCause() {
		return (Exception) super.getCause();
	}

	/**
	 * Factory method to generate a WrappedException. If the Exception argument is itself a WrappedException, the cause
	 * of that argument is rewrapped with the new message argument.
	 *
	 * @param message the exception message
	 * @param e       the original exception
	 * @return a new WrappedException
	 */
	public static WrappedException wrap(String message, Exception e) {
		if (e instanceof WrappedException) {
			return wrap(message, ((WrappedException) e).getCause());
		} else {
			return new WrappedException(message, Objects.requireNonNull(e));
		}
	}

	/**
	 * Factory method to generate a WrappedException. If the Exception argument is itself a WrappedException, the cause
	 * of that argument is rewrapped.
	 *
	 * @param e the original exception
	 * @return a new WrappedException
	 */
	public static WrappedException wrap(Exception e) {
		if (e instanceof WrappedException) {
			return wrap(e.getMessage(), ((WrappedException) e).getCause());
		} else {
			return new WrappedException(Objects.requireNonNull(e));
		}
	}
}
