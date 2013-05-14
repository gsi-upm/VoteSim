package es.upm.dit.gsi.voting;

import java.util.ArrayList;
import java.util.logging.Logger;

import sim.app.ubik.behaviors.sharedservices.UserInterface;
import sim.app.ubik.domoticDevices.SharedService;
import sim.util.MutableInt2D;


/**
 * 
 * @author Pablo Moncada Isla pmoncada@dit.upm.es
 *
 */
public abstract class VotingMethod {
	SharedService css;
	
	
	String selectedConfiguration;
	Boolean echo = true;
	ArrayList<MutableInt2D> orderedVotes;
	ArrayList<MutableInt2D> votes;
	
	Logger log = Logger.getLogger("VotingMethod");
	
	public VotingMethod(SharedService css) {
		this.css = css;
		
	}
	
	/**
	 * Returns the selected configuration by the Voting Method
	 * @return
	 */
	public String getConfiguration() {
		return null;
	}
	
	/**
	 * Returns number of users
	 * @return Number of users
	 */
	public int getUsersSize() {
		return this.css.getUsers().size();
	}
	
	/**
	 * Getter
	 * @return recurso compartido para el que se aplica el sistema de votaci�n
	 */
	public SharedService getCss() {
		return css;
	}

	/**
	 * Establece el SS a usar. Importante a veces si se ha inicializado el VM con un SS null.
	 * @param css
	 */
	public void setCss(SharedService css) {
		this.css = css;
	}
	
	/**
	 * Setter for the selected configuration
	 * @param conf
	 */
	public void setSelectedConfiguration(String conf) {
		this.selectedConfiguration = conf;
	}
	
	
	 public void setSelectedConfiguration(int index){
         this.selectedConfiguration = this.css.getConfigurations()[index];
     }
	
	/**
	 * Returns the first user that arrives to the shared service
	 * @return First user in arrival
	 */
	public UserInterface getFirstUser() {
		return this.css.getUsers().get(0);
	}
	
	/**
	 * Getter for selectedConfiguration
	 * @return Selected configuration by method
	 */
	public String getSelectedConfiguration() {
		return this.selectedConfiguration;
	}
	
	/**
	 * Devuelve los votos en string
	 * @param votes
	 * @param css
	 * @return
	 */
	protected String votesToString(ArrayList<MutableInt2D> votes, SharedService css) {
        String r = "";
        for (MutableInt2D mi : votes) {
            r += css.getConfigurations()[mi.x] + "/" + mi.y + ",";
        }
        return r.substring(0, r.length() - 1);
    }
	
	/**
     * 
     * @TODO esto duele a la vista, hay que usar el método de ordenación de Collections
     * Ordena un array de votos o preferencias (mutablesIn2D donde x es la
     * configuracion e y la preferencia/voto (los votos/preferencias estan
     * ligados a ambos ArrayList).
     *
     * @param votes
     * @return
     */
    protected ArrayList<MutableInt2D> orderPreferences(ArrayList<MutableInt2D> preferences) {
        @SuppressWarnings("unchecked")
		ArrayList<MutableInt2D> ordered = (ArrayList<MutableInt2D>) preferences.clone();
        MutableInt2D aux;
        for (int i = 1; i < ordered.size(); i++) {
            for (int j = 0; j < ordered.size() - i; j++) {
                if (ordered.get(j).y < ordered.get(j + 1).y) {
                    aux = ordered.get(j);
                    ordered.set(j, ordered.get(j + 1));
                    ordered.set(j + 1, aux);
                }
            }
        }

        return ordered;
    }
    
    /**
     * Devuelve los votos que recibe cada servicio en un array de Int2D, siendo
     * x el índice de la configuración del serivicio e y los votos recibidos.
     *
     * @param css
     * @return
     */
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
        }
        return votes;
    }
    
    /**
     * Obtiene los votos de cada usuario. En los m�todos de votaci�n est�ndar los votos son directamente
	 * las preferencias.
	 * En el resto de sistemas de votaci�n este m�todo debe ser overriden
     * @param ui
     * @return
     */
 public ArrayList<MutableInt2D> getUserVotes(UserInterface ui){
    	
    	ArrayList<MutableInt2D> votes = new ArrayList<MutableInt2D>();
    	ArrayList<MutableInt2D> ordered = ui.getNegotiation().getOrderedPreferences(css);
    	
    	String configurations[] = css.getConfigurations();    	
    	   	
    	//incializar votos con configuraciones
        for (int i = 0; i < ordered.size(); i++) {
            votes.add(new MutableInt2D(i, 0));
        }        
       
        for (int i = 0; i < configurations.length; i++) {
            votes.get(i).y += ui.getNegotiation().getPreferences(css).get(configurations[i]);
        }       
        
        return votes;
    	
    }
    
    /**
     * obtener la preferencia favorita
     *
     * @param i 0 la favorita, 1 la segunda...
     * @return
     */
    public String getNextPreference(UserInterface ui, SharedService s, int i) {
        return s.getConfigurations()[ui.getNegotiation().getOrderedPreferences(s).get(i).x];
    }
    
    
    /**
     * Returns ordered votes converted into string
     * @return orderedVotes
     */
    public String orderedVotesToString() {
    	return this.orderedVotes.toString();
    }
    
    public String userPreferencesToString() {
    	
    	 String userPreferences = "";    	 
    	 String configurations[] = css.getConfigurations();
         ArrayList<MutableInt2D> ordered = new ArrayList<MutableInt2D>();


         for (UserInterface ui : css.getUsers()) {
         	ordered = ui.getNegotiation().getOrderedPreferences(css);
         	userPreferences += "Preferencias del usuario "+ui.getName()+"\n----------------------------------\n";
         	 for (int i = 0; i < configurations.length; i++) {
         		userPreferences += configurations[ordered.get(i).x]+ ": "+ordered.get(i).y+"\n";                 
             }
         	 userPreferences += "\n\n";
             
         }
    	return userPreferences;
    }
    
    /** 
     * If there there is a draw it will return number bigger than zero.
     * The returned number means how many configurations are at the first position.
     * @author pmoncada
     * @return
     */
    public int getDrawCount() {
    	ArrayList<MutableInt2D> votes = getOrderedVotes();
    	int count = 0;
    	for(int i=1; i < votes.size(); i++) {
    		if(votes.get(0).y == votes.get(i).y )
    			count++;
    		else
    				return count;
    	}
    	
    	return count;
    	
    }
    
    /**
     * Returns true if there is a draw
     * @return
     */
    public boolean isDraw() {
    	if(this.getDrawCount() != 0)
    		return true;
    	else
    		return false;
    }

	public ArrayList<MutableInt2D> getOrderedVotes() {
		return orderedVotes;
	}

	public void setOrderedVotes(ArrayList<MutableInt2D> orderedVotes) {
		this.orderedVotes = orderedVotes;
	}
	
	
	
	 
	
	
	

}
