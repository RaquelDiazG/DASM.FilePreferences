package es.upm.miw.ficheros;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class AjustesFragment extends PreferenceFragment {

    String ListPreference;
    boolean CheckboxPreference;
    private Context contexto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        getPreferenceManager().setSharedPreferencesName("Preferences");
        addPreferencesFromResource(R.xml.preferences);

        //Guardar el contexto
        contexto =  getActivity().getApplicationContext();

        //Añadimos un listener para guardar las preferencias del almacenamiento
        final CheckBoxPreference pref = (CheckBoxPreference) findPreference("checkbox_preference");
        pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference pref, Object val) {
                Boolean checkBoxVal = (Boolean) val;
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

        //Añadimos un listener para guardar las preferencias del almacenamiento
        final EditTextPreference pref2 = (EditTextPreference) findPreference("edittext_preference");
        pref2.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference pref, Object val) {
                String nombreFichero = (String) val;
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
