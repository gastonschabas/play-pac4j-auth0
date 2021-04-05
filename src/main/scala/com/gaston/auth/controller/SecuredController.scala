package com.gaston.auth.controller

import org.pac4j.core.profile.CommonProfile
import org.pac4j.play.scala.{Security, SecurityComponents}
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, Result}

import javax.inject.Inject

class SecuredController @Inject() (val controllerComponents: SecurityComponents)
    extends Security[CommonProfile] {

  def unsecuredEndpoint: Action[AnyContent] = Action(
    Ok("Hello unsecured endpoint")
  )

  def securedEndpointWithReadPermission: Action[AnyContent] =
    Secure("DirectBearerAuthClient", "read") { implicit request =>
      jsonResponse(request, "read")
    }

  def securedEndpointWithWritePermission: Action[AnyContent] =
    Secure("DirectBearerAuthClient", "write") { implicit request =>
      jsonResponse(request, "write")
    }

  private def jsonResponse(request: AuthenticatedRequest[AnyContent], permission: String): Result = Ok(
    Json.obj(
      "profile" -> request.profiles.toString,
      "msg" -> s"Hello endpoint with $permission permission"
    )
  )

}
