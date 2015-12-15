package chiarahsieh.skypieah.happytranslator;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ChiaraHsieh on 2015/12/15.
 */
public class EntryClause {

    public String chi;
    public String eng;
    public String twn;

    public String imgPath;

    public EntryClause( String eng, String chi, String twn) {
        this.eng = eng;
        this.chi = chi;
        this.twn = twn;
    }

    public EntryClause(JSONObject jObj) throws JSONException {
        this.eng = jObj.optString(TAG.ENGLISH);
        this.chi = jObj.optString(TAG.CHINESE);
        this.twn = jObj.optString(TAG.TAIWANESE);
    }

    public JSONObject toJsonObject() throws JSONException {

        JSONObject jObj = new JSONObject();

        jObj.put(TAG.ENGLISH,eng);
        jObj.put(TAG.CHINESE,chi);
        jObj.put(TAG.TAIWANESE,twn);

        return jObj;
    }

    static class TAG {
        static String ENGLISH = "eng";
        static String CHINESE = "chi";
        static String TAIWANESE = "twn";
    }
}
