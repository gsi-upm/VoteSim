/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ubiksimdist;

import sim.app.ubik.UbikSimLauncher;
import sim.app.ubik.behaviors.Automaton;
import ubik3d.SweetHome3D;

/**
 *
 * @author Emilio Serrano, emilioserra@um.es
 */
public class UbikEditor {
    
   public static void main(String[] args) {
         args=new String[2];
        args[0]=""+4;
        args[1]=""+10;
        SweetHome3D.main(args);     
    }
}      
