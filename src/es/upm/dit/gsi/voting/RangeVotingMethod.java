package es.upm.dit.gsi.voting;


import sim.app.ubik.domoticDevices.SharedService;

public class RangeVotingMethod extends VotingMethod {

	public RangeVotingMethod(SharedService css) {
		super(css);
				
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
		doVoting();		
		if (echo) {
			log.finest("RangeVoting VOTES ORDERED for " + this.css.getName());
			log.finest(votesToString(this.orderedVotes, this.css));
			log.finest("Result: " + this.css.getCurrentConfiguration());
        }
		return this.selectedConfiguration;
	}

}
