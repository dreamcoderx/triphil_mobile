package com.tlicorporation.triphil.model;

public class Barcode {
    String model, refNo;
    int qty, cartonNo;

    public String getRefNo() {
        return refNo;
    }
    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }
    public Barcode() {}
    public int getCartonNo() {
        return cartonNo;
    }
    public void setCartonNo(int carton) {
        this.cartonNo = carton;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public int getQty() {
        return qty;
    }
    public void setQty(int qty) {
        this.qty = qty;
    }
}
