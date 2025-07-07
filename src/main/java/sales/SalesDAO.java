package sales;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.bson.Document;

public class SalesDAO {


    private static final String JDBC_URL = "mongodb+srv://vspillai02:cs4530exercise1@cs4530exercise1.a7aa12o.mongodb.net/?retryWrites=true&w=majority&appName=cs4530exercise1";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = null;

    public boolean newSale(HomeSale homeSale) {
        try (MongoClient mongoClient = MongoClients.create(JDBC_URL)) {
            MongoDatabase database = mongoClient.getDatabase("homesale");
            MongoCollection<Document> collection = database.getCollection("sales");

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
                    .append("settlement_date", homeSale.getSettlementDate());

            collection.insertOne(saleDoc);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Optional<HomeSale> getSaleById(String saleID) throws SQLException {
        String selectQuery = "SELECT * FROM property_data WHERE property_id = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            updateAnalytics(conn, "property_id_analytics", "property_id", saleID);
            List<HomeSale> sales = selectHomeSales(conn, selectQuery, saleID);
            return sales.isEmpty() ? Optional.empty() : Optional.of(sales.get(0));
        }
    }

    public List<HomeSale> getSalesByPostCode(String postCode) throws SQLException {
        String selectQuery = "SELECT * FROM property_data WHERE post_code = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            updateAnalytics(conn, "post_code_analytics", "post_code", postCode);
            return selectHomeSales(conn, selectQuery, postCode);
        }
    }

    // returns the individual prices for all sales. Potentially large
    public List<BigDecimal> getAllSalePrices() throws SQLException {
        String query = "SELECT purchase_price FROM property_data";
        List<BigDecimal> prices = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet set = stmt.executeQuery();
            while (set.next()) {
                prices.add(set.getBigDecimal("purchase_price"));
            }
        }
        return prices;
    }

    // returns all home sales. Potentially large
    public List<HomeSale> getAllSales() throws SQLException {
        String query = "SELECT * FROM property_data LIMIT 1000";
        List<HomeSale> sales = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet set = stmt.executeQuery();
            while (set.next()) {
                sales.add(createHomeSale(set));
            }
        }
        return sales;
    }

    public List<PricePerPostCode> getAveragePrice() throws SQLException {
        String query = getPricePerQuery("AVG");
        List<PricePerPostCode> pairs = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet set = stmt.executeQuery();
            while (set.next()) {
                pairs.add(createPricePer(set));
            }
        }
        return pairs;
    }

    public List<PricePerPostCode> getHighPrice() throws SQLException {
        String query = getPricePerQuery("MAX");
        List<PricePerPostCode> pairs = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet set = stmt.executeQuery();
            while (set.next()) {
                pairs.add(createPricePer(set));
            }
        }
        return pairs;
    }

    public List<PricePerPostCode> getLowPrice() throws SQLException {
        String query = getPricePerQuery("MIN");
        List<PricePerPostCode> pairs = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet set = stmt.executeQuery();
            while (set.next()) {
                pairs.add(createPricePer(set));
            }
        }
        return pairs;
    }

    private void updateAnalytics(Connection conn, String table, String keyName, String keyValue) throws SQLException {
        String query = String.format("INSERT INTO %s (%s, access_count) VALUES (?, 1) ON DUPLICATE KEY UPDATE access_count = access_count + 1", table, keyName);
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, keyValue);
            stmt.executeUpdate();
        }
    }

    private List<HomeSale> selectHomeSales(Connection conn, String query, String param) throws SQLException {
        List<HomeSale> sales = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, param);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    sales.add(createHomeSale(rs));
                }
            }
        }
        return sales;
    }

    private HomeSale createHomeSale(ResultSet set) throws SQLException {
        HomeSale homeSale = new HomeSale();

        homeSale.setPropertyId(set.getLong("property_id"));
        if (set.wasNull()) {
            homeSale.setPropertyId(null);  // Handle possible NULL
        }

        homeSale.setCouncilName(set.getString("council_name"));
        homeSale.setAddress(set.getString("address"));
        homeSale.setPostCode(set.getString("post_code"));
        homeSale.setPropertyType(set.getString("property_type"));
        homeSale.setStrataLotNumber(set.getString("strata_lot_number"));
        homeSale.setPrimaryPurpose(set.getString("primary_purpose"));
        homeSale.setZoning(set.getString("zoning"));
        homeSale.setPropertyName(set.getString("property_name"));
        homeSale.setLegalDescription(set.getString("legal_description"));
        homeSale.setAreaType(set.getString("area_type"));
        homeSale.setNatureOfProperty(set.getString("nature_of_property"));

        homeSale.setArea(set.getBigDecimal("area"));
        homeSale.setPurchasePrice(set.getBigDecimal("purchase_price"));

        Date downloadDate = set.getDate("download_date");
        homeSale.setDownloadDate(downloadDate != null ? downloadDate.toLocalDate() : null);

        Date contractDate = set.getDate("contract_date");
        homeSale.setContractDate(contractDate != null ? contractDate.toLocalDate() : null);

        Date settlementDate = set.getDate("settlement_date");
        homeSale.setSettlementDate(settlementDate != null ? settlementDate.toLocalDate() : null);

        return homeSale;
    }

    private PricePerPostCode createPricePer(ResultSet set) throws SQLException {
        PricePerPostCode pricePerPostCode = new PricePerPostCode();
        pricePerPostCode.setPostCode(set.getInt("post_code"));
        pricePerPostCode.setPricePerSquareMeter(set.getDouble("price_per_unit"));
        return pricePerPostCode;
    }

    private String getPricePerQuery(String op) {
        return "SELECT post_code, ROUND(" + op + "(purchase_price / CASE WHEN area_type = 'H' THEN area * 10000 ELSE area END), 2) AS price_per_unit FROM property_data WHERE area IS NOT NULL AND post_code IS NOT NULL AND post_code >= 2000 AND area_type IS NOT NULL GROUP BY post_code;";
    }

}
