package sales;

import java.math.BigDecimal;
import java.time.LocalDate;

public class HomeSale {
    private Long property_id;
    private String council_name;
    private String address;
    private String post_code;
    private String property_type;
    private String strata_lot_number;
    private String primary_purpose;
    private String zoning;
    private String property_name;
    private String legal_description;
    private String area_type;
    private String nature_of_property;
    private BigDecimal area; // scale 4
    private BigDecimal purchase_price; // scale 2
    private LocalDate download_date;
    private LocalDate contract_date;
    private LocalDate settlement_date;

    // No-args constructor
    public HomeSale() {
    }

    // Full constructor
    public HomeSale(Long property_id, String council_name, String address, String post_code,
                    String property_type, String strata_lot_number, String primary_purpose,
                    String zoning, String property_name, String legal_description,
                    String area_type, String nature_of_property, BigDecimal area,
                    BigDecimal purchase_price, LocalDate download_date,
                    LocalDate contract_date, LocalDate settlement_date) {
        this.property_id = property_id;
        this.council_name = council_name;
        this.address = address;
        this.post_code = post_code;
        this.property_type = property_type;
        this.strata_lot_number = strata_lot_number;
        this.primary_purpose = primary_purpose;
        this.zoning = zoning;
        this.property_name = property_name;
        this.legal_description = legal_description;
        this.area_type = area_type;
        this.nature_of_property = nature_of_property;
        this.area = area;
        this.purchase_price = purchase_price;
        this.download_date = download_date;
        this.contract_date = contract_date;
        this.settlement_date = settlement_date;
    }

    public Long getPropertyId() {
        return property_id;
    }

    public void setPropertyId(Long property_id) {
        this.property_id = property_id;
    }

    public String getCouncilName() {
        return council_name;
    }

    public void setCouncilName(String council_name) {
        this.council_name = council_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostCode() {
        return post_code;
    }

    public void setPostCode(String post_code) {
        this.post_code = post_code;
    }

    public String getPropertyType() {
        return property_type;
    }

    public void setPropertyType(String property_type) {
        this.property_type = property_type;
    }

    public String getStrataLotNumber() {
        return strata_lot_number;
    }

    public void setStrataLotNumber(String strata_lot_number) {
        this.strata_lot_number = strata_lot_number;
    }

    public String getPrimaryPurpose() {
        return primary_purpose;
    }

    public void setPrimaryPurpose(String primary_purpose) {
        this.primary_purpose = primary_purpose;
    }

    public String getZoning() {
        return zoning;
    }

    public void setZoning(String zoning) {
        this.zoning = zoning;
    }

    public String getPropertyName() {
        return property_name;
    }

    public void setPropertyName(String property_name) {
        this.property_name = property_name;
    }

    public String getLegalDescription() {
        return legal_description;
    }

    public void setLegalDescription(String legal_description) {
        this.legal_description = legal_description;
    }

    public String getAreaType() {
        return area_type;
    }

    public void setAreaType(String area_type) {
        this.area_type = area_type;
    }

    public String getNatureOfProperty() {
        return nature_of_property;
    }

    public void setNatureOfProperty(String nature_of_property) {
        this.nature_of_property = nature_of_property;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public BigDecimal getPurchasePrice() {
        return purchase_price;
    }

    public void setPurchasePrice(BigDecimal purchase_price) {
        this.purchase_price = purchase_price;
    }

    public LocalDate getDownloadDate() {
        return download_date;
    }

    public void setDownloadDate(LocalDate download_date) {
        this.download_date = download_date;
    }

    public LocalDate getContractDate() {
        return contract_date;
    }

    public void setContractDate(LocalDate contract_date) {
        this.contract_date = contract_date;
    }

    public LocalDate getSettlementDate() {
        return settlement_date;
    }

    public void setSettlementDate(LocalDate settlement_date) {
        this.settlement_date = settlement_date;
    }
}