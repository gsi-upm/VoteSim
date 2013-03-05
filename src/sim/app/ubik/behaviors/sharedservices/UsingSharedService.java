/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sim.app.ubik.behaviors.sharedservices;

import java.awt.Color;
import java.util.List;
import sim.app.ubik.behaviors.DoNothing;
import sim.app.ubik.behaviors.PositionTools;
import sim.app.ubik.behaviors.SimpleState;
import sim.app.ubik.building.rooms.Room;
import sim.app.ubik.domoticDevices.SharedService;
import sim.app.ubik.domoticDevices.TV;
import sim.app.ubik.people.Person;
import sim.engine.SimState;
import sim.util.Int2D;

/**
 *
 * @author Emilio Serrano, emilioserra@um.es
 */
public class UsingSharedService extends SimpleState {  
    
     //protected static final String sharedServicesNames[] = {"TV1","TV2","TV3"};
     protected SharedService css;// current shared service
    protected UserInterface user;
    public static int selectionCode=0;//código de selección de servicio
             
     public UsingSharedService(Person personImplementingAutomaton, int priority, int duration, String name) {
         super(personImplementingAutomaton, priority, duration, name);     
         user=(UserInterface) this.personImplementingAutomaton;  
     }

    @Override
    public void nextState(SimState ss) {                           
        /* Si css no es null, este no es el primer paso.Entonces,
         * si no me gusta el servicio y no es el recomendado, lo reinicio para sugerencia en siguinte paso*/
        if(css!=null && selectionCode>0 && !user.getNegotiation().isWanted(css.getCurrentConfiguration(), css) && !css.equals(suggestSharedService())){           
            stopUsingService(); 
            startUsingService();         
            
        }
        
        //si es el primer paso, coger servicio sugerido
        if(css==null) startUsingService();       
        
        //si no se consiguio un servicio se marca el fin
        if(css==null){
            this.setFinished(true);
            return;
        }      
        
        
      
    }
    

    
             
    /**
     * Desregistrar usuario tras uso. Se repite negociación con los que quedan
     * Se llama a negociación para que renegocien los agentes que quedan.
     * @param state
     * @return 
     */
    @Override
     public boolean isFinished(SimState state) {
        boolean r = super.isFinished(state);
        if(r && css!=null){            
           stopUsingService();
        }
        return r;
    }

    private SharedService suggestSharedService() {
         if(selectionCode==0) return SharedService.closestSharedService(this.personImplementingAutomaton,true);
         if(selectionCode==1) return SharedService.serviceWithMoreCommonWantedConfigurations((UserInterface) this.personImplementingAutomaton);
         if(selectionCode==2)  return  (new Clustering(this.personImplementingAutomaton.getUbik())).getRecommendation(user);                            
         return null;
    }

    private void stopUsingService() {
            css.removeUser(user);
            user.getNegotiation().negotiate(css);  
    }

    private void startUsingService() {
        css=suggestSharedService(); 
        //comrpobar si está en la misma habitación, si no, null
        Room r=PositionTools.getRoom(personImplementingAutomaton);        
        //compruebo que uesté en la habitación
        if(css!=null && !css.isUser(user) && SharedService.isInRoom(css, personImplementingAutomaton)) {
            css.addUser(this.user);   
            user.getNegotiation().negotiate(css);            
        }
    }
    
    
        

    

    
    
}