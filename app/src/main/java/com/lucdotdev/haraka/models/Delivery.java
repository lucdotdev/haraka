package com.lucdotdev.haraka.models;

public class Delivery {
    private String name;
    private String delivery_id;
    private String livreur_id;
    private String store_id;
    private int status;
    private String description;
    private String client_adress;
    private String client_number;
    private String client_name;
    private String photoPath;
    private String id;

    public Delivery(){}

    public Delivery(String name, String delivery_id, String livreur_id, String store_id, int status, String description, String client_adress, String client_number, String client_name, String photoPath){
        this.name = name;
        this.delivery_id = delivery_id;
        this.livreur_id = livreur_id;
        this.store_id = store_id;
        this.status = status;
        this.description = description;
        this.client_adress = client_adress;
        this.client_number = client_number;
        this.client_name = client_name;
        this.photoPath = photoPath;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDelivery_id() {
        return delivery_id;
    }

    public void setDelivery_id(String delivery_id) {
        this.delivery_id = delivery_id;
    }

    public String getLivreur_id() {
        return livreur_id;
    }

    public void setLivreur_id(String livreur_id) {
        this.livreur_id = livreur_id;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getClient_adress() {
        return client_adress;
    }

    public void setClient_adress(String client_adress) {
        this.client_adress = client_adress;
    }

    public String getClient_number() {
        return client_number;
    }

    public void setClient_number(String client_number) {
        this.client_number = client_number;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
