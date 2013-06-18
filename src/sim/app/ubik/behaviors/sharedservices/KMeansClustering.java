/*
* 
* 
* This file is part of VoteSim. VoteSim is a UbikSim library. 
* 
* VoteSim has been developed by members of the research Group on 
* Intelligent Systems [GSI] (Grupo de Sistemas Inteligentes), 
* acknowledged group by the  Technical University of Madrid [UPM] 
* (Universidad Politécnica de Madrid) 
* 
* Authors:
* Mercedes Garijo
* Carlos A. Iglesias
* Pablo Moncada
* Emilio Serrano
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
 */package sim.app.ubik.behaviors.sharedservices;

import java.util.ArrayList;
import java.util.List;

import es.upm.dit.gsi.voting.VotingMethod;

import sim.app.ubik.Ubik;
import sim.app.ubik.domoticDevices.SharedService;
import sim.app.ubik.people.Person;
import sim.util.MutableInt2D;
import weka.clusterers.AbstractClusterer;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;


/**
 * Clase para preseleccionar un grupo en base a clustering.
 */
public class KMeansClustering {
    
    Ubik ubik;
    List<SharedService> slist;
    FastVector attributes;
    boolean echo;
    VotingMethod vm;
  
    
    public KMeansClustering(Ubik u, VotingMethod vm){
        this.ubik=u;
        this.vm = vm;
         slist = SharedService.getServices(ubik, 0);     
         generateAttributes(false);
        
    }
    
    
    public SharedService getRecommendation(UserInterface ui){
        try {
            clusteringOfUsers();
            Instance instance = this.getInstance2(ui);
            AbstractClusterer ac=  clusteringOfUsers();            
            if(ac==null) return slist.get(ubik.random.nextInt(slist.size())); // no había instancias, servicio aleatorio            
            int cluster= ac.clusterInstance(instance);                
            if(echo){
                System.out.println(ui.getName() + ", recommended service: " + slist.get(cluster).getName());
                System.out.println(ac.toString());              
            }
            return slist.get(cluster);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * Clusters de usuarios en base a preferencias
     */
    private AbstractClusterer clusteringOfUsers() {    
        Instances ins =generateTrainingData();                
        if(ins.numInstances()!=0){
            return buildKmeans(ins,slist.size());
                                          
        }
        return null;
    }
    /**
     * Datos de entrenamiento,crea una instancia por cada persona que actualmente esté usando 
     * un servicio con sus preferencias.
     * @return 
     */
    private Instances generateTrainingData(){   
           
        Instances ins= new Instances("usersProfile", attributes, 1000);
        for(SharedService ss: slist){            
            for(UserInterface ui: ss.getUsers()){                
                ins.add(getInstance(ui));
            }
        }
        return ins;
    }
    
    /**
     * Cluster based on kmeans.
     * @param ins
     * @param clusters
     * @return 
     */
      private AbstractClusterer buildKmeans(Instances ins, int clusters) {
        try {                  
            SimpleKMeans  sk = new SimpleKMeans();
            sk.setNumClusters(clusters);               
            sk.buildClusterer(ins);
            //System.out.println(sk.toString());
            return sk;

        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("EXCEPTION GENERATING QUALTIATIVE MODEL ");
        }
        return null;
      }
      
      /**
       * 
       * @param withUser Includes a user field (the first) with the name
       */

    private void generateAttributes(boolean withUser) {
      
        attributes= new FastVector();
        if(withUser){
            List<Person> listp = ubik.getBuilding().getFloor(0).getPersonHandler().getPersons();
            FastVector my_nominal_values = new FastVector(listp.size()); 
            for(Person p: listp){
                my_nominal_values.addElement(p.getName());
            }
             attributes.addElement(new Attribute("user", my_nominal_values)); 
        }
       
        
        for(int i=0;i<slist.get(0).getConfigurations().length;i++){
            attributes.addElement(new Attribute(slist.get(0).getConfigurations()[i]));
        }
     
    }

    /**
     * Obtener instancia de una persona en el cluster
     * @param ui
     * @return 
     */
    private Instance getInstance(UserInterface ui) {
        SharedService ss= slist.get(0);
        Instance inst =  new Instance(ss.getConfigurations().length); 
                           
                for(int i=0;i<ss.getConfigurations().length;i++){
                    String nameConf=ss.getConfigurations()[i];
                    int valueConf= ui.getNegotiation().getPreferences(ss).get(nameConf);                                   
                    inst.setValue((Attribute) attributes.elementAt(i), valueConf);
         }
         return inst;
    }
    
    private  Instance getInstance2(UserInterface ui) {
    	
    	SharedService ss= slist.get(0);
     	Instance inst =  new Instance(ss.getConfigurations().length);
     	vm.setCss(ss);
        ArrayList<MutableInt2D> votes = vm.getUserVotes(ui);
                           
        for(int i=0;i<ss.getConfigurations().length;i++){                                          
            inst.setValue((Attribute) attributes.elementAt(i), votes.get(i).y);
         }
         return inst;
    }
    
    
    
 


}
