package controllers

import java.nio.ByteBuffer

import boopickle.Default._
import com.kabasoft.iws.shared.common._
import play.api.mvc._
import services.ApiService
import com.kabasoft.iws.shared._
import com.kabasoft.iws.shared.Model._
import play.Play
import java.io.File


object AssetsController extends Controller {
/*
  val AbsolutePath = """^(/|[a-zA-Z]:\\).*""".r

  /**
    * Generates an `Action` that serves a static resource from an external folder
    *
    * @param absoluteRootPath the root folder for searching the static resource files.
    * @param file the file part extracted from the URL
    */
  def at(rootPath: String, file: String): Action[AnyContent] = Action { request =>
    val fileToServe = rootPath match {
      case AbsolutePath(_) =>  new File(rootPath, file)
      case _ =>  new File(Play.application.getFile(rootPath), file)
    }

    if (fileToServe.exists) {
      Ok.sendFile(fileToServe, inline = true)
    } else {
      println("Photos controller failed to serve photo: " + file)
      NotFound
    }
  }
 */
}
