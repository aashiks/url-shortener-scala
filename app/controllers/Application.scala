package controllers


import play.api._
import play.api.mvc._
import play.api.i18n._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.libs.json.Json
import models._
import dal._
import org.apache.commons.validator.UrlValidator

import scala.concurrent.{ ExecutionContext, Future }

import javax.inject._

import scala.util.Random



/**
  * Created by aashiks on 1/13/16.
  */
class Application @Inject() (repo: ShortUrlRepository)
                            (implicit ec: ExecutionContext) extends Controller {

  def addURL() =  Action(parse.tolerantFormUrlEncoded) { request =>

    val url = request.body.get("originalUrl").map(_.head).get
    val validator = new UrlValidator(List("http","https").toArray)
    if(validator.isValid(url)) {
      val random = Random.alphanumeric.take(6).mkString
      val shortURL = ShortUrl(random,url)
      repo.insert(shortURL)
      Ok(Play.current.configuration.getString("shortened.domain").get +"/r/"+ random)
    } else {
      BadRequest("Not a valid URL")
    }
  }

  def index = Action{
    Ok(views.html.index())
  }
  def redirect(shortURL:String) = Action.async {
    repo.findById(shortURL)
      .map { u =>
        if(u.originalUrl.isEmpty){
          BadRequest("Not a valid short URL")
        }else{
          Redirect(u.originalUrl, 301)

        }
      }
  }
}
