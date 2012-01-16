/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ChooseAdmin.java
 *
 * Created on 08.01.2012, 17:13:19
 */
package first;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alexey
 */
public class ChooseAdmin extends javax.swing.JFrame {

    /** Creates new form ChooseAdmin */
    private LinkedList<Class<Admin>> admins;
    public ChooseAdmin() {
        initComponents();
        
        LinkedList<File> AllFiles = new LinkedList<File>();
        LinkedList<File> dir = new LinkedList<File>();
        dir.add(new File("."));
        dir.add(new File(
                  "d:/Programming/JavaProjects/NetworkCards/build/classes/"));
        do
        {
            LinkedList<File> dirtmp = new LinkedList<File>();
            for (File d : dir)
            {
                LinkedList<File> filesInDir = new LinkedList<File>(
                        java.util.Arrays.asList(
                        d.listFiles(new java.io.FileFilter() {

                        @Override
                        public boolean accept(File pathname) {
                            return pathname.isDirectory() 
                                   || pathname.getName().matches(".*class$");
                        }
                } )));
                for (File f : filesInDir)
                {
                    if (f.isDirectory())
                    {
                        if (!"first".equals(f.getName()) && !"dist".equals(f.getName()))
                        dirtmp.add(f);
                    }
                    else
                        AllFiles.add(f);
                }
            }
            dir = dirtmp;
        } while (dir.size() > 0);
        admins = new LinkedList<Class<Admin>>();
        GamesLoader loader = new GamesLoader(ClassLoader.getSystemClassLoader());
        for (File f : AllFiles)
        {
                /*Path path = f.toPath();
                boolean check = false;
                int i, j;
                for (i = 0; i < path.getNameCount() && !check; i++)
                {
                    check = "classes".equals(path.getName(i).toString());
                }
                String name = "";
                for (j = i; j < path.getNameCount(); j++)
                {
                    name = name.concat(path.getName(j).toString()).concat(".");
                }*/
                String tmp[] = f.getPath().split("classes\\\\");
                if (tmp.length < 2)
                    continue;
                String name = tmp[tmp.length - 1].replace('\\', '.');
                name = name.split(".class")[0];
                Class c = null;
                
                if (!(name.startsWith("java"))) {
                   try {
                        c = loader.findClass(name, f);
                        
                    } catch (SecurityException ex) {
                        Logger.getLogger(
                                ChooseAdmin.class.getName())
                                    .log(Level.SEVERE, null, ex);
                    } catch (Exception ex) {
                        Logger.getLogger(
                                ChooseAdmin.class.getName())
                                    .log(Level.SEVERE, null, ex);
                    }
                }
                boolean check = false;
                if (c != null) {
                    check = Admin.class.isAssignableFrom(c);
                    if (check)
                        admins.add(c);
                }
        }
        for (Class<Admin> c : admins)
        {
            try {
                String gameName;
                Method m = c.getDeclaredMethod("gameName", (Class<?>[]) null);
                gameName = m.invoke(null, (Object[]) null).toString();
                jAdminList.add(gameName);
            }
            catch (IllegalAccessException ex) {
                Logger.getLogger(ChooseAdmin.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(ChooseAdmin.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(ChooseAdmin.class.getName()).log(Level.SEVERE, null, ex);
            }catch (NoSuchMethodException ex) {
                Logger.getLogger(ChooseAdmin.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(ChooseAdmin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        /*
        admins = Admin.class.getClasses();
        for (Class<?> c : admins)
        {
            jAdminList.add(c.getName());
        }*/
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jAdminList = new java.awt.List();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jAdminList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jAdminListActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jAdminList, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jAdminList, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jAdminListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jAdminListActionPerformed
        try {
            // TODO add your handling code here:
            this.setVisible(false);
            Admin a = admins.get(jAdminList.getSelectedIndex()).newInstance();
            a.runInterface();
            a.startServer();
            a.startGathering();
            
        } catch (InstantiationException ex) {
            Logger.getLogger(ChooseAdmin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ChooseAdmin.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_jAdminListActionPerformed

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
            java.util.logging.Logger.getLogger(ChooseAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChooseAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChooseAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChooseAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new ChooseAdmin().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.List jAdminList;
    // End of variables declaration//GEN-END:variables
}
