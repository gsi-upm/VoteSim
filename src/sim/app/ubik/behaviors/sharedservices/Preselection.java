package sim.app.ubik.behaviors.sharedservices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.upm.dit.gsi.voting.VotingMethod;

import sim.app.ubik.behaviors.PositionTools;
import sim.app.ubik.building.rooms.Room;
import sim.app.ubik.domoticDevices.SharedService;
import sim.app.ubik.people.Person;
import sim.util.Int2D;
import sim.util.MutableInt2D;
import weka.core.Attribute;
import weka.core.EuclideanDistance;
import weka.core.FastVector;
import weka.core.Instance;

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
	public static SharedService serviceWithMoreCommonWantedConfigurations(UserInterface p) {
		
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
	public static SharedService getServiceByeuclideanDistance(UserInterface p, VotingMethod vm) {
		SharedService r = null;
		double distance = Double.MAX_VALUE;
		
		EuclideanDistance ed = new EuclideanDistance();
		
		
		
		
		System.out.println("[Preselection] (euclideanDistance) Calculando distancias para "+p.getName());
		for (SharedService ss : SharedService.getServices(p.getUbik(), 0)) {
			vm.setCss(ss);
			
			double serviceDistance = 0;
			if (ss.getUsers().isEmpty())
				return ss;
			
			for(UserInterface q : ss.getUsers()) {
				
				serviceDistance += calculateEuclideanDistance(vm.getUserVotes(p),vm.getUserVotes(q));
				//serviceDistance += ed.distance(getInstance(p,vm,ss), getInstance(q,vm,ss));
			}
			System.out.println("[Preselection] (euclideanDistance) Distancia para "+ss.getName()+": "+serviceDistance);
			if(serviceDistance < distance) {
				System.out.println("[Preselection] (euclideanDistance) Mejor distancia para "+ss.getName()+": "+serviceDistance);
				distance = serviceDistance;
				r = ss;
			}

			
		}
		return r;
	}
	
	/**
     * Calcula la distancia euclídea entre 2 conjuntos de votaciones p y q
     * @param p
     * @param q
     * @return Distancia euclídea
     */
    public static double calculateEuclideanDistance(ArrayList<MutableInt2D> p, ArrayList<MutableInt2D> q) {    	
    	if(p.size() != q.size())
    		return -1;
    	double sum = 0;
    	
    	for(int i=0; i < p.size(); i++) {
    		double difference = q.get(i).y - p.get(i).y;
    		sum += Math.pow(difference, 2);
    	}
    	
    	return Math.sqrt(sum);
    }
    
    /**
     * Calcula la distancia Manhattan o Taxicab entre 2 conjuntos de votaciones p y q
     * @param p
     * @param q
     * @return Distancia Manhattan o taxicab
     */
    public double calculateManhattanDistance(ArrayList<MutableInt2D> p, ArrayList<MutableInt2D> q) {    	
    	if(p.size() != q.size())
    		return -1;
    	double sum = 0;
    	
    	for(int i=0; i < p.size(); i++) {
    		double difference = p.get(i).y - q.get(i).y;
    		sum += Math.abs(difference);
    	}
    	
    	return sum;
    }
    
    
    
    /**
     * Obtener instancia de una persona en el cluster
     * @param ui
     * @return 
     */
    private static Instance getInstance(UserInterface ui, VotingMethod vm, SharedService ss) {
    	
        Instance inst =  new Instance(ss.getConfigurations().length);
        FastVector attributes = generateAttributes(ss);
        ArrayList<MutableInt2D> votes = vm.getUserVotes(ui);
                           
        for(int i=0;i<ss.getConfigurations().length;i++){
            String nameConf = ss.getConfigurations()[i];
            //int valueConf = ui.getNegotiation().getPreferences(ss).get(nameConf);                                   
            inst.setValue((Attribute) attributes.elementAt(i), votes.get(i).y);
         }
         return inst;
    }
    
    private static FastVector generateAttributes(SharedService ss) {
        
    	FastVector attributes= new FastVector();       
        
        for(int i=0;i<ss.getConfigurations().length;i++){
            attributes.addElement(new Attribute(ss.getConfigurations()[i]));
        }
        
        return attributes;
     
    }
	
	
}
