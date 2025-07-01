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
        String sql = "INSERT INTO property_data (property_id, download_date, council_name, purchase_price, address, post_code, property_type, strata_lot_number, property_name, area, area_type, contract_date, settlement_date, zoning, nature_of_property, primary_purpose, legal_description) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, homeSale.getPropertyId());
            stmt.setDate(2, Date.valueOf(homeSale.getDownloadDate()));
            stmt.setString(3, homeSale.getCouncilName());
            stmt.setBigDecimal(4, homeSale.getPurchasePrice());
            stmt.setString(5, homeSale.getAddress());
            stmt.setString(6, homeSale.getPostCode());
            stmt.setString(7, homeSale.getPropertyType());
            stmt.setString(8, homeSale.getStrataLotNumber());
            stmt.setString(9, homeSale.getPropertyName());
            stmt.setBigDecimal(10, homeSale.getArea());
            stmt.setString(11, homeSale.getAreaType());
            stmt.setDate(12, Date.valueOf(homeSale.getContractDate()));
            stmt.setDate(13, Date.valueOf(homeSale.getSettlementDate()));
            stmt.setString(14, homeSale.getZoning());
            stmt.setString(15, homeSale.getNatureOfProperty());
            stmt.setString(16, homeSale.getPrimaryPurpose());
            stmt.setString(17, homeSale.getLegalDescription());

            stmt.executeUpdate();
            return true;
        }
    }

    public Optional<HomeSale> getSaleById(String saleID) throws SQLException {
        String query = "SELECT * FROM property_data WHERE property_id = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, saleID);
            ResultSet set = stmt.executeQuery();
            if (set.next()) {
                return Optional.of(createHomeSale(set));
            }
        }
        return Optional.empty();
    }

    // returns a List of homesales  in a given postCode
    public List<HomeSale> getSalesByPostCode(String postCode) throws SQLException {
        String query = "SELECT * FROM property_data WHERE post_code = ?";
        List<HomeSale> sales = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, postCode);
            ResultSet set = stmt.executeQuery();
            while (set.next()) {
                sales.add(createHomeSale(set));
            }
        }
        return sales;
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


    private HomeSale createHomeSale(ResultSet set) throws SQLException {
        HomeSale homeSale = new HomeSale();

        homeSale.setPropertyId(set.getLong("property_id"));
        if (set.wasNull()) homeSale.setPropertyId(null);  // Handle possible NULL

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

}
