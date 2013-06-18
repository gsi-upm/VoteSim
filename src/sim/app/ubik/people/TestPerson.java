/*
* 
* 
* This file is part of VoteSim. VoteSim is a UbikSim library. 
* 
* VoteSim has been developed by members of the research Group on 
* Intelligent Systems [GSI] (Grupo de Sistemas Inteligentes), 
* acknowledged group by the  Technical University of Madrid [UPM] 
* (Universidad Politécnica de Madrid) 
* 
* Authors:
* Mercedes Garijo
* Carlos A. Iglesias
* Pablo Moncada
* Emilio Serrano
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
package sim.app.ubik.people;

import sim.app.ubik.Ubik;
import sim.app.ubik.behaviors.sharedservices.Preferences;
import sim.app.ubik.behaviors.sharedservices.UserInterface;
import sim.app.ubik.building.SpaceArea;
import sim.app.ubik.building.connectionSpace.Door;
import sim.app.ubik.building.rooms.Room;
import sim.engine.SimState;
import sim.util.Int2D;
import ubik3d.model.HomePieceOfFurniture;

public class TestPerson extends Person implements UserInterface {


   protected Room initialPosition=null; //
  
   protected Preferences neg;

    public TestPerson(int floor, HomePieceOfFurniture w, Ubik ubik) { 
      
        super(floor, w, ubik);        
        this.speed=0.5;            
        neg = new Preferences(this);

    }
    @Override
    public void step(SimState state) {
 
        if(initialPosition==null) {
            SpaceArea sa = (SpaceArea) ubik.getBuilding().getFloor(0).getSpaceAreaHandler().getSpaceArea(position.x, position.y);
            if(sa instanceof Room) {
                initialPosition = (Room) sa;
            } else if(sa instanceof Door) {
                Door door = (Door) sa;
                Int2D point = door.getAccessPoints()[0];
                initialPosition = (Room) ubik.getBuilding().getFloor(0).getSpaceAreaHandler().getSpaceArea(point.x,point.y);
            }
        }
        
        
        
        
        super.step(state);
        if(keyControlPerson != null) {
            return;
        }
          if(automaton==null) {
         
             automaton = new sim.app.ubik.behaviors.sharedservices.UserAgent(this);
        }
        automaton.nextState(state);
    }


    public String toString(){
        return name;
    }

   public Room getInitialPosition() {
        return initialPosition;
    }

    public void setInitialPosition(Room despacho) {
        this.initialPosition = despacho;
    }




    void considerToChangingBehavior(SimState state) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
   


    public Preferences getNegotiation() {
        return neg;
    }

}// Fin clase Teacher

