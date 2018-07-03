package com.app.inpahu.securityapp.Helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.app.inpahu.securityapp.Helpers.HttpHelper;
import com.app.inpahu.securityapp.Helpers.OnTaskCompleted;

import org.json.JSONObject;

public class CustomTask {

    public static class MyAsyncTask extends AsyncTask<String,String,String> {

        private OnTaskCompleted taskCompleted;
        private ProgressDialog dialog;
        private Context mContext;
        private JSONObject data;
        private String url;

        public MyAsyncTask(Context c, String urlr , JSONObject params, OnTaskCompleted callback){
            this.mContext = c;
            this.taskCompleted = callback;
            this.data = params;
            this.url = urlr;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(mContext, "","Por favor espera ...", true);
        }

        @Override
        protected String doInBackground(String... params) {
            return HttpHelper.executePost(url,data);
        }

        protected void onPostExecute(String response){
            dialog.dismiss();
            taskCompleted.onTaskCompleted(response);
        }
    }
}
