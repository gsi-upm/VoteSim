/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ubiksimdist;

import sim.app.ubik.Ubik;
import sim.app.ubik.UbikSimLauncher;
import sim.app.ubik.UbikSimWithUI;
import sim.app.ubik.behaviors.Automaton;
import sim.app.ubik.behaviors.sharedservices.MonitorService;
import sim.app.ubik.behaviors.sharedservices.MonitorServiceGUI;
import sim.app.ubik.behaviors.sharedservices.Negotiation;
import sim.app.ubik.people.PersonHandler;
import sim.app.ubik.people.TestPerson;
import sim.app.ubik.utils.GenericLogger;
import sim.display.Console;
import sim.engine.SimState;
import sim.engine.Steppable;

/**
 *
 * @author Emilio Serrano, emilioserra@um.es
 */
public class SharedServicesSim extends Ubik {
   
    public static int maxTimeForExecution=2000;
    public MonitorService ms;
    
    /**
     * @param args the command line arguments
     */
    public SharedServicesSim(long seed, int timeForExperiment) {
        super(seed);
        MonitorService.momentOfConflictToStop=timeForExperiment;
                
    }
    
    public SharedServicesSim(long seed) {
        super(seed);
        
    }
    
  
    public SharedServicesSim() {         
           super();
           setSeed(getSeedFromFile());         
    }
  
    @Override
    public void start() {
              
        super.start();      
        ms= new MonitorService(this); 
        Automaton.setEcho(false);
        //Negotiation.setEcho(false);
        //añadir personas
        PersonHandler ph=  getBuilding().getFloor(0).getPersonHandler();
        ph.addPersons(26, true, ph.getPersons().get(0));
        ph.changeNameOfAgents("a");
    }
    
    


/**
 * se le pasa semilla (long), tiempo de ejecución (entero)*
 * @param args 
 */
    public static void main(String []args) {
       
       SharedServicesSim state = new SharedServicesSim(System.currentTimeMillis());
       state.start();
        do{
                if (!state.schedule.step(state)) break;
        }while(state.schedule.getSteps() < maxTimeForExecution);//
        state.finish();     
      
     
    }
    

    
   
    
    
    


    
    
}
