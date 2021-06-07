package controllers

import javax.inject._ 
import play.api.mvc._

@Singleton
class formContoller @Inject() (cc: ControllerComponents) extends AbstractController(cc) {

        def form = Action {
            Ok(views.html.form())
        }
}