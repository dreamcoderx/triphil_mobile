// Generated by view binder compiler. Do not edit!
package com.tlicorporation.triphil.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.tlicorporation.triphil.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class RecyclerviewScannedBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final TextView tvRsCarton;

  @NonNull
  public final TextView tvRsModel;

  @NonNull
  public final TextView tvRsQty;

  @NonNull
  public final TextView tvRsScanid;

  private RecyclerviewScannedBinding(@NonNull LinearLayout rootView, @NonNull TextView tvRsCarton,
      @NonNull TextView tvRsModel, @NonNull TextView tvRsQty, @NonNull TextView tvRsScanid) {
    this.rootView = rootView;
    this.tvRsCarton = tvRsCarton;
    this.tvRsModel = tvRsModel;
    this.tvRsQty = tvRsQty;
    this.tvRsScanid = tvRsScanid;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static RecyclerviewScannedBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static RecyclerviewScannedBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.recyclerview_scanned, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static RecyclerviewScannedBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.tv_rs_carton;
      TextView tvRsCarton = ViewBindings.findChildViewById(rootView, id);
      if (tvRsCarton == null) {
        break missingId;
      }

      id = R.id.tv_rs_model;
      TextView tvRsModel = ViewBindings.findChildViewById(rootView, id);
      if (tvRsModel == null) {
        break missingId;
      }

      id = R.id.tv_rs_qty;
      TextView tvRsQty = ViewBindings.findChildViewById(rootView, id);
      if (tvRsQty == null) {
        break missingId;
      }

      id = R.id.tv_rs_scanid;
      TextView tvRsScanid = ViewBindings.findChildViewById(rootView, id);
      if (tvRsScanid == null) {
        break missingId;
      }

      return new RecyclerviewScannedBinding((LinearLayout) rootView, tvRsCarton, tvRsModel, tvRsQty,
          tvRsScanid);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
