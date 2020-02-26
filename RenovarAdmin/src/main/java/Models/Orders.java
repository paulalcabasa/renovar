package Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONObject;

public class Orders {
    private String id;
    private String email;
    private String firstname;
    private String lastname;
    private String street1;
    private String street2;
    private String country;
    private String city;
    private String state;
    private String zipcode;
    private String orders;
    private String date;
    private String total_price;
    private String status;


    public Orders(String id, String email, String firstname, String lastname, String street1,
                  String street2, String country, String city, String state, String zipcode, String orders,
                  String date, String total_price, String status) {
        this.id = id;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.street1 = street1;
        this.street2 = street2;
        this.country = country;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.orders = orders;
        this.date = date;
        this.total_price = total_price;
        this.status = status;
    }

    public String copyFullDetails()
    {
        String text =  "ORDER NO : " + getId() + "\n\nEMAIL : "+getEmail()+"\nFULLNAME : "+getFirstname() + " " + getLastname()
                + "\n\nBILLING/SHIPPING ADDRESS : \nStreet 1 :\t "+ getStreet1() + "\nStreet 2 :\t  "+ getStreet2()
                + "\nCity : "+ getCity() + "\nState/Province : "+ getState() + "\nCountry : "+ getCountry()
                + "\nZip/Postal Code : "+ getZipcode()+ "\nDate & Time : "+ getDate()
                +"\n\nORDERS\n";

        JSONObject jsonOrders = new JSONObject("{ \"dataArray\" : "+getOrders()+"}");
        JSONArray orderArray = jsonOrders.getJSONArray("dataArray");

        String orderText = "";

        for (int index = 0 ; index < orderArray.length(); index++) {
            JSONObject orderObject = orderArray.getJSONObject(index);
            int quantity = orderObject.getInt("quantity");
            String item_name = orderObject.getString("item_name");
            String image_url = orderObject.getString("image_url");
            double total_price = orderObject.getDouble("total_price");

            orderText += item_name + "\t\tx"+quantity+" pc/s \tUS$"+total_price+"\n";
        }

        return text + orderText + "\nGRAND TOTAL : " + getTotal_price();
    }

    public String getHTMLforEmail()
    {
        String htmlText = "";

        htmlText += "<B>ORDER DETAILS</B><br>";

        htmlText += "<table style = 'width:100%'>" +
                "<tr>" +
                    "<td><b>ORDER NO</b></td>"+
                    "<td>"+getId()+"</td>"+
                "</tr>" +
                "<tr>" +
                "<td><b>FULLNAME</b></td>"+
                "<td>"+getFirstname() +" " + getLastname() +"</td>"+
                "</tr>" +
                "<tr>" +
                "<td><b>EMAIL</b></td>"+
                "<td>"+getEmail()+"</td>"+
                "</tr>" +
                    "<tr>" +
                    "<td><b>DATE & TIME</b></td>"+
                    "<td>"+getDate() +"</td>"+
                "</tr>" +
                "</tr>" +
                "<tr>" +
                "<td><b>SHIPPING/BILLING ADDRESS</b></td>"+
                "<td>"+street1 + " " + street2 + " "+ city +", "+ state + " " + country +" " + zipcode+"</td>"+
                "</tr>" +
                "</table><br>";

        htmlText += "<B>ORDER LIST</B><br><table style=\"width:100%\"  border='1px solid black';>\n" +
                "  <tr>\n" +
                "    <th>PRODUCT NAME</th>\n" +
                "    <th>TOTAL PRICE</th> \n" +
                "    <th>QUANTITY</th>\n" +
                "  </tr>\n";

        JSONObject jsonOrders = new JSONObject("{ \"dataArray\" : "+getOrders()+"}");
        JSONArray orderArray = jsonOrders.getJSONArray("dataArray");

        for (int index = 0 ; index < orderArray.length(); index++) {
            JSONObject orderObject = orderArray.getJSONObject(index);
            int quantity = orderObject.getInt("quantity");
            String item_name = orderObject.getString("item_name");
            String image_url = orderObject.getString("image_url");
            double total_price = orderObject.getDouble("total_price");

//            orderText += item_name + "\t\tx"+quantity+" pc/s \tUS$"+total_price+"\n";

            htmlText += "<tr><td>"+item_name+"</td><td>"+quantity+"</td><td>US$"+total_price+"</td><tr>";
        }

        htmlText +=  "<tr>" +
                "<td></td>"+
                "<td><b>GRAND TOTAL</b></td>"+
                "<td>"+total_price+"</td>"+
                "</tr>";

        htmlText += "</table>";
        return htmlText;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStatus() {
        String stat = "";
        switch (status)
        {
            case "0" : stat = "Pending";
                break;
            case "1": stat = "Complete";
                break;
            case "2": stat = "Rejected";
                break;
        }
        return stat;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }
}
