import java.util.*;

public class DatabaseList {
    private HashMap<String, DBSQDatabase> databaselist= new HashMap<>();
    private DBSQDatabase currentDatabase;

    public boolean checkDatabaseexist(String name){
        return databaselist.containsKey(name);
    }

    public DBSQDatabase GetDatabase(String name){
        return databaselist.get(name);
    }

    public void AddDatabase(String name, DBSQDatabase database){
        databaselist.put(name,database);
    }

    public void RemoveDatabase(String name){
        databaselist.remove(name);
    }

    public void setCurrentDatabase(String name){
        currentDatabase = databaselist.get(name);
    }

    public DBSQDatabase getCurrentDatabase(){
        return currentDatabase;
    }

}
