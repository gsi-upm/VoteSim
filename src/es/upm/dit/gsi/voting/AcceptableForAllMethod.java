package es.upm.dit.gsi.voting;

import java.util.ArrayList;

import sim.app.ubik.behaviors.sharedservices.UserInterface;
import sim.app.ubik.domoticDevices.SharedService;
import sim.util.MutableInt2D;

public class AcceptableForAllMethod extends VotingMethod {
	
	private Boolean acceptableForAll;

	public Boolean getAcceptableForAll() {
		return acceptableForAll;
	}

	public void setAcceptableForAll(Boolean acceptableForAll) {
		this.acceptableForAll = acceptableForAll;
	}

	public AcceptableForAllMethod(SharedService css) {
		super(css);		
	}
	

	
	private void doVoting() {
		ArrayList<MutableInt2D> orderedVotes = this.orderPreferences(this.votingConfigurations(css));
        for (MutableInt2D mi : orderedVotes) {
            if (isAcceptableForAll(css.getConfigurations()[mi.x], css)) {
            	setSelectedConfiguration(mi.x);
                if (echo) {
                	log.finest("First vote acceptable for all: " + css.getConfigurations()[mi.x] + ", votes: " + votesToString(orderedVotes, css));
                }
                setAcceptableForAll(true);
            }
        }
        setAcceptableForAll(false);
	}
	
	@Override
	public String getSelectedConfiguration() {
		doVoting();
		if (getAcceptableForAll()) {
			return this.getSelectedConfiguration();           
        }else {
        	 if (echo) {
        		 log.finest("No vote acceptable for all in , deciding by votes");
             }
        	 VotingMethod rv = new RangeVotingMethod(css);
        	 return rv.getSelectedConfiguration();
        }
	}
	
	/**
     * Ver si una configuración es aceptable para todos
     *
     * @param configurations
     * @param preference
     */
    private boolean isAcceptableForAll(String configuration, SharedService css) {
        for (UserInterface ui : css.getUsers()) {
            if (!ui.getNegotiation().isAcceptable(configuration, css, true)) {
                return false;
            }
        }
        return true;
    }

}
