package database;

import java.util.UUID;

public class Product {
    private int id;
    private String prodId;
    private String title;
    private int cost;

    public Product() {
        this(0, "", 0);
    }

    public Product(int id, String prodId, String title, int cost) throws IllegalArgumentException {
        if (cost < 0) {
            throw new IllegalArgumentException("Cost can't be negative!");
        }
        this.id = id;
        this.prodId = prodId;
        this.title = title;
        this.cost = cost;
    }

    public Product(int id, String title, int cost) {
        this(id, UUID.randomUUID().toString(), title, cost);
    }

    public int getId() {
        return id;
    }

    public String getProdId() {
        return prodId;
    }

    public String getTitle() {
        return title;
    }

    public int getCost() {
        return cost;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCost(int cost) throws IllegalArgumentException {
        if (cost < 0) {
            throw new IllegalArgumentException("Cost can't be negative!");
        }
        this.cost = cost;
    }


    @Override
    public String toString() {
        return "Id=" + id + " ProdId=" + prodId + " Title=" + title + " Cost=" + cost;
    }
}
