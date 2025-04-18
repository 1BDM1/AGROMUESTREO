package com.techco.Agromuestreo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class registro_ng extends AppCompatActivity {

    Button Regresar,Registrar;
    EditText txtInputEmail, txtInputPassword , TxtInputPassword2;


    public FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_ng);

        mAuth = FirebaseAuth.getInstance();

        Regresar = findViewById(R.id.reg_re);
        txtInputEmail = findViewById(R.id.reg_correo);
        txtInputPassword = findViewById(R.id.reg_contraseña1);
        TxtInputPassword2 = findViewById(R.id.reg_contraseña2);
        Registrar = findViewById(R.id.reg_reg);




        Regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(registro_ng.this,RegisterActivity.class);
                startActivity(intent);
                registro_ng.this.finish();
               // Toast.makeText(registro_ng.this,"SI",Toast.LENGTH_SHORT).show();

            }
        });

        Registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email, password;
                email = String.valueOf(txtInputEmail.getText());
                password = String.valueOf(txtInputPassword.getText());
                String password2 = String.valueOf(TxtInputPassword2.getText());



                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(registro_ng.this, "Ingrese algún correo", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password) | TextUtils.isEmpty(password2)) {
                    Toast.makeText(registro_ng.this, "Ingrese alguna contraseña", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.equals(password2)) {

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(registro_ng.this, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(registro_ng.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Toast.makeText(registro_ng.this, "Registro falló, intente más tarde", Toast.LENGTH_SHORT);

                                }
                            }
                        });

                return;
            }else{
                    Toast.makeText(registro_ng.this, "Contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }


            }
        });



    }
}