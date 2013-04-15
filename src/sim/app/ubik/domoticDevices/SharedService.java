/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sim.app.ubik.domoticDevices;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import sim.app.ubik.Ubik;
import sim.app.ubik.behaviors.PositionTools;
import sim.app.ubik.behaviors.sharedservices.UserInterface;
import sim.app.ubik.building.rooms.Room;
import sim.app.ubik.people.Person;
import sim.util.Int2D;
import ubik3d.model.HomePieceOfFurniture;

/**
 * 
 * @author Emilio Serrano, emilioserra@um.es
 */
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

	public static List<SharedService> getServices(Ubik ubik, int floor) {
		List<SharedService> list = (List) ubik.getBuilding().getFloor(floor)
				.getDeviceHandler().getDevicesInstanceOf(SharedService.class);
		return list;

	}

}
