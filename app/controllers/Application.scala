package controllers

import dal.dal.ShortUrlRepository
import play.api._
import play.api.mvc._
import play.api.i18n._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.libs.json.Json
import models._
import dal._

import scala.concurrent.{ ExecutionContext, Future }

import javax.inject._


/**
  * Created by aashiks on 1/13/16.
  */
class Application @Inject() (repo: ShortUrlRepository)
                            (implicit ec: ExecutionContext) extends Controller {

  def addURL(originalUrl:String) = Action {
    Logger.debug(" --  " + originalUrl)
    Ok("")
  }

  def index = Action {
    Ok(views.html.index())
  }
}

