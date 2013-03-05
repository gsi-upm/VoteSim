/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sim.app.ubik.behaviors.sharedservices;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import sim.app.ubik.Ubik;
import sim.app.ubik.domoticDevices.SharedService;
import sim.app.ubik.people.Person;

/**
 *
 * @author Emilio Serrano, emilioserra@um.es
 */
public interface UserInterface {   
    public void setColor(Color c);     
    public String getName();
    public Ubik getUbik();
    public Negotiation getNegotiation();
}
