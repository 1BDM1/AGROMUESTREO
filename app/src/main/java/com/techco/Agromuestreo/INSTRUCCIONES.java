package com.techco.Agromuestreo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class INSTRUCCIONES extends AppCompatActivity {

    public ImageView img;
    public TextView txt, int1, int2, int3, int4, int5;
    public Button continuar;

    private String latitud;
    private String longitud;
    public LocationManager locationManager;
    private TextView statusTextView; // Texto que muestra el estado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carga_inicial);

        statusTextView = findViewById(R.id.statusTextView);

         locationManager = (LocationManager) INSTRUCCIONES.this.getSystemService(Context.LOCATION_SERVICE);

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            // Crear un AlertDialog.Builder
            AlertDialog.Builder builder = new AlertDialog.Builder(INSTRUCCIONES.this);
            // Establecer el título y el mensaje
            builder.setTitle("Ubicación desactivada");
            builder.setMessage("Por favor, activa la ubicación para usar esta aplicación.");
            // Establecer el botón positivo que lleva a la configuración de ubicación
            builder.setPositiveButton("Ir a configuración", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Crear un Intent con la acción Settings.ACTION_LOCATION_SOURCE_SETTINGS
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    // Iniciar la actividad con el intent
                    startActivity(intent);
                }
            });
            // Establecer el botón negativo que cierra el diálogo
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Cerrar el diálogo
                    dialog.dismiss();
                    //INSTRUCCIONES.this.finish();
                }
            });
            // Mostrar el diálogo
            builder.create().show();
            builder.setCancelable(false);
        }else{
            obtenerUbicacion();
        }


        // Inicializar y obtener ubicación


    }//fin de on create

    @SuppressLint("MissingPermission")
    private void obtenerUbicacion() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (location != null) {
                        latitud = String.valueOf(location.getLatitude());
                        longitud = String.valueOf(location.getLongitude());

                        // Validar y navegar a la siguiente actividad
                        if (latitud != null && longitud != null) {
                            statusTextView.setText("Ubicación obtenida, entrando...");
                            //Toast.makeText(INSTRUCCIONES.this, "Ubicación obtenida", Toast.LENGTH_SHORT).show();

                            // Pasar a la siguiente actividad
                            Intent intent = new Intent(INSTRUCCIONES.this, MainActivity.class);
                            startActivity(intent);
                            //INSTRUCCIONES.this.finish(); // Finalizar esta actividad
                        }
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {}

                @Override
                public void onProviderEnabled(String provider) {}

                @Override
                public void onProviderDisabled(String provider) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al obtener la ubicación", Toast.LENGTH_SHORT).show();
        }
    }


}//inicio de activity
