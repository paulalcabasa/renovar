package Models;

public class ProductsView {
    private String id;
    private String name;
    private String image_url;
    private String description;
    private double price;
    private int interval;
    private String category;
    private String camera_message;
    private String collection;
    private int category_id;
    private int collection_id;

    public ProductsView(String id, String name, String image_url, String description, double price, int interval, String category, String camera_message, String collection, int category_id, int collection_id) {
        this.id = id;
        this.name = name;
        this.image_url = image_url;
        this.description = description;
        this.price = price;
        this.interval = interval;
        this.category = category;
        this.camera_message = camera_message;
        this.collection = collection;
        this.category_id = category_id;
        this.collection_id = collection_id;
    }

    public ProductsView(String id, String name, String image_url, String description, double price, int interval, String category_id, String camera_message, String collection_id) {
        this.id = id;
        this.name = name;
        this.image_url = image_url;
        this.description = description;
        this.price = price;
        this.interval = interval;
        this.category = category_id;
        this.camera_message = camera_message;
        this.collection = collection_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getCollection_id() {
        return collection_id;
    }

    public void setCollection_id(int collection_id) {
        this.collection_id = collection_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCamera_message() {
        return camera_message;
    }

    public void setCamera_message(String camera_message) {
        this.camera_message = camera_message;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }
}
