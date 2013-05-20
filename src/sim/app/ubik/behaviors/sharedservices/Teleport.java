/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sim.app.ubik.behaviors.sharedservices;

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
 * @author Emilio Serrano, Ph.d.; eserrano@gsi.dit.upm.es
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

