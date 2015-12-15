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

    /**
     * Construct ArrayList of EntryClause from JSON file
     * @param fileName
     * @return list of dictionary entries
     */
    public static ArrayList<EntryClause> loadEntries(Context context, String fileName) {
        ArrayList<EntryClause> mEntryArrayList = new ArrayList<EntryClause>();
        String jsonData = "";

        try {
            File f = new File(context.getFilesDir().getPath() + "/" + fileName);
            //check whether file exists
            FileInputStream is = new FileInputStream(f);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonData = new String(buffer);
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

    public static void saveEntries(Context context, String fileName, ArrayList<EntryClause> mEntryArrayList) {

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
            FileWriter file = new FileWriter(context.getFilesDir().getPath() + "/" + fileName);
            file.write(jArr.toString());
            file.flush();
            file.close();
        } catch (IOException e) {
            Log.e("TAG", "Error in Writing: " + e.getLocalizedMessage());
        }
    }
}
