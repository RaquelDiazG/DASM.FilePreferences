package es.upm.miw.ficheros;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

public class FicherosActivity extends AppCompatActivity {

    private static String NOMBRE_FICHERO;
    private static String RUTA_FICHERO;
    private EditText lineaTexto;
    private Button botonAniadir;
    private TextView contenidoFichero;
    private Menu menu;

    @Override
    protected void onStart() {
        super.onStart();
        mostrarContenido(contenidoFichero);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficheros);

        //Identificamos los elementos
        lineaTexto = (EditText) findViewById(R.id.textoIntroducido);
        botonAniadir = (Button) findViewById(R.id.botonAniadir);
        contenidoFichero = (TextView) findViewById(R.id.contenidoFichero);

        //Recuperamos las preferencias
        getPreferencias();
    }

    /**
     * Al pulsar el botón añadir -> añadir al fichero.
     * Después de añadir -> mostrarContenido()
     *
     * @param v Botón añadir
     */
    public void accionAniadir(View v) {

        //Recuperamos las preferencias
        getPreferencias();

        try {  // Añadir al fichero
            FileOutputStream fos = new FileOutputStream(RUTA_FICHERO, true);
            fos.write(lineaTexto.getText().toString().getBytes());
            fos.write('\n');
            fos.close();
            mostrarContenido(contenidoFichero);
            Log.i("FICHERO", "Click botón Añadir -> AÑADIR al fichero");
        } catch (Exception e) {
            Log.e("FILE I/O", "ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Se pulsa sobre el textview -> mostrar contenido del fichero
     * Si está vacío -> mostrar un Toast
     *
     * @param textviewContenidoFichero TextView contenido del fichero
     */
    public void mostrarContenido(View textviewContenidoFichero) {
        //Recuperamos las preferencias
        getPreferencias();

        boolean hayContenido = false;
        File fichero = new File(RUTA_FICHERO);
        contenidoFichero.setText("");
        try {
            if (fichero.exists()) {
                BufferedReader fin = new BufferedReader(new FileReader(new File(RUTA_FICHERO)));
                String linea = fin.readLine();
                while (linea != null) {
                    hayContenido = true;
                    contenidoFichero.append(linea + '\n');
                    linea = fin.readLine();
                }
                fin.close();
                Log.i("FICHERO", "Click contenido Fichero -> MOSTRAR fichero");
            }
        } catch (Exception e) {
            Log.e("FILE I/O", "ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        if (!hayContenido) {
            Toast.makeText(this, getString(R.string.txtFicheroVacio), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    /**
     * Añade el menú con la opcion de vaciar el fichero
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflador del menú: añade elementos a la action bar
        getMenuInflater().inflate(R.menu.menu, menu);
        //Guardamos el menu
        this.menu = menu;
        //Recuperamos las preferencias
        getPreferencias();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("Menu", getString(item.getItemId()));
        switch (item.getItemId()) {
            case R.id.accionVaciar:
                borrarContenido();
                break;
            case R.id.action_settings:
                Intent intentAjustes = new Intent(this, Ajustes.class);
                startActivity(intentAjustes);
                break;
            case R.id.accionListar:
                Intent intentListado = new Intent(this, ListadoActivity.class);
                startActivity(intentListado);
                break;
        }
        return true;
    }

    /**
     * Vaciar el contenido del fichero, la línea de edición y actualizar
     */
    public void borrarContenido() {
        //Recuperamos las preferencias
        getPreferencias();

        try {  // Vaciar el fichero
            // FileOutputStream fos = openFileOutput(NOMBRE_FICHERO, Context.MODE_PRIVATE);
            FileOutputStream fos = new FileOutputStream(RUTA_FICHERO);
            fos.close();
            Log.i("FICHERO", "opción Limpiar -> VACIAR el fichero");
            lineaTexto.setText(""); // limpio la linea de edición
            mostrarContenido(contenidoFichero);
            //Notificacion
            Toast.makeText(this, "Fichero eliminado correctamente", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e("FILE I/O", "ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void getPreferencias() {
        //Recuperamos las preferencias
        SharedPreferences settings = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        //Recuperamos donde se quiere guardar el fichero
        boolean guardarEnTarjetaSD = settings.getBoolean(getString(R.string.prefRutaFichero), false);
        Log.d("Guardar en TarjetaSD", Boolean.toString(guardarEnTarjetaSD));
        //Recuperamos el nombre del fichero
        NOMBRE_FICHERO = settings.getString(getString(R.string.prefNombreFichero), "miFichero.txt");
        Log.d("Nombre del fichero", NOMBRE_FICHERO);
        if (guardarEnTarjetaSD) { //external (SD)
            RUTA_FICHERO = getExternalFilesDir(null) + "/" + NOMBRE_FICHERO;
        } else { //internal
            RUTA_FICHERO = getFilesDir() + "/" + NOMBRE_FICHERO;
        }
        Log.d("Ruta del fichero", RUTA_FICHERO);

        //Ocultar o mostrar el botón
        if (menu != null) {
            MenuItem itemInterna = menu.findItem(R.id.estadoMemoriaInterna);
            MenuItem itemExterna = menu.findItem(R.id.estadoMemoriaExterna);
            if (guardarEnTarjetaSD) { //SD
                itemInterna.setVisible(false);
                itemExterna.setVisible(true);
            } else { //interna
                itemInterna.setVisible(true);
                itemExterna.setVisible(false);
            }
        }
    }

}
