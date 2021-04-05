package com.gaston.auth.controller.error

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import play.api.test.Helpers.defaultAwaitTimeout
import play.api.test.{FakeRequest, Helpers}

class ControllerErrorHandlerTest extends AnyFunSuite with Matchers {

  val controllerErrorHandler = new ControllerErrorHandler

  test("on server error return 500 status code") {
    val result =
      controllerErrorHandler.onServerError(FakeRequest(), new Exception)
    Helpers.status(result) should be(Helpers.INTERNAL_SERVER_ERROR)
  }

  test("on server error return empty message") {
    val result =
      controllerErrorHandler.onServerError(FakeRequest(), new Exception)
    Helpers.contentAsString(result) should be(empty)
  }

  test("on client error it should return 4xx status code") {
    val result =
      controllerErrorHandler.onClientError(FakeRequest(), Helpers.BAD_REQUEST)
    Helpers.status(result) should be(Helpers.BAD_REQUEST)
  }

  test(
    s"on client error it should return a message starting with ${ControllerErrorHandler.onClientErrorMessage}"
  ) {
    val result =
      controllerErrorHandler.onClientError(FakeRequest(), Helpers.BAD_REQUEST)
    Helpers.status(result) should be(Helpers.BAD_REQUEST)
    Helpers.contentAsString(result) should startWith(
      ControllerErrorHandler.onClientErrorMessage
    )
  }
}
