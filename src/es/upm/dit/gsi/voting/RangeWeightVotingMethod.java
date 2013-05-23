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
import java.util.HashMap;

import sim.app.ubik.behaviors.sharedservices.Preferences;
import sim.app.ubik.behaviors.sharedservices.UserInterface;
import sim.app.ubik.domoticDevices.SharedService;
import sim.util.MutableInt2D;

public class RangeWeightVotingMethod extends VotingMethod {

	public RangeWeightVotingMethod(SharedService css) {
		super(css);		
		
	}
	
	/**
	 * This method implements the voting method and writes the selected configuration
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
	
	 private void selectConfigurationByNegotiationWeight(SharedService css) {
	        ArrayList<MutableInt2D> orderedVotes = Preferences.orderPreferences(Preferences.votingConfigurations(css));
	        for (MutableInt2D m : orderedVotes) {
	            HashMap<UserInterface, Integer> agentsToBeConvinced = this.getWeightToConvince(css, css.getConfigurations()[m.x]);
	            if (agentsToBeConvinced == null) {
	                continue;
	            } else {

	                if (echo) {
	                	log.finest(css.getName());
	                	log.finest("\t Before argumenting:" + this.weightToString(css));
	                    if (agentsToBeConvinced.isEmpty()) {
	                    	log.finest("\t No change of weights");
	                    }
	                }

	                setSelectedConfiguration(m.x);
	                giveWeightToConvince(css, css.getConfigurations()[m.x],  agentsToBeConvinced);
	                this.votes = this.votingConfigurations(css);
	 	            this.orderedVotes = this.orderPreferences(this.votes);



	                if (echo) {
	                	log.finest("\t After argumenting:" + this.weightToString(css));
	                	log.finest("\t Result: " + css.getCurrentConfiguration());
	                }

	                return;
	            }
	        }

	        if (echo) {
	        	log.finest("Argumentation did not work, deciding by votes");
	        }
	        doVoting();
	        
	        
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
	
	

	@Override
	public String getSelectedConfiguration() {
		selectConfigurationByNegotiationWeight(css);
		if (echo) {
			log.finest("VOTES ORDERED for " + this.css.getName());
			log.finest(votesToString(this.orderedVotes, this.css));
			log.finest("Result: " + this.css.getCurrentConfiguration());
        }
		return this.selectedConfiguration;
	}

}
