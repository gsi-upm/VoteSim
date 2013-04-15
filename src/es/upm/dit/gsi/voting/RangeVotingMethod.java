package es.upm.dit.gsi.voting;


import sim.app.ubik.domoticDevices.SharedService;

public class RangeVotingMethod extends VotingMethod {

	public RangeVotingMethod(SharedService css) {
		super(css);
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
	
	

	@Override
	public String getSelectedConfiguration() {		
		if (echo) {
            System.out.println("VOTES ORDERED for " + this.css.getName());
            System.out.println(votesToString(this.orderedVotes, this.css));
            System.out.println("Result: " + this.css.getCurrentConfiguration());
        }
		return this.selectedConfiguration;
	}

}
