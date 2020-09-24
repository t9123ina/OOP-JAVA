import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class DBSQTable {

    private String tablename;
    private ArrayList<String> attributelist= new ArrayList<>();

    public DBSQTable(String tablename) throws IOException {
        this.tablename = tablename;
    }

    public boolean checkAttributeExist(String attribute){
        return attributelist.contains(attribute) || attribute.equals("*");
    }

    public int getindexofAttribute(String attribute){
        return attributelist.indexOf(attribute);
    }

    /*when the user give the attribute list for creating the table,
    we call this function to insert the attribute list to be the haeder*/
    public void insertHeader(ArrayList<String> name) throws IOException {
        List<List<String>> table = new ArrayList<>();
        List<String> header = new ArrayList<>();
        header.add("id");
        header.addAll(name);
        table.add(header);
        attributelist.addAll(header);
        writeintofile(table);
    }

    /* drop existed column from the table */
    public void dropTablecolumn(String dropname) throws IOException{
        List<List<String>> fianltable =  changeAabstracttoReal(readTable());
        int deleteCol = getindexofAttribute(dropname);
        for(List<String> i: fianltable){
            i.remove(deleteCol);
        }
        attributelist.remove(dropname);
        writeintofile(fianltable);
    }

    /* add new column in the table */
    public void addTableColumn(String newheader) throws IOException  {
        List<List<String>> fianltable = changeAabstracttoReal(readTable());
        fianltable.get(0).add(newheader);
        // insert blank for the value in new column
        for(int i=1; i<fianltable.size();i++){
            fianltable.get(i).add(" ");
        }
        attributelist.add(newheader);
        writeintofile(fianltable);
    }

    /*Insert the input value into the table*/
    public void insertValue(ArrayList<String> value) throws IOException {
        int index=0;
        List<List<String>> table = readTable();
        List<String> insertvalue = new ArrayList<>();
        // unique id for inserted value
        for(List<String> t: table){
            index++;
        }
        insertvalue.add(Integer.toString(index));
        insertvalue.addAll(value);
        table.add(insertvalue);
        writeintofile(table);
    }

    /*Selecting the specific values which match the condition*/
    public List<List<String>> selectValue(String attributename, ArrayList<String> condition) throws IOException {
        if(condition.size()!=0){
            return returnAttribute(attributename,acceptCondition(condition));
        } else{
            return returnAttribute(attributename,readTable());
        }
    }

    /*Deleting the specific values which match the condition*/
    public void deleteValue(ArrayList<String> condition) throws IOException {
        List<List<String>> deleteValue = acceptCondition(condition);
        List<List<String>> originaltable = readTable();
        //remove header from deleteValue
        deleteValue.remove(0);
        //remove specific values from orignal table
        originaltable.removeIf(i -> deleteValue.contains(i));
        writeintofile(originaltable);
    }

    /*Updating the value and saving it into the table*/
    public void updateValue(ArrayList<String> updateValue, ArrayList<String> condition) throws IOException {
        List<List<String>> needupdateCol = acceptCondition(condition);
        List<List<String>> originaltable = readTable();
        int indexofupdateCol = getindexofAttribute(updateValue.get(0));
        //remove header from needupdateCol
        needupdateCol.remove(0);
        //update specific values from orignal table
        for (List<String> i: originaltable){
            if(needupdateCol.contains(i)){
                System.out.println(i.get(indexofupdateCol));
                i.set(indexofupdateCol , updateValue.get(2));
            }
        }
        writeintofile(originaltable);
    }

    /* write the values into file*/
    private void writeintofile(List<List<String>> originaltable) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(tablename));
        for (List<String> i: originaltable){
            for (String putVal: i){
                writer.append(putVal);
                if(i.indexOf(putVal)!=i.size()-1){
                    writer.append(",");
                }
            }
            writer.append("\n");
        }
        writer.flush();
        writer.close();
    }

    /*Return *(all) or certain attribute*/
    private List<List<String>> returnAttribute(String attributename,List<List<String>> tablelist){
        if(attributename.equals("*")){
            return tablelist;
        } else {
            int index= getindexofAttribute(attributename);
            List<List<String>> templist = new ArrayList<>();
            for(List<String> i: tablelist){
                List<String> tempval = new ArrayList<>();
                tempval.add(i.get(0));
                tempval.add(i.get(index));
                templist.add(tempval);
            }
            return templist;
        }
    }

    /*Combining values that meet multiple conditions*/
    private List<List<String>> acceptCondition(ArrayList<String> condition)throws IOException{
        List<List<String>> table = Meetcondition(condition);
        int conditionlen = condition.size();
        if(conditionlen>3){
            int countlen = 3;
            while(countlen < conditionlen-1){
                ArrayList<String> temp = Stringforinsert(condition,countlen+1);
                List<List<String>> temptable = Meetcondition(temp);
                // If the conditions use "AND" to combine
                if(condition.get(countlen).equals("AND")){
                    table.removeIf(i -> !temptable.contains(i));
                }
                // If the conditions use "OR" to combine
                if(condition.get(countlen).equals("OR")){
                    for(List<String> i: temptable){
                        if(!table.contains(i)){
                            table.add(i);
                        }
                    }
                }
                countlen+=4;
            }
        }
        return table;
    }

    /*Finding the values that meet each single condition*/
    private List<List<String>> Meetcondition(ArrayList<String> condition) throws IOException {
        Float temp;
        int col = getindexofAttribute(condition.get(0));
        List<List<String>> item = readTable();
        List<String> header = item.get(0);
        List<List<String>> meetConditionlist = new ArrayList<>();
        // remove header in order to compare value
        item.remove(0);
        switch(condition.get(1))
        {
            case "==" :
                item.removeIf(i -> !i.get(col).equals(condition.get(2)));
                break;
            case ">" :
                temp = Float.parseFloat(condition.get(2));
                item.removeIf(i -> Float.parseFloat(i.get(col)) <= temp);
                break;
            case "<" :
                temp = Float.parseFloat(condition.get(2));
                item.removeIf(i -> Float.parseFloat(i.get(col)) >= temp);
                break;
            case ">=" :
                temp = Float.parseFloat(condition.get(2));
                item.removeIf(i -> Float.parseFloat(i.get(col)) < temp);
                break;
            case "<="  :
                temp = Float.parseFloat(condition.get(2));
                item.removeIf(i -> Float.parseFloat(i.get(col)) > temp);
                break;
            case "!="  :
                item.removeIf(i -> i.get(col).equals(condition.get(2)));
                break;
            case "LIKE"  :
                item.removeIf(i -> !i.get(col).contains(condition.get(2)));
                break;
        }
        meetConditionlist.add(header);
        meetConditionlist.addAll(item);
        return meetConditionlist;
    }

    /*scanning the table, and putting the results into arraylist*/
    public List<List<String>> readTable() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(tablename));
        String line = null;
        List<List<String>> tt = new ArrayList<>();
        while((line=reader.readLine())!=null){
            tt.add(Arrays.asList(line.split(",")));
        }
        reader.close();
        return tt;
    }

    /*The next condition to check*/
    private ArrayList<String> Stringforinsert(ArrayList<String> condition, int startpoint){
        ArrayList<String> temp = new ArrayList<>();
        for(int i=startpoint;i<condition.size();i++){
            temp.add(condition.get(i));
        }
        return temp;
    }

    public List<List<String>> changeAabstracttoReal(List<List<String>>  originaltable){
        List<List<String>> fianltable = new ArrayList<>();
        for(List<String> i: originaltable){
            List<String> temp = new ArrayList<>();
            for(String k: i){
                temp.add(k);
            }
            fianltable.add(temp);
        }
        return fianltable;
    }
}
