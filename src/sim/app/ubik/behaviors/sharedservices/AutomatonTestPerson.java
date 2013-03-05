package sim.app.ubik.behaviors.sharedservices;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Color;
import java.util.ArrayList;
import sim.app.ubik.behaviors.Automaton;
import sim.app.ubik.behaviors.DoNothing;
import sim.app.ubik.behaviors.Move;
import sim.app.ubik.behaviors.PositionTools;
import sim.app.ubik.people.Person;
import sim.app.ubik.people.TestPerson;
import sim.engine.SimState;
import sim.util.Int2D;

/**
 *
 * @author Emilio Serrano, emilioserra@um.es
 */
public class AutomatonTestPerson extends Automaton {
    
 
         
    protected static final float CHANGEOFROOM= (float) 0.05; //probabilidad de salir de sitio inicial
    protected static final int MAXSTAY=250;//estancia máxima (antes de dejar sitio inicial y en sitios a los que se va)    
    protected TestPerson p =  (TestPerson) personImplementingAutomaton; 
    
    
  
    

    
   public AutomatonTestPerson(Person personImplementingAutomaton) {
        super(personImplementingAutomaton);
        
    }
   
    @Override
    public Automaton getDefaultState(SimState simState) {
        return new DoNothing(p,0,1,"stayAtRoom");
    }

    
 
      
    @Override
    public ArrayList<Automaton> createNewTransitions(SimState simState) {
      ArrayList<Automaton> r =   new ArrayList<Automaton>();            
      //transiciones al margen de emergencias
      if(!this.isTransitionPlanned("goHall") && p.getUbik().random.nextFloat()<CHANGEOFROOM){         
        addTransitionsToGoToHall(r);
      }

      return r;
    }

    
    /**
     * Comportamiento por defecto al margen de emergencias, ir al hall (posición aleatoria), esperar, volver a habitación, esperar
     * @param r Lista de transiciones a la que añadir
     */
    private void addTransitionsToGoToHall(  ArrayList<Automaton> r) {
          Move.FINISHIFPATHNOTGENERATED=true;
          Int2D destiny=  PositionTools.getRandomPositionInRoom(p, PositionTools.getRoom(p, "HALL"));
          r.add(new Move(p,0,-1,"goHall",destiny.x,destiny.y));//ir
          r.add(new UsingSharedService(p,0,p.getUbik().random.nextInt(MAXSTAY),"usingService"));//esperar, nombre "go" para controlar nuevas transiciones                   
          destiny=  PositionTools.getRandomPositionInRoom(p, p.getInitialPosition());
          r.add(new Move(this.personImplementingAutomaton,0,-1,"goHall", destiny.x, destiny.y));
          r.add(new DoNothing(p,0,p.getUbik().random.nextInt(MAXSTAY),"goHall"));//esperar en sitio, nombre "go" para controlar nuevas transiciones                    
    }

       
 
}
