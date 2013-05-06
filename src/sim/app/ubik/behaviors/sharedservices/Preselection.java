package sim.app.ubik.behaviors.sharedservices;

import sim.app.ubik.behaviors.PositionTools;
import sim.app.ubik.building.rooms.Room;
import sim.app.ubik.domoticDevices.SharedService;
import sim.app.ubik.people.Person;
import sim.util.Int2D;

public class Preselection  {
	
	
	/**
	 * Servicio más cercano a posición. Si checkIfItIsInRoom, da Null si no
	 * están en la misma habitación.
	 * 
	 * @return
	 */
	public static SharedService closestSharedService(Person p,
			boolean checkIfItIsInRoom) {
		Int2D pos = new Int2D(p.getPosition().x, p.getPosition().y);
		SharedService ss = (SharedService) p.getUbik().getBuilding()
				.getFloor(p.getFloor()).getDeviceHandler()
				.getNearestDevice(pos, SharedService.class);
		// si el servicio no está en la habitación de la persona no lo puede
		// usar se devuelve null
		if (checkIfItIsInRoom && !isInRoom(ss, p))
			return null;
		return ss;

	}

	/**
	 * 
	 * @param p
	 * @return
	 */
	public static boolean isInRoom(SharedService ss, Person p) {
		if (p == null || ss == null || PositionTools.getRoom(p) == null)
			return false;
		Room r = PositionTools.getRoom(p);
		return r.contains(ss.getPosition().x, ss.getPosition().y);

	}
	
	/**
	 * Servicio donde los usuarios tengan mayor número de configuraciones
	 * queridas comunes. En caso de haber un servicio libre se envía ahí.
	 * 
	 * @return
	 */
	public static SharedService serviceWithMoreCommonWantedConfigurations(
			UserInterface p) {

		float counter = -1;
		SharedService r = null;
		for (SharedService ss : SharedService.getServices(p.getUbik(), 0)) {
			if (ss.getUsers().isEmpty())
				return ss;
			float aux = p.getNegotiation()
					.getNumberOfCommonWantedConfigurations(ss)
					/ ss.getUsers().size();
			if (counter < aux) {
				r = ss;
				counter = aux;
			}
		}
		return r;

	}
	
	/**
	 * Calcula la distancia euclídea a la configuración elegida de cada uno de los servicios a las preferencias 
	 * del usuario.
	 * Si el servicio está vacío, la distancia es cero
	 * @param p
	 * @return
	 */
	public static SharedService euclideanDistance(
			UserInterface p) {
		SharedService r = null;
		int distance = Integer.MIN_VALUE;
		
		System.out.println("[Preselection] (euclideanDistance) Calculando distancias para "+p.getName());
		for (SharedService ss : SharedService.getServices(p.getUbik(), 0)) {
			if (ss.getUsers().isEmpty())
				return ss;
			
			//Service current configuration
			String scc = ss.getCurrentConfiguration();
			
			//Preference value
			int pv = Preferences.getPreferenceValueByKey(p, ss, scc); 
			System.out.println("[Preselection] (euclideanDistance) Servicio  "+ss.getName()+" con un valor de "+pv);
			if(pv > distance) {
				distance = pv;
				r = ss;
			}


		}
		System.out.println("[Preselection] (euclideanDistance) -> Elegido "+r.getName()+" con un valor de "+distance+"\n");
		
		return r;
		
	}

}