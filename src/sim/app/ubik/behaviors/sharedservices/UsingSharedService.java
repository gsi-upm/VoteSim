/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sim.app.ubik.behaviors.sharedservices;

import es.upm.dit.gsi.voting.AcceptableForAllMethod;
import es.upm.dit.gsi.voting.ApprovalVotingMethod;
import es.upm.dit.gsi.voting.BordaVotingMethod;
import es.upm.dit.gsi.voting.CumulativeVotingMethod;
import es.upm.dit.gsi.voting.FirstArrivalMethod;
import es.upm.dit.gsi.voting.PluralityVotingMethod;
import es.upm.dit.gsi.voting.RangeVotingMethod;
import es.upm.dit.gsi.voting.VotingMethod;
import sim.app.ubik.behaviors.PositionTools;
import sim.app.ubik.behaviors.SimpleState;
import sim.app.ubik.building.rooms.Room;
import sim.app.ubik.domoticDevices.SharedService;
import sim.app.ubik.people.Person;
import sim.engine.SimState;

/**
 *
 * @author Emilio Serrano, emilioserra@um.es
 */
public class UsingSharedService extends SimpleState {  
    
     //protected static final String sharedServicesNames[] = {"TV1","TV2","TV3"};
     protected SharedService css;// current shared service
    protected UserInterface user;
    public static int selectionCode=0;//código de selección de servicio
    
    protected static int codeOfNegotiation = 1;
    public static int codeOfSatisfactionFunction = 0;
    protected static boolean echo = true;
             
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
            //user.getNegotiation().negotiate(css);
            this.vote(css);
    }

    private void startUsingService() {
        css=suggestSharedService(); 
        //comrpobar si está en la misma habitación, si no, null
        Room r=PositionTools.getRoom(personImplementingAutomaton);        
        //compruebo que uesté en la habitación
        if(css!=null && !css.isUser(user) && SharedService.isInRoom(css, personImplementingAutomaton)) {
            css.addUser(this.user);   
            //user.getNegotiation().negotiate(css);
            this.vote(css);
        }
    }
    
 public void vote(SharedService css) {
    	
    	VotingMethod vm = null;
    	
        if (css.getUsers().isEmpty()) {
            return;
        }

        if (css.getUsers().size() == 1) {
            css.setConfiguration(user.getNegotiation().getNextPreference(user, css, 0));
            return;
        }
        
        System.out.println("Code of Negotiation="+codeOfNegotiation);
        
        if (codeOfNegotiation == 0) 
        	vm = new FirstArrivalMethod(css);
        
        else if (codeOfNegotiation == 1) 
        	vm = new RangeVotingMethod(css);
        
        else if (codeOfNegotiation == 2) 
        	vm = new AcceptableForAllMethod(css);
        
        else if (codeOfNegotiation == 3) 
        	vm = new PluralityVotingMethod(css);
        
        else if (codeOfNegotiation == 4) 
        	vm = new CumulativeVotingMethod(css);
        
        else if (codeOfNegotiation == 5) 
        	vm = new ApprovalVotingMethod(css);
        
        else if (codeOfNegotiation == 6) 
        	vm = new BordaVotingMethod(css);
        
        System.out.println(vm.userPreferencesToString());
        css.setConfiguration(vm.getSelectedConfiguration());
        
        /*
        if(vm.isDraw())
        	System.out.println("Ha habido empate de" +vm.getDrawCount()+ "preferencias");
        else
        	System.out.println("No hay empate");
        	
        	*/

     
    }
 
	 public static void setCodeOfSatisfactionFuction(int c) {
	     UsingSharedService.codeOfSatisfactionFunction = c;
	 }
	
	   public static int getCodeOfSatisfactionFuction() {
	     return UsingSharedService.codeOfSatisfactionFunction;
	 }
	 
	     
	     
	 public static void setCodeOfNegotiation(int c) {
		 UsingSharedService.codeOfNegotiation = c;
	 }
	 
	  public static int getCodeOfNegotiation() {
	    return codeOfNegotiation;
	 }

    
    
        

    

    
    
}