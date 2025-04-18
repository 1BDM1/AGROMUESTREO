package com.techco.Agromuestreo;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


import de.hdodenhof.circleimageview.CircleImageView;


public class User_fragment extends Fragment {


    private TextView userNombre,userEmail,userID,politica;
    private CircleImageView userImg;

    Button btnCerrarSesion,btnElimminarCta;
    //Variable para gestionar
    public FirebaseAuth mAuth;

    //Variables para deslogear de Google
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_user_fragment, container, false);


        userNombre = root.findViewById(R.id.userNombre);
        userEmail = root.findViewById(R.id.userEmail);
        userID = root.findViewById(R.id.userId);
        userImg = root.findViewById(R.id.userImagen);
        btnCerrarSesion = root.findViewById(R.id.btnLogout);
        btnElimminarCta = root.findViewById(R.id.btnEliminarCta);
        politica = root.findViewById(R.id.privacidad);
        // comp = findViewById(R.id.textView6);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //ESTABLECER CAMPOS DEL USUARIO
        userNombre.setText(currentUser.getDisplayName());
        userEmail.setText(currentUser.getEmail());
        userID.setText(currentUser.getUid());

       // con = new ConexionSQliteHelper(this, "db_Registro", null, 1);

        politica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Politica_privacidad.class);
                startActivity(intent);
                //RegisterActivity.this.finish();

            }
        });

        setHasOptionsMenu(true);

        //Cargar imagen del usuario
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.userimg)
                .error(R.drawable.userimg)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);

        Glide.with(this).load(currentUser.getPhotoUrl())
                .apply(options)
                .into(userImg);
        //Glide.with(RegisterActivity.this).load(currentUser.getPhotoUrl()).into(userImg);

        //Configurar las gso para google signIn con el fin de luego desloguear de google

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("604611761331-71nevjdqgfuh39ck79ptaa0o6bsggdcu.apps.googleusercontent.com")
                .requestEmail() .build(); mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);


        btnCerrarSesion.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                //Cerrar session con Firebase
                mAuth.signOut();

                //Cerrar sesión con google tambien: Google sign out
                mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete( Task<Void> task) {
                        // Abrir MainActivity con SigIn button
                        if(task.isSuccessful()){
                            Intent mainActivity = new Intent(getActivity(), RegisterActivity.class);
                            startActivity(mainActivity);
                            // Dashboard.this.finish();
                        }else{
                            Toast.makeText(getActivity(), "No se pudo cerrar sesión con google", Toast.LENGTH_LONG).show();
                        } }
                });

               /* Abrir MainActivity Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivity); Dashboard.this.finish();*/
            }
        });


        btnElimminarCta.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                //obtener el usuario actual
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // Get the account
                GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getActivity());
                if (signInAccount != null) {
                    AuthCredential credential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
                    //Re-autenticar el usuario para eliminarlo
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override public void onComplete( Task<Void> task) {
                            if (task.isSuccessful()) {
                                //Eliminar el usuario
                                user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override public void onSuccess(Void aVoid) {
                                        Log.d("dashBoard", "onSuccess:Usuario Eliminado");
                                        //llamar al metodo signOut para salir de aqui
                                        signOut();
                                    }
                                });
                            } else { Log.e("RegisterActivity", "onComplete: Error al eliminar el usuario", task.getException());
                            }
                        } });
                } else { Log.d("RegisterActivity", "Error: reAuthenticateUser: user account is null");
                }
            }
        });//fin onClick

        //Para realtime Database//

       // mRootReference = FirebaseDatabase.getInstance().getReference();


        return  root;
    }
    @Override
    public void onStart() {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null){
            //si no es null el usuario ya esta logueado
            // mover al usuario al dashboard
            Intent dashboardActivity = new Intent(getActivity(), RegisterActivity.class);
            startActivity(dashboardActivity);
            getActivity().finish();
        }
        super.onStart();
    }

    private void signOut() {
        //    sign out de firebase
        FirebaseAuth.getInstance().signOut();
        //sign out de "google sign in"
        mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override public void onComplete( Task<Void> task) {
                //regresar al login screen o MainActivity
                // Abrir mainActivity para que inicie sesión o sign in otra vez.
                Intent IntentMainActivity = new Intent(getActivity(), MainActivity.class);
                IntentMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(IntentMainActivity);
                getActivity().finish();
            }
        });


    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_setting).setVisible(false);
        menu.findItem(R.id.action_calendar).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }


}


