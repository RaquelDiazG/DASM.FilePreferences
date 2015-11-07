package es.upm.miw.ficheros;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListadoActivity extends AppCompatActivity {

    private String RUTA_EXTERNA;
    private String RUTA_INTERNA;
    private ListView listViewFiles;
    private ArrayAdapter<File> adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);

        //Recuperamos las rutas
        RUTA_EXTERNA = getExternalFilesDir(null) + "/";
        RUTA_INTERNA = getFilesDir() + "/";

        //Recuperamos todos los ficheros
        List<File> list = getAllFilesWithRouteAndName();

        //Creamos un adaptador y le añadimos los datos
        adaptador = new ArrayAdapter<File>(this, android.R.layout.simple_list_item_multiple_choice, list);
        listViewFiles = (ListView) findViewById(R.id.listFiles);
        listViewFiles.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listViewFiles.setAdapter(adaptador);

    }

    /*
     * Funcion auxiliar para recuperar todos los ficheros de memoria y tarjeta (ruta + nombre)
     */
    private List<File> getAllFilesWithRouteAndName() {
        List<File> filesSD = getListFilesFromDirectory(new File(RUTA_EXTERNA));
        Log.d("Ficheros externa", filesSD.toString());
        List<File> filesMemory = getListFilesFromDirectory(new File(RUTA_INTERNA));
        Log.d("Ficheros memory", filesMemory.toString());
        List<File> list = new ArrayList<>();
        list.addAll(filesMemory);
        list.addAll(filesSD);
        return list;
    }

    /*
     * Funcion auxiliar que recupera la ruta con los nombres de los ficheros
     */
    private List<File> getListFilesFromDirectory(File parentDir) {
        List<File> listFiles = new ArrayList<>();
        if (parentDir != null) {
            File[] files = parentDir.listFiles();
            for (File file : files) {
                if (file.isFile()) {
                    listFiles.add(file);
                }
            }
        }
        return listFiles;
    }

    /*
     * Funcion auxiliar para recuperar todos los ficheros de memoria y tarjeta (nombre)
     */
    private List<String> getAllFilesWithName() {
        List<String> filesSD = getFileNames(new File(RUTA_EXTERNA).listFiles());
        Log.d("Ficheros externa", filesSD.toString());
        List<String> filesMemory = getFileNames(new File(RUTA_INTERNA).listFiles());
        Log.d("Ficheros memory", filesMemory.toString());
        List<String> list = new ArrayList<>();
        list.addAll(filesSD);
        list.addAll(filesMemory);
        return list;
    }

    /*
     * Funcion auxiliar que recupera los nombres de los ficheros
     */
    private ArrayList<String> getFileNames(File[] file) {
        ArrayList<String> arrayFiles = new ArrayList<>();
        if (file.length == 0)
            return null;
        else {
            for (File aFile : file) {
                arrayFiles.add(aFile.getName());
            }
        }
        return arrayFiles;
    }

    public void eliminarSeleccionados(View view) { //Click en boton
        //Recuperamos los elementos seleccionados
        SparseBooleanArray checked = listViewFiles.getCheckedItemPositions();
        for (int i = 0; i < listViewFiles.getCount(); i++) {
            if (checked.get(i)) {
                //Borramos el fichero
                File file = (File) listViewFiles.getItemAtPosition(i);
                file.delete();
            }
        }
        //Notificamos al adaptador que hay cambios
        adaptador.clear();
        List<File> listFiles = getAllFilesWithRouteAndName();
        adaptador.addAll(listFiles);
        adaptador.notifyDataSetChanged();
        //Notificacion
        Toast.makeText(this, "Ficheros seleccionados eliminados correctamente", Toast.LENGTH_LONG).show();
    }

    public void eliminarTodos(View view) { //Click en boton
        for (int i = 0; i < listViewFiles.getCount(); i++) {
            //Borramos el fichero
            File file = (File) listViewFiles.getItemAtPosition(i);
            file.delete();
        }
        //Notificamos al adaptador que hay cambios
        adaptador.clear();
        List<File> listFiles = getAllFilesWithRouteAndName();
        adaptador.addAll(listFiles);
        adaptador.notifyDataSetChanged();
        //Notificacion
        Toast.makeText(this, "Todos los ficheros eliminados correctamente", Toast.LENGTH_LONG).show();
    }

}
