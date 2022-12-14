// Generated by view binder compiler. Do not edit!
package com.tlicorporation.triphil.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public final class UserListBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ConstraintLayout relativeLayout2;

  @NonNull
  public final TextView txtUserID;

  private UserListBinding(@NonNull ConstraintLayout rootView,
      @NonNull ConstraintLayout relativeLayout2, @NonNull TextView txtUserID) {
    this.rootView = rootView;
    this.relativeLayout2 = relativeLayout2;
    this.txtUserID = txtUserID;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static UserListBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static UserListBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.user_list, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static UserListBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      ConstraintLayout relativeLayout2 = (ConstraintLayout) rootView;

      id = R.id.txtUserID;
      TextView txtUserID = ViewBindings.findChildViewById(rootView, id);
      if (txtUserID == null) {
        break missingId;
      }

      return new UserListBinding((ConstraintLayout) rootView, relativeLayout2, txtUserID);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
