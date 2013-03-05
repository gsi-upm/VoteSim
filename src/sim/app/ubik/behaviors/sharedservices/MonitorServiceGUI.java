/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.app.ubik.behaviors.sharedservices;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import sim.app.ubik.chart.GenericChart;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import sim.app.ubik.Ubik;
import sim.app.ubik.UbikSimLauncher;
import sim.app.ubik.behaviors.PositionTools;
import sim.app.ubik.domoticDevices.SharedService;
import sim.app.ubik.people.Person;
import sim.app.ubik.people.PersonHandler;
import sim.app.ubik.utils.GenericLogger;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Int2D;
import sim.util.MutableDouble;
import ubiksimdist.SharedServicesSim;

/**
 *
 * @author esfupm
 */
public class MonitorServiceGUI extends javax.swing.JFrame implements Steppable, Stoppable {

    /**
     * Creates new form MonitorServices
     */
    
    
    protected MonitorService ms;
    protected SharedServicesSim sss;
    protected Ubik ubik;
    protected boolean extraInitDone=false;//init when the monitor service has been created

    
    public MonitorServiceGUI(GUIState guiState) {
        initComponents(); 
        sss =((SharedServicesSim) guiState.state); 
        ubik=sss;
        ms = sss.ms;
        ubik.schedule.scheduleRepeating(this);
        
        
    }
    
   

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        log = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jcomboneg = new javax.swing.JComboBox();
        jSpinnerAgents = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        nusers = new javax.swing.JLabel();
        satisfaction = new javax.swing.JLabel();
        chart = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        asatis = new javax.swing.JLabel();
        chart1 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        labelmomentsofconflict = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        labeluserwithwanted = new javax.swing.JLabel();
        labeluserwithwantedacc = new javax.swing.JLabel();
        chart2 = new javax.swing.JButton();
        chart3 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        worstwait = new javax.swing.JLabel();
        chart4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Monitor Shared Services");

        log.setColumns(20);
        log.setFont(new java.awt.Font("Lucida Console", 0, 13)); // NOI18N
        log.setRows(5);
        jScrollPane1.setViewportView(log);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Parameters"));

        jLabel4.setText("Negotiation ");

        jcomboneg.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Order of arrival", "Voting ", "Voting + acceptable for all" }));
        jcomboneg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcombonegActionPerformed(evt);
            }
        });

        jSpinnerAgents.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), null, Integer.valueOf(500), Integer.valueOf(1)));
        jSpinnerAgents.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerAgentsStateChanged(evt);
            }
        });

        jLabel7.setText("Agents");

        jLabel10.setText("Preselection");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Closest", "Common wanted/users", "Clustering" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jcomboneg, 0, 301, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSpinnerAgents, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jcomboneg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jSpinnerAgents, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel10)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Output"));

        jLabel2.setText("Current num. of users:");

        jLabel1.setText("Satisfaction per users: ");

        nusers.setText("0");

        satisfaction.setText("0");

        chart.setText("chart");
        chart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chartActionPerformed(evt);
            }
        });

        jLabel5.setText("Accumulated:");

        asatis.setText("0");

        chart1.setText("chart");
        chart1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chart1ActionPerformed(evt);
            }
        });

        jLabel6.setText("Moments of conflict:");

        labelmomentsofconflict.setText("0");

        jLabel8.setText("Users with accep. conf: ");

        jLabel9.setText("Accumulated:");

        labeluserwithwanted.setText("0");

        labeluserwithwantedacc.setText("0");

        chart2.setText("chart");
        chart2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chart2ActionPerformed(evt);
            }
        });

        chart3.setText("chart");
        chart3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chart3ActionPerformed(evt);
            }
        });

        jLabel3.setText("Worst wait:");

        worstwait.setText("0");

        chart4.setText("chart");
        chart4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chart4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addGap(34, 34, 34))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addGap(8, 8, 8)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel8)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(asatis, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                        .addComponent(nusers)
                        .addComponent(satisfaction, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(labelmomentsofconflict)
                    .addComponent(labeluserwithwanted)
                    .addComponent(labeluserwithwantedacc)
                    .addComponent(worstwait, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chart, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
                    .addComponent(chart1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(chart2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(chart3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(chart4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(nusers))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(labelmomentsofconflict))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(satisfaction)
                    .addComponent(chart, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(asatis, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chart1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(labeluserwithwanted)
                    .addComponent(chart2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(labeluserwithwantedacc)
                    .addComponent(chart3, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(worstwait, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chart4, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jcombonegActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcombonegActionPerformed
        Negotiation.codeOfNegotiation= jcomboneg.getSelectedIndex();
        clearLogs();
    }//GEN-LAST:event_jcombonegActionPerformed

    private void chartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chartActionPerformed
            new GenericChart(ubik, ms.momentsOfConflict,ms.satisfactionPerUsers, "Satisfaction per users", "Moments of conflict", "Users");
           
    }//GEN-LAST:event_chartActionPerformed

    private void chart1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chart1ActionPerformed
            new GenericChart(ubik, ms.momentsOfConflict,ms.satisfactionAccumulated, "Accumulated satisfaction per users", "Moments of conflict", "Users");
    }//GEN-LAST:event_chart1ActionPerformed

    private void jSpinnerAgentsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerAgentsStateChanged
        PersonHandler   ph=ubik.getBuilding().getFloor(0).getPersonHandler();
        int nagents= ph.getPersons().size();
        int selection = (Integer) jSpinnerAgents.getModel().getValue();
        if(nagents<selection){
             ph.addPersons(selection-nagents, true, ph.getPersons().get(0));
             ph.changeNameOfAgents("a");
            
        }
        if(nagents>selection){
             ph.removePersons(nagents-selection);          
        }
        
    }//GEN-LAST:event_jSpinnerAgentsStateChanged

    private void chart2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chart2ActionPerformed
          new GenericChart(ubik, ms.momentsOfConflict,ms.usersWithAcceptableConfigurations, "Rate of users with wanted configurations", "Moments of conflict", "Users");
    }//GEN-LAST:event_chart2ActionPerformed

    private void chart3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chart3ActionPerformed
 new GenericChart(ubik, ms.momentsOfConflict,ms.usersWithAcceptableConfigurationsAccumulated, "Accumulated rate of users with wanted configurations", "Moments of conflict", "Users");
    }//GEN-LAST:event_chart3ActionPerformed

    private void chart4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chart4ActionPerformed
            new GenericChart(ubik, ms.momentsOfConflict,ms.maxWithoutService, "Maximum time without a configuration wanted", "Moments of conflict", "Time");
    }//GEN-LAST:event_chart4ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
       UsingSharedService.selectionCode= this.jComboBox1.getSelectedIndex();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel asatis;
    private javax.swing.JButton chart;
    private javax.swing.JButton chart1;
    private javax.swing.JButton chart2;
    private javax.swing.JButton chart3;
    private javax.swing.JButton chart4;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jSpinnerAgents;
    private javax.swing.JComboBox jcomboneg;
    private javax.swing.JLabel labelmomentsofconflict;
    private javax.swing.JLabel labeluserwithwanted;
    private javax.swing.JLabel labeluserwithwantedacc;
    private javax.swing.JTextArea log;
    private javax.swing.JLabel nusers;
    private javax.swing.JLabel satisfaction;
    private javax.swing.JLabel worstwait;
    // End of variables declaration//GEN-END:variables

    /**
     * Actualizar el texto mostrado por el monitor
     *
     * @param ss
     */
    public void step(SimState ss) {
        if(!this.extraInitDone){
             
            jcomboneg.setSelectedIndex(Negotiation.codeOfNegotiation);           
            jSpinnerAgents.getModel().setValue(ubik.getBuilding().getFloor(0).getPersonHandler().getPersons().size());
            extraInitDone=true;
        }

        //System.out.println("STEP " + ubik.schedule.getSteps());
        //cargar registro: para cada servicio, para cada usuario rellenar datos
        jSpinnerAgents.getModel().setValue(ubik.getBuilding().getFloor(0).getPersonHandler().getPersons().size());  
        // actualizar etiquetas
        if (!log.getText().equals(ms.text)) log.setText(ms.text);        
         labelmomentsofconflict.setText(Integer.toString(ms.momentsOfConflict.intValue()));
        this.nusers.setText(Integer.toString(ms.numberOfUsersUsingServices));
        this.asatis.setText(Integer.toString(ms.satisfactionAccumulated.intValue()));
        labeluserwithwanted.setText(Float.toString(ms.usersWithAcceptableConfigurations.floatValue()));
        labeluserwithwantedacc.setText(Float.toString(ms.usersWithAcceptableConfigurationsAccumulated.floatValue()));
        this.satisfaction.setText(Float.toString(ms.satisfactionPerUsers.floatValue()));
        this.worstwait.setText((new Integer(ms.maxWithoutService.intValue()).toString()));              
    }

    public void stop() {
    }



    /**
     * Borra valores acumulables 
     */
    private void clearLogs() {
        if(ms==null) return;
        ms.satisfactionAccumulated.val=0;
        ms.usersWithAcceptableConfigurationsAccumulated.val=0;
        ms.maxWithoutService.val=0;
        this.worstwait.setText("0");
        this.labeluserwithwantedacc.setText("0");
        this.asatis.setText("0");
    }

  

   
}
