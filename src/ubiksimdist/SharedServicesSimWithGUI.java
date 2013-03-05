/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ubiksimdist;

import sim.app.ubik.Ubik;
import sim.app.ubik.UbikSimWithUI;
import sim.app.ubik.behaviors.sharedservices.MonitorServiceGUI;
import sim.display.Console;

/**
 *
 * @author Emilio Serrano, emilioserra@um.es
 */
public class SharedServicesSimWithGUI extends UbikSimWithUI {

    private MonitorServiceGUI myDisplay;
        
    public SharedServicesSimWithGUI(Ubik ubik) {
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
        Ubik ubik = new SharedServicesSim();
        UbikSimWithUI vid = new SharedServicesSimWithGUI(ubik);
        Console c = new Console(vid);	
        c.setIncrementSeedOnStop(false);
        c.setVisible(true);

}
}
