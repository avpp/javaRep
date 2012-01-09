/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DurakGameInterface.java
 *
 * Created on 08.01.2012, 17:59:18
 */
package durakVisualClient;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import first.Card.*;
import java.util.LinkedList;

/**
 *
 * @author Andrew
 */
public class DurakGameInterface extends javax.swing.JFrame {
    
    private class DrawGameCanvas extends Canvas {
        public DrawGameCanvas() {
            super();
        }
        
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            int width = m_canvas.getWidth();
            int height = m_canvas.getHeight();
            g.drawImage(m_cloth, 0, 0, width, height, null);
        }
    }
    
    //Ссылка на клиент, который создал форму
    private DurakPlayer m_durakClient;
    //Индекс карты, начиная с которой нужно
    //показывать карты
    private int m_currCardInd;

    /* ДЛЯ ГРАФИКИ */
    //Канва для рисования всякой всячины
    private DrawGameCanvas m_canvas;
    //Массивы с картинками карт
    BufferedImage[] m_imgsSpades;
    BufferedImage[] m_imgsDiamonds;
    BufferedImage[] m_imgsClubs;
    BufferedImage[] m_imgsHearts;
    //Сукно стола
    BufferedImage m_cloth;
    
    /** Creates new form DurakGameInterface */
    public DurakGameInterface() {
        initComponents();
        
        loadCardImages("./src/durakVisualClient/images/cards/");
        
        m_canvas = new DrawGameCanvas();
        int width = this.getWidth();
        int height = this.getHeight();
        m_canvas.setBounds(0, 0, width, height);
        this.add(m_canvas);
        
        String s = "./src/durakVisualClient/images/cloth.jpg";
        BufferedImage img = null;
            try {
                img = ImageIO.read(new File(s));
            } catch (IOException ex) {
                Logger.getLogger(DurakGameInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
        m_cloth = img;
        
        drawAll();
    }

    public void drawAll() {
        jBtn1.setIcon(new ImageIcon(m_imgsClubs[4]));
        jBtn2.setIcon(new ImageIcon(m_imgsDiamonds[9]));
        jBtn3.setIcon(new ImageIcon(m_imgsDiamonds[12]));
        jBtn4.setIcon(new ImageIcon(m_imgsSpades[10]));
        jBtn5.setIcon(new ImageIcon(m_imgsHearts[7]));
        jBtn6.setIcon(new ImageIcon(m_imgsSpades[5]));
        jBtnCardLeft.setVisible(false);
        //jBtnCardRight.setVisible(false);
    }
    
    private void loadCardImages(String path) {
        int all = 14;
        m_imgsSpades = new BufferedImage[all];
        m_imgsDiamonds = new BufferedImage[all];
        m_imgsHearts = new BufferedImage[all];
        m_imgsClubs = new BufferedImage[all];
        
        String[] pImgs = new File(path).list();
        for (String s : pImgs) {
            String color = s.substring(0, 1);
            String value = s.substring(1, 2);
            
            String name = "c".concat(value);
            
            int ind = first.Card.Value.valueOf(name).ordinal();
            
            BufferedImage img = null;
            try {
                img = ImageIO.read(new File(path.concat(s)));
            } catch (IOException ex) {
                Logger.getLogger(DurakGameInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if ("s".equals(color))
                m_imgsSpades[ind] = img;
            else if ("c".equals(color))
                m_imgsClubs[ind] = img;
            else if ("d".equals(color))
                m_imgsDiamonds[ind] = img;
            else if ("h".equals(color))
                m_imgsHearts[ind] = img;
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jBtn1 = new javax.swing.JButton();
        jBtn2 = new javax.swing.JButton();
        jBtn3 = new javax.swing.JButton();
        jBtn4 = new javax.swing.JButton();
        jBtn5 = new javax.swing.JButton();
        jBtn6 = new javax.swing.JButton();
        jBtnCardRight = new javax.swing.JButton();
        jBtnCardLeft = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("Form"); // NOI18N
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(netcards.NetCardsApp.class).getContext().getResourceMap(DurakGameInterface.class);
        jBtn1.setText(resourceMap.getString("jBtn1.text")); // NOI18N
        jBtn1.setName("jBtn1"); // NOI18N
        jBtn1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jBtn1MouseClicked(evt);
            }
        });

        jBtn2.setText(resourceMap.getString("jBtn2.text")); // NOI18N
        jBtn2.setName("jBtn2"); // NOI18N

        jBtn3.setText(resourceMap.getString("jBtn3.text")); // NOI18N
        jBtn3.setName("jBtn3"); // NOI18N

        jBtn4.setText(resourceMap.getString("jBtn4.text")); // NOI18N
        jBtn4.setName("jBtn4"); // NOI18N

        jBtn5.setText(resourceMap.getString("jBtn5.text")); // NOI18N
        jBtn5.setName("jBtn5"); // NOI18N

        jBtn6.setName("jBtn6"); // NOI18N

        jBtnCardRight.setText(resourceMap.getString("jBtnCardRight.text")); // NOI18N
        jBtnCardRight.setName("jBtnCardRight"); // NOI18N

        jBtnCardLeft.setText(resourceMap.getString("jBtnCardLeft.text")); // NOI18N
        jBtnCardLeft.setName("jBtnCardLeft"); // NOI18N

        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N

        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(79, 79, 79)
                .addComponent(jBtnCardLeft, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBtn3, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBtn4, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBtn5, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBtn6, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jBtnCardRight, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jButton2)
                .addContainerGap(39, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(558, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jBtn5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jBtn1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jBtn2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jBtn4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                            .addComponent(jBtn3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jBtnCardLeft, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(21, 21, 21))
                            .addComponent(jBtn6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jBtnCardRight, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20))
                            .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(28, 28, 28))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        int width = this.getWidth();
        int height = this.getHeight();
        m_canvas.setBounds(0, 0, width, height);
}//GEN-LAST:event_formComponentResized

private void jBtn1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBtn1MouseClicked
// TODO add your handling code here:
}//GEN-LAST:event_jBtn1MouseClicked

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
            java.util.logging.Logger.getLogger(DurakGameInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DurakGameInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DurakGameInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DurakGameInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new DurakGameInterface().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtn1;
    private javax.swing.JButton jBtn2;
    private javax.swing.JButton jBtn3;
    private javax.swing.JButton jBtn4;
    private javax.swing.JButton jBtn5;
    private javax.swing.JButton jBtn6;
    private javax.swing.JButton jBtnCardLeft;
    private javax.swing.JButton jBtnCardRight;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    // End of variables declaration//GEN-END:variables
}


