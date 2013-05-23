/*
* 
* 
* This file is part of VoteSim. VoteSim is a UbikSim library. 
* 
* VoteSim has been developed by members of the research Group on 
* Intelligent Systems [GSI] (Grupo de Sistemas Inteligentes), 
* acknowledged group by the Universidad Politécnica de Madrid [UPM] 
* (Technical University of Madrid) 
* 
* Authors:
* Emilio Serrano
* Pablo Moncada
* Mercedes Garijo
* Carlos A. Iglesias
* 
* Contact: 
* http://www.gsi.dit.upm.es/;
* 
* 
* 
* VoteSim, as UbikSim, is free software: 
* you can redistribute it and/or modify it under the terms of the GNU 
* General Public License as published by the Free Software Foundation, 
* either version 3 of the License, or (at your option) any later version. 
*
* 
* VoteSim is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with VoteSim. If not, see <http://www.gnu.org/licenses/>
 */

package sim.app.ubik.domoticDevices;

import sim.app.ubik.Ubik;
import sim.engine.SimState;
import ubik3d.model.HomePieceOfFurniture;


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
