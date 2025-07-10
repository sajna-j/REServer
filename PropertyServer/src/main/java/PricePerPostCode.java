public class PricePerPostCode {

  private String postCode;
  private double pricePerSquareMeter;

  public PricePerPostCode() {}

  public PricePerPostCode(String postCode, double pricePerSquareMeter) {
    this.postCode = postCode;
    this.pricePerSquareMeter = pricePerSquareMeter;
  }

  public String getPostCode() {
    return postCode;
  }

  public void setPostCode(String postCode) {
    this.postCode = postCode;
  }

  public double getPricePerSquareMeter() {
    return pricePerSquareMeter;
  }

  public void setPricePerSquareMeter(double pricePerSquareMeter) {
    this.pricePerSquareMeter = pricePerSquareMeter;
  }
}