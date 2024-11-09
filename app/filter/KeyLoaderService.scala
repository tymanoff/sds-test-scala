package filter

import com.typesafe.config.ConfigFactory

import javax.inject.Inject
import play.api.libs.ws.WSClient
import play.api.libs.json.Json

import java.security.spec.X509EncodedKeySpec
import java.security.{KeyFactory, PublicKey}
import java.util.Base64
import scala.concurrent.{ExecutionContext, Future}

class KeyLoaderService @Inject()(ws: WSClient)(implicit ec: ExecutionContext) {
  private var cachedPublicKey: Option[PublicKey] = None

  private val config = ConfigFactory.load()
  private val issuerUri = config.getString("keycloak.issuerUri")

  def getPublicKey: Future[PublicKey] = {
    cachedPublicKey match {
      case Some(key) => Future.successful(key)
      case None => fetchPublicKey
    }
  }

  private def fetchPublicKey: Future[PublicKey] = {
    ws.url(issuerUri).get().map { response =>
      val json = Json.parse(response.body)
      val publicKeyStr = (json \ "public_key").as[String]
      val publicKey = decodePublicKey(publicKeyStr)
      cachedPublicKey = Some(publicKey)
      publicKey
    }
  }

  private def decodePublicKey(publicKeyStr: String): PublicKey = {
    val publicKeyPEM = publicKeyStr
      .replace("-----BEGIN PUBLIC KEY-----", "")
      .replace("-----END PUBLIC KEY-----", "")
      .replaceAll("\\s", "")
    val keyBytes = Base64.getDecoder.decode(publicKeyPEM)
    val spec = new X509EncodedKeySpec(keyBytes)
    val keyFactory = KeyFactory.getInstance("RSA")
    keyFactory.generatePublic(spec)
  }
}
