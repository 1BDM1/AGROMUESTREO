package com.techco.Agromuestreo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {



    EditText txtInputEmail, txtInputPassword;
    Button  btnGoogle,Registro,IniciarS;
    String TAG = "GoogleSignIn";
    int RC_SIGN_IN = 9001,intento =0;
    TextView politica,Contraseña;
    private ProgressDialog mProgressBar;



    //Variable para gestionar FirebaseAuth
    private FirebaseAuth mAuth;
    // Agregar cliente de inicio de sesión de Google

    private GoogleSignInClient mGoogleSignInClient;

    public ProgressDialog mDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Utils.getDatabase();

        mAuth = FirebaseAuth.getInstance();

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);


        mDialog = new ProgressDialog(this);
        Contraseña = findViewById(R.id.contrasena);
        btnGoogle = findViewById(R.id.btnGoogle);
        politica = findViewById(R.id.politica);
        Registro = findViewById(R.id.REGI);
        IniciarS = findViewById(R.id.INSE);
        txtInputEmail = findViewById(R.id.ini_correo);
        txtInputPassword = findViewById(R.id.ini_contraseña);
        

        politica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, Politica_privacidad.class);
                startActivity(intent);
                //RegisterActivity.this.finish();

            }
        });


        Registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, registro_ng.class);
                startActivity(intent);
               // RegisterActivity.this.finish();
            }
        });


        IniciarS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = String.valueOf(txtInputEmail.getText());
                password = String.valueOf(txtInputPassword.getText());

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(RegisterActivity.this, "Ingrese algún correo", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterActivity.this, "Ingrese alguna contraseña", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                   @Override
                                                   public void onComplete( Task<AuthResult> task) {
                                                       if (task.isSuccessful()){
                                                           Toast.makeText(RegisterActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                                                           Intent dashboardActivity = new Intent(RegisterActivity.this, MainActivity.class);
                                                           startActivity(dashboardActivity);
                                                           RegisterActivity.this.finish();
                                                       }else{

                                                           Toast.makeText(RegisterActivity.this, "Error, intente de nuevo", Toast.LENGTH_SHORT).show();

                                                       }



                                                   }
                                               }
                        );


            }
        });


        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });


        //getActivity susituye a loginActivity.this, pues es un fragment.
        //mProgressBar = new ProgressDialog(RegisterActivity.this);
        //.requestIdToken(("604611761331-71nevjdqgfuh39ck79ptaa0o6bsggdcu.apps.googleusercontent.com"))
        //default_web_client_id
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString((R.string.default_web_client_id)))
                .requestEmail().build();
        // Crear un GoogleSignInClient con las opciones especificadas por gso.
        mGoogleSignInClient = GoogleSignIn.getClient(RegisterActivity.this, gso);

        mAuth = FirebaseAuth.getInstance();

      //1  onStart();





        Contraseña.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

intento = 0;
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(RegisterActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View view  = inflater.inflate(R.layout.email_text,null);
                builder.setView(view);
                androidx.appcompat.app.AlertDialog dialog = builder.create();

                TextView txtInputEmail = view.findViewById(R.id.ini_correo);
                Button Regresar = view.findViewById(R.id.cancelar);
                Button Enviar = view.findViewById(R.id.enviar);
                Window window =dialog.getWindow();
                if (window != null){
                    //window.setLayout(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.FILL_PARENT);
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,1000);
                    window.setWindowAnimations(androidx.appcompat.R.style.Animation_AppCompat_DropDownUp);
                }


                Regresar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();

                    }
                });

                Enviar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String email = String.valueOf(txtInputEmail.getText());

                        if (!email.isEmpty()){

                            mDialog.setMessage("Espere un momento...");
                            mDialog.setCancelable(false);
                            Reset(email);
                            intento = 1;
                            
                            if (intento > 0) {
                                dialog.dismiss();
                            }

                        }else{

                            Toast.makeText(RegisterActivity.this,"Ingrese algún correo", Toast.LENGTH_SHORT).show();
                        }


                    }
                });


                dialog.setCancelable(false);
                dialog.show();


            }

        });




    }

    private void Reset(String a){
        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(a).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {

                if (task.isSuccessful()){

                    Toast.makeText(RegisterActivity.this, "El correo se ha enviado correctamente", Toast.LENGTH_SHORT).show();

                    intento = intento + 1;
                }else{

                    Toast.makeText(RegisterActivity.this, "El correo no se envio, intente de nuevo más tarde", Toast.LENGTH_SHORT).show();

                }



            }
        });

    }


    @Override
    public void onStart() {

        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        updateUI(user);
        if (user != null) {
            //si no es null el usuario ya esta logueado
            // mover al usuario al dashboard
            Intent dashboardActivity = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(dashboardActivity);
            RegisterActivity.this.finish();
        }

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (task.isSuccessful()) {
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                    firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    Log.w(TAG, "Google sign in failed", e);
                }

            } else {
                Log.d(TAG, "Error, login no exitoso:" + task.getException().toString());
                Toast.makeText(this, "Ocurrio un error. " + task.getException().toString(), Toast.LENGTH_LONG).show();


                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Error");
                builder.setMessage(task.getException().toString());
                builder.setPositiveButton("OK", null);
                builder.show();

            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete( Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            //FirebaseUser user = mAuth.getCurrentUser();
                            // Inicitar DASHBOARD u otra actividad luego del SigIn Exitoso
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            Intent dashboardActivity = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(dashboardActivity);
                            RegisterActivity.this.finish();
                            //posible error arriba
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void updateUI(FirebaseUser user) {

    }


}
