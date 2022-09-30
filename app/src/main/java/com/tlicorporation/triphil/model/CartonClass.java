package com.tlicorporation.triphil.model;

import android.content.Context;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.tlicorporation.triphil.ConnectionClass;
public class CartonClass {

    Integer RowNo;
    Integer ScanNo;
    String DeliveryDate;
    Integer ContainerNo;
    String RefNo;
    Integer CartonNo;
    String ModelNo;
    Integer Qty;
    String ScannedBy;
    //Date ScannedDT;

    public CartonClass() {
    }
    
    public Integer getRowNo() {
        return RowNo;
    }

    public void setRowNo(Integer rowNo) {
        RowNo = rowNo;
    }
    public Integer getScanNo() {
        return ScanNo;
    }

    public void setScanNo(Integer scanNo) {
        ScanNo = scanNo;
    }

    public String getDeliveryDate() {
        return DeliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        DeliveryDate = deliveryDate;
    }

    public Integer getContainerNo() {
        return ContainerNo;
    }

    public void setContainerNo(Integer containerNo) {
        ContainerNo = containerNo;
    }

    public String getRefNo() {
        return RefNo;
    }

    public void setRefNo(String refNo) {
        RefNo = refNo;
    }

    public Integer getCartonNo() {
        return CartonNo;
    }

    public void setCartonNo(Integer cartonNo) {
        CartonNo = cartonNo;
    }

    public String getModelNo() {
        return ModelNo;
    }

    public void setModelNo(String modelNo) {
        ModelNo = modelNo;
    }

    public Integer getQty() {
        return Qty;
    }

    public void setQty(Integer qty) {
        Qty = qty;
    }

    public String getScannedBy() {
        return ScannedBy;
    }

    public void setScannedBy(String scannedBy) {
        ScannedBy = scannedBy;
    }

    

    public boolean SaveScan(Context ct) throws  SQLException {
        try {
            Connection con = ConnectionClass.CONN(ct);
            if (con == null) {
                Toast.makeText(ct,  "SaveScan: " + "SQL Server connection error!", Toast.LENGTH_LONG).show();
                return false;
            }

            String query = "INSERT INTO [mob].[tblContainerScannedCarton]" +
                    " (RowNo, ScanNo, DeliveryDate, ContainerNo" +
                    ", RefNo, CartonNo, ModelNo" +
                    ", Qty, ScannedBy)" +
                    " VALUES (?, ?, ?, ?" +
                    ", ?, ?, ?" +
                    ", ?, ?)" ;

            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, this.getRowNo());
            ps.setInt(2, this.getScanNo());
            ps.setString(3, this.getDeliveryDate());
            ps.setInt(4, this.getContainerNo());
            ps.setString(5, this.getRefNo());
            ps.setInt(6, this.getCartonNo());
            ps.setString(7, this.getModelNo());
            ps.setInt(8, this.getQty());
            ps.setString(9, this.getScannedBy());
            //ps.setString(10, getDateTime());
            ps.execute();
            return true;
        }
        catch (SQLException sqlEx){
            Toast.makeText(ct, sqlEx.getMessage(), Toast.LENGTH_LONG).show();
            return false;
        }
        catch (Exception ex) {
            Toast.makeText(ct, ex.getMessage(), Toast.LENGTH_LONG).show();
            return false;
        }
    }
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
