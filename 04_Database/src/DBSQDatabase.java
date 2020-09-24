import java.io.*;
import java.util.*;

public class DBSQDatabase {

    private String databasename;
    private HashMap<String, DBSQTable> tablelist = new HashMap<>();

    public DBSQDatabase(String databasename){
        this.databasename = databasename;
    }

    /*Creating a folder to be the database*/
    public void createDatabase() {
        File Database = new File(databasename);
        Database.mkdir();
    }

    /*Creating a csv file, and saving it into the folder which is the database*/
    public void createTable(String tablename) throws IOException {
        String filename = tablename+".csv";
        String path = databasename + File.separator + filename;
        File Table = new File(path);
        Table.createNewFile();
        tablelist.put(tablename,new DBSQTable(path));
    }

    /*Checking the table is exist or not, and if it exists, user can use the table*/
    public boolean checkTableExist(String tablename){
        return tablelist.containsKey(tablename);
    }

    /*Getting the table that belonged to database*/
    public DBSQTable getTable(String tablename)throws IOException {
        return tablelist.get(tablename);
    }

    /*drop the database. Before dropping the folder, the files in the folder need to be deleted*/
    public void dropDatabase(){
        File database =  new File(databasename);
        File[] entries = database.listFiles();
        for(File currentFile: entries){
            currentFile.delete();
        }
        tablelist.clear();
        database.delete();
    }

    /*drop the table*/
    public void dropTable(String tablename){
        tablelist.remove(tablename);
        File table =  new File(databasename+ File.separator +tablename+".csv");
        table.delete();
    }

    /*Joining two table together and outputing the reslut into the server*/
    public List<List<String>> joinValue(String table1name,String table2name, String attribute1, String attribute2) throws IOException {
        DBSQTable table_1 = getTable(table1name);
        DBSQTable table_2 = getTable(table2name);
        List<List<String>> table1 = table_1.changeAabstracttoReal(table_1.readTable());
        List<List<String>> table2 = table_2.changeAabstracttoReal(table_2.readTable());
        int count=1;
        int col1 =table_1.getindexofAttribute(attribute1);
        int col2 = table_2.getindexofAttribute(attribute2);

        // put all headers into join table
        List<List<String>> jointable = new ArrayList<>();
        List<String> header= new ArrayList<>();
        header.add("id");
        header = insertheader(header, table1, table1name);
        header = insertheader(header, table2, table2name);
        jointable.add(header);

        // put the joined values
        for (List<String> strings : table1) {
            for (List<String> h : table2) {
                if (strings.get(col1).equals(h.get(col2))) {
                    h.remove(0);
                    strings.remove(0);
                    List<String> combineval= new ArrayList<>();
                    combineval.add(Integer.toString(count));
                    combineval.addAll(strings);
                    combineval.addAll(h);
                    jointable.add(combineval);
                    count++;
                }
            }
        }
        return jointable;
    }

    private List<String> insertheader(List<String> header, List<List<String>> table, String tablename){
        for (String element : table.get(0)) {
            if (!element.equals("id")){
                header.add(tablename+ "."+ element + " ");
            }
        }
        return header;
    }


}
