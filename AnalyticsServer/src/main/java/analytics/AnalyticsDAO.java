package analytics;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Updates;

import org.bson.Document;

import static sales.SalesDAO.JDBC_URL;

public class AnalyticsDAO {

  private final MongoCollection<Document> collection;

  public AnalyticsDAO() throws MongoException {
    MongoClient mongoClient = MongoClients.create(JDBC_URL);
    MongoDatabase database = mongoClient.getDatabase("homesale");
    collection = database.getCollection("sales");
  }

  public Document viewPropertyAnalytics(String saleID) {
    return collection.find(Filters.eq("property_id", Long.parseLong(saleID)))
            .projection(Projections.include("property_accessed_count"))
            .first();
  }

  public Document incrementPropertyAnalytics(String saleID) {
    collection.updateOne(Filters.eq("property_id", Long.parseLong(saleID)), Updates.inc("property_accessed_count", 1));
    return viewPostcodeAnalytics(saleID);
  }

  public Document viewPostcodeAnalytics(String postCode) {
    return collection.find(Filters.eq("post_code", Long.parseLong(postCode)))
            .projection(Projections.include("post_code_accessed_count"))
            .first();
  }

  public Document incrementPostcodeAnalytics(String postCode) {
    collection.updateMany(Filters.eq("post_code", Long.parseLong(postCode)), Updates.inc("post_code_accessed_count", 1));
    return viewPostcodeAnalytics(postCode);
  }

}
