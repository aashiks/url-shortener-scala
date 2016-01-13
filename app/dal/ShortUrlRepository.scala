package dal


package dal

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile


import scala.concurrent.{ Future, ExecutionContext }

import models.ShortUrl
/**
  * Created by aashiks on 1/13/16.
  */
@Singleton
class ShortUrlRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  // We want the JdbcProfile for this provider
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  // These imports are important, the first one brings db into scope, which will let you do the actual db operations.
  // The second one brings the Slick DSL into scope, which lets you define the table and other queries.
  import dbConfig._
  import driver.api._

  /**
    * Here we define the table. It will have a name of shorturls
    */
  private class ShortUrlTable(tag: Tag) extends Table[ShortUrl](tag, "shorturls") {

    /** The ID column, which is the primary key, and auto incremented */
    def id = column[String]("id", O.PrimaryKey)

    def originalUrl = column[String]("original_url")


    def * = (id, originalUrl) <>((ShortUrl.apply _).tupled, ShortUrl.unapply)
  }

  private val shorturls = TableQuery[ShortUrlTable]

  def insert(shorturl: ShortUrl): Future[ShortUrl] =
    try {

      db.run(shorturls += shorturl)
      findById(shorturl.id)
    }
    finally {
      db.close
    }

//  def filterQueryById(id: String): Query[ShortUrlTable,ShortUrl, Seq] =
//    shorturls.filter(_.id === id)
//
//
  def findById(id: String): Future[ShortUrl] =
    try db.run(shorturls.filter(_.id===id).result.head)
    finally db.close
//
//
//  def filterQueryByUrl(url: String): Query[ShortUrlTable,ShortUrl, Seq] =
//    shorturls.filter(_.originalUrl === url)
//
  def findByURL(originalUrl: String): Future[ShortUrl] =
    try db.run(shorturls.filter(_.originalUrl === originalUrl).result.head)
    finally db.close


}