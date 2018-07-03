package com.app.inpahu.securityapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.app.inpahu.securityapp.Helpers.HttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        findViewById(R.id.submit_button).setOnClickListener(signUpTask);

    }

    private OnClickListener signUpTask = new OnClickListener() {
        @Override
        public void onClick(View v) {

            String name  = String.valueOf(((EditText) findViewById(R.id.name_edit_text)).getText()).trim();
            String email = String.valueOf(((EditText) findViewById(R.id.email_edit_text)).getText()).trim();
            String pass1 = String.valueOf(((EditText) findViewById(R.id.password1_edit_text)).getText()).trim();
            String pass2 = String.valueOf(((EditText) findViewById(R.id.password2_edit_text)).getText()).trim();

            String valid = validateFields(name,email,pass1,pass2);

            if(valid != null){
                Toast.makeText(getApplicationContext(), valid, Toast.LENGTH_SHORT).show();
            }
            else {
                new ExecuteSignUp().execute(name, email, pass1);
            }
        }
    };

    private String validateFields(String name, String email, String pass1, String pass2) {
        String msg = null;

        if(name.length() < 1 || email.length() < 1 || pass1.length() < 1 || pass2.length() < 1) {
            msg = "Por favor completa todos los campos";
        }
        else if (!validateMail(email)) {
            msg = "Por favor ingresa un email valido";
        }
        else if ( !pass1.equals(pass2)) {
            msg = "Las contraseñas no coinciden";
        }

        return msg;
    }


    /**************************************************/

    public static boolean validateMail(String emailStr) {
        return (Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).matcher(emailStr)).find();
    }

    private class ExecuteSignUp extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... params) {

            try {

                JSONObject data = new JSONObject();
                data.put("name", params[0]);
                data.put("email", params[1]);
                data.put("password", params[2]);

                HttpHelper.executePost("/users/create", data );

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return "";
        }

        protected void onPostExecute(String result) {
            Log.e("result" , result);
            try {
                JSONObject jsonResult = new JSONObject(result).getJSONObject("data");
                Log.e("result" , String.valueOf(jsonResult));
                Log.e("result" , String.valueOf(jsonResult.getInt("result")));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
