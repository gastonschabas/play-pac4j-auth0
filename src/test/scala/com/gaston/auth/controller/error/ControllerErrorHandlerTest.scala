package com.gaston.auth.controller.error

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import play.api.test.Helpers.defaultAwaitTimeout
import play.api.test.{FakeRequest, Helpers}

class ControllerErrorHandlerTest extends AnyFunSuite with Matchers {

  val controllerErrorHandler = new ControllerErrorHandler

  test("on server error return just a 500 status code") {
    Helpers.status(
      controllerErrorHandler.onServerError(FakeRequest(), new Exception)
    ) should be(Helpers.INTERNAL_SERVER_ERROR)
  }

  test("on client error return it should return the 4xx status code produced") {
    Helpers.status(
      controllerErrorHandler.onClientError(FakeRequest(), Helpers.BAD_REQUEST)
    ) should be(Helpers.BAD_REQUEST)
  }
}
