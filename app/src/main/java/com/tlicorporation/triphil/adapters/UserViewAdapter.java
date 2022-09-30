package com.tlicorporation.triphil.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.tlicorporation.triphil.R;
import com.tlicorporation.triphil.model.User;
import com.tlicorporation.triphil.model.UserView;
import com.tlicorporation.triphil.sql.DatabaseHelper;

import java.util.ArrayList;

public class UserViewAdapter extends RecyclerView.Adapter<UserViewAdapter.viewHolder> {

    Context context;
    AppCompatActivity activity;
    ArrayList<UserView> arrayList;
    DatabaseHelper database_helper;


    public UserViewAdapter(Context context, AppCompatActivity activity, ArrayList<UserView> arrayList) {
        this.context = context;
        this.activity  = activity ;
        this.arrayList = arrayList;
    }


    @Override
    public viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_row, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final viewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Log.e("testx", String.valueOf(arrayList.get(position).getUserid()));
        holder.userid.setText(String.valueOf(arrayList.get(position).getUserid()));
        holder.username.setText(arrayList.get(position).getUsername());
        holder.email.setText(arrayList.get(position).getEmail());

        //holder.title.setText(arrayList.get(position).getTitle());
        //holder.description.setText(arrayList.get(position).getDes());
        database_helper = new DatabaseHelper(context);

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View v) {
                //deleting user
                database_helper.deleteUserID(arrayList.get(position).getUserid());
                arrayList.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View v) {
                //display edit dialog
                showDialog(position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView userid, username, email;
        Button btnEdit, btnDelete;


        public viewHolder(View itemView) {
            super(itemView);
            userid = (TextView) itemView.findViewById(R.id.tvuserid);
            username = (TextView) itemView.findViewById(R.id.tvusername);
            email = (TextView) itemView.findViewById(R.id.tvemail);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
            btnEdit = (Button) itemView.findViewById(R.id.btnEdit);
         }
    }

    public void showDialog(final int pos) {
        final TextInputEditText username, useremail, password, confirmpassword;
        final AppCompatButton submit;
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        dialog.setContentView(R.layout.dialog);
        params.copyFrom(dialog.getWindow().getAttributes());
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

         username = (TextInputEditText) dialog.findViewById(R.id.textInputEditName);
         useremail = (TextInputEditText) dialog.findViewById(R.id.textInputEditTextEmail);
         password = (TextInputEditText) dialog.findViewById(R.id.textInputEditTextPassword);
         confirmpassword = (TextInputEditText) dialog.findViewById(R.id.textInputEditTextConfirmPassword);
        submit = (AppCompatButton) dialog.findViewById(R.id.appCompatButtonRegister);

        username.setText(arrayList.get(pos).getUsername());
        useremail.setText(arrayList.get(pos).getEmail());


        submit.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View v) {
                if (username.getText().toString().isEmpty()) {
                    username.setError("Please Enter Username");
                }else if(useremail.getText().toString().isEmpty()) {
                    useremail.setError("Please Enter User Email");
                }else if(password.getText().toString().isEmpty()) {
                    password.setError("Please Enter Password!");
                }else if(!password.getText().toString().equals(confirmpassword.getText().toString())) {
                    confirmpassword.setError("Password is not equal!");
                }else {
                    User usr = new User();
                    usr.setId(arrayList.get(pos).getUserid());
                    usr.setName(username.getText().toString());
                    usr.setEmail(useremail.getText().toString());
                    usr.setPassword(password.getText().toString());
                    database_helper.updateUser(usr);
                    arrayList.get(pos).setUsername(username.getText().toString());
                    arrayList.get(pos).setEmail(useremail.getText().toString());
                   // notifyDataSetChanged();
                    dialog.cancel();
                    notifyDataSetChanged();
                }
            }
        });
    }
}