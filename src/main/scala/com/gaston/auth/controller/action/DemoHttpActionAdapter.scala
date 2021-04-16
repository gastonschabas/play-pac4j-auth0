package com.gaston.auth.controller.action

import org.pac4j.core.context.HttpConstants
import org.pac4j.core.exception.http.HttpAction
import org.pac4j.play.PlayWebContext
import org.pac4j.play.http.PlayHttpActionAdapter
import play.mvc.{Result, Results, StatusHeader}

object DemoHttpActionAdapter extends PlayHttpActionAdapter {

  override def adapt(action: HttpAction, context: PlayWebContext): Result = {
    val supplementResponseFor =
      (code: Int, result: StatusHeader) =>
        Option(action)
          .filter(a => a.getCode == code)
          .map(_ => context.supplementResponse(result))

    supplementResponseFor(HttpConstants.UNAUTHORIZED, Results.unauthorized)
      .orElse(supplementResponseFor(HttpConstants.FORBIDDEN, Results.forbidden))
      .getOrElse(super.adapt(action, context))
  }
}
