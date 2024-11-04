package repository

import com.typesafe.config.ConfigFactory
import model.StudentEntity
import org.mongodb.scala.bson.codecs.Macros._
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala._
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.model.Filters._

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class StudentRepository @Inject()(implicit ec: ExecutionContext) {

  private val config = ConfigFactory.load()
  private val mongoUri = config.getString("mongodb.uri")
  private val databaseName = config.getString("mongodb.database")
  private val collectionName = config.getString("mongodb.collection")

  private val codecRegistry = fromRegistries(fromProviders(classOf[StudentEntity]), MongoClient.DEFAULT_CODEC_REGISTRY)
  private val mongoClient: MongoClient = MongoClient(mongoUri)
  private val database: MongoDatabase = mongoClient.getDatabase(databaseName).withCodecRegistry(codecRegistry)
  private val collection: MongoCollection[StudentEntity] = database.getCollection(collectionName)

  def findAll: Future[Seq[StudentEntity]] = {
    collection.find().toFuture()
  }

  def findById(id: String): Future[Option[StudentEntity]] = {
    collection.find(equal("_id", new ObjectId(id))).headOption()
  }

  def update(id: String, entity: StudentEntity): Future[StudentEntity] = {
    collection.replaceOne(equal("_id", new ObjectId(id)), entity).toFuture().map(_ => entity)
  }

  def create(entity: StudentEntity): Future[StudentEntity] = {
    collection.insertOne(entity).toFuture().map(_ => entity)
  }

  def delete(id: String): Future[Boolean] = {
    collection.deleteOne(equal("_id", new ObjectId(id))).toFuture().map(_.wasAcknowledged())
  }
}
