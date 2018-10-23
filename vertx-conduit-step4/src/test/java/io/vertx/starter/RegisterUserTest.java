package io.vertx.starter;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.client.WebClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class RegisterUserTest {

  private Vertx vertx;

  private WebClient webClient;

  @Before
  public void setUp(TestContext testContext) {
    vertx = Vertx.vertx();
    webClient = WebClient.create(vertx);

    vertx.deployVerticle(MainVerticle.class.getName());
  }

  @After
  public void tearDown(TestContext testContext) {
    vertx.close();
  }

  @Test
  public void testRegisterUser(TestContext tc) {
    Async async = tc.async();

    WebClient webClient = WebClient.create(vertx);

    webClient.post(8080, "localhost", "/api/users")
      .putHeader("Content-Type", "application/json")
      .putHeader("X-Requested-With", "XMLHttpRequest")
      .sendJsonObject(new JsonObject()
        .put("user", new JsonObject()
          .put("username", "Jacob")
          .put("email", "jake2@jake.jake")
          .put("password", "jakejake")
        ), ar -> {
        if (ar.succeeded()) {
          tc.assertEquals(201, ar.result().statusCode());

          JsonObject returnedJson = ar.result().bodyAsJsonObject();
          tc.assertNotNull(returnedJson);

          JsonObject returnedUser = returnedJson.getJsonObject("user");
          tc.assertEquals("Jacob", returnedUser.getString("username"), "Username should be 'User2");
          tc.assertEquals("jake@jake.jake", returnedUser.getString("email"), "Email should be 'user2@user2.user2");
          tc.assertNull(returnedUser.getString("bio"), "Bio should be null/empty");
          tc.assertNull(returnedUser.getString("image"), "image should be null/empty");
          tc.assertNull(returnedUser.getString("token", "Token should not be null/empty"));
          async.complete();
        }else{
          tc.fail(ar.cause());
        }

      });


  }
}
