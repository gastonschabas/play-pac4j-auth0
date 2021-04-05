package com.gaston.auth.filter

import org.pac4j.play.filters.SecurityFilter
import play.api.http.HttpFilters

import javax.inject.Inject

class SecuredFilter @Inject() (securityFilter: SecurityFilter)
    extends HttpFilters {

  def filters: Seq[SecurityFilter] = Seq(securityFilter)

}
