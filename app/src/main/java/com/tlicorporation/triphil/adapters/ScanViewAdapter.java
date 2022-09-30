package com.tlicorporation.triphil.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.tlicorporation.triphil.ConnectionClass;
import com.tlicorporation.triphil.R;
import com.tlicorporation.triphil.model.ScanView;
import com.tlicorporation.triphil.model.Search;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Objects;

public class ScanViewAdapter extends RecyclerView.Adapter<ScanViewAdapter.MyViewHolder> {
    private List<ScanView> scanList;
    private Context context;
    private Search search;

    public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView scanID, refNo, containerNo, rowNo, carton, model, qty;
    ImageView deletebtn;
        public MyViewHolder(View view) {
            super(view);
            scanID = view.findViewById(R.id.tvScanID);
            refNo = view.findViewById(R.id.tvRefNo);
            containerNo = view.findViewById(R.id.tvDeliveryDateContainer);
            rowNo = view.findViewById(R.id.tvRowNo);
            carton = view.findViewById(R.id.tvCarton);
            model = view.findViewById(R.id.tvModel);
            qty = view.findViewById(R.id.tvQty);
            deletebtn = view.findViewById(R.id.imageViewDelete);
        }
    }
    public ScanViewAdapter(List<ScanView> scanList, Context context, Search search) {
        this.scanList = scanList;
        this.context = context;
        this.search = search;
    }
    public List<ScanView> getList(){
        return this.scanList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.scanview_list_row, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ScanView scanVw = scanList.get(position);
        holder.scanID.setText(String.valueOf(scanVw.getScanID()));
        holder.refNo.setText(scanVw.getRefNo());
        holder.containerNo.setText(String.valueOf(scanVw.getContainerNo()));
        holder.rowNo.setText(String.valueOf(scanVw.getRowNo()));
        holder.carton.setText(scanVw.getCarton());
        holder.model.setText(scanVw.getModel());
        holder.qty.setText(String.valueOf(scanVw.getQty()));
        holder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteLine(scanVw.getScanID());
                        deleteRowGreater(scanVw.getDelDate(), scanVw.getContainerNo(),scanVw.getRowNo());
                        loadData(search);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Deleted successfully!", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return scanList.size();
    }
    boolean deleteLine(int ScanID) {
        try {
            Connection con = ConnectionClass.CONN(context);
            String query = "DELETE FROM [mob].[tblContainerScannedCarton]  " +
                    " where ScanID = ?" ;
            assert con != null;
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt (1, ScanID);
            stmt.executeQuery();
            return true;

        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage() , Toast.LENGTH_SHORT).show();
            return false;

        }
    }

    boolean deleteRowGreater(String delDate, Integer containerNo, Integer RowNo) {
        try {

            Connection con = ConnectionClass.CONN(context);
            String query = "DELETE " +
                    "FROM [mob].[tblContainerScannedCarton]  " +
                    " where RowNo > ?" +
                    " and DeliveryDate = ?" +
                    " and ContainerNo = ?";
            assert con != null;

            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1,RowNo);
            stmt.setString(2, delDate);
            stmt.setInt(3, containerNo);
            stmt.executeQuery();
            return true;

        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage() , Toast.LENGTH_LONG).show();
            return false;
        }
    }

    void loadData(Search search){
        Toast.makeText(context, "loading data...", Toast.LENGTH_LONG).show();
        try {
            Connection con = ConnectionClass.CONN(context);
            if (con == null) {
                Toast.makeText(context, "sql error", Toast.LENGTH_LONG).show();
                Log.e("error", "Error in connection with SQL server");
                return;
            }
            String sqlWhere = "";
            if (!(search.getDeliveryDate()== null || Objects.equals(search.getDeliveryDate(), ""))){
                sqlWhere = sqlWhereConcat(sqlWhere, " DeliveryDate = '" + search.getDeliveryDate() +  "'");
            }
            if (search.getContainerNo() != 0){
                sqlWhere = sqlWhereConcat(sqlWhere, " ContainerNo = '" + search.getContainerNo() + "'");
            }
            if (search.getCartonNo() != 0){
                sqlWhere = sqlWhereConcat(sqlWhere, " CartonNo = '" + search.getCartonNo() + "'");
            }
            if (!(search.getModelNo() == null || search.getModelNo() == "")){
                sqlWhere = sqlWhereConcat(sqlWhere, " ModelNo = '" + search.getModelNo() + "'");
            }
            String query = "select ScanID, RefNo, RowNo, CartonNo, ModelNo, Qty, " +
                    " ContainerNo, ScanNo, DeliveryDate" +
                    " from mob.tblContainerScannedCarton" ;
            if (!(sqlWhere=="")){
                query += " WHERE " + sqlWhere;
            }

            query += " ORDER BY RowNo";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            scanList.clear();
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
                scanList.add(scanVw);
            }
            notifyDataSetChanged();

        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String sqlWhereConcat(String s, String whereAdd){
        if (s.isEmpty()){
            return whereAdd;
        }
        return s + " and " + whereAdd;
    }
}
