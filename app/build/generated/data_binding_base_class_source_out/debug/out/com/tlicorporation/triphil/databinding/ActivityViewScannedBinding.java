// Generated by view binder compiler. Do not edit!
package com.tlicorporation.triphil.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.tlicorporation.triphil.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityViewScannedBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ConstraintLayout linearLayout;

  @NonNull
  public final RecyclerView recVwScanned;

  @NonNull
  public final TextView tvwReferenceNo;

  private ActivityViewScannedBinding(@NonNull ConstraintLayout rootView,
      @NonNull ConstraintLayout linearLayout, @NonNull RecyclerView recVwScanned,
      @NonNull TextView tvwReferenceNo) {
    this.rootView = rootView;
    this.linearLayout = linearLayout;
    this.recVwScanned = recVwScanned;
    this.tvwReferenceNo = tvwReferenceNo;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityViewScannedBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityViewScannedBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_view_scanned, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityViewScannedBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      ConstraintLayout linearLayout = (ConstraintLayout) rootView;

      id = R.id.recVwScanned;
      RecyclerView recVwScanned = ViewBindings.findChildViewById(rootView, id);
      if (recVwScanned == null) {
        break missingId;
      }

      id = R.id.tvwReferenceNo;
      TextView tvwReferenceNo = ViewBindings.findChildViewById(rootView, id);
      if (tvwReferenceNo == null) {
        break missingId;
      }

      return new ActivityViewScannedBinding((ConstraintLayout) rootView, linearLayout, recVwScanned,
          tvwReferenceNo);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
