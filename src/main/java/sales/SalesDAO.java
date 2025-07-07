package sales;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SalesDAO {


    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/properties";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = null;

    public boolean newSale(HomeSale homeSale) throws SQLException {
        String sql = "INSERT INTO property_data (property_id, council_name, address, post_code, property_type, strata_lot_number, primary_purpose, zoning, property_name, legal_description, area_type, nature_of_property, area, purchase_price, download_date, contract_date, settlement_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, homeSale.getPropertyId());
            stmt.setString(2, homeSale.getCouncilName());
            stmt.setString(3, homeSale.getAddress());
            stmt.setString(4, homeSale.getPostCode());
            stmt.setString(5, homeSale.getPropertyType());
            stmt.setString(6, homeSale.getStrataLotNumber());
            stmt.setString(7, homeSale.getPrimaryPurpose());
            stmt.setString(8, homeSale.getZoning());
            stmt.setString(9, homeSale.getPropertyName());
            stmt.setString(10, homeSale.getLegalDescription());
            stmt.setString(11, homeSale.getAreaType());
            stmt.setString(12, homeSale.getNatureOfProperty());
            stmt.setBigDecimal(13, homeSale.getArea());
            stmt.setBigDecimal(14, homeSale.getPurchasePrice());
            stmt.setDate(15, Date.valueOf(homeSale.getDownloadDate()));
            stmt.setDate(16, Date.valueOf(homeSale.getContractDate()));
            stmt.setDate(17, Date.valueOf(homeSale.getSettlementDate()));

            stmt.executeUpdate();
            return true;
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
