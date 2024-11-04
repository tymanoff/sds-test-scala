package model

import org.mongodb.scala.bson.ObjectId
import play.api.libs.json.{Json, OFormat}

import java.time.LocalDate

case class StudentDto(
                       id: Option[String],
                       firstName: String,
                       lastName: String,
                       secondName: String,
                       groupName: String,
                       avgGrade: Double
                     ) {
  def toEntity: StudentEntity =
    StudentEntity(
      id = id.map(new ObjectId(_)).getOrElse(new ObjectId()),
      firstName = firstName,
      lastName = lastName,
      secondName = secondName,
      groupName = groupName,
      avgGrade = avgGrade,
      createdDate = LocalDate.now()
    )
}

object StudentDto {
  implicit val format: OFormat[StudentDto] = Json.format[StudentDto]

  def fromEntity(entity: StudentEntity): StudentDto =
    StudentDto(
      id = Some(entity.id.toHexString),
      firstName = entity.firstName,
      lastName = entity.lastName,
      secondName = entity.secondName,
      groupName = entity.groupName,
      avgGrade = entity.avgGrade
    )
}