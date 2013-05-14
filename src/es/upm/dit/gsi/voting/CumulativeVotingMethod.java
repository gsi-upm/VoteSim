package es.upm.dit.gsi.voting;

import java.util.ArrayList;

import sim.app.ubik.behaviors.sharedservices.UserInterface;
import sim.app.ubik.domoticDevices.SharedService;
import sim.util.MutableInt2D;

public class CumulativeVotingMethod extends VotingMethod {
	
	//Available votes
	private int k;

	public CumulativeVotingMethod(SharedService css) {
		super(css);		
	}
	
	public CumulativeVotingMethod(SharedService css, int k) {
		super(css);
		this.k = k;		
	}
	

	/**
	 * This method implements the moting method and writes the selected configuration
	 * into selectedConfiguration.
	 * For this rankingVoting it orders the votes and gets the first one.
	 */
	public void doVoting() {
		this.k = css.getConfigurations().length;
		if (this.getUsersSize()> 1) {
	           String configurations[] = this.css.getConfigurations();	            
	           this.votes = this.votingConfigurations(this.css);
	           this.orderedVotes = this.orderPreferences(this.votes);	            
	           setSelectedConfiguration(configurations[this.orderedVotes.get(0).x]);      
	    }		
	}
	
	    
    /**
     * Devuelve los votos que recibe cada servicio en un array de Int2D, siendo
     * x el índice de la configuración del serivicio e y los votos recibidos.
     *
     * @param css
     * @return
     */
    @Override
    protected ArrayList<MutableInt2D> votingConfigurations(SharedService css) {

        String configurations[] = css.getConfigurations();
        ArrayList<MutableInt2D> votes = new ArrayList<MutableInt2D>();
        
        ArrayList<MutableInt2D> ordered = new ArrayList<MutableInt2D>();
        
        @SuppressWarnings("unused")
		int totalSum;
        
        //incializar votos con configuraciones
        for (int i = 0; i < configurations.length; i++) {
            votes.add(new MutableInt2D(i, 0));
        }
        //votar
        for (UserInterface ui : css.getUsers()) {
        	totalSum = 0;
        	ordered = ui.getNegotiation().getOrderedPreferences(css);
            for (int i = 0; i < configurations.length; i++) {                
                totalSum += ordered.get(i).y;                
            }
            
            for(int i = 0; i < configurations.length; i++) {
            	ArrayList<MutableInt2D> userVotes = getUserVotes(ui);
            	votes.get(ordered.get(i).x).y += userVotes.get(ordered.get(i).x).y;	            	
            }
        }
        return votes;
    }

    
    @Override
    public ArrayList<MutableInt2D> getUserVotes(UserInterface ui){
    	
    	ArrayList<MutableInt2D> votes = new ArrayList<MutableInt2D>();
    	ArrayList<MutableInt2D> ordered = ui.getNegotiation().getOrderedPreferences(css);
    	
    	int totalSum = 0;	
    	
    	/* incializar votos con configuraciones 
    	 * y obtener sumatorio total para calculo de probabilidades */    	 
        for (int i = 0; i < ordered.size(); i++) {
            votes.add(new MutableInt2D(i, 0));
            totalSum += ordered.get(i).y; 
        }
        int vg = 0;
        while(vg<k) {
        	for(int i = 0; i < ordered.size(); i++) {
        		double r = Math.random();
        		double probCv = (double) ordered.get(i).y/(double) totalSum;        		
        		if(r <= probCv && vg<k){
        			votes.get(ordered.get(i).x).y += 1;
        			vg++;
        		}
        	}        	
        }
        return votes;    	
    }

	

	@Override
	public String getSelectedConfiguration() {
		doVoting();
		if (echo) {
            log.finest("Plurality VOTES ORDERED for " + this.css.getName());
            log.finest(votesToString(this.orderedVotes, this.css));
            log.finest("Result: " + this.css.getCurrentConfiguration());
        }
		return this.selectedConfiguration;
	}

}
