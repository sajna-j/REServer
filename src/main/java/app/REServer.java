package app;

import io.javalin.Javalin;
import io.javalin.openapi.plugin.OpenApiPlugin;
import io.javalin.openapi.plugin.redoc.ReDocPlugin;
import io.javalin.openapi.plugin.swagger.SwaggerPlugin;
import sales.SalesController;
import sales.SalesDAO;

import static io.javalin.apibuilder.ApiBuilder.*;

public class REServer {
    public static void main(String[] args) {

        var sales = new SalesDAO();
        var salesHandler = new SalesController(sales);

        Javalin.create(config -> {
            config.registerPlugin(new OpenApiPlugin(pluginConfig -> {
                pluginConfig.withDefinitionConfiguration((version, definition) -> {
                    definition.withOpenApiInfo(info -> info.setTitle("Real Estate API"));
                });
            }));
            config.registerPlugin(new SwaggerPlugin());
            config.registerPlugin(new ReDocPlugin());

            config.router.apiBuilder(() -> {
                get("/", ctx -> ctx.result("Real Estate server is running"));
                get("/sales/{saleID}", ctx -> salesHandler.getSaleByID(ctx, ctx.pathParam("saleID")));
                get("/sales", ctx -> salesHandler.getAllSales(ctx));
                post("/sales", ctx -> salesHandler.createSale(ctx));
                get("/sales/postcode/{postcode}", ctx -> salesHandler.findSaleByPostCode(ctx, ctx.pathParam("postcode")));
                get("/sales/price_per_square_meter/average", ctx -> salesHandler.averagePricePerSquareMeter(ctx));
                get("/sales/price_per_square_meter/high", ctx -> salesHandler.highPricePerSquareMeter(ctx));
                get("/sales/price_per_square_meter/low", ctx -> salesHandler.lowPricePerSquareMeter(ctx));
            });
        }).start(7070);

        System.out.println("Swagger UI: http://localhost:7070/swagger-ui");
        System.out.println("ReDoc:      http://localhost:7070/redoc");
    }
}