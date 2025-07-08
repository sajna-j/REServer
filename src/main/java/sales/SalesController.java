package sales;

import com.mongodb.MongoException;

import io.javalin.http.Context;
import io.javalin.openapi.HttpMethod;
import io.javalin.openapi.OpenApi;
import io.javalin.openapi.OpenApiContent;
import io.javalin.openapi.OpenApiParam;
import io.javalin.openapi.OpenApiRequestBody;
import io.javalin.openapi.OpenApiResponse;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SalesController {

    private SalesDAO homeSales;

    public SalesController(SalesDAO homeSales) {
        this.homeSales = homeSales;
    }

    @OpenApi(
            path = "/sales",
            methods = HttpMethod.POST,
            summary = "Create a new home sale",
            description = "Accepts a HomeSale object in the request body and creates a new sale entry.",
            requestBody = @OpenApiRequestBody(
                    content = {@OpenApiContent(from = HomeSale.class)},
                    required = true
            ),
            responses = {
                    @OpenApiResponse(status = "201", description = "Sale Created"),
                    @OpenApiResponse(status = "400", description = "Failed to add sale"),
                    @OpenApiResponse(status = "500", description = "Database error")
            }
    )
    public void createSale(Context ctx) {

        // Extract Home Sale from request body
        // TO DO override Validator exception method to report better error message
        HomeSale sale = ctx.bodyValidator(HomeSale.class)
                            .get();

        // store new sale in data set
      try {
        if (homeSales.newSale(sale)) {
            ctx.result("Sale Created");
            ctx.status(201);
        } else {
            ctx.result("Failed to add sale");
            ctx.status(400);
        }
      } catch (MongoException e) {
          ctx.result("Database error: " + e.getMessage());
          ctx.status(500);
      }
    }

    @OpenApi(
            path = "/sales",
            methods = HttpMethod.GET,
            summary = "Get all home sales",
            description = "Returns a list of all home sales records in the database.",
            responses = {
                    @OpenApiResponse(status = "200", description = "List of sales found",
                            content = @OpenApiContent(from = HomeSale[].class)),
                    @OpenApiResponse(status = "404", description = "No sales found"),
                    @OpenApiResponse(status = "500", description = "Database error")
            }
    )
    public void getAllSales(Context ctx) {
      List <HomeSale> allSales = new ArrayList<>();
      try {
        allSales = homeSales.getAllSales();
      } catch (SQLException e) {
          ctx.result("Database error: " + e.getMessage());
          ctx.status(500);
      }
      if (allSales.isEmpty()) {
            ctx.result("No Sales Found");
            ctx.status(404);
        } else {
            ctx.json(allSales);
            ctx.status(200);
        }
    }

    @OpenApi(
            path = "/sales/{saleID}",
            methods = HttpMethod.GET,
            summary = "Get a sale by ID",
            description = "Returns a single home sale by its ID.",
            pathParams = {
                    @OpenApiParam(name = "saleID", description = "ID of the sale", required = true)
            },
            responses = {
                    @OpenApiResponse(status = "200", description = "Sale found",
                            content = @OpenApiContent(from = HomeSale.class)),
                    @OpenApiResponse(status = "404", description = "Sale not found"),
                    @OpenApiResponse(status = "500", description = "Database error")
            }
    )
    public void getSaleByID(Context ctx, String id) {

      Optional<HomeSale> sale = Optional.empty();
      try {
        sale = homeSales.getSaleById(id);
      } catch (SQLException e) {
          ctx.result("Database error: " + e.getMessage());
          ctx.status(500);
      }
      sale.map(ctx::json)
                .orElseGet (() -> error (ctx, "Sale not found", 404));

    }

    @OpenApi(
            path = "/sales/postcode/{postcodeID}",
            methods = HttpMethod.GET,
            summary = "Find sales by postcode",
            description = "Returns a list of home sales filtered by the given postcode.",
            pathParams = {
                    @OpenApiParam(name = "postcodeID", description = "Postcode to filter sales", required = true)
            },
            responses = {
                    @OpenApiResponse(status = "200", description = "List of sales for postcode",
                            content = @OpenApiContent(from = HomeSale[].class)),
                    @OpenApiResponse(status = "404", description = "No sales found for postcode"),
                    @OpenApiResponse(status = "500", description = "Database error")
            }
    )
    public void findSaleByPostCode(Context ctx, String postCode) {
      List<HomeSale> sales = new ArrayList<>();
      try {
        sales = homeSales.getSalesByPostCode(postCode);
      } catch (SQLException e) {
          ctx.result("Database error: " + e.getMessage());
          ctx.status(500);
      }
      if (sales.isEmpty()) {
            ctx.result("No sales for postcode found");
            ctx.status(404);
        } else {
            ctx.json(sales);
            ctx.status(200);
        }
    }

    @OpenApi(
            path = "/sales/price_per_square_meter/average",
            methods = HttpMethod.GET,
            summary = "Get all average prices",
            description = "Returns a list of average price per square meter for each postcode.",
            responses = {
                    @OpenApiResponse(status = "200", description = "Average prices found",
                            content = @OpenApiContent(from = HomeSale[].class)),
                    @OpenApiResponse(status = "404", description = "No sales found"),
                    @OpenApiResponse(status = "500", description = "Database error")
            }
    )
    public void averagePricePerSquareMeter(Context ctx) {
      List<PricePerPostCode> allPairs = new ArrayList<>();
      try {
        allPairs = homeSales.getAveragePrice();
      } catch (SQLException e) {
        ctx.result("Database error: " + e.getMessage());
        ctx.status(500);
      }
      if (allPairs.isEmpty()) {
        ctx.result("No Sales Found");
        ctx.status(404);
      } else {
        ctx.json(allPairs);
        ctx.status(200);
      }
    }

  @OpenApi(
          path = "/sales/price_per_square_meter/high",
          methods = HttpMethod.GET,
          summary = "Get all high prices",
          description = "Returns a list of highest price per square meter for each postcode.",
          responses = {
                  @OpenApiResponse(status = "200", description = "High prices found",
                          content = @OpenApiContent(from = HomeSale[].class)),
                  @OpenApiResponse(status = "404", description = "No sales found"),
                  @OpenApiResponse(status = "500", description = "Database error")
          }
  )
  public void highPricePerSquareMeter(Context ctx) {
    List<PricePerPostCode> allPairs = new ArrayList<>();
    try {
      allPairs = homeSales.getHighPrice();
    } catch (SQLException e) {
      ctx.result("Database error: " + e.getMessage());
      ctx.status(500);
    }
    if (allPairs.isEmpty()) {
      ctx.result("No Sales Found");
      ctx.status(404);
    } else {
      ctx.json(allPairs);
      ctx.status(200);
    }
  }

  @OpenApi(
          path = "/sales/price_per_square_meter/low",
          methods = HttpMethod.GET,
          summary = "Get all low prices",
          description = "Returns a list of lowest price per square meter for each postcode.",
          responses = {
                  @OpenApiResponse(status = "200", description = "Low prices found",
                          content = @OpenApiContent(from = HomeSale[].class)),
                  @OpenApiResponse(status = "404", description = "No sales found"),
                  @OpenApiResponse(status = "500", description = "Database error")
          }
  )
  public void lowPricePerSquareMeter(Context ctx) {
    List<PricePerPostCode> allPairs = new ArrayList<>();
    try {
      allPairs = homeSales.getLowPrice();
    } catch (SQLException e) {
      ctx.result("Database error: " + e.getMessage());
      ctx.status(500);
    }
    if (allPairs.isEmpty()) {
      ctx.result("No Sales Found");
      ctx.status(404);
    } else {
      ctx.json(allPairs);
      ctx.status(200);
    }
  }

    private Context error(Context ctx, String msg, int code) {
        ctx.result(msg);
        ctx.status(code);
        return ctx;
    }



    


}
