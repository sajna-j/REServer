package app;

import org.bson.Document;

import analytics.AnalyticsDAO;
import io.javalin.http.Context;

public class AnalyticsController {

  private final AnalyticsDAO analyticsDAO;

  public AnalyticsController(AnalyticsDAO analyticsDAO) {
    this.analyticsDAO = analyticsDAO;
  }

  public void incrementPropertyAnalytics(Context ctx) {
    String propertyId = ctx.pathParam("propertyID");
    Document result = analyticsDAO.incrementPropertyAnalytics(propertyId);
    if (result != null) {
      ctx.status(200).result(result.toString());
    } else {
      ctx.status(500).result("Failed to increment property view count");
    }
  }

  public void incrementPostcodeAnalytics(Context ctx) {
    String postcode = ctx.pathParam("postcodeID");
    Document result = analyticsDAO.incrementPostcodeAnalytics(postcode);
    if (result != null) {
      ctx.status(200).result(result.toString());
    } else {
      ctx.status(500).result("Failed to increment postcode view count");
    }
  }

  public void viewPropertyAnalytics(Context ctx) {
    String propertyId = ctx.pathParam("propertyID");
    Document count = analyticsDAO.viewPropertyAnalytics(propertyId);
    if (count != null) {
      ctx.status(200).result((String) count.get("property_accessed_count"));
    } else {
      ctx.status(404).result("Property not found");
    }
  }

  public void viewPostcodeAnalytics(Context ctx) {
    String postcode = ctx.pathParam("postcodeID");
    Document count = analyticsDAO.viewPostcodeAnalytics(postcode);
    if (count != null) {
      ctx.status(200).result((String) count.get("post_code_accessed_count"));
    } else {
      ctx.status(404).result("Postcode not found");
    }
  }
}