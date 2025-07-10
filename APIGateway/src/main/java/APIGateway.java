
import io.javalin.Javalin;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static io.javalin.apibuilder.ApiBuilder.*;

public class APIGateway {
  public static void main(String[] args) {
    HttpClient client = HttpClient.newHttpClient();

    Javalin.create(config -> {
      config.router.apiBuilder(() -> {

        get("/", ctx -> ctx.result("API Gateway is running"));

        // GET /sales/{saleID}
        get("/sales/{saleID}", ctx -> {
          String saleId = ctx.pathParam("saleID");

          // 1. Call Analytics Server
          /*
          try {
            HttpRequest analyticsReq = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:7071/analytics/property/" + saleId + "/increment"))
                    .PUT(HttpRequest.BodyPublishers.noBody())
                    .build();

            client.send(analyticsReq, HttpResponse.BodyHandlers.ofString());
          } catch (Exception e) {
            e.printStackTrace();
          }
         */

          // 2. Call Property Server
          HttpRequest propReq = HttpRequest.newBuilder()
                  .uri(URI.create("http://localhost:7072/sales/" + saleId))
                  .build();
          HttpResponse<String> propRes = client.send(propReq, HttpResponse.BodyHandlers.ofString());

          // 3. Return the property data
          ctx.result(propRes.body());
          ctx.status(propRes.statusCode());
        });

        //GET /sales
        get("/sales", ctx -> {

          // 1. Call Property Server
          HttpRequest propReq = HttpRequest.newBuilder()
                  .uri(URI.create("http://localhost:7072/sales"))
                  .build();
          HttpResponse<String> propRes = client.send(propReq, HttpResponse.BodyHandlers.ofString());

          // 3. Return the property data
          ctx.result(propRes.body());
          ctx.status(propRes.statusCode());
        });

        // GET /sales/postcode/{postcodeID}
        get("/sales/postcode/{postcodeID}", ctx -> {
          String postcodeID = ctx.pathParam("postcodeID");

          // 1. Call Analytics Server
          /*
          try {
            HttpRequest analyticsReq = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:7071/analytics/postcode/" + postcodeID + "/increment"))
                    .PUT(HttpRequest.BodyPublishers.noBody())
                    .build();

            client.send(analyticsReq, HttpResponse.BodyHandlers.ofString());
          } catch (Exception e) {
            e.printStackTrace();
          }
          */

          // 2. Call Property Server
          HttpRequest propReq = HttpRequest.newBuilder()
                  .uri(URI.create("http://localhost:7072/sales/postcode/" + postcodeID))
                  .build();
          HttpResponse<String> propRes = client.send(propReq, HttpResponse.BodyHandlers.ofString());

          // 3. Return the property data
          ctx.result(propRes.body());
          ctx.status(propRes.statusCode());
        });

        //GET average price per square meter
        get("/sales/price_per_square_meter/average", ctx -> {

          // 1. Call Property Server
          HttpRequest propReq = HttpRequest.newBuilder()
                  .uri(URI.create("http://localhost:7072/sales/price_per_square_meter/average"))
                  .build();
          HttpResponse<String> propRes = client.send(propReq, HttpResponse.BodyHandlers.ofString());

          // 3. Return the property data
          ctx.result(propRes.body());
          ctx.status(propRes.statusCode());
        });

        //GET max price per square meter
        get("/sales/price_per_square_meter/high", ctx -> {

          // 1. Call Property Server
          HttpRequest propReq = HttpRequest.newBuilder()
                  .uri(URI.create("http://localhost:7072/sales/price_per_square_meter/high"))
                  .build();
          HttpResponse<String> propRes = client.send(propReq, HttpResponse.BodyHandlers.ofString());

          // 3. Return the property data
          ctx.result(propRes.body());
          ctx.status(propRes.statusCode());
        });

        //GET min price per square meter
        get("/sales/price_per_square_meter/low", ctx -> {

          // 1. Call Property Server
          HttpRequest propReq = HttpRequest.newBuilder()
                  .uri(URI.create("http://localhost:7072/sales/price_per_square_meter/low"))
                  .build();
          HttpResponse<String> propRes = client.send(propReq, HttpResponse.BodyHandlers.ofString());

          // 3. Return the property data
          ctx.result(propRes.body());
          ctx.status(propRes.statusCode());
        });

        //GET property analytics count
        get("/analytics/property/{propertyID}", ctx -> {
          String propertyID = ctx.pathParam("propertyID");

          // 1. Call Property Server
          HttpRequest propReq = HttpRequest.newBuilder()
                  .uri(URI.create("http://localhost:7071/analytics/property/" + propertyID))
                  .build();
          HttpResponse<String> propRes = client.send(propReq, HttpResponse.BodyHandlers.ofString());

          // 3. Return the property data
          ctx.result(propRes.body());
          ctx.status(propRes.statusCode());
        });

        //GET postcode analytics count
        get("/analytics/postcode/{postcodeID}", ctx -> {
          String postcodeID = ctx.pathParam("postcodeID");

          // 1. Call Property Server
          HttpRequest propReq = HttpRequest.newBuilder()
                  .uri(URI.create("http://localhost:7071/analytics/postcode/" + postcodeID))
                  .build();
          HttpResponse<String> propRes = client.send(propReq, HttpResponse.BodyHandlers.ofString());

          // 3. Return the property data
          ctx.result(propRes.body());
          ctx.status(propRes.statusCode());
        });

      });
    }).start(7070); // API Gateway on port 7070
  }
}
