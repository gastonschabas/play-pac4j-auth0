package com.gaston.auth

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import play.api.test.Helpers.defaultAwaitTimeout
import play.api.test.{FakeRequest, Helpers}

class HelloTest extends AnyFunSuite with Matchers {

  test("hello index endpoint should return http status code 200") {
    val hello = new Hello(Helpers.stubControllerComponents())
    Helpers.status(hello.index.apply(FakeRequest())) should be(Helpers.OK)
  }
}
