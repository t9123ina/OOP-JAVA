import java.util.*;

class StagPlayer extends Entity{

    private Integer healthLevel;
    private HashMap<String, Artefact> inventory = new HashMap<String,Artefact>();
    private ArrayList<Location> locations;
    private HashMap<String, Location> locationHashMap;
    private Location currentLocation;

    public StagPlayer(StagModelEntities ee, String playerName){
        super(playerName,"game player");
        this.healthLevel = 3;
        this.locationHashMap = ee.getLocationMap();
        this.locations =ee.getLocationList();
        initialCurrentLocation();
    }

    public Integer getHealthLevel() {
        return healthLevel;
    }

    public void addHealthLevel(){
        healthLevel++;
    }

    public void decreaseHealthLevel(){
        healthLevel--;
    }

    public void addInventory(String inventoryName,Artefact artefact ){
        inventory.put(inventoryName,artefact);
    }

    public Set<String> getInventoryList(){
        return inventory.keySet();
    }

    public Artefact getSpecificInventory(String inventoryName){
        return inventory.get(inventoryName);
    }

    public void removeInventory(String inventoryName){
        inventory.remove(inventoryName);
    }

    public boolean containInventory(String inventoryName){
        return inventory.containsKey(inventoryName);
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = locationHashMap.get(currentLocation);
    }

    public Location getCurrentLocation(){
        return currentLocation;
    }

    public void initialCurrentLocation(){
        if(!locations.get(0).getName().equals("unplaced")){
            currentLocation = locations.get(0);
        }else{
            currentLocation = locations.get(1);
        }
    }

    public ArrayList<String> getPathtoLocation(){
        return this.currentLocation.getpathtoLocation();
    }
}