/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.app.ubik.behaviors.sharedservices;

import java.util.ArrayList;
import java.util.HashMap;

import es.upm.dit.gsi.voting.*;

import sim.app.ubik.domoticDevices.SharedService;
import sim.util.MutableInt2D;

/**
 * Objeto con información acerca de negociaciones así como las preferencias de
 * cada agente para un servicio concreto. Los agentes podrían tener un hashMap
 * asociando una instancia a cada tipo de servicio.
 *
 * @author Emilio Serrano, emilioserra@um.es
 */
public class Negotiation {

    /**
     * 0: first agent configurates service 1: decision voting; 2:
     */
    protected static int codeOfNegotiation = 1;
    protected static int codeOfSatisfactionFunction = 0;
    protected static boolean echo = true;

 
    protected UserInterface user;
    protected HashMap<String, Integer> preferences;
    /**
     * Preferencia más alta
     */
    protected int upperBoundForCalification = 10;
    /**
     * A configuration is wanted if its the number wantedcriteria or less in the ranking of favourite configurations
     */
    protected int wantedcriteria=3;
    protected int weightInNegotiation = upperBoundForCalification;
    
    /**
     * Variable que, actualizada desde fuera, permite llevar la cuenta del tiempo que un agente espera
     * a tener un servicio querido.
     */
    protected int stepsWithoutWantedService;
    
       public static void setEcho(boolean b) {
        echo=b;
    }
 
    public Negotiation(UserInterface u) {
        this.user = u;
    }

    /**
     * Obtener preferencias aleatorias, un hushMap con cada posibilidad y una
     * califcación de 0 a 10
     *
     * @return
     */
    public HashMap<String, Integer> getRandomPreferences(SharedService ss) {
        HashMap<String, Integer> p = new HashMap<String, Integer>();
        for (int i = 0; i < ss.getConfigurations().length; i++) {
            p.put(ss.getConfigurations()[i], user.getUbik().random.nextInt(upperBoundForCalification + 1));
        }



        return p;
    }

    
    public float getSatisfaction(SharedService ss) {
      int preferenceForService=getPreferences(ss).get(ss.getCurrentConfiguration()).intValue();
      if(codeOfSatisfactionFunction==0)  return preferenceForService;
      if(codeOfSatisfactionFunction>0){
           if(preferenceForService<=4) return 0;
           if(preferenceForService>4 && preferenceForService<=7) return 5;
           if(preferenceForService>7) return 10;
       }
       return 0;
    }

    public HashMap<String, Integer> getPreferences(SharedService ss) {
        if (preferences == null) {
            preferences = getRandomPreferences(ss);
        }
        return this.preferences;
    }

    /**
     * Preferencias ordenadas para el objeto negociacion (depende del usuario)
     *
     * @param ss
     * @return
     */
    public ArrayList<MutableInt2D> getOrderedPreferences(SharedService ss) {
        ArrayList<MutableInt2D> r = new ArrayList<MutableInt2D>();
        String conf[] = ss.getConfigurations();
        for (int i = 0; i < conf.length; i++) {
            r.add(new MutableInt2D(i, this.getPreferences(ss).get(conf[i])));
        }
        return this.orderPreferences(r);
    }

    public String orderedPreferencesToString(SharedService ss) {
        ArrayList<MutableInt2D> list = getOrderedPreferences(ss);
        String r = "";
        for (MutableInt2D m : list) {
            r += ss.getConfigurations()[m.x] + "/" + m.y + " ";
        }
        return r;
    }

    public void setPreferences(SharedService s, HashMap<String, Integer> p) {
        this.preferences = p;
    }

    /**
     * @deprecated Pensado para preferencias sin pesos Devuelve unas
     * preferencias aleatorias del servicio por orden de preferencia: de 1 a la
     * mitad de las configuraciones se cogen configuraciones aleatorias. Se
     * asume orden de preferencia. Los agentes implentando userInterface pueden
     * implementar distintas formas de rellenar sus preferencias Este método no
     * considera pesos en las preferencias, todo te gusta por igual
     * @return
     */
    public ArrayList<String> getRandomPreferencesWithoutWeight(SharedService s) {
        //entre 1 y configuraciones/2 preferencias
        int numberOfPreferences = 1 + user.getUbik().random.nextInt(s.getConfigurations().length / 2 - 1);
        ArrayList<String> preferences = new ArrayList<String>();
        for (int i = 0; i < numberOfPreferences; i++) {
            int index;
            do {
                index = user.getUbik().random.nextInt(s.getConfigurations().length);
            } while (preferences.contains(s.getConfigurations()[index]));
            preferences.add(s.getConfigurations()[index]);
        }
        return preferences;
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
     * Devuelve los votos que recibe cada servicio en un array de Int2D, siendo
     * x el índice de la configuración del serivicio e y los votos recibidos.
     *
     * @param css
     * @return
     */
    protected ArrayList<MutableInt2D> votingConfigurations(SharedService css) {

        String configurations[] = css.getConfigurations();
        ArrayList<MutableInt2D> votes = new ArrayList<MutableInt2D>();
        //incializar votos con configuraciones
        for (int i = 0; i < configurations.length; i++) {
            votes.add(new MutableInt2D(i, 0));
        }
        //votar
        for (UserInterface ui : css.getUsers()) {
            for (int i = 0; i < configurations.length; i++) {
                votes.get(i).y += ui.getNegotiation().getPreferences(css).get(configurations[i]);
            }
        }
        return votes;
    }

    /**
     * Ordena un array de votos o preferencias (mutablesIn2D donde x es la
     * configuracion e y la preferencia/voto (los votos/preferencias estan
     * ligados a ambos ArrayList).
     *
     * @param votes
     * @return
     */
    protected ArrayList<MutableInt2D> orderPreferences(ArrayList<MutableInt2D> preferences) {
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




   public static void setCodeOfSatisfactionFuction(int c) {
        Negotiation.codeOfSatisfactionFunction = c;
    }
   
      public static int getCodeOfSatisfactionFuction() {
        return Negotiation.codeOfSatisfactionFunction;
    }
    
        
        
    public static void setCodeOfNegotiation(int c) {
        Negotiation.codeOfNegotiation = c;
    }
    
     public static int getCodeOfNegotiation() {
       return codeOfNegotiation;
    }

    public void negotiate(SharedService css) {
    	
    	VotingMethod vm = null;
    	
        if (css.getUsers().isEmpty()) {
            return;
        }

        if (css.getUsers().size() == 1) {
            css.setConfiguration(getNextPreference(user, css, 0));
            return;
        }
        
        if (codeOfNegotiation == 0) 
        	vm = new FirstArrivalMethod(css);
        
        else if (codeOfNegotiation == 1) 
        	vm = new RangeVotingMethod(css);
        
        else if (codeOfNegotiation == 2) 
        	vm = new AcceptableForAllMethod(css);
        
        else if (codeOfNegotiation == 3) 
        	vm = new PluralityVotingMethod(css);
        
        else if (codeOfNegotiation == 4) 
        	vm = new CumulativeVotingMethod(css);
        
        else if (codeOfNegotiation == 5) 
        	vm = new ApprovalVotingMethod(css);
        
        else if (codeOfNegotiation == 6) 
        	vm = new BordaVotingMethod(css);
        
        System.out.println(vm.userPreferencesToString());
        css.setConfiguration(vm.getSelectedConfiguration());
        
        /*
        if(vm.isDraw())
        	System.out.println("Ha habido empate de" +vm.getDrawCount()+ "preferencias");
        else
        	System.out.println("No hay empate");
        	
        	*/

     
    }



    /**
     * Aceptable si tiene nota media, cada agente puede tener una extensión de
     * esta clase con una implementacion distinta.
     *
     * @param configuration
     * @param css
     * @param acceptableIfNothingIsAcceptable, si nada alcanza la nota media se
     * devuelve que es aceptable
     * @return
     */
    public boolean isAcceptable(String configuration, SharedService css, boolean acceptableIfNothingIsAcceptable) {
        int acceptableMark = upperBoundForCalification / 2;
        if (this.getPreferences(css).get(configuration) >= acceptableMark) {
            return true;
        }
        //si no tengo nada calificado como aceptable se considera que lo es.
        if (this.getOrderedPreferences(css).get(0).y < acceptableMark && acceptableIfNothingIsAcceptable) {
            return true;
        }
        return false;
    }

    /**
     * Is wanted? Method used to pay weight of negotiation when argumenting.
     * True if the configuration is in the "wantedcriteria".
     * With wantedcriteria=1, only the most wanted
     * configuration is considered to return true;
     *
     * @param configuration
     * @param css
     * @return
     */
    public boolean isWanted(String configuration, SharedService css) {
        for (int i = 0; i < wantedcriteria; i++) {
            String p = getNextPreference(user, css, i);
            if (p.equals(configuration)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Given a service, it returns a number with the number of shared wanted configurations
     * It can be used before selecting a service to go with people alike.
     * @param css
     * @return 
     */
    public int getNumberOfCommonWantedConfigurations(SharedService css){
        int counter=0;
        for(UserInterface ui: css.getUsers()){
            HashMap<String,Integer> preferences= ui.getNegotiation().getPreferences(css);
            for(String s: preferences.keySet()){
                if(ui.getNegotiation().isWanted(s, css) && this.isWanted(s, css)) counter++;
            }            
        }
        return counter;
    }


}
