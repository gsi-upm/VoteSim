/*
* 
* 
* This file is part of VoteSim. VoteSim is a UbikSim library. 
* 
* VoteSim has been developed by members of the research Group on 
* Intelligent Systems [GSI] (Grupo de Sistemas Inteligentes), 
* acknowledged group by the  Technical University of Madrid [UPM] 
* (Universidad Politécnica de Madrid) 
* 
* Authors:
* Mercedes Garijo
* Carlos A. Iglesias
* Pablo Moncada
* Emilio Serrano
* 
* Contact: 
* http://www.gsi.dit.upm.es/;
* 
* 
* 
* VoteSim, as UbikSim, is free software: 
* you can redistribute it and/or modify it under the terms of the GNU 
* General Public License as published by the Free Software Foundation, 
* either version 3 of the License, or (at your option) any later version. 
*
* 
* VoteSim is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with VoteSim. If not, see <http://www.gnu.org/licenses/>
 */
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
        	log.finest(css.getName() + "/" + configuration + ", configuration given by first arrival, agent " + this.getFirstUser().getName());
        }
	}
	
	@Override
	public String getSelectedConfiguration() {
		doVoting();		
		return this.selectedConfiguration;
	}

}
