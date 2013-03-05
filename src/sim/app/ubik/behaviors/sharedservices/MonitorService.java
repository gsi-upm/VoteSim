/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sim.app.ubik.behaviors.sharedservices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import sim.app.ubik.Ubik;
import sim.app.ubik.domoticDevices.SharedService;
import sim.app.ubik.people.PersonHandler;
import sim.app.ubik.utils.GenericLogger;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.MutableDouble;

/**
 *
 * @author Emilio Serrano, emilioserra@um.es
 */
public class MonitorService implements Steppable, Stoppable {
      protected Ubik ubik;
    
    public  static int momentOfConflictToStop=-1;        
    protected int[] stepsWithoutHavingAWantedService;
    public GenericLogger gl;

    
    
    public MutableDouble satisfactionPerUsers = new MutableDouble(0);    
    public MutableDouble satisfactionAccumulated= new MutableDouble(0);
    public MutableDouble usersWithAcceptableConfigurations= new MutableDouble(0);
    public MutableDouble usersWithAcceptableConfigurationsAccumulated= new MutableDouble(0);
    public MutableDouble momentsOfConflict = new MutableDouble(0);    
    public MutableDouble maxWithoutService = new MutableDouble(0); 
    public String text="";//texto a mostrar en ventana
    public int numberOfUsersUsingServices;
    
    
    public MonitorService(Ubik ubik) {
        this.ubik=ubik;
        ubik.schedule.scheduleRepeating(this);
        String[] logHeadings={"spu","uwac","mws"};
        gl= new GenericLogger(logHeadings);
      
 
    }

    public void step(SimState ss) {

         //actualizar lista de momentos de conflicto y contadores de satisfaccion
        float satisfactionCounter = 0;
        int momentOfConflict=0;//flag, 1 menas that there have been a conflict
        
        int usersWithAcceptableConfigurationsCounter=0;
        int numberOfUsers = 0;
        
         String line = System.getProperty("line.separator");
        List<SharedService> listss = SharedService.getServices(ubik, 0);
        for (SharedService serv : listss) {
            ArrayList<UserInterface> users = serv.getUsers();
            if (users.size()>=2) {//si no hay al menos dos usuarios no hay conflicto
                text="";
                //actualizar datos del monitor
                momentOfConflict=1;
                text += serv.getName() + " shared, current configuration: " + serv.getCurrentConfiguration() + ", configurations: " + Arrays.toString( serv.getConfigurations()) + line;
                text+= "Users: " + line;
                numberOfUsers += users.size();
                for (UserInterface ui : users) {
                    text += "\t" + ui.getName();                
                    text += ", o.pref. : " + ui.getNegotiation().orderedPreferencesToString(serv) + line;
                    satisfactionCounter += ui.getNegotiation().getSatisfaction(serv);
                    if(ui.getNegotiation().isAcceptable(serv.getCurrentConfiguration(), serv,false)) usersWithAcceptableConfigurationsCounter++; 
                }
                numberOfUsersUsingServices=numberOfUsers;
                text += line + line;
                
            }
        }
        
        if(momentOfConflict==1 ){
         
           
           satisfactionPerUsers.val =   satisfactionCounter / ((double) numberOfUsers);  
         
           satisfactionAccumulated.val = satisfactionAccumulated.val +  satisfactionPerUsers.val;
            
           usersWithAcceptableConfigurations.val =  usersWithAcceptableConfigurationsCounter / ((double) numberOfUsers);
           usersWithAcceptableConfigurationsAccumulated.val +=  usersWithAcceptableConfigurations.val ;                       
            
           //actualizar el contador de tiempo sin servicio querido
           updateStepsWithoutHavingAWantedService();
           momentsOfConflict.val++;
           
           double toLog[]= {this.satisfactionAccumulated.val, this.usersWithAcceptableConfigurationsAccumulated.val, this.maxWithoutService.val};
           gl.addStep(toLog);
         
           if(momentsOfConflict.val==this.momentOfConflictToStop){    
               System.out.println("End!, acc. satisfaction:" +   usersWithAcceptableConfigurationsAccumulated.val);            
               ubik.kill();
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
