package sales;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

public class HomeSale {
    private Long propertyId;
    private String councilName;
    private String address;
    private String postCode;
    private String propertyType;
    private String strataLotNumber;
    private String primaryPurpose;
    private String zoning;
    private String propertyName;
    private String legalDescription;
    private String areaType;
    private String natureOfProperty;
    private BigDecimal area; // scale 4
    private BigDecimal purchasePrice; // scale 2
    private LocalDate downloadDate;
    private LocalDate contractDate;
    private LocalDate settlementDate;

    // No-args constructor
    public HomeSale() {
    }

    // Full constructor
    public HomeSale(Long propertyId, String councilName, String address, String postCode,
                    String propertyType, String strataLotNumber, String primaryPurpose,
                    String zoning, String propertyName, String legalDescription,
                    String areaType, String natureOfProperty, BigDecimal area,
                    BigDecimal purchasePrice, LocalDate downloadDate,
                    LocalDate contractDate, LocalDate settlementDate) {
        this.propertyId = propertyId;
        this.councilName = councilName;
        this.address = address;
        this.postCode = postCode;
        this.propertyType = propertyType;
        this.strataLotNumber = strataLotNumber;
        this.primaryPurpose = primaryPurpose;
        this.zoning = zoning;
        this.propertyName = propertyName;
        this.legalDescription = legalDescription;
        this.areaType = areaType;
        this.natureOfProperty = natureOfProperty;
        this.area = area;
        this.purchasePrice = purchasePrice;
        this.downloadDate = downloadDate;
        this.contractDate = contractDate;
        this.settlementDate = settlementDate;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public String getCouncilName() {
        return councilName;
    }

    public void setCouncilName(String councilName) {
        this.councilName = councilName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getStrataLotNumber() {
        return strataLotNumber;
    }

    public void setStrataLotNumber(String strataLotNumber) {
        this.strataLotNumber = strataLotNumber;
    }

    public String getPrimaryPurpose() {
        return primaryPurpose;
    }

    public void setPrimaryPurpose(String primaryPurpose) {
        this.primaryPurpose = primaryPurpose;
    }

    public String getZoning() {
        return zoning;
    }

    public void setZoning(String zoning) {
        this.zoning = zoning;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getLegalDescription() {
        return legalDescription;
    }

    public void setLegalDescription(String legalDescription) {
        this.legalDescription = legalDescription;
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public String getNatureOfProperty() {
        return natureOfProperty;
    }

    public void setNatureOfProperty(String natureOfProperty) {
        this.natureOfProperty = natureOfProperty;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public LocalDate getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(LocalDate downloadDate) {
        this.downloadDate = downloadDate;
    }

    public LocalDate getContractDate() {
        return contractDate;
    }

    public void setContractDate(LocalDate contractDate) {
        this.contractDate = contractDate;
    }

    public LocalDate getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(LocalDate settlementDate) {
        this.settlementDate = settlementDate;
    }
}