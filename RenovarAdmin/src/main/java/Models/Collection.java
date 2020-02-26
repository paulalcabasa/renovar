package Models;

public class Collection {
    private int id;
    private String category;
    private String image_url;
    private int category_id;

    public Collection(int id, String category, String image_url, int category_id) {
        this.id = id;
        this.category = category;
        this.image_url = image_url;
        this.category_id = category_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }
}
