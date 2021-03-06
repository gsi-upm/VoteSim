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

package votesimdist;

import sim.app.ubik.Ubik;
import sim.app.ubik.UbikSimWithUI;
import sim.app.ubik.behaviors.sharedservices.MonitorServiceGUI;
import sim.display.Console;

public class VoteSimWithGUI extends UbikSimWithUI {

    private MonitorServiceGUI myDisplay;
        
    public VoteSimWithGUI(Ubik ubik) {
		super(ubik);		
	}

    @Override
    public void start() {
        super.start();
        myDisplay = new MonitorServiceGUI(this);
        myDisplay.setVisible(true);      
        controller.registerFrame(myDisplay);
        
    }

    @Override
    public void finish() {
        super.finish();
        
        controller.unregisterFrame(myDisplay);
    }    
    
     /**
     * Ejecutar con GUI, la nueva versión de UbikSimWithUI se encarga de generar en initFrame el MonitorServicesGUI (que a su vez
     * lee monitor services de esta clase)
     * @param args 
     */
    public static void main(String []args) {
        Ubik ubik = new VoteSim();
        UbikSimWithUI vid = new VoteSimWithGUI(ubik);
        Console c = new Console(vid);	
        c.setTitle("VoteSim");
        c.setIncrementSeedOnStop(false);
        c.setVisible(true);

}
}