package model

import org.bson.types.ObjectId
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.bson.annotations.BsonProperty
import play.api.libs.json.{Format, JsError, JsResult, JsString, JsSuccess, JsValue, Json, OFormat}

import java.lang.annotation.Documented
import java.time.LocalDate

@Documented
case class StudentEntity(
                          @BsonProperty("_id") id: ObjectId = new ObjectId(),
                          firstName: String,
                          lastName: String,
                          secondName: String,
                          groupName: String,
                          avgGrade: Double,
                          createdDate: LocalDate
                        )

object StudentEntity {
  implicit val objectIdFormat: Format[ObjectId] = new Format[ObjectId] {
    def reads(json: JsValue): JsResult[ObjectId] = json match {
      case JsString(s) if ObjectId.isValid(s) => JsSuccess(new ObjectId(s))
      case _ => JsError("Invalid ObjectId")
    }

    def writes(objectId: ObjectId): JsValue = JsString(objectId.toHexString)
  }

  implicit val format: OFormat[StudentEntity] = Json.format[StudentEntity]
}
