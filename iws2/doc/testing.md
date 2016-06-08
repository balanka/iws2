# Testing

Testing Scala.js application is as easy as testing regular Scala applications, except you have to choose a test framework that is OK with
the limitations of the JavaScript environment. Many popular frameworks like Specs2 depend on JVM features (like reflection) that
are not available in the JS land, so Li Haoyi went ahead and created [uTest](https://github.com/lihaoyi/utest), a simple yet powerful
testing framework that works wonderfully well with Scala.js. ScalaTest has now been ported to work with Scala.js (version 3.0.0-M5 and above) so that's
another option you can use.

To define tests, you just need to extend from `TestSuite` and override the `tests` method.

```scala
object SPACircuitTests extends TestSuite {
  override def tests = TestSuite {
    'test { ... }
  }
}
```

Take a look at
[`SPACircuitTests.scala`](https://github.com/ochrons/scalajs-spa-tutorial/tree/master/client/src/test/scala/spatutorial/client/services/SPACircuitTests.scala)
for some examples of test cases.

To run tests in SBT, you'll need to add a dependency for `"com.lihaoyi" %%% "utest" % "0.3.1"` and configure the test framework with `testFrameworks += new
TestFramework("utest.runner.Framework")`. Now you can run the tests using regular `test` and `testOnly` commands in the SBT prompt.

