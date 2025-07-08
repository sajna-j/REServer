package sales;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import java.math.BigDecimal;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.bson.Document;

public class SalesDAO {


    private static final String JDBC_URL = "mongodb+srv://vspillai02:cs4530exercise1@cs4530exercise1b.a7aa12o.mongodb.net/?retryWrites=true&w=majority&appName=cs4530exercise1";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = null;

    private final MongoCollection<Document> collection;

    public SalesDAO() throws MongoException {
        MongoClient mongoClient = MongoClients.create(JDBC_URL);
        MongoDatabase database = mongoClient.getDatabase("homesale");
        collection = database.getCollection("sales");
    }

    public boolean newSale(HomeSale homeSale) {
        try {
            Document saleDoc = new Document("property_id", homeSale.getPropertyId())
                    .append("council_name", homeSale.getCouncilName())
                    .append("address", homeSale.getAddress())
                    .append("post_code", homeSale.getPostCode())
                    .append("property_type", homeSale.getPropertyType())
                    .append("strata_lot_number", homeSale.getStrataLotNumber())
                    .append("primary_purpose", homeSale.getPrimaryPurpose())
                    .append("zoning", homeSale.getZoning())
                    .append("property_name", homeSale.getPropertyName())
                    .append("legal_description", homeSale.getLegalDescription())
                    .append("area_type", homeSale.getAreaType())
                    .append("nature_of_property", homeSale.getNatureOfProperty())
                    .append("area", homeSale.getArea())
                    .append("purchase_price", homeSale.getPurchasePrice())
                    .append("download_date", homeSale.getDownloadDate())
                    .append("contract_date", homeSale.getContractDate())
                    .append("settlement_date", homeSale.getSettlementDate())
                    .append("property_accessed_count", homeSale.getPropertyAccessedCount())
                    .append("post_code_accessed_count", homeSale.getPostCodeAccessedCount());

            collection.insertOne(saleDoc);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Optional<HomeSale> getSaleById(String saleID) throws MongoException {
        Document document = collection.find(Filters.eq("property_id", Long.parseLong(saleID))).first();
        collection.updateOne(Filters.eq("property_id", Long.parseLong(saleID)), Updates.inc("property_accessed_count", 1));
        return Optional.ofNullable(fromDocument(document));
    }

    public List<HomeSale> getSalesByPostCode(String postCode) throws MongoException {
        List<HomeSale> sales = new ArrayList<>();
        for (Document document : collection.find(Filters.eq("post_code", Long.parseLong(postCode)))) {
            HomeSale homeSale = fromDocument(document);
            if (homeSale != null) {
                sales.add(homeSale);
            }
        }
        collection.updateMany(Filters.eq("post_code", Long.parseLong(postCode)), Updates.inc("post_code_accessed_count", 1));
        return sales;
    }

    // returns all home sales. Potentially large
    public List<HomeSale> getAllSales() throws MongoException {
        List<HomeSale> sales = new ArrayList<>();
        for (Document document : collection.find()) {
            HomeSale homeSale = fromDocument(document);
            if (homeSale != null) {
                sales.add(homeSale);
            }
        }
        return sales;
    }

    private List<PricePerPostCode> getPrice(String op) throws MongoException {
        List<PricePerPostCode> pairs = new ArrayList<>();
        for (Document document : collection.find()) {
            PricePerPostCode pair = createPricePer(document);
            pairs.add(pair);
        }
        pairs = coalesce(pairs, op);
        return pairs;
    }

    public List<PricePerPostCode> getAveragePrice() throws SQLException {
        return getPrice("AVG");
    }

    public List<PricePerPostCode> getHighPrice() throws MongoException {
        return getPrice("MAX");
    }

    public List<PricePerPostCode> getLowPrice() throws MongoException {
        return getPrice("MIN");
    }

    private PricePerPostCode createPricePer(Document doc) throws MongoException {
        PricePerPostCode pricePerPostCode = new PricePerPostCode();
        pricePerPostCode.setPostCode(String.valueOf(doc.get("post_code")));
        pricePerPostCode.setPricePerSquareMeter(getPricePerSquareMeter(doc));
        return pricePerPostCode;
    }

    private double getPricePerSquareMeter(Document doc) {
        String areaObject = String.valueOf(doc.get("area"));
        double area;
        try {
            area = Double.parseDouble(areaObject);
        }
        catch (Exception e) {
            area = 0;
        }
        double purchasePrice = Double.parseDouble(doc.get("purchase_price").toString());
        String areaType = doc.getString("area_type");

        double effectiveArea = "H".equals(areaType) ? area * 10000 : area;
        double pricePerUnit = 0.0;

        if (effectiveArea != 0) {
            double rawValue = purchasePrice / effectiveArea;
            pricePerUnit = Math.round(rawValue * 100.0) / 100.0;
        }
        return pricePerUnit;
    }

    private static List<PricePerPostCode> coalesce(List<PricePerPostCode> pairs, String op) {
        Map<String, List<Double>> hash = new HashMap<>();
        List<PricePerPostCode> returnValue = new ArrayList<>();

        for (PricePerPostCode pair : pairs) {
            String postCode = pair.getPostCode();
            double pricePer = pair.getPricePerSquareMeter();

            if (hash.containsKey(postCode)) {
                List<Double> newList = hash.get(postCode);
                newList.add(pricePer);
                hash.put(postCode, newList);
            }
            else {
                hash.put(postCode, new ArrayList<>(List.of(pricePer)));
            }
        }


        for (String key : hash.keySet()) {
            double operated;

            if (op.equals("MIN")) {
                operated = hash.get(key).stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
            }
            else if (op.equals("MAX")) {
                operated = hash.get(key).stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
            }
            else {
                operated = hash.get(key).stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            }

            returnValue.add(new PricePerPostCode(key, operated));
        }

        return returnValue;
    }

    public static HomeSale fromDocument(Document doc) {
        if (doc == null) return null;

        return new HomeSale(
                doc.get("property_id") instanceof Number ? ((Number) doc.get("property_id")).longValue() : 0,
                String.valueOf(doc.get("council_name")),
                String.valueOf(doc.get("address")),
                String.valueOf(doc.get("post_code")),
                String.valueOf(doc.get("property_type")),
                String.valueOf(doc.get("strata_lot_number")),
                String.valueOf(doc.get("primary_purpose")),
                String.valueOf(doc.get("zoning")),
                String.valueOf(doc.get("property_name")),
                String.valueOf(doc.get("legal_description")),
                String.valueOf(doc.get("area_type")),
                String.valueOf(doc.get("nature_of_property")),
                toBigDecimal(doc, "area"),
                toBigDecimal(doc, "purchase_price"),
                toLocalDate(doc, "download_date"),
                toLocalDate(doc, "contract_date"),
                toLocalDate(doc, "settlement_date"),
                doc.getInteger("property_accessed_count"),
                doc.getInteger("post_code_accessed_count")
        );
    }


    private static BigDecimal toBigDecimal(Document doc, String key) {
        Object value = doc.get(key);
        if (value == null || value.equals("")) return BigDecimal.valueOf(0);
        if (value instanceof BigDecimal) return (BigDecimal) value;
        if (value instanceof Number) return BigDecimal.valueOf(((Number) value).doubleValue());
        if (value instanceof String) return new BigDecimal((String) value);
        return null;
    }

    private static LocalDate toLocalDate(Document doc, String key) {
        Object value = doc.get(key);
        if (value instanceof java.util.Date) {
            return Instant.ofEpochMilli(((java.util.Date) value).getTime())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        }
        return null;
    }
}
