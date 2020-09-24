import com.alexmerz.graphviz.*;
import com.alexmerz.graphviz.objects.*;

import java.io.*;
import java.util.*;

class StagModelEntities {

    private Location location;
    private PathtoLocation pathtoLocation;
    private HashMap<String, Location> locationMap = new HashMap<String, Location>();
    private ArrayList<Location> locations = new ArrayList<Location>();
    private HashMap<String, StagPlayer> playerHashMap = new HashMap<String, StagPlayer>();

    public StagModelEntities(String entityFilename){
        try {
            FileReader reader = new FileReader(entityFilename);
            Parser parser = new Parser();
            parser.parse(reader);
            ArrayList<Graph> graphs = parser.getGraphs();
            ArrayList<Graph> subGraphs = graphs.get(0).getSubgraphs();

            for(Graph g : subGraphs){

                ArrayList<Graph> subGraphs1 = g.getSubgraphs();
                for (Graph g1 : subGraphs1){
                    ArrayList<Node> nodesLoc = g1.getNodes(false);
                    Node nLoc = nodesLoc.get(0);
                    location = new Location(nLoc.getId().getId(),g1.getId().getId());
                    initialLocation();

                    ArrayList<Graph> subGraphs2 = g1.getSubgraphs();
                    for (Graph g2 : subGraphs2) {
                        ArrayList<Node> nodesEnt = g2.getNodes(false);
                        initialSubgraph(g2,nodesEnt);
                    }
                }

                // for path
                ArrayList<Edge> edges = g.getEdges();
                for (Edge e : edges){
                    pathtoLocation = new PathtoLocation(e.getSource().getNode().getId().getId(), e.getTarget().getNode().getId().getId());
                    initialpathtoLocation();
                }
            }

        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe);
        } catch (com.alexmerz.graphviz.ParseException pe) {
            System.out.println(pe);
        }
    }

    private void initialLocation(){
        String locationName =  location.getName();
        locationMap.put(locationName, location);
        locations.add(location);
    }

    private void initialSubgraph(Graph g2, ArrayList<Node> nodesEnt){
        for (Node nEnt : nodesEnt) {
            switch (g2.getId().getId()) {
                case "furniture":
                    Furniture furniture = new Furniture(nEnt.getId().getId(), nEnt.getAttribute("description"));
                    String furnitureName = furniture.getName();
                    location.addFurniture(furnitureName, furniture);
                    break;

                case "artefacts":
                    Artefact artefact = new Artefact(nEnt.getId().getId(), nEnt.getAttribute("description"));
                    String artefactName = artefact.getName();
                    location.addArtefact(artefactName, artefact);
                    break;

                case "characters":
                    Character character = new Character(nEnt.getId().getId(), nEnt.getAttribute("description"));
                    String characterName = character.getName();
                    location.addCharacter(characterName, character);
                    break;
            }
        }
    }

    private void initialpathtoLocation(){
        String FromLocationName = pathtoLocation.getFromLocation();
        String ToLocationName =pathtoLocation.getToLocation();
        locationMap.get(FromLocationName).addPathtoLocation(ToLocationName);
    }

    public HashMap<String, Location> getLocationMap() {
        return locationMap;
    }

    public ArrayList<Location> getLocationList(){
        return locations;
    }

    public void addPlayer(String playerName,StagPlayer stagplayer) {
        playerHashMap.put(playerName,stagplayer);
    }

    public boolean containPlayer(String playerName){
        return playerHashMap.containsKey(playerName);
    }

    public StagPlayer getPlayer(String playerName){
        return playerHashMap.get(playerName);
    }

    public void removePlayer(String playerName){
        playerHashMap.remove(playerName);
    }
}