package WHS_planner.Core;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by matthewelbing on 16.09.16.
 */
public class JSON {

    private FileWriter fileWriter;
    private JSONObject object;
    private JSONParser parser;

    public JSON (String filepath) throws IOException {
        fileWriter = new FileWriter(filepath);
        object = new JSONObject();
        parser = new JSONParser();
    }
    public void writePair(String key, String data){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(key, data);
    }
    public void writeJsonArray(String key, String data){

    }
}
