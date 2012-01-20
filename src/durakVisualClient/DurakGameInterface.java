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

import durakVisualClient.DurakPlayer.ThrowOrTrump;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import netCardInterfaces.Card;
import netCardInterfaces.Card.*;
import netCardInterfaces.GamblingTable;
import netCardInterfaces.Turn;
import java.awt.Font;
import java.awt.Graphics2D;
import java.net.URL;
import java.util.LinkedList;
import javax.swing.JButton;

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
            
            int width = getWidth();
            int height = getHeight();
            
            Graphics2D g2d = (Graphics2D)g;
            
            drawCloth(g, width, height);
            drawGamblingTable(g);
            drawThrowOrTrump(g2d, width, height);
            drawDeck(g2d, width, height);
            drawPlayers(g2d, width, height);
        }
    }
    
    private DurakPlayer m_durakClient;
    
    public void setDurakPlayer(DurakPlayer d) {
        m_durakClient = d;
    }
    
    public DurakPlayer getDurakPlayer() {
        return m_durakClient;
    }
    
    //Индекс карты, начиная с которой нужно
    //показывать карты
    private int m_currCardInd;

    private LinkedList<JButton> m_userBtnCards;
    
    /* ДЛЯ ГРАФИКИ */
    //Канва для рисования всякой всячины
    private DrawGameCanvas m_canvas;
    
    BufferedImage[] m_imgsSpades;
    BufferedImage[] m_imgsDiamonds;
    BufferedImage[] m_imgsClubs;
    BufferedImage[] m_imgsHearts;
    
    BufferedImage m_cloth;
    
    /*
     * Рубашки карт (вертикально и горизонтально
     * расположенные)
     */
    BufferedImage m_backHoriz;
    BufferedImage m_backVertic;
    /*
     * Координаты начала отрисовки
     * игровой ситуации
     */
    int m_gambTabX;
    int m_gambTabY;
    /*
     * Смещения для отрисовки карт
     * gamblingTable
     * на игровом столе
     */
    int m_stackShiftX;
    int m_stackShiftY;
    int m_cardInStackShiftX;
    int m_cardInStackShiftY;
    
    /** Creates new form DurakGameInterface */
    public DurakGameInterface() {
        initComponents();
        
        m_currCardInd = 0;
        
        jBtnGetOrPass.setText("Затянуть/Пропустить");
        m_userBtnCards = new LinkedList<JButton>();
        m_userBtnCards.add(jBtn1);
        m_userBtnCards.add(jBtn2);
        m_userBtnCards.add(jBtn3);
        m_userBtnCards.add(jBtn4);
        m_userBtnCards.add(jBtn5);
        m_userBtnCards.add(jBtn6);
        
        String str = "";
        loadCardImages(str);
        loadCloth(str);
        
        m_canvas = new DrawGameCanvas();
        int width = this.getWidth();
        int height = this.getHeight();
        m_canvas.setBounds(0, 0, width, height);
        this.add(m_canvas);
        
        m_gambTabX = width / 4;
        m_gambTabY = (int)(height / 2.5);
        
        m_stackShiftX = 100;
        m_stackShiftY = 150;
        
        m_cardInStackShiftX = 15;
        m_cardInStackShiftY = 25;
    }

    public void drawAll() {
        int width = m_canvas.getWidth();
        int height = m_canvas.getHeight();
        BufferedImage img = new BufferedImage(
                width, height, BufferedImage.TYPE_INT_BGR);
        Graphics2D g2d = (Graphics2D)img.getGraphics();
        
        drawCloth(g2d, width, height);
        drawUserCards();
        drawGamblingTable(g2d);
        drawThrowOrTrump(g2d, width, height);
        drawDeck(g2d, width, height);
        drawPlayers(g2d, width, height);
        
        setBtnNames();
        
        m_canvas.getGraphics().drawImage(img, 0, 0, null);
    }
    
    private void drawCloth(Graphics g, int width, int height) {
        g.drawImage(m_cloth, 0, 0, width, height, null);
    }
    
    private void drawGamblingTable(Graphics g) {
        if (m_durakClient == null)
            return;
        
        int x = m_gambTabX - m_stackShiftX;
        int y = m_gambTabY - m_stackShiftY;
        GamblingTable gamt = m_durakClient.getM_gambTable();
        int countInRow = 3;
        int count = 0;
        
        for (LinkedList<Turn> stack : gamt.table) {
            count++;
            
            if (count == countInRow + 1) {
                x = m_gambTabX;
                y += m_stackShiftY;
            } else {
                x += m_stackShiftX;
            }
            
            int inStackX = x - m_cardInStackShiftX;
            int inStackY = y - m_cardInStackShiftY;
            
            for (Turn t : stack) {
                inStackX += m_cardInStackShiftX;
                inStackY += m_cardInStackShiftY;
                
                BufferedImage img = imgFromCard(t.getCard());
                g.drawImage(img, inStackX, inStackY, null);
            }
        }
    }
    
    private void drawUserCards() {
        if (m_durakClient == null)
            return;
        
        int visibleOnHands = m_userBtnCards.size();
        int allCards = m_durakClient.getM_cards().size();
        
        for (JButton btn : m_userBtnCards) {
            btn.setVisible(false);
        }
        /*if (m_durakClient.getM_cards() == null)
            System.out.printf("No cards!!!\n");
        System.out.printf("count of cards = %d\n", m_durakClient.getM_cards().size());
        for (int i = 0; i < m_durakClient.getM_cards().size(); i++)
        {
            Card cc = m_durakClient.getM_cards().get(i);
            if (cc != null)
                System.out.printf("card %s number %d\n", cc.toString(), m_durakClient.getM_cards().indexOf(cc));
            else
                System.out.printf("no card number %d\n", i);
        }*/
        for (int cardInd = m_currCardInd, btnInd = 0;
                (cardInd < allCards) && (btnInd < visibleOnHands);
                    cardInd++, btnInd++) {
            Card c = m_durakClient.getM_cards().get(cardInd);
            BufferedImage img = imgFromCard(c);
            ImageIcon ii= new ImageIcon(img);
            m_userBtnCards.get(btnInd).setIcon(ii);
            m_userBtnCards.get(btnInd).setVisible(true);
        }
        
        if (m_currCardInd != 0) {
            jBtnCardLeft.setVisible(true);
        } else {
            jBtnCardLeft.setVisible(false);
        }
        
        if (m_currCardInd < (allCards - visibleOnHands)) {
            jBtnCardRight.setVisible(true);
        } else {
            jBtnCardRight.setVisible(false);
        }
    }
    
    private void drawDeck(Graphics2D g2d, int width, int height) {
        if (m_durakClient == null)
            return;
        
        if (m_durakClient.getM_trump() == null) {
            return;
        }
        
        int amount = m_durakClient.getM_deck().getCurrentAmount();
        int x = width - width / 5;
        int y = height / 20;
        //int shiftForAmount = 40;
        BufferedImage imgTrmp = 
                imgFromCard(m_durakClient.getM_trump());
        
        if (amount > 0) {
            g2d.drawImage(imgTrmp, x, y, null);
        }
        
        if (amount > 1) {
            g2d.drawImage(m_backHoriz, 
                    x + (imgTrmp.getWidth() - imgTrmp.getHeight()) / 2,
                    y - 10, null);
            g2d.drawString(String.valueOf(amount),
                    x, y + 20);
        }
    }
    
    private void drawThrowOrTrump(Graphics2D g2d, int w, int h) {
        if (m_durakClient == null) {
            return;
        }
        
        String s;
        ThrowOrTrump thrTrmp = m_durakClient.getThrowOrTrump();
        
        if (thrTrmp == ThrowOrTrump.discard) {
            s = "Подкидывай!";
        } else if (thrTrmp == ThrowOrTrump.trump) {
            s = "Отбивайся!";
        } else {
            s = "";
        }
        
        int x = w / 4;
        int y = h - h / 4;
        
        int fontSize = 18;
        g2d.setFont(new Font("Tahoma",  Font.BOLD, fontSize));
        g2d.setColor(java.awt.Color.ORANGE);
        g2d.drawString(s, x, y);
    }
    
    private void drawPlayers(Graphics2D g2d, int width, int height) {
        int shiftDown = 30;
        int x = 25;
        int y = height / 8;
        int fontSize = 20;
        g2d.setFont(new Font("Calibri", Font.PLAIN, fontSize));
        
        g2d.setColor(java.awt.Color.ORANGE);
        g2d.drawString("Игрок/карты", x, y);
        
        g2d.setColor(java.awt.Color.WHITE);
        
        for (DurakAdversary d : m_durakClient.getM_adversaries()) {
            y += shiftDown;
            String info = d.getName().concat(" / ")
                    .concat(String.valueOf(d.getAmountOfCards()));
            
            if (m_durakClient.getName().equals(d.getName())) {
                info = "Me: ".concat(info);
            }
            g2d.drawString(info, x, y);
        }
        
        int a = m_durakClient.getM_active();
        int p = m_durakClient.getM_passive();
        
        int gX = width / 3;
        int gY = height / 20;
        
        if (a >=0 && a < m_durakClient.getM_adversaries().size()
                && p >=0 && p < m_durakClient.getM_adversaries().size()) {
            DurakAdversary active = m_durakClient.getM_adversaries().get(a);
            DurakAdversary passive = m_durakClient.getM_adversaries().get(p);

            int s = 18;
            g2d.setFont(new Font("Tahoma",  Font.BOLD, s));
            g2d.setColor(java.awt.Color.ORANGE);
            g2d.drawString(active.getName().concat("  /")
                    .concat(String.valueOf(active.getAmountOfCards()))
                    .concat("    VS    ")
                    .concat(passive.getName().concat("  /")
                    .concat(String.valueOf(passive.getAmountOfCards()))),
                    gX, gY);
        }
    }
    
    private void setBtnNames() {
        ThrowOrTrump tot = m_durakClient.getThrowOrTrump();
        
        if (tot == ThrowOrTrump.discard) {
            jBtnGetOrPass.setText("Пасовать");
        } else if (tot == ThrowOrTrump.trump) {
            jBtnGetOrPass.setText("Беру!");
        }
    }
    
    /*
     * Вспомогательные методы****************************************
     */
    
    private BufferedImage imgFromCard (Card c) {
        if (c == null) {
            return null;
        }
        
        Card.Color col = c.getColor();
        Card.Value val = c.getValue();
        BufferedImage[] imgs = null;
        
        if (col == Card.Color.spades) {
            imgs = m_imgsSpades;
        } else if (col == Card.Color.clubs) {
            imgs = m_imgsClubs;
        } else if (col == Card.Color.diamonds) {
            imgs = m_imgsDiamonds;
        } else if (col == Card.Color.hearts) {
            imgs = m_imgsHearts;
        }
            
        return imgs[val.ordinal()];
    }
    
    private void loadCardImages(String path) {
        int all = 14;
        m_imgsSpades = new BufferedImage[all];
        m_imgsDiamonds = new BufferedImage[all];
        m_imgsHearts = new BufferedImage[all];
        m_imgsClubs = new BufferedImage[all];
        try {
            m_backHoriz = ImageIO.read(
                    getClass().getClassLoader().getResource(
                    "images/cards/backHoriz.png"));
            m_backVertic = ImageIO.read(
                    getClass().getClassLoader().getResource(
                    "images/cards/backVertic.png"));
            
            for (int i = 0; i < 13; i++)
            {
                String val = Card.Value.values()[i].name().substring(1);
                //System.out.printf("load card %s ", val);
                m_imgsSpades[i] = ImageIO.read(
                        getClass().getClassLoader().getResource(
                        "images/cards/s".concat(val).concat(".png")));
                m_imgsClubs[i] = ImageIO.read(
                        getClass().getClassLoader().getResource(
                        "images/cards/c".concat(val).concat(".png")));
                m_imgsDiamonds[i] = ImageIO.read(
                        getClass().getClassLoader().getResource(
                        "images/cards/d".concat(val).concat(".png")));
                m_imgsHearts[i] = ImageIO.read(
                        getClass().getClassLoader().getResource(
                        "images/cards/h".concat(val).concat(".png")));
                //System.out.printf("card %s\n", m_imgsClubs[i].getColorModel().toString());
            }
        } catch (IOException ex) {
            Logger.getLogger(DurakGameInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
/*        String[] pImgs = new File(path).list();
        for (String s : pImgs) {
            String pathToCard = path.concat(s);
            
            BufferedImage img = null;
            try {
                img = ImageIO.read(new File(pathToCard));
            } catch (IOException ex) {
                Logger.getLogger(DurakGameInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if (s.matches(".*backHoriz.*")) {
                m_backHoriz = img;
            } else if (s.matches(".*backVertic.*")) {
                m_backVertic = img;
            } else {
                String color = s.substring(0, 1);
                String value = s.substring(1, 2);
                String name = "c".concat(value);
                int ind = first.Card.Value.valueOf(name).ordinal();
                
                if ("s".equals(color))
                    m_imgsSpades[ind] = img;
                else if ("c".equals(color))
                    m_imgsClubs[ind] = img;
                else if ("d".equals(color))
                    m_imgsDiamonds[ind] = img;
                else if ("h".equals(color))
                    m_imgsHearts[ind] = img;
            }
        }*/
    }
    
    private void loadCloth(String str) {
        URL u = getClass().getClassLoader().getResource("images/cloth.jpg");
        try {
            /*
            BufferedImage img = null;
                try {
                    img = ImageIO.read(new File(str));
                } catch (IOException ex) {
                    Logger.getLogger(DurakGameInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
             * 
             */
            //ImageIcon i = new ImageIcon(u);
            m_cloth = ImageIO.read(u);
        } catch (IOException ex) {
            Logger.getLogger(DurakGameInterface.class.getName()).log(Level.SEVERE, null, ex);
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
        jBtnGetOrPass = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("Form"); // NOI18N
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(DurakGameInterface.class);
        jBtn1.setText(resourceMap.getString("jBtn1.text")); // NOI18N
        jBtn1.setName("jBtn1"); // NOI18N
        jBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtn1ActionPerformed(evt);
            }
        });

        jBtn2.setText(resourceMap.getString("jBtn2.text")); // NOI18N
        jBtn2.setName("jBtn2"); // NOI18N
        jBtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtn2ActionPerformed(evt);
            }
        });

        jBtn3.setText(resourceMap.getString("jBtn3.text")); // NOI18N
        jBtn3.setName("jBtn3"); // NOI18N
        jBtn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtn3ActionPerformed(evt);
            }
        });

        jBtn4.setText(resourceMap.getString("jBtn4.text")); // NOI18N
        jBtn4.setName("jBtn4"); // NOI18N
        jBtn4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtn4ActionPerformed(evt);
            }
        });

        jBtn5.setText(resourceMap.getString("jBtn5.text")); // NOI18N
        jBtn5.setName("jBtn5"); // NOI18N
        jBtn5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtn5ActionPerformed(evt);
            }
        });

        jBtn6.setName("jBtn6"); // NOI18N
        jBtn6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtn6ActionPerformed(evt);
            }
        });

        jBtnCardRight.setText(resourceMap.getString("jBtnCardRight.text")); // NOI18N
        jBtnCardRight.setName("jBtnCardRight"); // NOI18N
        jBtnCardRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnCardRightActionPerformed(evt);
            }
        });

        jBtnCardLeft.setText(resourceMap.getString("jBtnCardLeft.text")); // NOI18N
        jBtnCardLeft.setName("jBtnCardLeft"); // NOI18N
        jBtnCardLeft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnCardLeftActionPerformed(evt);
            }
        });

        jBtnGetOrPass.setText(resourceMap.getString("jBtnGetOrPass.text")); // NOI18N
        jBtnGetOrPass.setName("jBtnGetOrPass"); // NOI18N
        jBtnGetOrPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnGetOrPassActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(179, 179, 179)
                .addComponent(jBtnCardLeft)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jBtnCardRight)
                .addGap(76, 76, 76)
                .addComponent(jBtnGetOrPass, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(558, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jBtn5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBtn1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBtn2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBtn4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                    .addComponent(jBtn3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBtn6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jBtnCardRight, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jBtnGetOrPass, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)))
                .addGap(20, 20, 20))
            .addGroup(layout.createSequentialGroup()
                .addGap(576, 576, 576)
                .addComponent(jBtnCardLeft, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(41, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        int width = this.getWidth();
        int height = this.getHeight();
        m_canvas.setBounds(0, 0, width, height);
}//GEN-LAST:event_formComponentResized

    private void jBtn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtn3ActionPerformed
        // TODO add your handling code here:
        System.out.printf("Pressed button: %d\n", 3);
        
        int ind = m_currCardInd + 2;
        Card c = m_durakClient.getM_cards().get(ind);
        m_durakClient.sendTurn(c);
    }//GEN-LAST:event_jBtn3ActionPerformed

    private void jBtn4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtn4ActionPerformed
        // TODO add your handling code here:
        System.out.printf("Pressed button: %d\n", 4);
        
        int ind = m_currCardInd + 3;
        Card c = m_durakClient.getM_cards().get(ind);
        m_durakClient.sendTurn(c);
    }//GEN-LAST:event_jBtn4ActionPerformed

    private void jBtn5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtn5ActionPerformed
        // TODO add your handling code here:
        System.out.printf("Pressed button: %d\n", 5);
        int ind = m_currCardInd + 4;
        Card c = m_durakClient.getM_cards().get(ind);
        m_durakClient.sendTurn(c);
    }//GEN-LAST:event_jBtn5ActionPerformed

    private void jBtn6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtn6ActionPerformed
        // TODO add your handling code here:
        System.out.printf("Pressed button: %d\n", 6);
        
        int ind = m_currCardInd + 5;
        Card c = m_durakClient.getM_cards().get(ind);
        m_durakClient.sendTurn(c);
    }//GEN-LAST:event_jBtn6ActionPerformed

    private void jBtnCardLeftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCardLeftActionPerformed
        // TODO add your handling code here:
        if (m_currCardInd > 0) {
            m_currCardInd--;
            drawUserCards();
        }
    }//GEN-LAST:event_jBtnCardLeftActionPerformed

    private void jBtnCardRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCardRightActionPerformed
        // TODO add your handling code here:
        int count = m_durakClient.getM_cards().size();
        
        if (m_currCardInd < count - 6) {
            m_currCardInd++;
            drawUserCards();
        }
    }//GEN-LAST:event_jBtnCardRightActionPerformed

    private void jBtnGetOrPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnGetOrPassActionPerformed
        // TODO add your handling code here:
        System.out.printf("Pressed button: PASS\n");
        
        m_durakClient.sendTurn(null);
    }//GEN-LAST:event_jBtnGetOrPassActionPerformed

    private void jBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtn1ActionPerformed
        // TODO add your handling code here:
        System.out.printf("Pressed button: %d\n", 1);
    
        int ind = m_currCardInd;
        Card c = m_durakClient.getM_cards().get(ind);
        m_durakClient.sendTurn(c);
    }//GEN-LAST:event_jBtn1ActionPerformed

    private void jBtn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtn2ActionPerformed
        // TODO add your handling code here:
        System.out.printf("Pressed button: %d\n", 2);
        
        int ind = m_currCardInd + 1;
        Card c = m_durakClient.getM_cards().get(ind);
        m_durakClient.sendTurn(c);
    }//GEN-LAST:event_jBtn2ActionPerformed

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

            @Override
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
    private javax.swing.JButton jBtnGetOrPass;
    // End of variables declaration//GEN-END:variables
}


