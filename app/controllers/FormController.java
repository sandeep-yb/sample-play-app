package controllers;

import com.typesafe.config.Config;
import javax.inject.Inject;
import org.pac4j.play.LogoutController;
import play.mvc.*;
import play.data.DynamicForm;
import java.awt.event.*;

public class FormController extends LogoutController {

    public Result index() {
        return ok(views.html.form.render());
    }

    // public Result validate(ActionEvent request) {
    //     //DynamicForm requestData = formFactory.form().bindFromRequest(request);
    //     String firstname = request.get("firstName");
    //     String lastname = request.get("lastName");
    //     return ok("Hello " + firstname + " " + lastname);   
    // }


  
}
