package com.tlicorporation.triphil.task;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tlicorporation.triphil.async.AsyncTask;
import com.tlicorporation.triphil.model.User;
import com.tlicorporation.triphil.sqldatabase.UserLog;

import java.util.ArrayList;
import java.util.List;

public class DownloadTask extends AsyncTask<String,Integer, List<String>> {
    TextView mTextView;
    ProgressBar mProgressBar;
    UserLog user;
    Context ctx;

    public DownloadTask(TextView txtStatus, ProgressBar pbar, UserLog user, Context ctx) {
        this.mTextView = txtStatus;
        this.mProgressBar = pbar;
        this.user = user;
        this.ctx = ctx;
        mTextView.setText("constructor");
    }

    @Override
    protected void onPostExecute(List<String> strings) {

        mTextView.setText("message" + strings.get(0));
        super.onPostExecute(strings);
    }

    @Override
    protected String onPostExecute(String result) {
        mTextView.setText("postext2" +result);
        return null;
    }

    @Override
    protected void onPreExecute() {
        mProgressBar.setVisibility(View.VISIBLE);
        mTextView.setText("pre execute");
        super.onPreExecute();
    }

    @Override
    protected List<String> doInBackground(String s) throws Exception {
        mTextView.setText("do in background!");
        List<String> strings = new ArrayList<>();
        //if (!user.isUserValid(this)) {
        // mTextView.setText(user.getMsg());
        //    strings.add(user.getMsg());
        //    return strings;
       // }
        //strings.add("one");
        //strings.add("two");
        //strings.add("three");
        return strings;  //List<String> strings = List.of("one", "two", "three");

    }

    /*
        @Override
        protected List<String> doInBackground(String s) throws Exception {
            List<String> strings = List.of("one", "two", "three");
            //List<String> list = Lists.newArrayList("one", "two", "three");
            return strings;
        }


     */

    @Override
    protected void onBackgroundError(Exception e) {
        mTextView.setText(e.getMessage());
        mProgressBar.setVisibility(View.INVISIBLE);

    }
}

