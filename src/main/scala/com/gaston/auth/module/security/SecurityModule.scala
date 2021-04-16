package com.gaston.auth.module.security

import com.gaston.auth.controller.action.DemoHttpActionAdapter
import com.gaston.auth.module.security.authorization.CommonProfileAuthorizationGenerator
import com.google.inject.{AbstractModule, Provides}
import com.nimbusds.jose.jwk.{JWK, JWKSet}
import org.pac4j.core.authorization.authorizer.RequireAnyPermissionAuthorizer
import org.pac4j.core.client.Clients
import org.pac4j.core.client.direct.AnonymousClient
import org.pac4j.core.config.Config
import org.pac4j.core.profile.CommonProfile
import org.pac4j.http.client.direct.DirectBearerAuthClient
import org.pac4j.jwt.config.encryption.RSAEncryptionConfiguration
import org.pac4j.jwt.config.signature.RSASignatureConfiguration
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator
import org.pac4j.jwt.util.JWKHelper
import org.pac4j.play.scala.{DefaultSecurityComponents, SecurityComponents}
import org.pac4j.play.store.{
  PlayCookieSessionStore,
  PlaySessionStore,
  ShiroAesDataEncrypter
}
import play.api.{Configuration, Environment, Logging}

import java.net.URL
import java.nio.charset.StandardCharsets
import java.security.KeyPair
import scala.jdk.CollectionConverters._

class SecurityModule(environment: Environment, configuration: Configuration)
    extends AbstractModule
    with Logging {

  @SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
  override def configure(): Unit = {
    val sKey =
      configuration.get[String]("play.http.secret.key")
    val dataEncrypter = new ShiroAesDataEncrypter(
      sKey.getBytes(StandardCharsets.UTF_8)
    )
    val playSessionStore = new PlayCookieSessionStore(dataEncrypter)
    bind(classOf[PlaySessionStore]).toInstance(playSessionStore)
    bind(classOf[SecurityComponents]).to(classOf[DefaultSecurityComponents])
  }

  @Provides
  def provideBearerAuthClient: DirectBearerAuthClient = {
    val publicKeys: JWKSet =
      JWKSet.load(new URL(configuration.get[String]("auth.jwks.url")))
    val jwkSet: Seq[JWK] = publicKeys.getKeys.asScala.toList
    val rsaKeyPairFromJwk: Seq[KeyPair] =
      jwkSet.map(jwk => JWKHelper.buildRSAKeyPairFromJwk(jwk.toJSONString))
    val jwtAuthenticator = new JwtAuthenticator()
    rsaKeyPairFromJwk.foreach { rsaKeyPair =>
      jwtAuthenticator.addEncryptionConfiguration(
        new RSAEncryptionConfiguration(rsaKeyPair)
      )
      jwtAuthenticator.addSignatureConfiguration(
        new RSASignatureConfiguration(rsaKeyPair)
      )
    }

    val bearerClient = new DirectBearerAuthClient(jwtAuthenticator)
    bearerClient.setAuthorizationGenerator(CommonProfileAuthorizationGenerator)
    bearerClient
  }

  @Provides
  def provideConfig(directBearerAuthClient: DirectBearerAuthClient): Config = {
    val clients = new Clients(directBearerAuthClient, new AnonymousClient())
    val config = new Config(clients)
    config.addAuthorizer(
      "read",
      new RequireAnyPermissionAuthorizer[CommonProfile]("pac4j:read")
    )
    config.addAuthorizer(
      "write",
      new RequireAnyPermissionAuthorizer[CommonProfile]("pac4j:write")
    )
    config.setHttpActionAdapter(DemoHttpActionAdapter)
    config
  }

}
