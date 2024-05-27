package com.project.foodApp.Model;


import java.io.Serializable;

public class Cart implements Serializable {
    private int id;
    private int ProductId;
    private String ProductName;
    private int Quantity;
    private String Price;
    private String Total;
    private String ImagePath;
    private String userId;

    public Cart() {
    }

    public Cart(int productId, String productName, int quantity, String price, String total, String imagePath, String userId) {
        ProductId = productId;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        Total = total;
        ImagePath = imagePath;
        this.userId = userId;
    }

    public Cart(int id, int productId, String productName, int quantity, String price, String total, String imagePath) {
        this.id = id;
        ProductId = productId;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        Total = total;
        ImagePath = imagePath;
    }

    public Cart(int productId, String productName, int quantity, String price, String total, String imagePath) {
        ProductId = productId;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        Total = total;
        ImagePath = imagePath;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", ProductId=" + ProductId +
                ", ProductName='" + ProductName + '\'' +
                ", Quantity=" + Quantity +
                ", Price='" + Price + '\'' +
                ", Total='" + Total + '\'' +
                '}';
    }
}
