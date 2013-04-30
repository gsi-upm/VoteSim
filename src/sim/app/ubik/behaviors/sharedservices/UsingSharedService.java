/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sim.app.ubik.behaviors.sharedservices;

import java.util.ArrayList;
import java.util.HashMap;

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
import sim.util.MutableInt2D;

/**
 *
 * @author Emilio Serrano, emilioserra@um.es
 */
public class UsingSharedService extends SimpleState {  
    
     //protected static final String sharedServicesNames[] = {"TV1","TV2","TV3"};
     protected SharedService css;// current shared service
    protected UserInterface user;
    public static int selectionCode=0;//código de selección de servicio
    
    protected static int codeOfNegotiation = 0;
    public static int codeOfSatisfactionFunction = 0;
    public static boolean enableNegotationByWeight = true;
    
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
         if(selectionCode==0) return Preselection.closestSharedService(this.personImplementingAutomaton,true);
         if(selectionCode==1) return Preselection.serviceWithMoreCommonWantedConfigurations((UserInterface) this.personImplementingAutomaton);
         if(selectionCode==2)  return  (new Clustering(this.personImplementingAutomaton.getUbik())).getRecommendation(user);   
         if(selectionCode==3)  return  Preselection.euclideanDistance((UserInterface) this.personImplementingAutomaton); 
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
        if(css!=null && !css.isUser(user) && Preselection.isInRoom(css, personImplementingAutomaton)) {
            css.addUser(this.user);   
            //user.getNegotiation().negotiate(css);
            if(enableNegotationByWeight == true)
            	selectConfigurationByNegotiationWeight(css);
            else
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
	  
	  
	  
	  private void selectConfigurationByNegotiationWeight(SharedService css) {
	        ArrayList<MutableInt2D> orderedVotes = Preferences.orderPreferences(Preferences.votingConfigurations(css));
	        for (MutableInt2D m : orderedVotes) {
	            HashMap<UserInterface, Integer> agentsToBeConvinced = this.getWeightToConvince(css, css.getConfigurations()[m.x]);
	            if (agentsToBeConvinced == null) {
	                continue;
	            } else {

	                if (echo) {
	                    System.out.println(css.getName());
	                    System.out.println("\t Before argumenting:" + this.weightToString(css));
	                    if (agentsToBeConvinced.isEmpty()) {
	                        System.out.println("\t No change of weights");
	                    }
	                }

	                css.setConfiguration(m.x);
	                giveWeightToConvince(css, css.getConfigurations()[m.x],  agentsToBeConvinced);



	                if (echo) {
	                    System.out.println("\t After argumenting:" + this.weightToString(css));
	                    System.out.println("\t Result: " + css.getCurrentConfiguration());
	                }

	                return;
	            }
	        }

	        if (echo) {
	            System.out.println("Argumentation did not work, deciding by votes");
	        }

	        this.vote(css);
	    }

	    /**
	     *
	     * It calculates the Weight to be given to each agent which does not want a
	     * configuration to convince it to use it. A HashMap is returned with each
	     * agents which needs to receive weight and the weight which must be given.
	     * Method isWanted is used to know who gives and who takes weight. null if
	     * agents who want a configuration have not enough weight to convince who
	     * don't want it.
	     */
	    private HashMap<UserInterface, Integer> getWeightToConvince(SharedService css, String configuration) {
	        int weightAvailable = 0;
	        int weightNeeded = 0;
	        HashMap<UserInterface, Integer> r = new HashMap<UserInterface, Integer>();

	        for (UserInterface ui : css.getUsers()) {
	            if (ui.getNegotiation().isWanted(configuration, css)) {
	                weightAvailable += ui.getNegotiation().weightInNegotiation;
	            } else {
	                int satisfactionWithWhatAgentWants = ui.getNegotiation().getOrderedPreferences(css).get(0).y;
	                int satisfactionWithProposedConfiguration = ui.getNegotiation().getPreferences(css).get(configuration);
	                int weightRequired = (satisfactionWithWhatAgentWants - satisfactionWithProposedConfiguration);
	                if (weightRequired > 0) {
	                    r.put(ui, weightRequired);
	                    weightNeeded += weightRequired;
	                }
	            }
	        }
	        if (weightAvailable < weightNeeded) {
	            return null;
	        }
	        return r;


	    }

	    /**
	     * Giving weight of negotiation to users convinced of an option. Method
	     * isWanted is used to know who gives and who takes weight.
	     */
	    private void giveWeightToConvince(SharedService css, String configuration, HashMap<UserInterface, Integer> requiringAgents) {
	        while (!requiringAgents.isEmpty()) {//hasta vaciar el mapa de receptores
	            //System.out.println("SERVICE: " + css.getName() + " " + css.getUsers());
	            //System.out.println("SHARING, MAP OF RECEIVERS " + requiringAgents.toString());
	            //System.out.println("WEIGHTS " + weightToString(css));
	            ArrayList<UserInterface> receivingList = new ArrayList<UserInterface>(requiringAgents.keySet());
	            for (UserInterface userreciving : receivingList) {//para cada receptor  
	                for (UserInterface usergiving : css.getUsers()) {//para todos los usuarios "convencedores" o "dadores de peso"                             
	                    if (usergiving.getNegotiation().isWanted(configuration, css)) {// si dan peso porque les gusta para lo que se convence
	                        if (usergiving.getNegotiation().weightInNegotiation != 0) {//si queda peso para dar                               
	                            userreciving.getNegotiation().weightInNegotiation++;
	                            usergiving.getNegotiation().weightInNegotiation--;
	                            int weightToBeReceived = requiringAgents.get(userreciving);
	                            weightToBeReceived--;
	                            //if(weightToBeReceived<0) System.exit(0);
	                            requiringAgents.put(userreciving, weightToBeReceived);
	                            if (weightToBeReceived == 0) {
	                                requiringAgents.remove(userreciving);
	                                break;
	                            }
	                        }
	                    }
	                }
	            }
	        }
	    }

	    public String weightToString(SharedService css) {
	        String s = "";
	        int counter = 0;
	        for (UserInterface ui : css.getUsers()) {
	            int w = ui.getNegotiation().weightInNegotiation;
	            s += ui.getName() + "/" + w + " ";
	            counter += w;
	        }
	        return css.getUsers().size() + " users, total weight " + counter + " | " + s;



	    }
	    
	    

    
    
        

    

    
    
}