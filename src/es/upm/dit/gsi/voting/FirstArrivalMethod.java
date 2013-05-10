package es.upm.dit.gsi.voting;

import sim.app.ubik.domoticDevices.SharedService;

public class FirstArrivalMethod extends VotingMethod {

	/**
	 * Devuelve la preferencia asociada al primer usuario
	 * Mediante el método getNextPreference escogemos del usuario que ha llegado el primero su preferencia favorita
	 * marcada con el número 0.
	 * 
	 * @param css
	 */
	public FirstArrivalMethod(SharedService css) {
		super(css);	       
	}
	
	public void doVoting() {
		String configuration = getNextPreference(this.getFirstUser(), css, 0);
        this.setSelectedConfiguration(configuration);
        if (echo) {
            System.out.println(css.getName() + "/" + configuration + ", configuration given by first arrival, agent " + this.getFirstUser().getName());
        }
	}
	
	@Override
	public String getSelectedConfiguration() {
		doVoting();		
		return this.selectedConfiguration;
	}

}
