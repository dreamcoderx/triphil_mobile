package com.tlicorporation.triphil.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tlicorporation.triphil.R;
import com.tlicorporation.triphil.model.ScanView;

import java.util.List;

public class ScannedVwAdapt extends RecyclerView.Adapter<ScannedVwAdapt.MyViewHolder> {
    private List<ScanView> scanList;
    public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView tvRowNo, tvCarton, tvModel,tvQty;

        public MyViewHolder(View view) {
            super(view);
            //tvRowNo, tvCarton, tvModel,tvQty
            tvRowNo = view.findViewById(R.id.tvRowNo);
            tvCarton = view.findViewById(R.id.tvCarton);
            tvModel = view.findViewById(R.id.tvModel);
            tvQty = view.findViewById(R.id.tvQty);

        }
    }
    public ScannedVwAdapt(List<ScanView> scanList) {
        this.scanList = scanList;
    }
    public List<ScanView> getList(){
        return this.scanList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.scanned_list_row, parent, false);

        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //tvRowNo, tvCarton, tvModel,tvQty
        ScanView scanVw = scanList.get(position);
        holder.tvRowNo.setText(String.valueOf(scanVw.getRowNo()));
        holder.tvCarton.setText(scanVw.getCarton());
        holder.tvModel.setText(scanVw.getModel());
        holder.tvQty.setText(String.valueOf(scanVw.getQty()));
    }
    @Override
    public int getItemCount() {
        return scanList.size();
    }
}
