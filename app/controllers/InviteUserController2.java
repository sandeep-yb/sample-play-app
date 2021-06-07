package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.typesafe.config.Config;
import common.Util;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import models.DSubnet;
import models.DVpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import play.mvc.Controller;

public class InviteUserController2 extends Controller {

private final WSClient wsClient;
private static final Logger LOG = LoggerFactory.getLogger(PlatformClientImpl.class);
// private final String firstName;
// private final String lastName;
// private final String email;


@Inject
public InviteUserController2(WSClient wsClient) {
    this.wsClient = wsClient;
}



    public JsonNode index(String firstName, String lastName, String email) throws Exception  {

//  ws.url("https://yugabyte-aakumar-poc.okta.com/api/v1/users?activate=true")
//           .addHeader("Content-Type", "application/json")
//           .addHeader("Authorization", "SSWS 00RQzCaQY8mCInIXqCXflk4MjQxpxXPl9ftUc9SEhp")
//           .addHeader("Accept", "application/json")
//           .post(requestBody)

        String url1 = "https://yugabyte-aakumar-poc.okta.com/api/v1/users?activate=true";
        Map<String, String> mapA = new HashMap<>();
        mapA.put("Content-Type", "application/json");
        mapA.put("Authorization", "SSWS 00RQzCaQY8mCInIXqCXflk4MjQxpxXPl9ftUc9SEhp");
        mapA.put("Accept", "application/json");

         ObjectNode userIntentJson =
        Json.newObject()
        .put( "firstName",firstName) 
        .put("lastName", lastName)
        .put("email", email)
        .put("login",  email);

        ObjectNode requestBody = Json.newObject();
        requestBody.set("profile", userIntentJson);

        return ok(makeApiCall(requestBody, url1,"POST", mapA)).asJson();
    }


 private WSRequest requestWithHeaders(String url, Map<String, String> headers) {
    WSRequest request = this.wsClient.url(url);
    if (headers != null && !headers.isEmpty()) {
      for (Map.Entry<String, String> entry : headers.entrySet()) {
        request.addHeader(entry.getKey(), entry.getValue());
      }
    }

    return request;
  }


     private JsonNode makeApiCall(JsonNode data, String url, String method, Map<String, String> authHeader) {
    JsonNode responseJson = null;
    CompletionStage<JsonNode> jsonPromise;
   try {
      WSRequest request = requestWithHeaders(url, authHeader);

      LOG.debug("makeApiCall: http api {} to {}, data {}.", method, url, data);

      switch (method) {
        case "GET":
          jsonPromise = request.get().thenApply(WSResponse::asJson);
          break;
        case "POST":
          jsonPromise = request.post(data).thenApply(WSResponse::asJson);
          break;
     
        default:
          throw new IllegalArgumentException("API method '" + method + "' is not supported");
      }

      responseJson = this.handleJSONPromise(jsonPromise);
      if (responseJson == null || responseJson.get("error") != null) {
        if (responseJson != null) {
          LOG.debug(
              "Error in API call {} to url {} - {}",
              method,
              url,
              responseJson.get("error").asText());
        }
        throw new IllegalArgumentException("API call contained error.");
      }
    } catch (Exception e) {
      LOG.debug("API call {} to url {} failed.", method, url, e);
    }

    return responseJson;
  }
    

  private JsonNode handleJSONPromise(CompletionStage<JsonNode> jsonPromise) {
    try {
      return jsonPromise.toCompletableFuture().get();
    } catch (InterruptedException | ExecutionException e) {
      return this.errorJSON(e.getMessage());
    }
  }

  private JsonNode errorJSON(Object message) {
    ObjectNode jsonMsg = Json.newObject();

    if (message instanceof JsonNode) jsonMsg.set("error", (JsonNode) message);
    else jsonMsg.put("error", (String) message);

    return jsonMsg;
  }


}


