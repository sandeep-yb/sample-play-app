/*
 * Copyright (C) Lightbend Inc. <https://www.lightbend.com>
 */

package controllers;

// #ws-oauth-controller
import play.libs.oauth.OAuth;
import play.libs.oauth.OAuth.ConsumerKey;
import play.libs.oauth.OAuth.OAuthCalculator;
import play.libs.oauth.OAuth.RequestToken;
import play.libs.oauth.OAuth.ServiceInfo;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import com.google.common.base.Strings;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

//posting json data
import play.libs.Json;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.ws.WSBodyReadables;

//logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import play.mvc.Result;
import play.mvc.Results;

public class InviteUserController  extends Controller {

private final WSClient ws;
final Logger log = LoggerFactory.getLogger(this.getClass());


  @Inject
  public InviteUserController(WSClient ws) {
    this.ws = ws;
  }

  public Result index(String firstName, String lastName, String email) throws Exception {

      ObjectNode bodyJson = Json.newObject();

      ObjectNode userIntentJson =
        Json.newObject()
        .put( "firstName",firstName) 
        .put("lastName", lastName)
        .put("email", email)
        .put("login",  email);

        ObjectNode requestBody = Json.newObject();
        requestBody.set("profile", userIntentJson);


//         {
//   "id": "00uudzbkhbxZLUYuD5d6",
//   "scope": "USER",
//   "credentials": {
//     "userName": "sandeepkumaraadha@gmail.com"
//   }
// }  
    // create user
   
    CompletionStage<JsonNode> jsonNode =  
    ws.url("https://yugabyte-aakumar-poc.okta.com/api/v1/users?activate=true")
          .addHeader("Content-Type", "application/json")
          .addHeader("Authorization", "")
          .addHeader("Accept", "application/json")
          .post(requestBody)
          .thenApply(result -> result.getBody(WSBodyReadables.instance.json()));
        //   .thenApply(result1 -> {

        //   JsonNode output = result1.toCompletableFuture().get();
        //   String email1 = output.get("profile").get("email").toString();
        //   String id1 = output.get("id").toString();

        //    ObjectNode bodyJson2 = Json.newObject();
        //    ObjectNode userIntentJson2 =
        //       Json.newObject()
        //       .put("userName", email1); 

        //       ObjectNode requestBody2 = Json.newObject();
        //       requestBody2.put("id", id1);
        //       requestBody2.put("scope", "USER");
        //       requestBody2.set("credentials", userIntentJson2);

        //        return  ws.url("https://yugabyte-aakumar-poc.okta.com/api/v1/apps/0oath4l92O99k63ES5d6/users")
        //   .addHeader("Content-Type", "application/json")
        //   .addHeader("Authorization", "")
        //   .addHeader("Accept", "application/json")
        //  .post(requestBody2)
        //   .thenApply(response -> response.getBody(WSBodyReadables.instance.json()));
        //   });

    // return CompletableFuture.completedFuture(Results.ok(jsonNode.toString()));
      // return jsonNode;
          // .thenApply(result -> ok(views.html.data,render(result.asJson())));
          //  .thenApply(result -> {

          //   //result = result.asJson();  
//          ObjectNode objectNode = jsonNode.deepCopy();
          
         
          JsonNode output = jsonNode.toCompletableFuture().get();
          String email1 = output.get("profile").get("email").textValue();
          String id1 = output.get("id").textValue();

            //eturn CompletableFuture.completedFuture(Results.ok(email1));
            //log.debug("Attempting risky calculation.");
            ObjectNode userIntentJson2 =
              Json.newObject()
              .put("userName", email1); 

              ObjectNode requestBody2 = Json.newObject();
              requestBody2.put("id", id1);
              requestBody2.put("scope", "USER");
              requestBody2.set("credentials", userIntentJson2);

            // return CompletableFuture.completedFuture(Results.ok(requestBody2));

          // return ws.url("https://yugabyte-aakumar-poc.okta.com/api/v1/apps/0oath4l92O99k63ES5d6/users")
          // .addHeader("Content-Type", "application/json")
          // .addHeader("Authorization", "")
          // .addHeader("Accept", "application/json")
          // .post(requestBody2)
          // .thenApply(response -> ok(response.asJson()));

  //log.info(requestBody2.toString());
       CompletionStage<JsonNode> jsonNode2= ws.url("https://yugabyte-aakumar-poc.okta.com/api/v1/apps/0oath4l92O99k63ES5d6/users")
          .addHeader("Content-Type", "application/json")
          .addHeader("Authorization", "")
          .addHeader("Accept", "application/json")
          .post(requestBody2)
          .thenApply(response -> response.getBody(WSBodyReadables.instance.json()));  

        JsonNode output2 = jsonNode.toCompletableFuture().get();
     // log.info(output2.toString());
     return Results.status(200, "User invited"); 
     //   return Results.status(200,output2.toString());
           //});

  }
 
}
// #ws-oauth-controller