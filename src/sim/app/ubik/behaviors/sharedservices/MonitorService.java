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
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import sim.app.ubik.Ubik;
import sim.app.ubik.domoticDevices.SharedService;
import sim.app.ubik.people.PersonHandler;
import sim.app.ubik.utils.GenericLogger;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.MutableDouble;

public class MonitorService implements Steppable, Stoppable {
      protected Ubik ubik;
    
    public  static int momentOfConflictToStop=-1;        
    protected int[] stepsWithoutHavingAWantedService;
    public GenericLogger gl;
    
    
    public MutableDouble satisfactionPerUsers = new MutableDouble(0);   
    public MutableDouble satisfactionAccumulated= new MutableDouble(0);
    public double globalSatisfaction = 0;
    public double globalSatisfactionAccumulated;
    private static int step;
    public MutableDouble usersWithAcceptableConfigurations= new MutableDouble(0);
    public MutableDouble usersWithAcceptableConfigurationsAccumulated= new MutableDouble(0);
    public MutableDouble momentsOfConflict = new MutableDouble(0);
    public MutableDouble servicesWithConflict = new MutableDouble(0);    
    public MutableDouble maxWithoutService = new MutableDouble(0); 
    public String text="";//texto a mostrar en ventana
    public int numberOfUsersUsingServices;
    
    Logger log = Logger.getLogger("MonitorService");
    
    
    public MonitorService(Ubik ubik) {
        this.ubik=ubik;
        ubik.schedule.scheduleRepeating(this);
        String[] logHeadings={"spu","uwac","mws"};
        gl= new GenericLogger(logHeadings);
      
 
    }


    public void step(SimState ss) {

         //actualizar lista de momentos de conflicto y contadores de satisfaccion
        double satisfactionCounter = 0;
        int momentOfConflict=0;//flag, 1 menas that there have been a conflict
        globalSatisfaction = 0;
        servicesWithConflict.val = 0;
        step++;
        
        int usersWithAcceptableConfigurationsCounter=0;
        int numberOfUsers = 0;
        
         String line = System.getProperty("line.separator");
        List<SharedService> listss = SharedService.getServices(ubik, 0);
        text="";
        for (SharedService serv : listss) {
            ArrayList<UserInterface> users = serv.getUsers();
            if (users.size()>=2) {//si no hay al menos dos usuarios no hay conflicto
            	
            	// Incrementamos el numero de servicios en conflicto
            	servicesWithConflict.val++;
            	
                //actualizar datos del monitor
                momentOfConflict=1;
                text += serv.getName() + " shared, current configuration: " + serv.getCurrentConfiguration() + ", configurations: " + Arrays.toString( serv.getConfigurations()) + line;
                text+= "Users: " + line;
                numberOfUsers += users.size();
                for (UserInterface ui : users) {
                    text += "\t" + ui.getName();                
                    text += ", o.pref. : " + ui.getNegotiation().orderedPreferencesToString(serv) + line;
                    //satisfactionCounter += ui.getNegotiation().getUserSatisfaction(serv);
                    satisfactionCounter += serv.getUserSatisfaction(ui);
                    if(ui.getNegotiation().isAcceptable(serv.getCurrentConfiguration(), serv,false)) usersWithAcceptableConfigurationsCounter++; 
                }
                
                numberOfUsersUsingServices=numberOfUsers;
                text += line + line;
                //System.out.println("Max satisfaction: "+Negotiation.getMaxSatisfaction(serv)+"/"+(serv.getUsers().size()*10)+" for service "+serv.getName());
                
                /* Se suma la satisfaccion que aporta cada servicio en conflicto */
                globalSatisfaction += serv.getBoundedServiceSatisfaction();    
            }
            
            
        }

        
        /* Ya se han terminado de ver todos los sercvicios en conclicto que ha habido */
        
        if(momentOfConflict==1 ){
        	
            //actualizar el contador de tiempo sin servicio querido
            updateStepsWithoutHavingAWantedService();
            momentsOfConflict.val++;
        	
        	/* Se calcula la media de satisfaccion dividiendo entre el numero de servicios en conflicto que han contribuido */
        	globalSatisfaction = globalSatisfaction/servicesWithConflict.val;
        	
        	
        	/* Se actualiza la satisfaccion acumulada */
	        globalSatisfactionAccumulated = satisfactionAccumulated.val*(momentsOfConflict.val-1) + globalSatisfaction;
        	
           satisfactionPerUsers.val  = globalSatisfaction;
         
           /* Se actualiza el valor de la acumulada, que para que este entre 0 y 1 se divide entre el numero de momentos en conflicto */
           satisfactionAccumulated.val = globalSatisfactionAccumulated/momentsOfConflict.val;
           
           
           usersWithAcceptableConfigurations.val =  usersWithAcceptableConfigurationsCounter / ((double) numberOfUsers);
           usersWithAcceptableConfigurationsAccumulated.val +=  usersWithAcceptableConfigurations.val ;    
           
           log.finest("Step: "+step);
           log.finest("SAS: "+globalSatisfaction);
           log.finest("Accumulated SAS: "+globalSatisfactionAccumulated);
            

           
           double toLog[]= {this.satisfactionAccumulated.val, this.usersWithAcceptableConfigurationsAccumulated.val, this.maxWithoutService.val};
           gl.addStep(toLog);
         
           if(momentsOfConflict.val==this.momentOfConflictToStop){    
               System.out.println("End!, acc. satisfaction:" +   usersWithAcceptableConfigurationsAccumulated.val);            
               ubik.kill();
            }
           
           if(Preferences.echo) {   
        	
   	        System.out.println("GlobalSatisfactionAcummulated sin dividir: "+globalSatisfactionAccumulated);
   	        System.out.println("Moments of conflict: "+momentsOfConflict.val);
   	        System.out.println("Servicios en conflicto: "+servicesWithConflict.val);
   	        System.out.println("Global dividida: "+satisfactionAccumulated.val);
   	        System.out.println("");
           }
            
        }
        
       
    }
    
    
      private void updateStepsWithoutHavingAWantedService() {
         PersonHandler   ph=ubik.getBuilding().getFloor(0).getPersonHandler();
        if(stepsWithoutHavingAWantedService==null || stepsWithoutHavingAWantedService.length!=ph.getPersons().size()){
            stepsWithoutHavingAWantedService=new int[ph.getPersons().size()];
        }
        
     
        for (SharedService serv :  SharedService.getServices(ubik, 0)) {
            ArrayList<UserInterface> users = serv.getUsers();
            if (users.size()<2) continue; //sólo se tiene en cuenta is al menos hay dos personas
            for(UserInterface ui: users){
                if(ui.getNegotiation().isWanted(serv.getCurrentConfiguration(), serv)) ui.getNegotiation().stepsWithoutWantedService=0;
                else{
                   ui.getNegotiation().stepsWithoutWantedService++;
                   if(maxWithoutService.val<ui.getNegotiation().stepsWithoutWantedService){
                       maxWithoutService.val= ui.getNegotiation().stepsWithoutWantedService;
                   }
                }
                
            }
        }
       
    }

    public void stop() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
