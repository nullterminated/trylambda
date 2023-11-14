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
/**
 * <h2>trylambda</h2>
 * <p>
 * <img src=
 * "https://i.pinimg.com/originals/b9/4a/7f/b94a7f548b303e81b1bcd8da4d0912ec.jpg"
 * alt="nerds"> <br>
 * A small Java library to improve checked exception handling in lambda
 * expressions.
 * </p>
 * <h3 id="contents">Contents</h3>
 * <ol>
 * <li><a href="#why">Why trylambda?</a></li>
 * <li><a href="#wrap">Wrapped Exceptions</a></li>
 * <li><a href="#eith">Either</a></li>
 * <li><a href="#res">Try With Resources</a></li>
 * <li><a href="#ex">Examples</a></li>
 * <li><a href="#lic">License</a></li>
 * </ol>
 * <h3 id="why">Why</h3>
 * <p>
 * trylambda aims to solve two problems.
 * </p>
 * <ul>
 * <li>There's no built in way to deal with checked exceptions in Java 8 lambda
 * expressions.</li>
 * <li>Using try with resources blocks in Java 7 often results in test induced
 * design damage.</li>
 * </ul>
 * <h3 id="wrap">Wrapped Exceptions</h3>
 * <p>
 * The first strategy for dealing with checked exceptions in lambda expressions
 * is to wrap them in a runtime exception and rethrow. trylambda extends the
 * standard {@link java.util.function.Consumer Consumer},
 * {@link java.util.function.Function Function}, and
 * {@link java.util.function.Supplier Supplier} interfaces to provide versions
 * of each that throw checked exceptions. This is done by overriding their
 * functional interface with a default implementation which wraps any exception
 * thrown in a {@link com.github.nullterminated.trylambda.WrappedException
 * WrappedException}.
 * </p>
 * <h3 id="eith">Either</h3>
 * <p>
 * Simply throwing the exception isn't typically sufficient. For that reason,
 * trylambda implements the {@link com.github.nullterminated.trylambda.Either
 * Either} type from other functional programming languages. Rather than
 * throwing an exception, it can be caught and returned as part of the result.
 * Code using the returned value may then handle either the exception or the
 * expected result.
 * </p>
 * <h3 id="res">Try With Resources</h3>
 * <p>
 * <img src="https://i.stack.imgur.com/SggR6.png" alt="4 0f 8 branches
 * missed"><br>
 * With these classes, trylambda can execute a try with resources block, without
 * negatively impacting unit test branch coverage. The
 * {@link com.github.nullterminated.trylambda.Try Try} class has utility methods
 * to execute try with resources blocks in a functional manner. The result is
 * fewer, easier to test branches.
 * </p>
 * <h3 id="ex">Examples</h3>
 * <p>
 * As an example of checked exception handling, consider opening a list of URLs.
 * url.openConnection() may throw an IOException. Dealing with this in a stream
 * of URLs is straightforward with trylambda. It looks like this:
 * </p>
 *
 * <pre>
 * <code>
 * List&lt;Either&lt;Exception,URLConnection&gt;&gt; eithers = urls.stream()
 * .map(url -&gt; Try.either(() -&gt; url.openConnection()))
 * .collect(Collectors.toList());
 * </code>
 * </pre>
 * <p>
 * URLConnections are an easy case, because they do not need to be closed. As an
 * example of auto closing, let's first consider what a traditional try with
 * resources block might look like. For this example, we want to open a database
 * connection, and handle the results:
 * </p>
 *
 * <pre>
 * <code>
try (Connection conn = DriverManager.getConnection(url);
	PreparedStatement stmt = conn.prepareStatement(sql);
	ResultSet rslt = stmt.executeQuery();) {
		R result = handleResults(conn, stmt, rslt);
} catch (SQLException e) {
		//Handle exception here
}
 * </code>
 * </pre>
 * <p>
 * A unit test which only checks success will miss 21 of 24 branches. Using
 * trylambda, this same expression can be rewritten as:
 * </p>
 *
 * <pre>
 * <code>
either(() -&gt; trys(() -&gt; DriverManager.getConnection(url),
	conn -&gt; trys(() -&gt; conn.prepareStatement(sql),
		stmt -&gt; trys(() -&gt; stmt.executeQuery(),
			rslt -&gt; () -&gt; handleResults(conn, stmt, rslt)))).get());
 * </code>
 * </pre>
 * <p>
 * With success testing all branches. This permits skipping the inherent penalty
 * to test coverage associated with try with resources blocks, while still
 * enabling the testing of exceptional cases that may happen. Rather than
 * returning a result type R, instead the result here is Either&lt;Exception,
 * R&gt; returned by the outermost call to the either method.
 * </p>
 * <h3 id="lic">License</h3>
 * <p>
 * trylambda is dual license. It is available under AGPLv3 or a commercial
 * license. The current license fee for commercial licensing is $5.00 per
 * project per year.
 * </p>
 */
package com.github.nullterminated.trylambda;
