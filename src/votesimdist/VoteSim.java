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

package votesimdist;

import sim.app.ubik.Ubik;
import sim.app.ubik.behaviors.Automaton;
import sim.app.ubik.behaviors.sharedservices.MonitorService;
import sim.app.ubik.people.PersonHandler;


public class VoteSim extends Ubik {
   
    public static int maxTimeForExecution = 10000;
    public MonitorService ms;
    
    /**
     * @param args the command line arguments
     */
    public VoteSim(long seed, int timeForExperiment) {
        super(seed);
        MonitorService.momentOfConflictToStop=timeForExperiment;
                
    }
    
    public VoteSim(long seed) {
        super(seed);
        
    }
    
  
    public VoteSim() {         
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
       
       VoteSim state = new VoteSim(System.currentTimeMillis());
       state.start();
       
        do{
                if (!state.schedule.step(state)) break;
        }while(state.schedule.getSteps() < maxTimeForExecution);//
        state.finish();     
      
     
    }
    

    
   
    
    
    


    
    
}