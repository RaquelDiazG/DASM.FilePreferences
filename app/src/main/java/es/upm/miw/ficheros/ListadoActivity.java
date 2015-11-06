package es.upm.miw.ficheros;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListadoActivity extends AppCompatActivity {

    private String RUTA_EXTERNA;
    private String RUTA_INTERNA;
    private ListView lvFiles;
    private ArrayAdapter<File> adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);

        //Recuperamos todos los ficheros
        List<File> list = getAllFiles();

        //Creamos un adaptador
        adaptador = new ArrayAdapter<File>(this, android.R.layout.simple_list_item_multiple_choice, list);
        lvFiles = (ListView) findViewById(R.id.listFiles);
        lvFiles.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lvFiles.setAdapter(adaptador);

/*
        List<String> filesSD = getFileNames(new File(RUTA_EXTERNA).listFiles());
        List<String> filesMemory = getFileNames(new File(RUTA_INTERNA).listFiles());
        List<String> list = new ArrayList<>();
        list.addAll(filesSD);
        list.addAll(filesMemory);
        //Creamos un adaptador y asignamos los datos
        ArrayAdapter<String> adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, list);
        lvFiles = (ListView) findViewById(R.id.listFiles);
        lvFiles.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lvFiles.setAdapter(adaptador);
        */
    }

    private List<File> getAllFiles() {
        RUTA_EXTERNA = getExternalFilesDir(null) + "/";
        RUTA_INTERNA = getFilesDir() + "/";
        List<File> filesSD = getListFilesFromDirectory(new File(RUTA_EXTERNA));
        Log.d("Ficheros externa", filesSD.toString());
        List<File> filesMemory = getListFilesFromDirectory(new File(RUTA_INTERNA));
        Log.d("Ficheros memory", filesMemory.toString());
        List<File> list = new ArrayList<>();
        list.addAll(filesMemory);
        list.addAll(filesSD);
        return list;
    }

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

    public void eliminarSeleccionados(View view) {
        //Recuperamos los elementos seleccionados
        SparseBooleanArray checked = lvFiles.getCheckedItemPositions();
        for (int i = 0; i < lvFiles.getCount(); i++) {
            if (checked.get(i)) {
                //Borramos el fichero
                File file = (File) lvFiles.getItemAtPosition(i);
                file.delete();
            }
        }
        //Notificamos al adaptador que hay cambios
        adaptador.clear();
        List<File> listFiles = getAllFiles();
        adaptador.addAll(listFiles);
        adaptador.notifyDataSetChanged();
    }

    public void eliminarTodos(View view) {
        for (int i = 0; i < lvFiles.getCount(); i++) {
            //Borramos el fichero
            File file = (File) lvFiles.getItemAtPosition(i);
            file.delete();
        }
        //Notificamos al adaptador que hay cambios
        adaptador.clear();
        List<File> listFiles = getAllFiles();
        adaptador.addAll(listFiles);
        adaptador.notifyDataSetChanged();
    }

}
