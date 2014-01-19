package controllers

import models.Websocket
import play.api.libs.iteratee.Concurrent
import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.mvc.WebSocket

object Application extends Controller {

	def index = Action {
		Ok(views.html.index("Your new application is ready."))
	}

	def updateView = Action {
	  Websocket.updateTest
		Ok(views.html.index("Your new application is ready."))
	}

			
	def ws = WebSocket.using[String] { request =>
	  	val (out,channel) = Concurrent.broadcast[String]
			Websocket.join(out, channel)
	}
}
