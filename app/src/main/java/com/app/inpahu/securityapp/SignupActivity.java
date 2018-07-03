package com.app.inpahu.securityapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.app.inpahu.securityapp.Helpers.CustomTask;
import com.app.inpahu.securityapp.Helpers.Generics;
import com.app.inpahu.securityapp.Helpers.HttpHelper;
import com.app.inpahu.securityapp.Helpers.OnTaskCompleted;

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

            if(valid != null)
                Toast.makeText(getApplicationContext(), valid, Toast.LENGTH_SHORT).show();
            else
                new CustomTask.MyAsyncTask(SignupActivity.this, "/users/create",getData(name,email,pass1),signUpCompleted ).execute();

        }
    };

    private JSONObject getData(String name, String email, String pass) {
        try {

            JSONObject data = new JSONObject();
            data.put("name", name);
            data.put("email", email);
            data.put("password", pass);

            return data;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String validateFields(String name, String email, String pass1, String pass2) {
        String msg = null;

        if(name.length() < 1 || email.length() < 1 || pass1.length() < 1 || pass2.length() < 1) {
            msg = "Por favor completa todos los campos";
        }
        else if (!Generics.validateMail(email)) {
            msg = "Por favor ingresa un email valido";
        }
        else if ( !pass1.equals(pass2)) {
            msg = "Las contraseñas no coinciden";
        }

        return msg;
    }

    private OnTaskCompleted signUpCompleted = new OnTaskCompleted() {
        @Override
        public void onTaskCompleted(String response) {
            try {
                JSONObject jobj = new JSONObject(response).getJSONObject("data");
                int id = jobj.getInt("result");
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }catch (Exception e){
                Toast.makeText(getApplicationContext(), "Este email ya se encuentra registrado!", Toast.LENGTH_SHORT).show();
            }
        }
    };

}
