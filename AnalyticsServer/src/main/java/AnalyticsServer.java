import io.javalin.Javalin;
import io.javalin.openapi.plugin.OpenApiPlugin;
import io.javalin.openapi.plugin.redoc.ReDocPlugin;
import io.javalin.openapi.plugin.swagger.SwaggerPlugin;

import static io.javalin.apibuilder.ApiBuilder.*;

public class AnalyticsServer {
    public static void main(String[] args) {

        var sales = new AnalyticsDAO();
        var analyticsHandler = new AnalyticsController(sales);

        Javalin.create(config -> {
            config.registerPlugin(new OpenApiPlugin(pluginConfig -> {
                pluginConfig.withDefinitionConfiguration((version, definition) -> {
                    definition.withOpenApiInfo(info -> info.setTitle("Real Estate Analytics API"));
                });
            }));
            config.registerPlugin(new SwaggerPlugin(uiConfig -> uiConfig.setUiPath("/docs/swagger")));
            config.registerPlugin(new ReDocPlugin(uiConfig -> uiConfig.setUiPath("/docs/redoc")));

            config.router.apiBuilder(() -> {
                get("/", ctx -> ctx.result("Analytics server is running"));
                put("/analytics/property/{propertyID}/increment", analyticsHandler::incrementPropertyAnalytics);
                put("/analytics/postcode/{postcodeID}/increment", analyticsHandler::incrementPostcodeAnalytics);
                get("/analytics/property/{propertyID}", analyticsHandler::viewPropertyAnalytics);
                get("/analytics/postcode/{postcodeID}", analyticsHandler::viewPostcodeAnalytics);
            });
        }).start(7071);
    }
}