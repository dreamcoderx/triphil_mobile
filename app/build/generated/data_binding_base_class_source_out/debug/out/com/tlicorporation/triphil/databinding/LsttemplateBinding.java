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

public final class LsttemplateBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final TextView lblbarcode;

  @NonNull
  public final TextView lblrowno;

  @NonNull
  public final TextView lblrowno1;

  @NonNull
  public final TextView lblscanid;

  @NonNull
  public final TextView lblscanid1;

  private LsttemplateBinding(@NonNull LinearLayout rootView, @NonNull TextView lblbarcode,
      @NonNull TextView lblrowno, @NonNull TextView lblrowno1, @NonNull TextView lblscanid,
      @NonNull TextView lblscanid1) {
    this.rootView = rootView;
    this.lblbarcode = lblbarcode;
    this.lblrowno = lblrowno;
    this.lblrowno1 = lblrowno1;
    this.lblscanid = lblscanid;
    this.lblscanid1 = lblscanid1;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static LsttemplateBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static LsttemplateBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.lsttemplate, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static LsttemplateBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.lblbarcode;
      TextView lblbarcode = ViewBindings.findChildViewById(rootView, id);
      if (lblbarcode == null) {
        break missingId;
      }

      id = R.id.lblrowno_;
      TextView lblrowno = ViewBindings.findChildViewById(rootView, id);
      if (lblrowno == null) {
        break missingId;
      }

      id = R.id.lblrowno;
      TextView lblrowno1 = ViewBindings.findChildViewById(rootView, id);
      if (lblrowno1 == null) {
        break missingId;
      }

      id = R.id.lblscanid_;
      TextView lblscanid = ViewBindings.findChildViewById(rootView, id);
      if (lblscanid == null) {
        break missingId;
      }

      id = R.id.lblscanid;
      TextView lblscanid1 = ViewBindings.findChildViewById(rootView, id);
      if (lblscanid1 == null) {
        break missingId;
      }

      return new LsttemplateBinding((LinearLayout) rootView, lblbarcode, lblrowno, lblrowno1,
          lblscanid, lblscanid1);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
