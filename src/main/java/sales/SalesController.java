package sales;

import io.javalin.http.Context;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SalesController {

    private SalesDAO homeSales;

    public SalesController(SalesDAO homeSales) {
        this.homeSales = homeSales;
    }

    // implements POST /sales
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
      } catch (SQLException e) {
          ctx.result("Database error: " + e.getMessage());
          ctx.status(500);
      }
    }

    // implements Get /sales
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

    // implements GET /sales/{saleID}
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

    // Implements GET /sales/postcode/{postcodeID}
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

    private Context error(Context ctx, String msg, int code) {
        ctx.result(msg);
        ctx.status(code);
        return ctx;
    }



    


}
