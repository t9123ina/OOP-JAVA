import org.json.simple.*;
import org.json.simple.parser.*;

import java.io.*;
import java.util.*;

class StagModelActions {

    private ActionsSet actionsSet;
    private ArrayList<ActionsSet>  ActionsList = new ArrayList<ActionsSet>();

    // parse json file
    public StagModelActions(String actionFilename){
        try {
            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader(actionFilename);
            JSONObject obj = (JSONObject) parser.parse(reader);
            Set<Object> set = obj.keySet();
            Iterator<Object> iterator = set.iterator();

            while (iterator.hasNext()) {
                Object object = iterator.next();
                JSONArray jsonArr = (JSONArray) obj.get(object);
                for (Object o : jsonArr) {
                    actionsSet = new ActionsSet();
                    parseJson((JSONObject) o);
                    ActionsList.add(actionsSet);
                }
            }
        }catch (FileNotFoundException fnfe) {
            System.out.println(fnfe);
        }catch (ParseException | IOException pe) {
            pe.printStackTrace();
        }
    }

    private void parseJson(JSONObject jsonObject){
        Set<Object> set = jsonObject.keySet();
        Iterator<Object> iterator = set.iterator();

        while (iterator.hasNext()) {
            Object obj = iterator.next();
            if (jsonObject.get(obj) instanceof JSONArray) {
                setActionsMap(jsonObject,obj);
            } else {
                String ss = (String) jsonObject.get(obj);
                actionsSet.setNarrtion(ss);
            }
        }
    }

    // parse the
    private void setActionsMap(JSONObject jsonObject, Object obj){
        JSONArray jsonArr = (JSONArray) jsonObject.get(obj);
        for (Object o : jsonArr) {
            String ss = (String) o;
            switch (obj.toString()) {
                case "consumed":
                    actionsSet.addConsumed(ss);
                    break;
                case "triggers":
                    actionsSet.addTriggers(ss);
                    break;
                case "subjects":
                    actionsSet.addSubjects(ss);
                    break;
                case "produced":
                    actionsSet.addProduced(ss);
                    break;
            }
        }
    }

    public ArrayList<ActionsSet> getActionsList(){
        return ActionsList;
    }
}