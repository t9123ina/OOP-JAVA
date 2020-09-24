import java.io.*;

public class HandleCommand {

    private String command;
    private String[] splitcommand;

    public HandleCommand(String command) throws IOException {
        this.command = command;
        this.splitcommand = command.split(" ");
    }

    // check "USE" query
    public Boolean checkUSEquery(){
        return checklastchar() && splitcommand.length == 2;
    }

    // check "CREATE" query
    public Boolean checkCREATEquery(){
        if(splitcommand[1].equals("DATABASE")){
            return checklastchar() && splitcommand.length == 3;
        }
        if(splitcommand[1].equals("TABLE")){
            if(splitcommand.length>3){
                return checklastchar() && checklist() && !splitcommand[2].contains("\\(");
            }else{
                return checklastchar() && splitcommand.length == 3;
            }
        }
        return false;
    }

    // check "DROP" query
    public Boolean checkDROPquery(){
        return checklastchar() && splitcommand.length == 3
                && (splitcommand[1].equals("TABLE") || splitcommand[1].equals("DATABASE"));
    }

    // check "ALTER" query
    public Boolean checkALTERquery(){
        return checklastchar() && splitcommand.length == 5 && splitcommand[1].equals("TABLE")
                && (splitcommand[3].equals("ADD") || splitcommand[3].equals("DROP")) && checkStringValue();
    }

    // check "INSERT" query
    public Boolean checkINSERTquery(){
        return checklastchar() &&  splitcommand[1].equals("INTO") && splitcommand[3].equals("VALUES")
                && checklist() && checkStringValue();
    }

    // check "SELECT" query
    public Boolean checkSELECTquery(){
        if(splitcommand.length == 4){
            return checklastchar() && splitcommand[2].equals("FROM") && checkStringValue() && checkCondition();
        }
        if(splitcommand.length > 4) {
            return checklastchar() && splitcommand[2].equals("FROM") && splitcommand[4].equals("WHERE")
                    && checkCondition() && checkStringValue();
        }
        return false;
    }

    // check "UPDATE" query
    public Boolean checkUPDATEquery(){
        return checklastchar() &&  splitcommand[2].equals("SET") && splitcommand[6].equals("WHERE")
                && checkCondition() && checkStringValue();
    }

    // check "DELETE" query
    public Boolean checkDELETEquery(){
        return checklastchar() &&  splitcommand[1].equals("FROM") && splitcommand[3].equals("WHERE")
                && checkCondition() && checkStringValue();
    }

    // check "JOIN" query
    public Boolean checkJOINquery(){
        return checklastchar() &&  splitcommand[2].equals("AND") && splitcommand[4].equals("ON")
                &&  splitcommand[6].equals("AND") && splitcommand.length == 8;
    }

    // check ';'
    private Boolean checklastchar(){
        return command.charAt(command.length() - 1) == ';';
    }

    // check '()' and check ","
    private Boolean checklist(){
        int countfirstbra=0,countsecondbra=0, conjnum=0;
        boolean makesureorder=true,makesurecomma=true;
        for(String i: splitcommand){
            if(i.contains("\\(")){ countfirstbra++; }

            if(i.contains("\\)")){ countsecondbra++; }

            if(countsecondbra > countfirstbra){ makesureorder= false; }

            if(countfirstbra>0 && countsecondbra==0 && !i.contains(",")) { makesurecomma= false; }
        }
        return countfirstbra == countsecondbra && makesureorder && makesurecomma;
    }

    // check '()' and check "and\or"
    private Boolean checkCondition(){
        int countfirstbra=0,countsecondbra=0, conjnum=0;
        boolean makesureorder=true;
        for(String i: splitcommand){
            if((i.equals("AND") || i.equals("OR")) && countfirstbra == countsecondbra && countfirstbra>0){ conjnum++; }

            if(i.contains("\\(")){ countfirstbra++; }

            if(i.contains("\\)")){ countsecondbra++; }

            if(countsecondbra > countfirstbra){ makesureorder= false; }
        }
        if(countfirstbra>1){
            // Use and/or to link each "()"
            return countfirstbra == countsecondbra && makesureorder && conjnum+1 == countfirstbra;
        }else{
            return countfirstbra == countsecondbra && makesureorder;
        }
    }

    // check string value
    private Boolean checkStringValue(){
        int countnum=0;
        command = command.replaceAll("'","@");
        for(int i =0; i<command.length();i++){

            if(command.charAt(i) == '@'){ countnum++; }
        }
        return countnum%2==0;
    }
}
