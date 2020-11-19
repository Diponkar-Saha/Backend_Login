package com.example.backend_login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private RetrofitInterface retrofitInterface;
    private Retrofit retrofit;
    private String BASE_URL="http://10.0.2.2:3000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);


        findViewById(R.id.signinm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                handleLoginDialog();
            }
        });
        findViewById(R.id.signupm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignDialog();
            }
        });
    }

    private void handleLoginDialog() {
        View view=getLayoutInflater().inflate(R.layout.login_dialog,null);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setView(view).show();
        Button loginBtn=view.findViewById(R.id.loginbtn1);
        final EditText emailEdit=view.findViewById(R.id.emailEdit);
        final EditText passwordEdit=view.findViewById(R.id.passwordEdit);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> map = new HashMap<>();

                map.put("email", emailEdit.getText().toString());
                map.put("password", passwordEdit.getText().toString());

                Call<LoginResult> call = retrofitInterface.executeLogin(map);

                call.enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {

                        if (response.code() == 200) {//LoginResult

                            LoginResult result = response.body();

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                            builder1.setTitle(result.getName());
                            builder1.setMessage(result.getEmail());

                            builder1.show();

                        } else if (response.code() == 404) {
                            Toast.makeText(MainActivity.this, "Wrong Credentials",
                                    Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

    }
    private void handleSignDialog() {
        View view=getLayoutInflater().inflate(R.layout.signup_dialog,null);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setView(view).show();

        Button signBtn1=view.findViewById(R.id.signbtn1);
        final EditText nameEdit = view.findViewById(R.id.nameEditR);
        final EditText emailEdit=view.findViewById(R.id.emailEditR);
        final EditText passwordEdit=view.findViewById(R.id.passwordEditR);

        signBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> map = new HashMap<>();
                map.put("name", nameEdit.getText().toString());
                map.put("email", emailEdit.getText().toString());
                map.put("password", passwordEdit.getText().toString());

                Call<Void> call = retrofitInterface.executeSignup(map);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        if (response.code() == 200) {
                            Toast.makeText(MainActivity.this,
                                    "Signed up successfully", Toast.LENGTH_LONG).show();
                        } else if (response.code() == 404) {
                            Toast.makeText(MainActivity.this, "Already registered",
                                    Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }


                });

            }
        });



    }
    }

