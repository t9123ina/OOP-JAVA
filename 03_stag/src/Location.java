import java.util.*;

class Location extends Entity
{

    private HashMap<String, Artefact> artefactMap = new HashMap<String, Artefact>();
    private HashMap<String, Character> CharacterMap = new HashMap<String, Character>();
    private HashMap<String, Furniture> furnitureMap = new HashMap<String, Furniture>();
    private ArrayList<String> pathtoLocation = new ArrayList<String>();

    public Location(String name, String description){
        super(name,description);
    }

    public void addArtefact(String artefactname, Artefact artefact){
        this.artefactMap.put(artefactname,artefact);
    }

    public Artefact getSpecificArtefact(String ArtefactName){
        return artefactMap.get(ArtefactName);
    }

    public ArrayList<Artefact> getArtefact(){
        return new ArrayList<Artefact>(artefactMap.values());
    }

    public void removeArtefact(String artefact){
        this.artefactMap.remove(artefact);
    }

    public Boolean containArtefact(String artefact){
        return this.artefactMap.containsKey(artefact);
    }

    public ArrayList<Character> getCharacter(){
        return new ArrayList<Character>(CharacterMap.values());
    }

    public void addCharacter(String EntityName,Character character){
        this.CharacterMap.put(EntityName,character);
    }

    public Boolean containCharacter(String Entity) {
        return this.CharacterMap.containsKey(Entity);
    }

    public Character getSpecificCharacter(String CharacterName){
        return CharacterMap.get(CharacterName);
    }

    public void removeCharacter(String Character){
        this.CharacterMap.remove(Character);
    }

    public ArrayList<Furniture> getFurniture(){
        return new ArrayList<Furniture>(furnitureMap.values());
    }

    public void addFurniture(String EntityName,Furniture furniture){
        this.furnitureMap.put(EntityName,furniture);
    }

    public Boolean containFurniture(String Entity) {
        return this.furnitureMap.containsKey(Entity);
    }

    public Furniture getSpecificFurniture(String FurnitureName){
        return furnitureMap.get(FurnitureName);
    }

    public void removeFurniture(String Furniture){
        this.furnitureMap.remove(Furniture);
    }

    public void addPathtoLocation(String pathtoLocation){
        this.pathtoLocation.add(pathtoLocation);
    }

    public ArrayList<String> getpathtoLocation(){
        return pathtoLocation;
    }

}