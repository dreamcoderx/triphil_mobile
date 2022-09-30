package com.tlicorporation.triphil.model;

public class Container {
    private int ContainerNo, RowNo;
    private String DeliveryDate, ReferenceNo;
    private String ScannedBy;
    public Container() {
    }

    public int getContainerNo() {
        return ContainerNo;
    }

    public void setContainerNo(int containerNo) {
        ContainerNo = containerNo;
    }

    public int getRowNo() {
        return RowNo;
    }

    public void setRowNo(int rowNo) {
        RowNo = rowNo;
    }

    public String getDeliveryDate() {
        return DeliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        DeliveryDate = deliveryDate;
    }

    public String getReferenceNo() {
        return ReferenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        ReferenceNo = referenceNo;
    }

    public String getScannedBy() {
        return ScannedBy;
    }

    public void setScannedBy(String scannedBy) {
        ScannedBy = scannedBy;
    }
}
