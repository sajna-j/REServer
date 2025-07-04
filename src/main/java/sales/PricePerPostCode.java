package sales;

public class PricePerPostCode {

  private int postCode;
  private double pricePerSquareMeter;

  public PricePerPostCode() {
  }

  public PricePerPostCode(int postCode, double pricePerSquareMeter) {
    this.postCode = postCode;
    this.pricePerSquareMeter = pricePerSquareMeter;
  }

  public int getPostCode() {
    return postCode;
  }

  public void setPostCode(int postCode) {
    this.postCode = postCode;
  }

  public double getPricePerSquareMeter() {
    return pricePerSquareMeter;
  }

  public void setPricePerSquareMeter(double pricePerSquareMeter) {
    this.pricePerSquareMeter = pricePerSquareMeter;
  }
}