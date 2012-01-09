/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DAdminInterface.java
 *
 * Created on 08.01.2012, 21:30:06
 */
package durakServer;

/**
 *
 * @author Alexey
 */
public class DAdminInterface extends javax.swing.JFrame {

    public static DAdmin admin;
    /** Creates new form DAdminInterface */
    public DAdminInterface() {
        initComponents();
        admin.setInterface(this);
        admin.startServer();
        admin.startGathering();
    }
    
    public void refreshPlayerList()
    {
        jClientList.removeAll();
        for (String plName : admin.getPlayerList())
        {
            jClientList.add(plName);
        }
        jReservedPlayers.removeAll();
        for (String plName : admin.getReservedPlayerList())
        {
            jReservedPlayers.add(plName);
        }
    }
    public void addMessage(String s)
    {
        jAdminMessagesList.add(s, 0);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jClientList = new java.awt.List();
        jReservedPlayers = new java.awt.List();
        jClientListLabel = new java.awt.Label();
        jReservedPlayerListLabel = new java.awt.Label();
        jStartGame = new java.awt.Button();
        jStopGame = new java.awt.Button();
        jAdminMessagesList = new java.awt.List();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jClientList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jClientListActionPerformed(evt);
            }
        });

        jReservedPlayers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jReservedPlayersActionPerformed(evt);
            }
        });

        jClientListLabel.setText("Клиенты");

        jReservedPlayerListLabel.setText("Потенциальные игроки");

        jStartGame.setLabel("Начать игру");
        jStartGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jStartGameActionPerformed(evt);
            }
        });

        jStopGame.setLabel("Остановить игру");
        jStopGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jStopGameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jAdminMessagesList, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jClientList, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jClientListLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jReservedPlayers, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jReservedPlayerListLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jStartGame, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jStopGame, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jClientListLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jClientList, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jReservedPlayerListLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jStartGame, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(7, 7, 7)
                                .addComponent(jStopGame, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jReservedPlayers, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jAdminMessagesList, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jClientListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jClientListActionPerformed
        // TODO add your handling code here:
        if (admin.addPlayer(jClientList.getSelectedIndex()))
            refreshPlayerList();
    }//GEN-LAST:event_jClientListActionPerformed

    private void jReservedPlayersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jReservedPlayersActionPerformed
        // TODO add your handling code here:
        if (admin.removePlayer(jReservedPlayers.getSelectedIndex()))
            refreshPlayerList();
    }//GEN-LAST:event_jReservedPlayersActionPerformed

    private void jStartGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jStartGameActionPerformed
        // TODO add your handling code here:
        admin.createDealer();
        admin.startGame();
    }//GEN-LAST:event_jStartGameActionPerformed

    private void jStopGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jStopGameActionPerformed
        // TODO add your handling code here:
        admin.stopGame();
































    }//GEN-LAST:event_jStopGameActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DAdminInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DAdminInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DAdminInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DAdminInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new DAdminInterface().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.List jAdminMessagesList;
    private java.awt.List jClientList;
    private java.awt.Label jClientListLabel;
    private java.awt.Label jReservedPlayerListLabel;
    private java.awt.List jReservedPlayers;
    private java.awt.Button jStartGame;
    private java.awt.Button jStopGame;
    // End of variables declaration//GEN-END:variables
}
