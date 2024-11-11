package controller

import play.api.mvc._
import play.api.libs.ws.WSClient
import play.api.Configuration

import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class OAuthController @Inject()(ws: WSClient, config: Configuration, cc: ControllerComponents) extends AbstractController(cc) {

  def login = Action { implicit request =>
    val url = config.get[String]("keycloak.authUrl")
    val clientId = config.get[String]("keycloak.clientId")
    val redirectUri = config.get[String]("keycloak.redirectUri")
    val responseType = "code"
    val scope = "openid"

    Redirect(s"$url?client_id=$clientId&redirect_uri=$redirectUri&response_type=$responseType&scope=$scope")
  }

  def callback(code: Option[String], state: Option[String]) = Action.async { implicit request =>
    code match {
      case Some(authCode) =>
        val tokenUrl = config.get[String]("keycloak.tokenUrl")
        val clientId = config.get[String]("keycloak.clientId")
        val clientSecret = config.get[String]("keycloak.clientSecret")
        val redirectUri = config.get[String]("keycloak.redirectUri")

        val requestData = Map(
          "client_id" -> Seq(clientId),
          "client_secret" -> Seq(clientSecret),
          "code" -> Seq(authCode),
          "grant_type" -> Seq("authorization_code"),
          "redirect_uri" -> Seq(redirectUri)
        )

        ws.url(tokenUrl).withHttpHeaders("Content-Type" -> "application/x-www-form-urlencoded").post(requestData.map { case (k, v) => s"$k=${v.head}" }.mkString("&")).map { response =>
          val accessToken = (response.json \ "access_token").as[String]
          Ok(s"Access Token: $accessToken")
        }
      case None =>
        Future.successful(BadRequest("No code found"))
    }
  }
}
