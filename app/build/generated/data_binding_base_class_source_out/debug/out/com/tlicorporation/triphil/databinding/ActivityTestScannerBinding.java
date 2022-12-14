// Generated by view binder compiler. Do not edit!
package com.tlicorporation.triphil.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.tlicorporation.triphil.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityTestScannerBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btnDisable;

  @NonNull
  public final Button btnEnable;

  @NonNull
  public final EditText etBarcode;

  @NonNull
  public final TextView tvBarcode;

  private ActivityTestScannerBinding(@NonNull ConstraintLayout rootView, @NonNull Button btnDisable,
      @NonNull Button btnEnable, @NonNull EditText etBarcode, @NonNull TextView tvBarcode) {
    this.rootView = rootView;
    this.btnDisable = btnDisable;
    this.btnEnable = btnEnable;
    this.etBarcode = etBarcode;
    this.tvBarcode = tvBarcode;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityTestScannerBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityTestScannerBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_test_scanner, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityTestScannerBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnDisable;
      Button btnDisable = ViewBindings.findChildViewById(rootView, id);
      if (btnDisable == null) {
        break missingId;
      }

      id = R.id.btnEnable;
      Button btnEnable = ViewBindings.findChildViewById(rootView, id);
      if (btnEnable == null) {
        break missingId;
      }

      id = R.id.etBarcode;
      EditText etBarcode = ViewBindings.findChildViewById(rootView, id);
      if (etBarcode == null) {
        break missingId;
      }

      id = R.id.tvBarcode;
      TextView tvBarcode = ViewBindings.findChildViewById(rootView, id);
      if (tvBarcode == null) {
        break missingId;
      }

      return new ActivityTestScannerBinding((ConstraintLayout) rootView, btnDisable, btnEnable,
          etBarcode, tvBarcode);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
