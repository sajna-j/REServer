package sales;

public class PricePerPostCode {

  private int post_code;
  private double price_per_square_meter;

  public PricePerPostCode() {
  }

  public PricePerPostCode(int post_code, double price_per_square_meter) {
    this.post_code = post_code;
    this.price_per_square_meter = price_per_square_meter;
  }

  public int getPost_code() {
    return post_code;
  }

  public void setPost_code(int post_code) {
    this.post_code = post_code;
  }

  public double getPrice_per_square_meter() {
    return price_per_square_meter;
  }

  public void setPrice_per_square_meter(double price_per_square_meter) {
    this.price_per_square_meter = price_per_square_meter;
  }
}