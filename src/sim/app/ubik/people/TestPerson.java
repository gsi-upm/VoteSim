package sim.app.ubik.people;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import sim.app.ubik.Ubik;
import sim.app.ubik.behaviors.PositionTools;

import sim.app.ubik.behaviors.sharedservices.Negotiation;
import sim.app.ubik.behaviors.sharedservices.UserInterface;
import sim.app.ubik.building.SpaceArea;
import sim.app.ubik.building.connectionSpace.Door;

import sim.app.ubik.building.rooms.Room;
import sim.app.ubik.domoticDevices.SharedService;
import sim.app.ubik.domoticDevices.TV;
import sim.engine.SimState;
import sim.util.Int2D;
import ubik3d.model.HomePieceOfFurniture;

/**
 *
  * @author Juan A. Botía, Pablo Campillo, Francisco Campuzano, and Emilio Serrano
 */
public class TestPerson extends Person implements UserInterface {


   protected Room initialPosition=null; //
  
   protected Negotiation neg;

    public TestPerson(int floor, HomePieceOfFurniture w, Ubik ubik) { 
      
        super(floor, w, ubik);        
        this.speed=0.5;            
        neg = new Negotiation(this);

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
         
             automaton = new sim.app.ubik.behaviors.sharedservices.AutomatonTestPerson(this);
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
    
   


    public Negotiation getNegotiation() {
        return neg;
    }

}// Fin clase Teacher

