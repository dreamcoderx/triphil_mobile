package com.tlicorporation.triphil.mssql;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.tlicorporation.triphil.ConnectionClass;
import com.tlicorporation.triphil.model.Barcode;
import com.tlicorporation.triphil.model.ScanView;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContainerScan {

    private int ContainerNo;
    private String DeliveryDate, ReferenceNo;
    private String ScannedBy;


    private int Counter;
    private int CurrentRow;
    private int RowMaxQty;

    private int TotGlovesQty;
    private int TotCartonQty;
    private String Message;
    private Connection con;

    public int getCurrentRow() {
        return CurrentRow;
    }

    public void setCurrentRow(int currentRow) {
        CurrentRow = currentRow;
    }

    public void incrementRowNo() {
        CurrentRow = CurrentRow + 1;
    }

    public boolean isMaxReached(){
        if (getCounter() >= getRowMaxQty()){
            return true;
        }else{
            return false;
        }
    }

    public int getCounter() {
        return Counter;
    }

    public void setCounter(int counter) {
        Counter = counter;
    }

    public int getRowMaxQty() {
        return RowMaxQty;
    }

    public void setRowMaxQty(int rowMaxQty) {
        RowMaxQty = rowMaxQty;
    }

    public void incrementRowMax() {
        RowMaxQty = RowMaxQty + 1;
    }




    public ContainerScan(Context context) {
        this.context = context;
    }
    public int getContainerNo() {
        return ContainerNo;
    }

    public void setContainerNo(int containerNo) {
        ContainerNo = containerNo;
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



    public int getTotGlovesQty() {
        return TotGlovesQty;
    }

    public int getTotCartonQty() {
        return TotCartonQty;
    }


    public Connection getCon() {
        return con;
    }
    public void setCon(Connection con) {
        this.con = con;
    }
    public String getMessage() {
        return Message;
    }
    public void setMessage(String message) {
        Message = message;
    }
    public Context getContext() {
        return context;
    }
    public void setContext(Context context) {
        this.context = context;
    }
    private Context context;


    public void getTotalGlovesCartonsQty() throws SQLException {
        Connection con = ConnectionClass.CONN(context);
        if (con == null) {
            setMessage("Connection Error!");
            return;
        }
        CallableStatement cs ;
        ResultSet rs;
        cs = con.prepareCall("{call dbo.spShipmentScanningInfo(?)}");
        cs.setString(1, getDeliveryDate());
        rs = cs.executeQuery();
        if (rs.next()) {
            TotGlovesQty = rs.getInt("TotalGlovesQty");
            TotCartonQty = rs.getInt("TotalCartonQty");
        }
    }

    public boolean GetLastScanRowNo(){
        try {
            Connection con = ConnectionClass.CONN(context);
            if (con == null) {
                Toast.makeText(context, "SQL Server Error!", Toast.LENGTH_LONG).show();
                setCurrentRow(1);
                return false;
            }

            String sql = "select ISNULL(max(RowNo), 0 ) maxRowNo";
            sql += " from [mob].[tblContainerScannedCarton]";
            sql += " where DeliveryDate = ? and ContainerNo = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, getDeliveryDate());
            stmt.setInt(2, getContainerNo());
            ResultSet rs = stmt.executeQuery();
            if (rs.next() ) {
                setCurrentRow( rs.getInt("MaxRowNo"));
                return true;
            }
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
        return false;
    }

    public void GetRowMaxDb() {
        try {
            Connection con = ConnectionClass.CONN(context);
            if (con == null) {
                this.setMessage("Database Error!");
                return;
            }
            String qry = "SELECT [id],[ShipDate],[RowNo],[ContainerNo],[MaxQty]" +
                    " FROM mob.tblRowMax " +
                    " where ShipDate = ?" +
                    " and RowNo = ?" +
                    " and ContainerNo = ?";
            PreparedStatement stmt = con.prepareStatement(qry);

            stmt.setString(1, getDeliveryDate());
            stmt.setInt(2, getCurrentRow());
            stmt.setInt(3, getContainerNo());

            ResultSet rs = stmt.executeQuery( );
            if (rs.next() ) {
                RowMaxQty = rs.getInt("MaxQty");
                return;
            }
            RowMaxQty = 0;
            return;
        }catch (Exception e){
            setMessage(e.getMessage());
            Log.e("error", "");

        }
    }

    public int getCountRow(){
        try {
            Connection con = ConnectionClass.CONN(context);
            if (con == null) {
                setMessage("getCountRow: "+ "Error in connection with SQL server");
                return 0;
            }

            String query = "select count(DISTINCT CartonNo) cnt from mob.tblContainerScannedCarton" +
                    " where DeliveryDate = ? and ContainerNo = ? and RowNo = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1,getDeliveryDate());
            stmt.setInt(2, getContainerNo());
            stmt.setInt(3, getCurrentRow());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("cnt");
            }

        } catch (Exception ex) {
            setMessage(ex.getMessage());
            return 0;
        }
        return 0;
    }

    public int getCtrGloves(){
        try {
            Connection con = ConnectionClass.CONN(context);
            if (con == null) {
                this.setMessage("Error in connection with SQL server");
                return 0;
            }

            String query = "select ISNULL(sum(Qty),0) sumGloves from mob.tblContainerScannedCarton where DeliveryDate = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, getDeliveryDate());
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

    public int getCtrCartons(){
        try {
            Connection con = ConnectionClass.CONN(context);
            if (con == null) {
                this.setMessage("Error in connection with SQL Server");
                return 0;
            }

            String query = "select count(DISTINCT CartonNo) cnt from mob.tblContainerScannedCarton where DeliveryDate = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,getDeliveryDate());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return  rs.getInt("cnt");
            }
            return 0;

        } catch (Exception ex) {
            this.setMessage(ex.getMessage());
            return 0;
        }

    }

    public int getContainerQty(){
        try {
            Connection con = ConnectionClass.CONN(context);
            if (con == null) {
                this.setMessage("Error in connection with SQL Server");
                return 0;
            }

            String qry = "select count(DISTINCT CartonNo) cnt from mob.tblContainerScannedCarton";
            qry += " where DeliveryDate = ? and ContainerNo = ?";


            PreparedStatement stmt = con.prepareStatement(qry);
            stmt.setString(1,getDeliveryDate());
            stmt.setInt(2, getContainerNo());
            ResultSet rs = stmt.executeQuery();
            if (rs.next() ) {
                return  rs.getInt("cnt");
            }
            return 0;
        } catch (Exception ex) {
            setMessage(ex.getMessage());
            //Toast.makeText(ctx, ex.getMessage(), Toast.LENGTH_SHORT).show();
            return 0;
        }
    }


    public boolean updateRowMax() {
        Connection con = ConnectionClass.CONN(context);

        try{

            if (con == null) {
                setMessage("db error!");
                return false;
            }

            String sqUpd = "UPDATE mob.tblRowMax" +
                    " SET MaxQty = ?" +
                    " WHERE ShipDate = ? and RowNo = ? and ContainerNo = ?";

            PreparedStatement stmt = con.prepareStatement(sqUpd);
            stmt.setInt(1, getRowMaxQty());
            stmt.setString(2, getDeliveryDate());
            stmt.setInt(3, getCurrentRow());
            stmt.setInt(4, getContainerNo());

            if (stmt.executeUpdate() < 1){

                String sqIns = "INSERT INTO mob.tblRowMax" +
                        " (ShipDate, RowNo, ContainerNo, MaxQty)" +
                        " VALUES(?,?,?,?)";
                PreparedStatement stmtIns = con.prepareStatement(sqIns);
                stmtIns.setString(1, getDeliveryDate());
                stmtIns.setInt(2, getCurrentRow());
                stmtIns.setInt(3, getContainerNo());
                stmtIns.setInt(4, getRowMaxQty());
                stmtIns.execute();
            }

            return true;

        }catch (SQLException sqEx){
            setMessage(sqEx.getMessage());
            //Toast.makeText(this.ctx, sqEx.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    public void funcCtrGlovesCartonsQty(String delDate){
        try {
            Connection con = ConnectionClass.CONN(context);

            if (con == null) {
                setMessage("sql error!");
                return;
            }
            CallableStatement cs ;
            ResultSet rs;
            cs = con.prepareCall("{call dbo.spShipmentScanningInfo(?)}");
            cs.setString(1, delDate);
            rs = cs.executeQuery();
            if (rs.next()) {
                TotGlovesQty =   rs.getInt("TotalGlovesQty");
                TotCartonQty = rs.getInt("TotalCartonQty");
            }
        } catch (Exception ex) {
            setMessage(ex.getMessage());
            //Toast.makeText(this, "setCtrGlovesCartonsQty: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public boolean SaveMixedModel(Barcode bc){
        try{
            Connection con = ConnectionClass.CONN(context);
            if (con == null) {
                setMessage("db error!");
                return false;
            }

            String q = "select * from tag.tbl_ScannedCarton";
            q += " where CartonNo = ?";
            q += " and RefNo = ?" ;

            PreparedStatement ps = con.prepareStatement(q);
            //ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            ps.setInt(1,bc.getCartonNo());
            ps.setString(2, bc.getRefNo());
            ResultSet rs = ps.executeQuery();

            int ctr =0;
            ArrayList<Barcode> arrList = new ArrayList<>();


            while (rs.next()) {
                ctr = ctr + 1;
                Barcode barcode = new Barcode();
                barcode.setModel(rs.getString("ModelNo"));
                barcode.setCartonNo(rs.getInt("CartonNo"));
                barcode.setQty(rs.getInt("Qty"));
                barcode.setRefNo(bc.getRefNo());
                arrList.add(barcode);

            }

            if (ctr < 2){
                setMessage("invalid mixed model!");
                return false;
            }

            for (int c = 0; c < arrList.size(); c++) {
                Barcode bcSave = new Barcode();
                bcSave.setRefNo(bc.getRefNo());
                bcSave.setCartonNo(arrList.get(c).getCartonNo());
                bcSave.setModel(arrList.get(c).getModel());
                bcSave.setQty(arrList.get(c).getQty());
                SaveDataSQL(bcSave);
            }
            return true;
        } catch (SQLException sqEx) {
           setMessage("SaveMixedModel: " + sqEx.getMessage());
           return false;
        }
        catch(Exception ex){
            setMessage("saveMixedModel" + ex.getMessage());
            return false;
        }
    }

    boolean isScannedContainer(Barcode bc){
        // default true
        try {
            Connection con = ConnectionClass.CONN(context);
            if (con == null) {
                setMessage("isScannedContainer: "+" SQL SERVER connection error!");
                return true;
            }

            String query = "select * from mob.tblContainerScannedCarton" +
                    " where CartonNo = ?" +
                    " and ModelNo = ?" +
                    " and Qty= ?" +
                    " and RefNo = ?";

            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, bc.getCartonNo());
            stmt.setString(2, bc.getModel());
            stmt.setInt(3, bc.getQty());
            stmt.setString(4, bc.getRefNo());
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                return false;
            }
            String msg;
            msg = "Barcode already scanned!";
            msg += " Carton: " + bc.getCartonNo() ;
            msg += ", Model: " + bc.getModel() ;
            msg += ", Qty: " + bc.getQty() ;
            msg += ", Ref No: " + bc.getRefNo() ;

            setMessage(msg);
            return true;

        } catch (Exception ex) {
            setMessage(ex.getMessage());
            return true;
        }
    }

    public boolean SaveDataSQL(Barcode bc){
        try {
            Connection con = ConnectionClass.CONN(context);
            if (con == null) {
                setMessage("db error!");
                return false;
            }
            if (!isScannedQA(bc)){
                setMessage("not scanned in qa!");
                return false;
            }

            if (isScannedContainer(bc)) {
                setMessage("already scanned!");
                return false;
            }

            if (!saveData(bc)){
                setMessage("error save!");
                return false;
            }
            return true;
        }catch (Exception ex){
            setMessage(ex.getMessage());
            return false;
        }
    }

    boolean saveData(Barcode bc) {
        try {
            if (SaveScan(bc)){
                setMessage("save successful!");
                return true;
            }
            return false;

        } catch(SQLException sqx){
            setMessage(sqx.getMessage());
            return false;
        }
        catch (Exception ex) {
            setMessage(ex.getMessage());
            return false;
        }
    }

    boolean isScannedQA(Barcode bc){
        Connection con = ConnectionClass.CONN(context);
        if (con == null) {
            setMessage("db error");
            //Toast.makeText(ScanActivity.this, "Error in connection with SQL server", Toast.LENGTH_LONG).show();
            return false;
        }

        String query = "select * from tag.tbl_ScannedCarton"
                + " where CartonNo = ?"
                + " and ModelNo = ?"
                + " and Qty = ?"
                + " and RefNo = ?";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1,bc.getCartonNo());
            ps.setString(2, bc.getModel());
            ps.setInt(3, bc.getQty());
            ps.setString(4, bc.getRefNo());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return true;
            }
            String msg;
            msg = "Barcode not found in Scanned QA!";
            msg += " Carton: " + bc.getCartonNo();
            msg += " Model: " + bc.getModel();
            msg += " Qty:" + bc.getQty();
            msg += " RefNo:" + bc.getRefNo();
            setMessage(msg);
            return false;

        } catch (SQLException ex) {
            setMessage("isScannedQA:" + ex.getMessage());
            //Toast.makeText(ScanActivity.this, "isScannedQA: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
            return false;
        }
    }

    public boolean SaveScan(Barcode bc) throws  SQLException {
        try {
            Connection con = ConnectionClass.CONN(context);
            if (con == null) {
                setMessage("sql error!");
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
            ps.setInt(1, getCurrentRow());

            ps.setInt(2, getCountRow() + 1);

            ps.setString(3, getDeliveryDate());
            ps.setInt(4, getContainerNo());
            ps.setString(5, getReferenceNo());
            ps.setInt(6, bc.getCartonNo());
            ps.setString(7, bc.getModel() );
            ps.setInt(8, bc.getQty());
            ps.setString(9, getScannedBy());
            ps.execute();
            return true;
        }
        catch (SQLException sqlEx){
            setMessage(sqlEx.getMessage());
            return false;
        }
        catch (Exception ex) {
            setMessage(ex.getMessage());
            return false;
        }
    }

    public boolean setRowMaxDb_(int maxQty) {
        try{
            Connection con = ConnectionClass.CONN(context);
            if (con == null) {
                setMessage("sql error!");
                return false;
            }
            String sqUpd = "UPDATE mob.tblRowMax" +
                    " SET MaxQty = ?" +
                    " WHERE ShipDate = ? and RowNo = ? and ContainerNo = ?";
            PreparedStatement stmt = con.prepareStatement(sqUpd);
            stmt.setInt(1, maxQty);
            stmt.setString(2, getDeliveryDate());
            stmt.setInt(3, getCurrentRow());
            stmt.setInt(4, getContainerNo());

            if (stmt.executeUpdate() < 1){
                String sqIns = "INSERT INTO mob.tblRowMax" +
                        " (ShipDate, RowNo, ContainerNo, MaxQty)" +
                        " VALUES(?,?,?,?)";
                PreparedStatement stmtIns = con.prepareStatement(sqIns);
                stmtIns.setString(1,getDeliveryDate());
                stmtIns.setInt(2, getCurrentRow());
                stmtIns.setInt(3, getContainerNo());
                stmtIns.setInt(4, maxQty);
                stmtIns.execute();
            }
            setRowMaxQty(maxQty);
            return true;
        }catch (SQLException sqEx){
            setMessage(sqEx.getMessage());
            return false;
        }
    }



    public List<ScanView> getScannedList()  {

        List<ScanView> arrList = new ArrayList<>();
        Connection con = ConnectionClass.CONN(context);

        if (con == null) {
            setMessage("sql error!");
            return arrList;
        }

        String query = "select ScanID, RefNo, RowNo, CartonNo, ModelNo, Qty, " +
                " ContainerNo, ScanNo, DeliveryDate" +
                " from mob.tblContainerScannedCarton" +
                " where DeliveryDate = ?" +
                " and ContainerNo = ?" +
                " and RowNo = ?" +
                " ORDER BY ScanID DESC";
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(query);
            stmt.setString(1, getDeliveryDate());
            stmt.setInt(2, getContainerNo());
            stmt.setInt(3, getCurrentRow());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ScanView scanVw = new ScanView(rs.getInt("ScanID")
                        , rs.getInt("ContainerNo")
                        , rs.getString("RefNo")
                        , rs.getInt("RowNo")
                        , rs.getString("CartonNo")
                        , rs.getString("ModelNo")
                        , rs.getInt("Qty")
                        , rs.getInt("ScanNo")
                        , rs.getString("DeliveryDate"));
                arrList.add(scanVw);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            setMessage(e.getMessage());
        }

      return arrList;
    }
}
