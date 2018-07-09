package com.app.inpahu.securityapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.app.inpahu.securityapp.Helpers.CustomTask;
import com.app.inpahu.securityapp.Helpers.Generics;
import com.app.inpahu.securityapp.Helpers.HttpHelper;
import com.app.inpahu.securityapp.Helpers.OnTaskCompleted;
import com.app.inpahu.securityapp.Helpers.PreferencesHelper;
import com.app.inpahu.securityapp.Objects.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private String emailTemp;
    private String passTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        (findViewById(R.id.signup_link)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignupActivity.class));
                finish();
            }
        });

        (findViewById(R.id.login_button)).setOnClickListener(loginTask);
    }


    private View.OnClickListener loginTask = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String email = String.valueOf(((EditText) findViewById(R.id.login_email_edit_text)).getText()).trim();
            String pass = String.valueOf(((EditText) findViewById(R.id.login_password_edit_text)).getText()).trim();
            String valid = validateFields(email,pass);

            if(valid != null)
                Toast.makeText(getApplicationContext(), valid, Toast.LENGTH_SHORT).show();
            else {
                emailTemp = email;
                passTemp = pass;
                new CustomTask.MyAsyncTask(LoginActivity.this, "/users/login", getData(email, pass), loginCompleted).execute();
            }
        }

    };

    private JSONObject getData(String email, String pass) {
        try {
            JSONObject data = new JSONObject();
            data.put("email", email);
            data.put("password", pass);
            return data;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String validateFields(String email, String pass) {
        String msg = null;

        if(email.length() < 1 || pass.length() < 1) {
            msg = "Por favor completa todos los campos";
        }
        else if (!Generics.validateMail(email)) {
            msg = "Por favor ingresa un email valido";
        }

        return msg;
    }

    private OnTaskCompleted loginCompleted = new OnTaskCompleted() {
        @Override
        public void onTaskCompleted(String response) {
            try {
                JSONObject jobj = new JSONObject(response).getJSONObject("user");
                int id = jobj.getInt("id");
                String name = jobj.getString("name");
                PreferencesHelper.saveUser(getApplicationContext(), new User(id, name, emailTemp, passTemp));
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }catch (Exception e){
                Toast.makeText(getApplicationContext(), "Usuario o contraseña invalidos!", Toast.LENGTH_SHORT).show();
            }
        }
    };


}
