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
 */
package votesimdist;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

import sim.app.ubik.behaviors.sharedservices.MonitorService;
import sim.app.ubik.behaviors.sharedservices.Preferences;
import sim.app.ubik.behaviors.sharedservices.AgreementServiceAgent;
import sim.app.ubik.utils.GenericLogger;

/**
 * Clase para experimentación de servicios compartidos.
 * Cuidado! se acaba la memoria de montón, no poner más de 12 tandas de 1000 experimentos
 */
public class VoteSimBatch {

    static int experiments = 20;
    static int timeForExperiment = 2000;  

    /* 
     * Voting Methods que se van a ejecutar
     * 0 = Order of Arrival
     * 1 = Range voting
     * 2 = Range voting + acceptable for all
     * 3 = Plurality
     * 4 = Cumulative
     * 5 = Approval
     * 6 = Borda
     * 7 = Weight
     * 8 = Approval more than five
     */
    static final int[] votingMethods = {0,1,3,4,5,6,7};
    
    /*
     * Preselection Methods que se van a usar
     * 0 = Closest (No preselection)
     * 1 = Common / wanted users
     * 2 = K-means clustering
     * 3 = EM Clustering
     * 4 = Euclidean distance
     * 5 = Manhattan distance 
     */
    static final int[] preselectionMethods = {0,2,3,4,5};
    
    
    static ArrayList<String> headings;
    static String fileName;
    
    public static void main(String[] args) throws IOException {

      
      String date = (new Date()).toString().replace(':', '.');
      fileName = "Batchoutput " + date;      
     //media para configuracion en headings.get(0), sd para misma, media en conf para headings.get(1)...etc
        ArrayList<GenericLogger> r = experimentsForDifferentParameters();        
        printInFile(r);
        System.exit(0);
    }
/**
 * Diferentes hornadas de experimentos para cada parámetro
 * 2 generic loggers por cada configuración (con  media y desviación), ver método batchOfExperiments
 * @return 
 */
    private static ArrayList<GenericLogger> experimentsForDifferentParameters() throws IOException {

        ArrayList<GenericLogger> r = new ArrayList<GenericLogger>();
        headings = new ArrayList<String>();//cabeceras extra para cada generic logger
        
            for (int j : preselectionMethods) {
            	
            	System.out.println("####################################");
            	System.out.println("Ejecutando preseleccion numero: "+j);
            	System.out.println("####################################");
            	
                for (int z : votingMethods) {//Los definidos arriba
                	
                	System.out.println("******************************************");
                	System.out.println("Ejecutando Metodo de votacion numero: "+z);
                	System.out.println("******************************************");
                    AgreementServiceAgent.selectionCode = j;
                    AgreementServiceAgent.setCodeOfNegotiation(z);
                    r.addAll(batchOfExperiments());
                    String heading =  " preselection " + AgreementServiceAgent.selectionCode + " votingS " + AgreementServiceAgent.getCodeOfNegotiation();
                    PrintWriter w3 = new PrintWriter(new BufferedWriter(new FileWriter(fileName + " output.txt", true)));
                    w3.println( heading + " " + (new Date()).toString());
                    w3.close();
                    headings.add(heading);//para media
                    headings.add(heading);//para desviacion
                    deleteTempFiles();
                    System.out.println("Terminado de ejecutar VotingMethod Numero "+z);
                 
                }
        }
        return r;
    }

    /**
     * Hornada de experimentos
     * ArrayList con dos genericloggers: media y desviación para una hornada de
     * experimentos
     *
     * @return
     */
    public static ArrayList<GenericLogger> batchOfExperiments() {
        ArrayList<GenericLogger> listOfResults = new ArrayList<GenericLogger>();

        for (int i = 0; i < experiments; i++) {
        	System.out.println("////////////////////////////////////////");
        	System.out.println("Ejecucion del experimento numero: "+i);
        	System.out.println("////////////////////////////////////////");
        	
            GenericLogger gl1 = oneExperiment(i*1000);
            listOfResults.add(gl1);
        }

        System.out.println(listOfResults);

        ArrayList<GenericLogger> r = new ArrayList<GenericLogger>();
        r.add(GenericLogger.getMean(listOfResults));
        r.add(GenericLogger.getStandardDeviation(listOfResults));
        return r;

    }

    /**
     * Un experimento simple
     * @param seed
     * @return
     */
    public static GenericLogger oneExperiment(int seed) {
       Preferences.setEcho(false); 
       VoteSim state = new VoteSim(seed,timeForExperiment );
        state.start();               
        do{        		
                if (!state.schedule.step(state))             	
                	break;                
        		
        }while(state.ms.momentsOfConflict.val<  MonitorService.momentOfConflictToStop*2);//para que termine desde el monitor
        state.finish();

        return state.ms.gl;                
    }

    private static void deleteTempFiles() throws IOException {
        File f = new File("C:\\Users\\esfupm\\eTeks");         
        FileUtils.deleteDirectory(f);                   
    }

    private static void printInFile(ArrayList<GenericLogger> r) throws IOException {    
                
        PrintWriter w1 = new PrintWriter(new BufferedWriter(new FileWriter(fileName + " mean.txt", false)));
        PrintWriter w2 = new PrintWriter(new BufferedWriter(new FileWriter(fileName + " sd.txt", false)));        
        
        //headings
         w1.print("step\t");
           w2.print("step\t");
            for (int i = 0; i < r.size(); i++) {
                if (i % 2 == 0) {//media en los pares
                  w1.print(r.get(i).getHeadings(null,headings.get(i)));  
                }
                else{
                   w2.print(r.get(i).getHeadings(null,headings.get(i)));  
                }
                              
            }
            w1.println();
            w2.println();
        
        //datos fila  a fila para meter en una fila distintas hornadas
        for (int step = 0; step < timeForExperiment; step++) {
            w1.print(step+1 + "\t");
            w2.print(step+1 + "\t");
            for (int i = 0; i < r.size(); i++) {
                if (i % 2 == 0) {//media en los pares
                  w1.print(r.get(i).toString(step));  
                } else {//desviación en impares
                  w2.print(r.get(i).toString(step));  
                }
            }
            w1.println();
            w2.println();
        }
        
        w1.close();
        w2.close();

    }
}