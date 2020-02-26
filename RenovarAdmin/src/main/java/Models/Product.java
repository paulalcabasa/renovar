package Models;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Product {
    private String id;
    private String name;
    private String image_url;
    private String description;
    private double price;
    private int interval;
    private int category_id;
    private String camera_message;
    private int collection_id;

    public Product() {
    }

    public Product(String id, String name, String image_url, String description, double price, int interval, int category_id, String camera_message, int collection_id) {
        this.id = id;
        this.name = name;
        this.image_url = image_url;
        this.description = description;
        this.price = price;
        this.interval = interval;
        this.category_id = category_id;
        this.camera_message = camera_message;
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

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCamera_message() {
        return camera_message;
    }

    public void setCamera_message(String camera_message) {
        this.camera_message = camera_message;
    }

    public int getCollection_id() {
        return collection_id;
    }

    public void setCollection_id(int collection_id) {
        this.collection_id = collection_id;
    }

    public String insertProduct() throws IOException {

        String link = "http://renovar.health/renovarmobile/insert_product.php";
        String data = URLEncoder.encode("interval", "UTF-8") + "=" +
                URLEncoder.encode(String.valueOf(interval), "UTF-8");
        data += "&" + URLEncoder.encode("name", "UTF-8") + "=" +
                URLEncoder.encode(name, "UTF-8");
        data += "&" + URLEncoder.encode("image_url", "UTF-8") + "=" +
                URLEncoder.encode(image_url, "UTF-8");
        data += "&" + URLEncoder.encode("price", "UTF-8") + "=" +
                URLEncoder.encode(String.valueOf(price), "UTF-8");
        data += "&" + URLEncoder.encode("description", "UTF-8") + "=" +
                URLEncoder.encode(description, "UTF-8");
        data += "&" + URLEncoder.encode("category_id", "UTF-8") + "=" +
                URLEncoder.encode(String.valueOf(category_id), "UTF-8");
        data += "&" + URLEncoder.encode("collection_id", "UTF-8") + "=" +
                URLEncoder.encode(String.valueOf(collection_id), "UTF-8");
        data += "&" + URLEncoder.encode("cam_message", "UTF-8") + "=" +
                URLEncoder.encode(getCamera_message(), "UTF-8");

        URL url = new URL(link);
        URLConnection conn = url.openConnection();

        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

        wr.write(data);
        wr.flush();

        BufferedReader reader = new BufferedReader(new
                InputStreamReader(conn.getInputStream()));

        StringBuilder sb = new StringBuilder();
        String line = null;

        while ((line = reader.readLine()) != null) {
            sb.append(line);
            break;
        }
        return sb.toString();
    }

    public String updateProduct() throws IOException {

        String link = "http://renovar.health/renovarmobile/update_product.php";
        String data = URLEncoder.encode("interval", "UTF-8") + "=" +
                URLEncoder.encode(String.valueOf(interval), "UTF-8");
        data += "&" + URLEncoder.encode("name", "UTF-8") + "=" +
                URLEncoder.encode(name, "UTF-8");
        data += "&" + URLEncoder.encode("image_url", "UTF-8") + "=" +
                URLEncoder.encode(image_url, "UTF-8");
        data += "&" + URLEncoder.encode("price", "UTF-8") + "=" +
                URLEncoder.encode(String.valueOf(price), "UTF-8");
        data += "&" + URLEncoder.encode("description", "UTF-8") + "=" +
                URLEncoder.encode(description, "UTF-8");
        data += "&" + URLEncoder.encode("category_id", "UTF-8") + "=" +
                URLEncoder.encode(String.valueOf(category_id), "UTF-8");
        data += "&" + URLEncoder.encode("collection_id", "UTF-8") + "=" +
                URLEncoder.encode(String.valueOf(collection_id), "UTF-8");
        data += "&" + URLEncoder.encode("cam_message", "UTF-8") + "=" +
                URLEncoder.encode(camera_message, "UTF-8");
        data += "&" + URLEncoder.encode("id", "UTF-8") + "=" +
                URLEncoder.encode(id, "UTF-8");

        URL url = new URL(link);
        URLConnection conn = url.openConnection();

        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

        wr.write(data);
        wr.flush();

        BufferedReader reader = new BufferedReader(new
                InputStreamReader(conn.getInputStream()));

        StringBuilder sb = new StringBuilder();
        String line = null;

        while ((line = reader.readLine()) != null) {
            sb.append(line);
            break;
        }
        return sb.toString();
    }

    public String deleteProduct() throws IOException {

        String link = "http://renovar.health/renovarmobile/delete_product.php";
        String data = URLEncoder.encode("id", "UTF-8") + "=" +
                URLEncoder.encode(id, "UTF-8");

        URL url = new URL(link);
        URLConnection conn = url.openConnection();

        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

        wr.write(data);
        wr.flush();

        BufferedReader reader = new BufferedReader(new
                InputStreamReader(conn.getInputStream()));

        StringBuilder sb = new StringBuilder();
        String line = null;

        while ((line = reader.readLine()) != null) {
            sb.append(line);
            break;
        }
        return sb.toString();
    }
}
