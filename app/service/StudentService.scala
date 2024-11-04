package service

import model.StudentDto
import repository.StudentRepository

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class StudentService @Inject()(studentRepository: StudentRepository)(implicit ec: ExecutionContext) {

  def getStudents: Future[Seq[StudentDto]] = {
    studentRepository.findAll.map(_.map(StudentDto.fromEntity))
  }

  def changeStudent(id: String, studentDto: StudentDto): Future[Option[StudentDto]] = {
    studentRepository.findById(id).flatMap {
      case Some(existingEntity) => val updatedEntity = studentDto.toEntity.copy(id = existingEntity.id)
        studentRepository.update(id, updatedEntity).map(entity => Some(StudentDto.fromEntity(entity)))
      case None => Future.successful(None)
    }
  }

  def createStudent(studentDto: StudentDto): Future[StudentDto] = {
    studentRepository.create(studentDto.toEntity).map(StudentDto.fromEntity)
  }

  def deleteStudent(id: String): Future[Boolean] = {
    if (id.isEmpty) Future.successful(false)
    else studentRepository.delete(id)
  }
}
