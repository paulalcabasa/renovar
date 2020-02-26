package Models;

public class ItemOrder {
    private int quantity;
    private String item_name;
    private String image_url;
    private double total_price;

    public ItemOrder(int quantity, String item_name, String image_url, double total_price) {
        this.quantity = quantity;
        this.item_name = item_name;
        this.image_url = image_url;
        this.total_price = total_price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }
}
