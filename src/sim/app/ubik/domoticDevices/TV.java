/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.app.ubik.domoticDevices;

import sim.app.ubik.Ubik;
import sim.engine.SimState;
import ubik3d.model.HomePieceOfFurniture;


/**
 * @author Emilio Serrano, emilioserra@um.es
 */
public class TV extends SharedService {

    protected static final String[] genres= {"sports","sitcom","documentary", "soap", "cartoon", "news", "reality", "quiz", "movie"};
    
    
    public TV(int floor, HomePieceOfFurniture device3DModel, Ubik ubik) {
        super(floor, device3DModel, ubik);
        
    }
    
  

    public void step(SimState ss) {
       
    }

    public void stop() {
       
    }

    @Override
    public String[] getConfigurations() {
        return genres;
    }


 


    

}
