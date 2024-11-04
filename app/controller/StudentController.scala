package controller

import model.StudentDto
import play.api.libs.json._
import play.api.mvc._
import service.StudentService

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class StudentController @Inject()(cc: ControllerComponents, studentService: StudentService)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  private val logger = play.api.Logger(this.getClass)

  def getStudents: Action[AnyContent] = Action.async {
    logger.info("Getting all students")
    studentService.getStudents.map { students =>
      Ok(Json.toJson(students))
    }
  }

  def updateStudent(id: String): Action[JsValue] = Action.async(parse.json) { request =>
    logger.info(s"Updating student with id $id")
    request.body.validate[StudentDto].fold(
      errors => {
        logger.error(s"Error while parsing request: $errors")
        Future.successful(BadRequest(Json.obj("message" -> JsError.toJson(errors))))
      },
      studentDto => {
        studentService.changeStudent(id, studentDto).map {
          case Some(student) => Ok(Json.toJson(student))
          case None => NotFound(Json.obj("message" -> s"Student with id $id not found"))
        }
      }
    )
  }

  def createStudent: Action[JsValue] = Action.async(parse.json) { request =>
    logger.info("Creating student")
    request.body.validate[StudentDto].fold(
      errors => {
        logger.error(s"Error while parsing request: $errors")
        Future.successful(BadRequest(Json.obj("message" -> JsError.toJson(errors))))
      },
      studentDto => {
        studentService.createStudent(studentDto).map { student =>
          Created(Json.toJson(student))
        }
      }
    )
  }

  def deleteStudent(id: String): Action[AnyContent] = Action.async {
    logger.info(s"Deleting student with id $id")
    studentService.deleteStudent(id).map {
      case true => NoContent
      case false => NotFound(Json.obj("message" -> s"Student with id $id not found"))
    }
  }
}
