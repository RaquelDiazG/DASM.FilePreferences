package es.upm.miw.ficheros;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.widget.Toast;


public class AjustesFragment extends PreferenceFragment {

    private Context contexto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Guardar el contexto
        contexto = getActivity().getApplicationContext();

        // Leer preferencias del recurso XML
        getPreferenceManager().setSharedPreferencesName("Preferences");
        addPreferencesFromResource(R.xml.preferences);

        //Añadimos un listener para guardar las preferencias del almacenamiento (checkbox)
        final CheckBoxPreference checkbox_preference = (CheckBoxPreference) findPreference("checkbox_preference");
        checkbox_preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference pref, Object val) {
                //Recuperamos el valor
                Boolean checkBoxVal = (Boolean) val;
                //Añadimos el valor a las preferencias
                SharedPreferences sharedPref = getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(getString(R.string.prefRutaFichero), checkBoxVal);
                editor.commit();
                //Notificacion
                Toast.makeText(contexto, "Preferencia añadida correctamente", Toast.LENGTH_LONG).show();
                Log.d("Preferencia SD añadida", Boolean.toString(checkBoxVal));
                return true;
            }
        });

        //Añadimos un listener para guardar las preferencias del almacenamiento (nombre fichero)
        final EditTextPreference edittext_preference = (EditTextPreference) findPreference("edittext_preference");
        edittext_preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference pref, Object val) {
                //Recuperamos el valor
                String nombreFichero = (String) val;
                //Añadimos el valor a las preferencias
                SharedPreferences sharedPref = getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.prefNombreFichero), nombreFichero);
                editor.commit();
                //Notificacion
                Toast.makeText(contexto, "Preferencia añadida correctamente", Toast.LENGTH_LONG).show();
                Log.d("Pref nombre añadida", nombreFichero);
                return true;
            }
        });
    }


}
