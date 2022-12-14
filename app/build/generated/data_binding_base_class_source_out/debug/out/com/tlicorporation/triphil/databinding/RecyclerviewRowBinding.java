// Generated by view binder compiler. Do not edit!
package com.tlicorporation.triphil.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public final class RecyclerviewRowBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Button btnDelete;

  @NonNull
  public final Button btnEdit;

  @NonNull
  public final TextView tvemail;

  @NonNull
  public final TextView tvuserid;

  @NonNull
  public final TextView tvusername;

  private RecyclerviewRowBinding(@NonNull LinearLayout rootView, @NonNull Button btnDelete,
      @NonNull Button btnEdit, @NonNull TextView tvemail, @NonNull TextView tvuserid,
      @NonNull TextView tvusername) {
    this.rootView = rootView;
    this.btnDelete = btnDelete;
    this.btnEdit = btnEdit;
    this.tvemail = tvemail;
    this.tvuserid = tvuserid;
    this.tvusername = tvusername;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static RecyclerviewRowBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static RecyclerviewRowBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.recyclerview_row, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static RecyclerviewRowBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnDelete;
      Button btnDelete = ViewBindings.findChildViewById(rootView, id);
      if (btnDelete == null) {
        break missingId;
      }

      id = R.id.btnEdit;
      Button btnEdit = ViewBindings.findChildViewById(rootView, id);
      if (btnEdit == null) {
        break missingId;
      }

      id = R.id.tvemail;
      TextView tvemail = ViewBindings.findChildViewById(rootView, id);
      if (tvemail == null) {
        break missingId;
      }

      id = R.id.tvuserid;
      TextView tvuserid = ViewBindings.findChildViewById(rootView, id);
      if (tvuserid == null) {
        break missingId;
      }

      id = R.id.tvusername;
      TextView tvusername = ViewBindings.findChildViewById(rootView, id);
      if (tvusername == null) {
        break missingId;
      }

      return new RecyclerviewRowBinding((LinearLayout) rootView, btnDelete, btnEdit, tvemail,
          tvuserid, tvusername);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
