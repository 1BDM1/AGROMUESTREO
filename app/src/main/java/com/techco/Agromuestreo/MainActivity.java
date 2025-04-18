package com.techco.Agromuestreo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import android.app.ActionBar;
import android.widget.Toast;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.android.material.navigation.NavigationView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.techco.Agromuestreo.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration,mAppBarConfiguration2;
    private ActivityMainBinding binding;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setSupportActionBar(binding.appBarMain.toolbar);
        /*binding.appBarMain.fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());*/
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.



        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.user_fragment,R.id.nav_home, R.id.nav_gallery,R.id.noticias,R.id.nav_slideshow,R.id.Enfermedad)
                .setOpenableLayout(drawer)
                .build();

        //Enfermedad
        //,R.id.Enfermedad

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        menu.findItem(R.id.action_setting).setVisible(false);
        menu.findItem(R.id.action_calendar).setVisible(false);
        menu.findItem(R.id.cambiar).setVisible(false);
        //menu.findItem(R.id.ic_busqueda).setVisible(false);

        return true;
    }



    public void iniciar (View view){
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }

    public void openFacebookPage(MenuItem item) {
        String url = "https://www.facebook.com/profile.php?id=61551576052593&mibextid=LQQJ4d";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        // Verificar si el usuario tiene instalada la app de Facebook
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = ((PackageManager) packageManager).queryIntentActivities(intent, 0);
        boolean isIntentSafe = activities.size() > 0;
        if (isIntentSafe) {
            // Abrir la página en la app de Facebook
            intent.setPackage("com.facebook.katana");
        }
        startActivity(intent);
    }


    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

 /*  public void openFolder(){
        //
        //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.parse(//Environment.getExternalStorageDirectory().getPath()
                //0+ "/Agromuestreo/");7
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/Agromuestreo");

       Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
       intent.addCategory(Intent.CATEGORY_OPENABLE);
       intent.setType("application/pdf");
        intent.setDataAndType(uri, "application/pdf");
        startActivity(intent);
        //Intent.createChooser(intent, "Open");
    }
*/


  /*  public void openFolder(){
        Uri selectedUri = Uri.parse( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/Agromuestreo");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(selectedUri, "pdf/text");
        startActivity(Intent.createChooser(intent, "Open folder"));
    }
*/
}
