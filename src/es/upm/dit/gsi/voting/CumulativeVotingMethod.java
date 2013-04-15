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
		this.k = css.getConfigurations().length;
		doVoting();
	}
	
	public CumulativeVotingMethod(SharedService css, int k) {
		super(css);
		this.k = k;
		doVoting();
	}
	

	/**
	 * This method implements the moting method and writes the selected configuration
	 * into selectedConfiguration.
	 * For this rankingVoting it orders the votes and gets the first one.
	 */
	public void doVoting() {
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
        
        int count;
        
        //incializar votos con configuraciones
        for (int i = 0; i < configurations.length; i++) {
            votes.add(new MutableInt2D(i, 0));
        }
        //votar
        for (UserInterface ui : css.getUsers()) {
        	count = 0;
        	ordered = ui.getNegotiation().getOrderedPreferences(css);
            for (int i = 0; i < configurations.length; i++) {                
                count += ordered.get(i).y;                
            }
            for(int i = 0; i < configurations.length; i++) {
            	
            	double ponderate = ((double) ordered.get(i).y/(double) count)*(double) k;
            	votes.get(ordered.get(i).x).y += (int) ponderate;
            }
        }
        return votes;
    }

	

	@Override
	public String getSelectedConfiguration() {		
		if (echo) {
            System.out.println("Plurality VOTES ORDERED for " + this.css.getName());
            System.out.println(votesToString(this.orderedVotes, this.css));
            System.out.println("Result: " + this.css.getCurrentConfiguration());
        }
		return this.selectedConfiguration;
	}

}
