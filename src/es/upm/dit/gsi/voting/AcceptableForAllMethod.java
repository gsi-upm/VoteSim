/*
* 
* 
* This file is part of VoteSim. VoteSim is a UbikSim library. 
* 
* VoteSim has been developed by members of the research Group on 
* Intelligent Systems [GSI] (Grupo de Sistemas Inteligentes), 
* acknowledged group by the Universidad Politécnica de Madrid [UPM] 
* (Technical University of Madrid) 
* 
* Authors:
* Emilio Serrano
* Pablo Moncada
* Mercedes Garijo
* Carlos A. Iglesias
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
