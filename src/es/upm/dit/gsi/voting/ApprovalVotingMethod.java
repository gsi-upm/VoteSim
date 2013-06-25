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

import java.util.ArrayList;

import sim.app.ubik.behaviors.sharedservices.UserInterface;
import sim.app.ubik.domoticDevices.SharedService;
import sim.util.MutableInt2D;

public class ApprovalVotingMethod extends VotingMethod {

		//Default votes, it will vote for the top 3.
		private int k = 3;
		private boolean moreThanFive = false;

		public ApprovalVotingMethod(SharedService css) {
			super(css);
			
		}
		
		public ApprovalVotingMethod(SharedService css, int k) {
			super(css);
			this.k = k;
		}
		
		
		public ApprovalVotingMethod(SharedService css, boolean moreThanFive) {
			super(css);
			this.moreThanFive = moreThanFive;
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

	        //incializar votos con configuraciones
	        for (int i = 0; i < configurations.length; i++) {
	            votes.add(new MutableInt2D(i, 0));
	        }
	        //votar
	        for (UserInterface ui : css.getUsers()) {
	        	ordered = ui.getNegotiation().getOrderedPreferences(css);

	            for (int i = 0; i < configurations.length; i++) {
	            	ArrayList<MutableInt2D> userVotes = getUserVotes(ui);  
	            	votes.get(ordered.get(i).x).y += userVotes.get(ordered.get(i).x).y;	 	            	
	            }
	            log.finest("[getUserVotes] Votes for "+ui.getName()+":"+ getUserVotesToString(ui));
	        }
	        return votes;
	    }
	    
	    @Override
	    public ArrayList<MutableInt2D> getUserVotes(UserInterface ui){
	    	
	    	ArrayList<MutableInt2D> votes = new ArrayList<MutableInt2D>();
	    	ArrayList<MutableInt2D> ordered = ui.getNegotiation().getOrderedPreferences(css);	    	
	    	   	
	    	//incializar votos con configuraciones
	        for (int i = 0; i < ordered.size(); i++) {
	            votes.add(new MutableInt2D(i, 0));
	        }
	        
	        if(moreThanFive == false) {
		        for(int i = 0; i < k; i++) 
	            	votes.get(ordered.get(i).x).y += 1;	
	        }else {
	        	 String configurations[] = css.getConfigurations();
	        	for(int i = 0; i < configurations.length; i++) {
	        		if(ordered.get(i).y > 5)
	        			votes.get(ordered.get(i).x).y += 1;	
	        	}	        	
	        }
	        
	        return votes;	    	
	    }

		

		@Override
		public String getSelectedConfiguration() {
			doVoting();
			if (echo) {
				log.finest("[VotingMethod] Approval VOTES ORDERED for " + this.css.getName());
				log.finest("[VotingMethod] "+votesToString(this.orderedVotes, this.css));
				log.finest("[VotingMethod] Result: " + this.css.getCurrentConfiguration());
	        }
			return this.selectedConfiguration;
		}
	
	

}
