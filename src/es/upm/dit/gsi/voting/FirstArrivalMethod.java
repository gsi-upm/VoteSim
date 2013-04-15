package es.upm.dit.gsi.voting;

import sim.app.ubik.domoticDevices.SharedService;

public class FirstArrivalMethod extends VotingMethod {

	public FirstArrivalMethod(SharedService css) {
		super(css);		 
	        String configuration = getNextPreference(this.getFirstUser(), css, 0);
	        this.setSelectedConfiguration(configuration);
	        if (echo) {
	            System.out.println(css.getName() + "/" + configuration + ", configuration given by first arrival, agent " + this.getFirstUser().getName());
	        }
	}

}
