import io.javalin.Javalin;
import io.javalin.openapi.plugin.OpenApiPlugin;
import io.javalin.openapi.plugin.redoc.ReDocPlugin;
import io.javalin.openapi.plugin.swagger.SwaggerPlugin;

import static io.javalin.apibuilder.ApiBuilder.*;

public class PropertyServer {
  public static void main(String[] args) {

    var sales = new SalesDAO();
    var salesHandler = new SalesController(sales);

    Javalin.create(config -> {
      config.registerPlugin(new OpenApiPlugin(pluginConfig -> {
        pluginConfig.withDefinitionConfiguration((version, definition) -> {
          definition.withOpenApiInfo(info -> info.setTitle("Real Estate API"));
        });
      }));
      config.registerPlugin(new SwaggerPlugin(uiConfig -> uiConfig.setUiPath("/docs/swagger")));
      config.registerPlugin(new ReDocPlugin(uiConfig -> uiConfig.setUiPath("/docs/redoc")));

      config.router.apiBuilder(() -> {
        get("/", ctx -> ctx.result("Property server is running"));
        get("/sales/{saleID}", ctx -> salesHandler.getSaleByID(ctx, ctx.pathParam("saleID")));
        get("/sales", salesHandler::getAllSales);
        post("/sales", salesHandler::createSale);
        get("/sales/postcode/{postcode}", ctx -> salesHandler.findSaleByPostCode(ctx, ctx.pathParam("postcode")));
        get("/sales/price_per_square_meter/average", salesHandler::averagePricePerSquareMeter);
        get("/sales/price_per_square_meter/high", salesHandler::highPricePerSquareMeter);
        get("/sales/price_per_square_meter/low", salesHandler::lowPricePerSquareMeter);
      });
    }).start(7071);
  }
}