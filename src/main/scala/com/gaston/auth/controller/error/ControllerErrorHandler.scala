package com.gaston.auth.controller.error

import play.api.Logging
import play.api.http.HttpErrorHandler
import play.api.mvc.Results.{InternalServerError, Status}
import play.api.mvc.{RequestHeader, Result}

import scala.concurrent.Future

class ControllerErrorHandler extends HttpErrorHandler with Logging {

  def onClientError(
    request: RequestHeader,
    statusCode: Int,
    message: String
  ): Future[Result] = {
    Future.successful(Status(statusCode)("A client error occurred: " + message))
  }

  def onServerError(
    request: RequestHeader,
    exception: Throwable
  ): Future[Status] = {
    Future.successful {
      logger.error("Error occurred", exception)
      InternalServerError
    }
  }
}
