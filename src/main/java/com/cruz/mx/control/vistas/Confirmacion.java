/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cruz.mx.control.vistas;

import com.cruz.mx.control.dao.beans.PersonalBean;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author acruzb
 */
public class Confirmacion extends javax.swing.JDialog {

    private ImageIcon image;

    /**
     * Creates new form Confirmacion
     *
     * @param parent
     * @param modal
     * @param image
     * @param personal
     */
    public Confirmacion(java.awt.Frame parent, boolean modal, ImageIcon image, PersonalBean personal) {
        super(parent, modal);
        initComponents();
        try{
            this.image = (personal.getFoto() != null && !personal.getFoto().equals("")) ? base64ToImageIcon(personal.getFoto()): image;
        }
        catch(IOException e){
            this.image = image;
        }
        labelNombre.setText(personal.getNombre());
        labelPaterno.setText(personal.getaPaterno());
        labelMaterno.setText(personal.getaMaterno());
        this.setResizable(false);
        this.setTitle("Información del empleado.");
        init();
    }

    private void init() {
        panelFoto.add(new PanelImagen(panelFoto, image), BorderLayout.CENTER);
    }
    
    private ImageIcon base64ToImageIcon(String base64) throws IOException{
        return new ImageIcon(base64ToBufferedImage(base64));
    }
    
    public BufferedImage base64ToBufferedImage(String imageB64) throws IOException {
        return ImageIO.read(new ByteArrayInputStream(Base64.decodeBase64(imageB64.getBytes())));
    }

    /**
     * Clase para pintar la imagen dentro de un panel
     */
    class PanelImagen extends javax.swing.JPanel {
        private final int x;
        private final int y;
        private final ImageIcon imageIcon;

        public PanelImagen(JPanel jPanel1, ImageIcon imageIcon) {
            this.x = jPanel1.getWidth();
            this.y = jPanel1.getHeight();
            this.setSize(x, y);
            this.imageIcon = imageIcon;
        }

        @Override
        public void paint(Graphics g) {
            g.drawImage(imageIcon.getImage(), 0, 0, x, y, null);
        }    

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
        panelFoto = new javax.swing.JPanel();
        labelNombre = new javax.swing.JLabel();
        labelPaterno = new javax.swing.JLabel();
        labelMaterno = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        panelFoto.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelFoto.setMinimumSize(new java.awt.Dimension(150, 150));
        panelFoto.setPreferredSize(new java.awt.Dimension(150, 150));
        panelFoto.setLayout(new java.awt.BorderLayout());

        labelNombre.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        labelNombre.setText("Abel");

        labelPaterno.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        labelPaterno.setText("Cruz");

        labelMaterno.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        labelMaterno.setText("Baños");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelFoto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelNombre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelPaterno, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelMaterno, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(labelNombre)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelPaterno)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelMaterno))
                    .addComponent(panelFoto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel labelMaterno;
    private javax.swing.JLabel labelNombre;
    private javax.swing.JLabel labelPaterno;
    private javax.swing.JPanel panelFoto;
    // End of variables declaration//GEN-END:variables
}