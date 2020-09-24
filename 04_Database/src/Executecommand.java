import java.io.*;
import java.util.*;

public class Executecommand {

    private String command;
    private String[] splitcommand;
    private BufferedWriter out;
    private HandleCommand handleCommand;
    private DBSQDatabase currentdatabase;
    private DBSQTable currenttable;
    private DatabaseList databaseList;

    public Executecommand(String command, BufferedWriter out,DatabaseList databaseList) throws IOException {
        this.command = command;
        this.splitcommand = command.split(" ");
        this.out =out;
        this.handleCommand = new HandleCommand(command);
        this.databaseList = databaseList;
        switch (splitcommand[0]){
            case "USE":
                if(handleCommand.checkUSEquery()){ executingUSEquery(); }
                else{ out.write("Invalid Query"); }
                break;
            case "CREATE":
                if(handleCommand.checkCREATEquery()){ executingCREATEquery(); }
                else{ out.write("Invalid Query"); }
                break;
            case "DROP":
                if(handleCommand.checkDROPquery()){ executingDROPquery(); }
                else{ out.write("Invalid Query"); }
                break;
            case "ALTER":
                if(handleCommand.checkALTERquery()){ executingALTERquery(); }
                else{ out.write("Invalid Query"); }
                break;
            case "INSERT":
                if(handleCommand.checkINSERTquery()){ executingINSERTquery(); }
                else{ out.write("Invalid Query"); }
                break;
            case "SELECT":
                if(handleCommand.checkSELECTquery()){ executingSELECTquery(); }
                else{ out.write("Invalid Query"); }
                break;
            case "UPDATE":
                if(handleCommand.checkUPDATEquery()){ executingUPDATEquery(); }
                else{ out.write("Invalid Query"); }
                break;
            case "DELETE":
                if(handleCommand.checkDELETEquery()){ executingDELETEquery(); }
                else{ out.write("Invalid Query"); }
                break;
            case "JOIN":
                if(handleCommand.checkJOINquery()){ executingJOINquery(); }
                else{ out.write("Invalid Query"); }
                break;
        }

    }

    private void executingUSEquery()throws IOException{
        ArrayList<String> removeSpecialChar= removespecialChar();
        if (databaseList.checkDatabaseexist(removeSpecialChar.get(1))){
            databaseList.setCurrentDatabase(removeSpecialChar.get(1));
            out.write("OK");
        }else{
            out.write("No database exist!");
        }
    }

    private void executingCREATEquery()throws IOException{
        ArrayList<String> removeSpecialChar= removespecialChar();

        if(removeSpecialChar.get(1).equals("DATABASE")){
            if(!databaseList.checkDatabaseexist(removeSpecialChar.get(2))){
                DBSQDatabase newone = new DBSQDatabase(removeSpecialChar.get(2));
                newone.createDatabase();
                databaseList.AddDatabase(removeSpecialChar.get(2), newone);
                out.write("OK");
            }else{
                out.write("Database already created!");
            }
        }

        if(removeSpecialChar.get(1).equals("TABLE") && databaseList.getCurrentDatabase()!= null){
            currentdatabase = databaseList.getCurrentDatabase();

            if(!currentdatabase.checkTableExist(removeSpecialChar.get(2))){
                currentdatabase.createTable(removeSpecialChar.get(2));
                currenttable = currentdatabase.getTable(removeSpecialChar.get(2));
                if(removeSpecialChar.size()>3){
                    currenttable.insertHeader(Stringforinsert(removeSpecialChar,3,removeSpecialChar.size()));
                }
                out.write("OK");
            }else{
                out.write("Table already created!");
            }
        }
    }

    private void executingDROPquery()throws IOException{
        ArrayList<String> removeSpecialChar= removespecialChar();

        if(removeSpecialChar.get(1).equals("DATABASE")){
            if(databaseList.checkDatabaseexist(removeSpecialChar.get(2))){
                currentdatabase = databaseList.GetDatabase(removeSpecialChar.get(2));
                currentdatabase.dropDatabase();
                databaseList.RemoveDatabase(removeSpecialChar.get(2));
                out.write("OK");
            }else{ out.write("Database does not exist"); }
        }

        if(removeSpecialChar.get(1).equals("TABLE")){
            currentdatabase = databaseList.getCurrentDatabase();
            if(currentdatabase.checkTableExist(removeSpecialChar.get(2))){
                currentdatabase.dropTable(removeSpecialChar.get(2));
                out.write("OK");
            }else{ out.write("Table does not exist"); }
        }
    }

    private void executingALTERquery()throws IOException{
        ArrayList<String> removeSpecialChar= removespecialChar();
        currentdatabase = databaseList.getCurrentDatabase();

        if(currentdatabase.checkTableExist(removeSpecialChar.get(2))){
            currenttable = currentdatabase.getTable(removeSpecialChar.get(2));

            if(removeSpecialChar.get(3).equals("DROP")){
                if(currenttable.checkAttributeExist(removeSpecialChar.get(4))){
                    currenttable.dropTablecolumn(removeSpecialChar.get(4));
                    out.write("OK");
                }else{ out.write("Attribute does not exist"); }
            }

            if(removeSpecialChar.get(3).equals("ADD")){
                if(!currenttable.checkAttributeExist(removeSpecialChar.get(4))){
                    currenttable.addTableColumn(removeSpecialChar.get(4));
                    out.write("OK");
                }else{ out.write("Attribute already exist"); }
            }
        }else{
            out.write("Table does not exist");
        }
    }

    private void executingINSERTquery()throws IOException{
        ArrayList<String> removeSpecialChar= removespecialChar();
        currentdatabase = databaseList.getCurrentDatabase();

        if(currentdatabase.checkTableExist(removeSpecialChar.get(2))){
            currenttable = currentdatabase.getTable(removeSpecialChar.get(2));
            currenttable.insertValue(Stringforinsert(removeSpecialChar,4,removeSpecialChar.size()));
            out.write("OK");
        }else{
            out.write("Table does not exist");
        }
    }

    private void executingSELECTquery()throws IOException{
        ArrayList<String> removeSpecialChar= removespecialChar();
        currentdatabase = databaseList.getCurrentDatabase();

        if(currentdatabase.checkTableExist(removeSpecialChar.get(3))){
            currenttable = currentdatabase.getTable(removeSpecialChar.get(3));
            if(currenttable.checkAttributeExist(removeSpecialChar.get(1))) {

                writetable(currenttable.selectValue(splitcommand[1], Stringforinsert(removeSpecialChar,5,removeSpecialChar.size())));
            }
            else{
                out.write("Attribute does not exist");
            }
        }else{
            out.write("Table does not exist");
        }
    }

    private void executingJOINquery()throws IOException{
        ArrayList<String> removeSpecialChar= removespecialChar();
        currentdatabase = databaseList.getCurrentDatabase();

        if(currentdatabase.checkTableExist(removeSpecialChar.get(1)) && currentdatabase.checkTableExist(removeSpecialChar.get(3))){

            if(currentdatabase.getTable(removeSpecialChar.get(1)).checkAttributeExist(removeSpecialChar.get(5)) &&
                    currentdatabase.getTable(removeSpecialChar.get(3)).checkAttributeExist(removeSpecialChar.get(7))) {
                writetable(currentdatabase.joinValue(removeSpecialChar.get(1),removeSpecialChar.get(3),removeSpecialChar.get(5),removeSpecialChar.get(7)));
            }
            else{
                out.write("Attribute does not exist");
            }
        }else{
            out.write("Table does not exist");
        }
    }

    private void executingDELETEquery()throws IOException{
        ArrayList<String> removeSpecialChar= removespecialChar();
        currentdatabase = databaseList.getCurrentDatabase();

        if(currentdatabase.checkTableExist(removeSpecialChar.get(2))){
            currenttable = currentdatabase.getTable(removeSpecialChar.get(2));
            currenttable.deleteValue(Stringforinsert(removeSpecialChar,4,removeSpecialChar.size()));
            out.write("OK");
        }else{
            out.write("Table does not exist");
        }
    }

    private void executingUPDATEquery()throws IOException{
        ArrayList<String> removeSpecialChar= removespecialChar();
        currentdatabase = databaseList.getCurrentDatabase();

        if(currentdatabase.checkTableExist(removeSpecialChar.get(1))){
            currenttable = currentdatabase.getTable(removeSpecialChar.get(1));
            ArrayList<String> changedVal = Stringforinsert(removeSpecialChar,3,6);
            ArrayList<String> condition = Stringforinsert(removeSpecialChar,7,removeSpecialChar.size());
            currenttable.updateValue(changedVal,condition);
            out.write("OK");
        }else{
            out.write("Table does not exist");
        }
    }

    private void writetable(List<List<String>> table) throws IOException {
        for(List<String> j : table){
            for (String i: j){ out.write(i+" "); }
            out.write("\n");
        }
    }

    /*Cleaning special characters and Splitting command for executing*/
    private ArrayList<String> removespecialChar(){
        command = command.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll(";", "").replaceAll(",", "");
        String[] stringsplit = command.split("'");;
        String[] spacesplit =command.split(" ");
        int count=0;
        ArrayList<String> removespecialChar = new ArrayList<>();
        for(String s:stringsplit){
            String[] kk =s.split(" ");
            for (String value : kk) {
                // for String (because String may contain many words)
                if ("'".concat(value).equals(spacesplit[count])) {
                    removespecialChar.add(s);
                    count += kk.length;
                    break;
                }
                if (!value.equals("")) {
                    removespecialChar.add(value);
                    count++;
                }
            }
        }
        return removespecialChar;
    }

    /*Temporary String for inserting into table*/
    private ArrayList<String> Stringforinsert(ArrayList<String> originlist,int startpoint, int endpoint){
        ArrayList<String> temp = new ArrayList<>();
        for(int i=startpoint;i<endpoint;i++){
            temp.add(originlist.get(i));
        }
        return temp;
    }



}
