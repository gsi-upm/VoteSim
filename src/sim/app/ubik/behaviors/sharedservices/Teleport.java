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
 */package sim.app.ubik.behaviors.sharedservices;

import java.util.ArrayList;
import sim.app.ubik.behaviors.Automaton;
import sim.app.ubik.behaviors.DoNothing;
import sim.app.ubik.behaviors.PositionTools;
import sim.app.ubik.building.rooms.Room;
import sim.app.ubik.people.TestPerson;
import sim.engine.SimState;
import sim.util.Int2D;

/**
 * Teleport agent to room in rando position if it is free. It waits until there is some position free
 */
public class Teleport extends Automaton{
    Room r;
    
    /**
     * 
     * @param p
     * @param priority
     * @param duration
     * @param name
     * @param r Room to be teleported in random free position
     */
    public Teleport(TestPerson personImplementingAutomaton, int priority, int duration, String name, Room r) {
           super(personImplementingAutomaton,priority,duration,name);
           this.r=r;
    }

    @Override
    public Automaton getDefaultState(SimState ss) {
         return (new DoNothing(personImplementingAutomaton,0,1,"wait for position"));
    }

    @Override
    public ArrayList<Automaton> createNewTransitions(SimState ss) {
        for (int i = 0; i < 10; i++) {
               Int2D destiny=  PositionTools.getRandomPositionInRoom(personImplementingAutomaton, r);
               if(!PositionTools.isObstacle(personImplementingAutomaton, destiny.x, destiny.y)){
                   PositionTools.getOutOfSpace(personImplementingAutomaton);
                   PositionTools.putInSpace(personImplementingAutomaton, destiny.x, destiny.y);
                   this.setFinished(true);
                   return null;
               }
        }
        return null;
    }

}

