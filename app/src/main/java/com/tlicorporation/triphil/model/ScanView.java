package com.tlicorporation.triphil.model;

public class ScanView {
    int scanID;
    int containerNo;
    String refNo;
    int rowNo;
    String carton;
    String model;
    int qty;
    int scanNo;
    String delDate;
    private boolean isSelected;

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getScanID() {
        return scanID;
    }
    public int getContainerNo() {
        return containerNo;
    }
    public String getRefNo() {
        return refNo;
    }
    public int getRowNo() {
        return rowNo;
    }
    public String getCarton() {
        return carton;
    }
    public String getModel() {
        return model;
    }
    public int getQty() {
        return qty;
    }

    public String getDelDate() {
        return delDate;
    }
    public ScanView(int scanID, int containerNo, String refNo, int rowNo, String carton,
                    String model, int qty, int scanNo, String deldate) {
        this.scanID = scanID;
        this.refNo = refNo;
        this.containerNo = containerNo;
        this.rowNo = rowNo;
        this.carton = carton;
        this.model = model;
        this.qty = qty;
        this.scanNo = scanNo;
        this.delDate = deldate;
    }

    public ScanView() {
    }
}