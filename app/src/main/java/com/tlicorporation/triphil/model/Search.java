package com.tlicorporation.triphil.model;

public class Search {
    String deliveryDate;
    int containerNo;
    int cartonNo;
    String modelNo;

    public Search() {
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public int getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(int containerNo) {
        this.containerNo = containerNo;
    }

    public int getCartonNo() {
        return cartonNo;
    }

    public void setCartonNo(int cartonNo) {
        this.cartonNo = cartonNo;
    }

    public String getModelNo() {
        return modelNo;
    }

    public void setModelNo(String modelNo) {
        this.modelNo = modelNo;
    }
}
