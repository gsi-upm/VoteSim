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

package sim.app.ubik.behaviors.sharedservices;


import java.util.ArrayList;

import sim.app.ubik.behaviors.Automaton;
import sim.app.ubik.behaviors.DoNothing;
import sim.app.ubik.behaviors.Move;
import sim.app.ubik.behaviors.PositionTools;
import sim.app.ubik.people.Person;
import sim.app.ubik.people.TestPerson;
import sim.engine.SimState;
import sim.util.Int2D;

public class UserAgent extends Automaton {
    
 
         
    protected static final float CHANGEOFROOM= (float) 0.05; //probabilidad de salir de sitio inicial
    protected static final int MAXSTAY=100;//estancia máxima (antes de dejar sitio inicial y en sitios a los que se va)    
    protected TestPerson p =  (TestPerson) personImplementingAutomaton; 
    
    
    
   public UserAgent(Person personImplementingAutomaton) {
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
      if(!this.isTransitionPlanned("goHall") && !this.isTransitionPlanned("goBackRoom") && p.getUbik().random.nextFloat()<CHANGEOFROOM){         
        addTransitionsToGoToHallTeleporting(r);
      }

      return r;
    }

    
        /**
     * Comportamiento por defecto al margen de emergencias, ir al hall (posición aleatoria), esperar, volver a habitación, esperar
     * @param r Lista de transiciones a la que añadir
     */
    private void addTransitionsToGoToHallTeleporting(  ArrayList<Automaton> r) {
               
          r.add(new DoNothing(p,0,p.getUbik().random.nextInt(MAXSTAY),"goHall"));//esperar en sitio, nombre "go" para controlar nuevas transiciones         
          r.add(new Teleport(p,0,-1,"goHall",PositionTools.getRoom(p, "HALL")));//ir
          r.add(new AgreementServiceAgent(p,0,p.getUbik().random.nextInt(MAXSTAY),"usingService"));//esperar, nombre "go" para controlar nuevas transiciones                           
          r.add(new Teleport(p,0,-1,"goBackRoom", p.getInitialPosition()));
                         
    }
    
    
    /**
     * Comportamiento por defecto al margen de emergencias, ir al hall (posición aleatoria), esperar, volver a habitación, esperar
     * @param r Lista de transiciones a la que añadir
     * @deprecated Some problems generating route, use teleporting
     */
    private void addTransitionsToGoToHall(  ArrayList<Automaton> r) {
          Move.FINISHIFPATHNOTGENERATED=true;          
          r.add(new DoNothing(p,0,p.getUbik().random.nextInt(MAXSTAY),"goHall"));//esperar en sitio, nombre "go" para controlar nuevas transiciones   
          Int2D destiny=  PositionTools.getRandomPositionInRoom(p, PositionTools.getRoom(p, "HALL"));
          r.add(new Move(p,0,-1,"goHall",destiny.x,destiny.y));//ir
          r.add(new AgreementServiceAgent(p,0,p.getUbik().random.nextInt(MAXSTAY),"usingService"));//esperar, nombre "go" para controlar nuevas transiciones                   
          destiny=  PositionTools.getRandomPositionInRoom(p, p.getInitialPosition());
          r.add(new Move(this.personImplementingAutomaton,0,-1,"goHall", destiny.x, destiny.y));
                         
    }

       
 
}
