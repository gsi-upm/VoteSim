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


package sim.app.ubik.domoticDevices;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import sim.app.ubik.Ubik;
import sim.app.ubik.behaviors.sharedservices.Preferences;
import sim.app.ubik.behaviors.sharedservices.UserInterface;
import sim.app.ubik.behaviors.sharedservices.UsingSharedService;
import sim.app.ubik.people.Person;
import ubik3d.model.HomePieceOfFurniture;

public abstract class SharedService extends FixedDomoticDevice {
	
	
	protected ArrayList<UserInterface> users;
	/** colores a dar a los distintos servicios en uso */
	protected static Color[] colors = { Color.BLUE, Color.CYAN, Color.MAGENTA,
			Color.YELLOW, Color.GRAY };
	protected static int lastIndexColor = 0;
	protected Color color;
	protected Ubik ubik;
	

	/**
	 * Configuración del serivcio actual
	 */
	protected String currentConfiguration;

	public SharedService(int floor, HomePieceOfFurniture device3DModel,
			Ubik ubik) {
		super(floor, device3DModel, ubik);
		users = new ArrayList<UserInterface>();
		color = colors[lastIndexColor % colors.length];
		lastIndexColor++;
		this.ubik = ubik;

	}

	public ArrayList<UserInterface> getUsers() {
		return users;
	}

	/**
	 * Añadir un usuario, cambiar color servicio y usuario Por defecto, se toma
	 * la primera preferencia del primer agente que entra a servicio
	 * 
	 * @param p
	 */
	public void addUser(UserInterface p) {
		if (users.isEmpty()) {
			this.getDevice3DModel().setColor(color.getRGB());
		}
		p.setColor(color);// agente con color de servicio
		users.add(p);
	}

	public void removeUser(UserInterface p) {
		users.remove(p);
		if (users.isEmpty())
			this.getDevice3DModel().setColor(null);
		p.setColor(null);
	}

	/**
	 * @param p
	 * @return
	 */
	public boolean isUser(UserInterface p) {
		return users.contains(p);
	}

	/**
	 * @return
	 */
	public String getCurrentConfiguration() {
		return currentConfiguration;
	}

	public void setConfiguration(int index) {
		this.currentConfiguration = getConfigurations()[index];
	}

	public void setConfiguration(String conf) {
		this.currentConfiguration = conf;
	}

	/**
	 * Cargar posibles configuraciones del servicio Este método debe ser
	 * extendido en las distintas clases que herenden
	 * 
	 */
	public abstract String[] getConfigurations();

	public static SharedService getService(Person p, String serviceName) {
		return (SharedService) p.getUbik().getBuilding().getFloor(p.getFloor())
				.getDeviceHandler().getDeviceByName(serviceName);
	}

	

	

	public static List<SharedService> getServices(Ubik ubik, int floor) {
		List<SharedService> list = (List) ubik.getBuilding().getFloor(floor)
				.getDeviceHandler().getDevicesInstanceOf(SharedService.class);
		return list;

	}
	
	/**
     * Mira la nota que le da el usuario a la configuración que ha sido seleccionada
     * @param ss
     * @return
     */
    public double getUserSatisfaction(UserInterface ui) {      
      int preferenceForService=ui.getNegotiation().getPreferences(this).get(this.getCurrentConfiguration()).intValue();
      return calculateSatisfaction(preferenceForService);
    }
    
    
    
    /**
     * Mira la nota que le da el usuario a una configuración dada
     * @param ss
     * @param configuration configuración para la que se quiere mirar la satisfacción
     * @author pmoncada
     * @return
     */
    public double getUserSatisfaction(UserInterface ui, String configuration) {
      int preferenceForService=ui.getNegotiation().getPreferences(this).get(configuration).intValue();      
      return calculateSatisfaction(preferenceForService);
    }

    
    /**
     * @author pmoncada
     * @param preferenceForService
     * @return
     */
    public double calculateSatisfaction(int preferenceForService) {
    	if(UsingSharedService.codeOfSatisfactionFunction==0)  return preferenceForService;
        if(UsingSharedService.codeOfSatisfactionFunction>0){
             if(preferenceForService<=4) return 0;
             if(preferenceForService>4 && preferenceForService<=7) return 5;
             if(preferenceForService>7) return 10;
         }
        return 0;
    }
    
    /**
     * Devuelve la satisfacción que genera la configuración elegida para un servicio
     * @param css
     * @author pmoncada
     * @return 
     */
    public double getSatisfaction() {
    	double confSatisfaction = 0;
		for (UserInterface ui : this.getUsers()) {
			confSatisfaction += this.getUserSatisfaction(ui);				
		}
		
    	return confSatisfaction;
    }
    
    /**
     * Devuelve la satisfacción que genera una configuración cualquiera
     * @param css
     * @author pmoncada
     * @return 
     */
    public double getSatisfaction(String configuration) {
    	double confSatisfaction = 0;
		for (UserInterface ui : this.getUsers()) {
			confSatisfaction += this.getUserSatisfaction(ui, configuration);				
		}
		
    	return confSatisfaction;
    }
    
    /**
	 * Calculates all satisfaction for all configurations and returns 
	 * the maximun
	 * @author pmoncada
	 * @return Max satisfaction
	 */
	public double getMaxSatisfaction() {
		double maxSatisfaction = 0;
		
		for (String conf : this.getConfigurations()) {
			double confSatisfaction = getSatisfaction(conf);
			if(confSatisfaction > maxSatisfaction)	
				maxSatisfaction = confSatisfaction;			

		}
		if(Preferences.echo)
			System.out.println("maxSatisfaction="+maxSatisfaction);
		
		return maxSatisfaction;
		
	}
	
	/**
	 * Calculates all satisfaction for all configurations and returns 
	 * the minimum
	 * @author pmoncada
	 * @return Min satisfaction
	 */
	public double getMinSatisfaction() {
		double minSatisfaction = Double.MAX_VALUE;
		
		for (String conf : this.getConfigurations()) {
			double confSatisfaction = getSatisfaction(conf);
			if(confSatisfaction < minSatisfaction)	
				minSatisfaction = confSatisfaction;			

		}
		
		if(Preferences.echo)
			System.out.println("minSatisfaction="+minSatisfaction);
			
		return minSatisfaction;
		
	}	
	/**
	 * Función SAS definida por Emilio
	 * @author pmoncada
	 * @return
	 */
	public double getServiceSatisfaction() {
		double servSatisfaction = this.getSatisfaction();
		
		if(Preferences.echo)
			System.out.println("servSatisfaction="+servSatisfaction);
		
		return servSatisfaction;
	}
	
	/**
	 * Normalizando servSas (con proporcion del rango) obtenemos la satisfaccion del servicio normalizada
	 * @author pmoncada
	 * @return Satisfaccion normalizada
	 */
	public double getNormServSas() {
		double minSatisfaction = getMinSatisfaction();
		double normServSas = (getServiceSatisfaction()-minSatisfaction)/(getMaxSatisfaction()-minSatisfaction);
		
		if(Preferences.echo)
			System.out.println("normServSas="+normServSas+"\n");
		
		return normServSas;
	}
	
	/**
	 * Se calcula realizando la suma de la satisfacción de todos los usuarios y dividiendo entre el número de usuarios*10.
	 * @author pmoncada
	 * @return Satisfacción acotada entre [0,1]
	 */
	public double getBoundedServiceSatisfaction() {
		
		double boundedServSas = this.getSatisfaction()/(this.getUsers().size()*10);
	
		if(Preferences.echo)
			System.out.println("boundedServSas="+boundedServSas+"\n");
		return boundedServSas;
		
	}

}
