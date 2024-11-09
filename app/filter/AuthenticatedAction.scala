package filter

import pdi.jwt.{Jwt, JwtAlgorithm}
import play.api.mvc._

import java.security.PublicKey
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class AuthenticatedAction @Inject()(
                                     parser: BodyParsers.Default,
                                     keyLoaderService: KeyLoaderService
                                   )(implicit ec: ExecutionContext) extends ActionBuilderImpl(parser) {

  private val logger = play.api.Logger(this.getClass)

  override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]): Future[Result] = {
    request.headers.get("Authorization") match {
      case Some(authHeader) if authHeader.startsWith("Bearer ") =>
        val token = authHeader.substring("Bearer ".length)
        keyLoaderService.getPublicKey.flatMap { publicKey =>
          if (isValidToken(token, publicKey)) {
            logger.info("Token is valid")
            block(request)
          } else {
            logger.info("Token is invalid")
            Future.successful(Results.Unauthorized("Invalid or missing token"))
          }
        }
      case _ =>
        logger.info("Authorization header missing or not starting with Bearer")
        Future.successful(Results.Unauthorized("You must be authenticated to access this page."))
    }
  }

  private def isValidToken(token: String, publicKey: PublicKey): Boolean = {
    logger.info(s"Checking token: $token")
    Jwt.decode(token, publicKey, Seq(JwtAlgorithm.RS256)) match {
      case Success(claim) =>
        logger.info("Token is valid")
        true
      case Failure(exception) =>
        logger.error(s"Token validation failed: ${exception.getMessage}")
        false
    }
  }
}


