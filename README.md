# trylambda
![nerds](https://i.pinimg.com/originals/b9/4a/7f/b94a7f548b303e81b1bcd8da4d0912ec.jpg)

A small Java library to improve checked exception handling in lambda expressions.

## Contents

1. [Why trylambda?](#why-trylambda)
2. [Wrapped Exceptions](#wrapped-exceptions)
3. [Either](#either)
4. [Try with resources](#try-with-resources)
5. [Examples](#examples)
6. [License](#license)

## Why trylambda?

* There's no built in way to deal with checked exceptions in Java 8 lambda expressions.
* Using try with resources blocks in Java 7 often results in test induced design damage.

## Wrapped Exceptions

The first strategy for dealing with checked exceptions in lambda expressions is to wrap them in a runtime exception and rethrow. trylambda extends the standard Consumer, Function, and Supplier interfaces to provide versions of each that throw checked exceptions. This is done by overriding their functional interface with a default implementation which wraps any exception thrown in a WrappedException.

## Either

Simply throwing the exception isn't typically sufficient. For that reason, trylambda implements the Either type from other functional programming languages. Rather than throwing an exception, it can be caught and returned as part of the result. Code using the returned value may then handle either the exception or the expected result.

## Try with Resources

![4 0f 8 branches missed](https://i.stack.imgur.com/SggR6.png)

With these classes, trylambda can execute a try with resources block, without negatively impacting unit test branch coverage. The Try class has utility methods to execute try with resources blocks in a functional manner. The result is fewer, easier to test branches.

## Examples

To add trylambda to a project with maven, add the following to the pom.xml depenedencies declaration:

```xml
<dependency>
    <groupId>com.github.nullterminated</groupId>
    <artifactId>trylambda</artifactId>
    <version>1.1</version>
</dependency>
```

As an example of checked exception handling, consider opening a list of URLs. url.openConnection() may throw an IOException. Dealing with this in a stream of URLs is straightforward with trylambda. It looks like this:

```java
List<Either<Exception,URLConnection>> eithers = urls.stream()
		.map(url -> Try.either(() -> url.openConnection()))
		.collect(Collectors.toList());
```

URLConnections are an easy case, because they do not need to be closed. As an example of auto closing, let's first consider what a traditional try with resources block might look like. For this example, we want to open a database connection, and handle the results:

```java
try (Connection conn = DriverManager.getConnection(url);
	PreparedStatement stmt = conn.prepareStatement(sql);
	ResultSet rslt = stmt.executeQuery();) {
		R result = handleResults(conn, stmt, rslt);
} catch (SQLException e) {
		//Handle exception here
}
```

A unit test which only checks success will miss 21 of 24 branches. Using trylambda, this same expression can be rewritten as:

```java
either(() -> trys(() -> DriverManager.getConnection(url),
	conn -> trys(() -> conn.prepareStatement(sql),
		stmt -> trys(() -> stmt.executeQuery(),
			rslt -> () -> handleResults(conn, stmt, rslt)))).get());
```

With success testing all branches. This permits skipping the inherent penalty to test coverage associated with try with resources blocks, while still enabling the testing of exceptional cases that may happen. Rather than returning a result type R, instead the result here is Either&lt;Exception, R&gt; returned by the outermost call to the either method.

## License

trylambda is dual license. It is available under AGPLv3 or a commercial license. The current license fee for commercial licensing is $5.00 per project per year.
