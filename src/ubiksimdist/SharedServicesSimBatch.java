/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ubiksimdist;


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
import sim.app.ubik.behaviors.sharedservices.UsingSharedService;
import sim.app.ubik.utils.GenericLogger;

/**
 * Clase para experimentación de servicios compartidos.
 * Cuidado! se acaba la memoria de montón, no poner más de 12 tandas de 1000 experimentos
 * @author Emilio Serrano, emilioserra@um.es
 * @author Pablo Moncada, pmoncada@dit.upm.es
 */
public class SharedServicesSimBatch {

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
    static final int[] preselectionMethods = {1,2};
    
    
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
       // for (int i = 0; i < 1; i++) {//cada función de satisfacción hasta i 2
            for (int j : preselectionMethods) {
            	System.out.println("####################################");
            	System.out.println("Ejecutando preseleccion numero: "+j);
            	System.out.println("####################################");
                for (int z : votingMethods) {//Los definidos arriba
                   // UsingSharedService.setCodeOfSatisfactionFuction(i);
                	System.out.println("******************************************");
                	System.out.println("Ejecutando Metodo de votacion numero: "+z);
                	System.out.println("******************************************");
                    UsingSharedService.selectionCode = j;
                    UsingSharedService.setCodeOfNegotiation(z);
                    r.addAll(batchOfExperiments());
                    //"satis.function " + UsingSharedService.getCodeOfSatisfactionFuction() +
                    String heading =  " preselection " + UsingSharedService.selectionCode + " votingS " + UsingSharedService.getCodeOfNegotiation();
                    PrintWriter w3 = new PrintWriter(new BufferedWriter(new FileWriter(fileName + " output.txt", true)));
                    w3.println( heading + " " + (new Date()).toString());
                    w3.close();
                    headings.add(heading);//para media
                    headings.add(heading);//para desviacion
                    deleteTempFiles();
                    System.out.println("Terminado de ejecutar VotingMethod Numero "+z);
                 
                }
            //}
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
            //w.println("EXPERIMENT " + i + " RESULTS ");
            //w.println(gl1.toString());
        }

        System.out.println(listOfResults);

        ArrayList<GenericLogger> r = new ArrayList<GenericLogger>();
        r.add(GenericLogger.getMean(listOfResults));
        r.add(GenericLogger.getStandardDeviation(listOfResults));
        return r;

    }

    /**
     * Un experimento simple
     *
     * @param seed
     * @return
     */
    public static GenericLogger oneExperiment(int seed) {
       Preferences.setEcho(false); 
       SharedServicesSim state = new SharedServicesSim(seed,timeForExperiment );
        state.start();               
        do{        		
                if (!state.schedule.step(state)) {
                	//JOptionPane.showMessageDialog(null,
                		    //"number of rows in log " + state.ms.gl.log.size() );                	
                	break;
                }
        		//if (!state.schedule.step(state)) return oneExperiment(seed+5);
        		
        }while(state.ms.momentsOfConflict.val<  MonitorService.momentOfConflictToStop*2);//para que termine desde el monitor
        state.finish();
        
        //for(int i = state.ms.gl.getRows(); i < timeForExperiment; i++)
        	//state.ms.gl.addStep(new double[] {0.0, 0.0, 0.0});
        
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