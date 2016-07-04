package chiarahsieh.skypieah.happytranslator;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ChiaraHsieh on 2015/12/15.
 */
public class HappyDict {

    public static String name = "happydict.json";

    /**
     * Construct ArrayList of EntryClause from JSON file
     * @return list of dictionary entries
     */
    public static ArrayList<EntryClause> loadEntries(Context context) {
        ArrayList<EntryClause> mEntryArrayList = new ArrayList<EntryClause>();
        String jsonData = "";

        try {
            synchronized(SearchActivity.sDataLock) {
                File internalAppDir = context.getFilesDir();
                File f = new File(internalAppDir,name);
                Log.d("HappyDict",f.getAbsolutePath());
                FileInputStream is = new FileInputStream(f);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                jsonData = new String(buffer);
            }
        } catch (IOException e) {
            Log.e("TAG", "Error in Reading: " + e.getLocalizedMessage());
        }

        try {
            JSONArray jArr = new JSONArray(jsonData);
            for(int i=0; i<jArr.length(); i++) {
                JSONObject jObj = jArr.getJSONObject(i);
                mEntryArrayList.add(new EntryClause(jObj));
            }
        } catch (JSONException e) {
            Log.e("TAG", "Error in Converting: " + e.getLocalizedMessage());
        }
        return mEntryArrayList;
    }

    public static void saveEntries(Context context, ArrayList<EntryClause> mEntryArrayList) {

        JSONArray jArr = new JSONArray();

        try {
            for(EntryClause entry : mEntryArrayList) {
                JSONObject jObj = entry.toJsonObject();
                jArr.put(jObj);
            }
        } catch (JSONException e) {
            Log.e("TAG", "Error in Converting: " + e.getLocalizedMessage());
        }

        try {
            synchronized(SearchActivity.sDataLock) {
                FileWriter file = new FileWriter(context.getFilesDir().getPath() + "/" + name);
                file.write(jArr.toString());
                file.flush();
                file.close();
            }
        } catch (IOException e) {
            Log.e("TAG", "Error in Writing: " + e.getLocalizedMessage());
        }
    }

    public static void exportFile() {

    }
}
