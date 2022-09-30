package com.tlicorporation.triphil.model;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.tlicorporation.triphil.ConnectionClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ContainerClass {
    private int containerNo;
    private int rowNo;
    private int maxQty;
    private String shipDate;
    private String message;
    private String refNo;
    private int containerQty;

    public ContainerClass() {
    }
    public int getContainerNo() {
        return containerNo;
    }
    public void setContainerNo(int containerNo) {
        this.containerNo = containerNo;
    }
    public int getRowNo() {
        return rowNo;
    }
    public void setRowNo(int rowNo) {
        this.rowNo = rowNo;
    }
    public int getMaxQty() {
        return maxQty;
    }
    public void setMaxQty(int maxQty) {
        this.maxQty = maxQty;
    }
    public String getShipDate() {
        return shipDate;
    }
    public void setShipDate(String shipDate) {
        this.shipDate = shipDate;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getRefNo() {
        return refNo;
    }
    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }
    public int GetMaxRowNo(Context ctx){
        try {
        Connection con = ConnectionClass.CONN(ctx);
        if (con == null) {
            Toast.makeText(ctx, "", Toast.LENGTH_LONG).show();
            return 0;
        }

        String sql = "select ISNULL(max(RowNo), 0 ) maxRowNo";
        sql += " from [mob].[tblContainerScannedCarton]";
        sql += " where DeliveryDate = ? and ContainerNo = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1,this.getShipDate());
        stmt.setInt(2, this.getContainerNo());
        ResultSet rs = stmt.executeQuery();
        if (rs.next() ) {
            return  rs.getInt("MaxRowNo");
        }
        return 0;
        } catch (Exception ex) {
            Toast.makeText(ctx, ex.getMessage(), Toast.LENGTH_SHORT).show();
            return 0;
        }
    }

    public int getCtrGloves(Context ctx){
        try {
            Connection con = ConnectionClass.CONN(ctx);
            if (con == null) {
                this.setMessage("Error in connection with SQL server");
                return 0;
            }

            String query = "select ISNULL(sum(Qty),0) sumGloves from mob.tblContainerScannedCarton where DeliveryDate = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, this.getShipDate());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return  rs.getInt("sumGloves");
            }
        return 0;
        } catch (Exception ex) {
            this.setMessage(ex.getMessage());
            Log.e("error", ex.getMessage());
            return 0;
        }
    }

   public int getCtrCartons(Context ctx){
        try {
        Connection con = ConnectionClass.CONN(ctx);
        if (con == null) {
            this.setMessage("Error in connection with SQL Server");
            return 0;
        }

        String query = "select count(DISTINCT CartonNo) cnt from mob.tblContainerScannedCarton where DeliveryDate = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, this.getShipDate());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return  rs.getInt("cnt");
        }
        return 0;

        } catch (Exception ex) {
            this.setMessage(ex.getMessage());
            Toast.makeText(ctx, ex.getMessage(), Toast.LENGTH_LONG).show();
            return 0;
        }

    }

    public int getContainerQty(Context ctx){
        try {
            Connection con = ConnectionClass.CONN(ctx);
            if (con == null) {
                this.setMessage("Error in connection with SQL Server");
                return 0;
            }

            String qry = "select count(DISTINCT CartonNo) cnt from mob.tblContainerScannedCarton";
            qry += " where DeliveryDate = ? and ContainerNo = ?";

            //String sql = "select count(*) cnt";
            //sql += " from [mob].[tblContainerScannedCarton]";
            //sql += " where DeliveryDate = ? and ContainerNo = ? group by CartonNo, RefNo";

            PreparedStatement stmt = con.prepareStatement(qry);
            stmt.setString(1,this.getShipDate());
            stmt.setInt(2, this.getContainerNo());
            ResultSet rs = stmt.executeQuery();
            if (rs.next() ) {
                return  rs.getInt("cnt");
            }
            return 0;
        } catch (Exception ex) {
            Toast.makeText(ctx, ex.getMessage(), Toast.LENGTH_SHORT).show();
            return 0;
        }
    }
}
