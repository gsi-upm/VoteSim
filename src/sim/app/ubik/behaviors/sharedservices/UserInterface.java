/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sim.app.ubik.behaviors.sharedservices;

import java.awt.Color;

import sim.app.ubik.Ubik;

/**
 *
 * @author Emilio Serrano, emilioserra@um.es
 */
public interface UserInterface {   
    public void setColor(Color c);     
    public String getName();
    public Ubik getUbik();
    public Preferences getNegotiation();
}
