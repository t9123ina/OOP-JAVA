import java.io.*;
import java.util.*;

class StagController {

    private StagModelEntities ee;
    private BufferedWriter out;
    private String line;
    private Location currentLocation;
    private ArrayList<String> commandsList = new ArrayList<String>();
    private ArrayList<ActionsSet> ActionsList;
    private HashMap<String, Location> locationHashMap;
    private ArrayList<String> pathtoLocation;
    private StagPlayer player;

    public StagController(BufferedReader in, BufferedWriter out, StagModelEntities ee, StagModelActions aa)
            throws IOException {
        this.ee =ee;
        this.out = out;
        this.line = in.readLine();
        this.ActionsList = aa.getActionsList();
        this.locationHashMap = ee.getLocationMap();
        convertCommand();
    }

    private void convertCommand() throws IOException {
        StringTokenizer tokenizer = new StringTokenizer(line);

        while(tokenizer.hasMoreTokens()){ commandsList.add(tokenizer.nextToken().toLowerCase()); }

        if(commandsList.size()<1){ out.write("You are not put the right commands "+ "\n"); }

        // Set current player
        if (ee.containPlayer(commandsList.get(0)))
        {
            this.player = ee.getPlayer(commandsList.get(0));
        }else{
            player = new StagPlayer(ee,commandsList.get(0));
            ee.addPlayer(commandsList.get(0),player);
        }

        this.currentLocation = player.getCurrentLocation();
        this.pathtoLocation = player.getPathtoLocation();
        ExecuteCommand();
    }

    private void ExecuteCommand() throws IOException {
        if(commandsList.contains("inventory") || commandsList.contains("inv")){
            getInventoryList();
        } else if(commandsList.contains("get")){
            takeInventorytoList();
        } else if (commandsList.contains("drop")){
            dropInventoryfromList();
        } else if (commandsList.contains("goto")){
            gotoLocation();
        } else if(commandsList.contains("look")){
            lookLocation();
        } else if(commandsList.contains("health")){
            out.write("You have "+ Integer.toString(player.getHealthLevel())+"\n");
        } else{dealActionsCommand();}
    }

    private void getInventoryList() throws IOException {
        out.write("You are carrying: \n"+ player.getInventoryList() +"\n");
    }

    private void takeInventorytoList() throws IOException {
        boolean hasinventory = false;
        String inventoryName = new String();

        for (String s : commandsList) {
            if (currentLocation.containArtefact(s)) {
                hasinventory = true;
                inventoryName = s;
                break;
            }
        }

        if(hasinventory){
            player.addInventory(inventoryName,currentLocation.getSpecificArtefact(inventoryName));
            currentLocation.removeArtefact(inventoryName);
            out.write("You get "+ inventoryName+" into your inventory\n");
        }else{
            out.write("You cannot get artefact in your bag, please try again\n");
        }
    }

    private void dropInventoryfromList() throws IOException {
        boolean hasinventory = false;
        String inventoryName = new String();
        for (String s : commandsList) {
            if (player.containInventory(s)) {
                hasinventory = true;
                inventoryName = s;
                break;
            }
        }
        if(hasinventory){
            currentLocation.addArtefact(inventoryName,player.getSpecificInventory(inventoryName));
            player.removeInventory(inventoryName);
            out.write("You remove "+ inventoryName+" from your inventory\n");
        }else{
            out.write("You cannot remove artefact from your inventory, please try again\n");
        }
    }

    private void gotoLocation() throws IOException {
        boolean hasdestination = false;
        String toLocation = new String();
        for (String value : commandsList) {
            for (String s : pathtoLocation) {
                if (value.equals(s) && !value.equals(currentLocation.getName())) {
                    hasdestination=true;
                    toLocation = value;
                    break;
                }
            }
        }
        if(hasdestination){
            player.setCurrentLocation(toLocation);
            out.write("You move to "+ toLocation+"\n");
        }else{
            out.write("You cannot move to the location, please try again\n");
        }
    }

    private void lookLocation() throws IOException {
        out.write(currentLocation.getName() + " have\n");

        out.write("Character: "+"\n");
        for (Character cc: currentLocation.getCharacter()){
            out.write( "Name: "+ cc.getName()+ "   Description: "+ cc.getDescription()+"\n");
        }
        out.write("Furniture: "+ "\n");
        for (Furniture ff: currentLocation.getFurniture()){
            out.write( "Name: "+ ff.getName()+ "   Description: "+ ff.getDescription()+"\n");
        }
        out.write( "Artefact: "+"\n");
        for (Artefact aa: currentLocation.getArtefact()){
            out.write( "Name: "+ aa.getName()+ "   Description: "+ aa.getDescription()+"\n");
        }
        out.write( "You can go to: "+ pathtoLocation + "\n");

    }

    private void dealActionsCommand() throws IOException {
        int countexecutealbe=0;
        ActionsSet currentAction = new ActionsSet();

        // check trigger word
        for (String s : commandsList) {
            for (ActionsSet actionsSet : ActionsList) {
                if (actionsSet.containTriggers(s)) {
                    currentAction = actionsSet;
                }
            }
        }
        // check subjects
        for (int l = 0; l < currentAction.getSubjects().size(); l++) {
            String subject = currentAction.getSubjects().get(l);
            if (currentLocation.containArtefact(subject) || currentLocation.containCharacter(subject)
                    || currentLocation.containFurniture(subject) || player.containInventory(subject)) {
                countexecutealbe++;
            }
        }
        executeAction(countexecutealbe, currentAction);
    }

    private void executeAction(Integer countexecutealbe, ActionsSet currentAction) throws IOException {
        if( countexecutealbe==currentAction.getSubjects().size()){
            dealConsumed(currentAction);
            dealProduced(currentAction);
            out.write(currentAction.getNarration()+"\n");
        }else{
            out.write("You cannot execute the commands, please try again\n");
        }
    }

    private void dealConsumed(ActionsSet currentAction) throws IOException {
        for(int k=0;k<currentAction.getConsumed().size();k++){

            if(currentLocation.containFurniture(currentAction.getConsumed().get(k))){
                currentLocation.containFurniture(currentAction.getConsumed().get(k));
            }
            if(currentLocation.containCharacter(currentAction.getConsumed().get(k))){
                currentLocation.containCharacter(currentAction.getConsumed().get(k));
            }
            if (currentLocation.containArtefact(currentAction.getConsumed().get(k))){
                currentLocation.removeArtefact(currentAction.getConsumed().get(k));
            }
            if (currentLocation.containFurniture(currentAction.getConsumed().get(k))){
                currentLocation.removeFurniture(currentAction.getConsumed().get(k));
            }
            if (currentLocation.containCharacter(currentAction.getConsumed().get(k))){
                currentLocation.removeCharacter(currentAction.getConsumed().get(k));
            }
            if(player.containInventory(currentAction.getConsumed().get(k))){
                player.removeInventory(currentAction.getConsumed().get(k));
            }
            if (currentAction.getConsumed().contains("health")){
                player.decreaseHealthLevel();
                if(player.getHealthLevel()==0){
                    String playername = player.getName();
                    ee.removePlayer(playername);
                    ee.addPlayer(playername,new StagPlayer(ee,playername));
                    out.write("You have no health, therefore, you return to start\n");
                }
            }
        }
    }

    private void dealProduced(ActionsSet currentAction){
        for(int k=0;k<currentAction.getProduced().size();k++){
            if(locationHashMap.containsKey(currentAction.getProduced().get(k))){
                player.setCurrentLocation(currentAction.getProduced().get(k));
            }else{
                Location unplaced = locationHashMap.get("unplaced");
                if (unplaced.containArtefact(currentAction.getProduced().get(k))){
                    player.addInventory(currentAction.getProduced().get(k),unplaced.getSpecificArtefact(currentAction.getProduced().get(k)));
                }
                if (unplaced.containCharacter(currentAction.getProduced().get(k))){
                    currentLocation.addCharacter(currentAction.getProduced().get(k),unplaced.getSpecificCharacter(currentAction.getProduced().get(k)));
                }
                if (unplaced.containFurniture(currentAction.getProduced().get(k))){
                    currentLocation.addFurniture(currentAction.getProduced().get(k),unplaced.getSpecificFurniture(currentAction.getProduced().get(k)));
                }
            }
            if (currentAction.getProduced().contains("health")){
                player.addHealthLevel();
            }
        }
    }

}