import io.javalin.Javalin;
import static io.javalin.apibuilder.ApiBuilder.*;

public class APIGateway {

  public static void main(String[] args) {

    PropertyServer propertyServer = new PropertyServer();
    AnalyticsServer analyticsServer = new AnalyticsServer();

    Javalin app = Javalin.create().routes(() -> {

      get("/", ctx -> ctx.result("API Gateway is running"));

      get("/sales", ctx -> {
        try {
          String response = propertyServer.getAllSales();
          ctx.result(response);
        } catch (Exception e) {
          ctx.status(500).result("Error contacting Property Service: " + e.getMessage());
        }
      });

      post("/sales", ctx -> {
        try {
          String body = ctx.body();
          String response = propertyServer.createSale(body);
          ctx.result(response);
          ctx.status(201);
        } catch (Exception e) {
          ctx.status(500).result("Error contacting Property Service: " + e.getMessage());
        }
      });

      get("/sales/{id}", ctx -> {
        try {
          String id = ctx.pathParam("id");
          analyticsServer.incrementPropertyView(id);
          String response = propertyServer.getSaleById(id);
          ctx.result(response);
        } catch (Exception e) {
          ctx.status(500).result("Error contacting Property or Analytics Service: " + e.getMessage());
        }
      });

      get("/sales/postcode/{postcode}", ctx -> {
        try {
          String postcode = ctx.pathParam("postcode");
          analyticsServer.incrementPostcodeView(postcode);
          String response = propertyServer.getSalesByPostcode(postcode);
          ctx.result(response);
        } catch (Exception e) {
          ctx.status(500).result("Error contacting Property or Analytics Service: " + e.getMessage());
        }
      });

      get("/sales/price_per_square_meter/average", ctx -> {
        try {
          String response = propertyServer.getAvgPricePerSqM();
          ctx.result(response);
        } catch (Exception e) {
          ctx.status(500).result("Error contacting Property Service: " + e.getMessage());
        }
      });

      get("/sales/price_per_square_meter/high", ctx -> {
        try {
          String response = propertyServer.getHighPricePerSqM();
          ctx.result(response);
        } catch (Exception e) {
          ctx.status(500).result("Error contacting Property Service: " + e.getMessage());
        }
      });

      get("/sales/price_per_square_meter/low", ctx -> {
        try {
          String response = propertyServer.getLowPricePerSqM();
          ctx.result(response);
        } catch (Exception e) {
          ctx.status(500).result("Error contacting Property Service: " + e.getMessage());
        }
      });

    });

    app.start(7070);
    System.out.println("API Gateway running on http://localhost:7070");
  }
}