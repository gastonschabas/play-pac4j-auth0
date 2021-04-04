package com.gaston.auth

import play.api.mvc.{
  AbstractController,
  Action,
  AnyContent,
  ControllerComponents
}

import javax.inject.Inject

class Hello @Inject() (cc: ControllerComponents)
    extends AbstractController(cc) {

  def index: Action[AnyContent] = Action(Ok)
}
