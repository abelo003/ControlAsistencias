/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cruz.mx.control.vistas;

import com.cruz.mx.control.dao.AvisoDao;
import com.cruz.mx.control.dao.beans.AvisoBean;
import com.cruz.mx.control.dao.beans.PersonalBean;
import com.cruz.mx.control.utils.FechaUtils;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author acruzb
 */
public class Confirmacion extends javax.swing.JDialog {
    
    private PersonalBean personal;
    private AvisoDao avisoDao;

    /**
     * Creates new form Confirmacion
     *
     * @param parent
     * @param modal
     */
    public Confirmacion(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setResizable(false);
        this.setTitle("Información del empleado.");
        init();
    }

    private void init() {
        textAreaAviso.setLineWrap(true);
        avisoDao = Principal.getObject(AvisoDao.class);
        btnOk.requestFocus();
    }
    
    public void limpiarCampos(){
        textAreaAviso.setText("");
    }
    
    public void visualizarConfirmacion(PersonalBean personal, boolean avisos){
        this.personal = personal;
        labelNombre.setText(personal.getNombre() + " " + personal.getaPaterno() + " " + personal.getaMaterno());
        if(avisos){
            checarAvisosInvidivual();
        }
        this.setVisible(true);
    }
    
    private void checarAvisosInvidivual(){
        String stringAviso = "";
        List<AvisoBean> avisos = avisoDao.consultarAvisosIndividual(personal.getClave(), FechaUtils.getFechaHoy());
        if(null != avisos){
            for(AvisoBean aviso : avisos){
                System.out.println(aviso);
                stringAviso += aviso.getTipo() + "\n" + aviso.getContenido()+ "\n";
            }
            textAreaAviso.setText(stringAviso);
        }
        checarAvisosGeneral();
    }
    
    private void checarAvisosGeneral(){
        String stringAviso = "";
        List<AvisoBean> avisos = avisoDao.consultarAvisosGeneral(FechaUtils.getFechaHoy());
        if(null != avisos){
            for(AvisoBean aviso : avisos){
                System.out.println(aviso);
                stringAviso += aviso.getTipo() + "\n" + aviso.getContenido()+ "\n";
            }
            textAreaAviso.setText(textAreaAviso.getText() + stringAviso);
        }
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
        labelNombre = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textAreaAviso = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnOk = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        labelNombre.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labelNombre.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelNombre.setText("Abel Cruz Baños");

        textAreaAviso.setEditable(false);
        textAreaAviso.setColumns(20);
        textAreaAviso.setFont(new java.awt.Font("Monospaced", 0, 16)); // NOI18N
        textAreaAviso.setRows(5);
        textAreaAviso.setEnabled(false);
        jScrollPane1.setViewportView(textAreaAviso);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Avisos");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Empleado:");

        btnOk.setText("Ok");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(labelNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelNombre)
                    .addComponent(jLabel2))
                .addGap(10, 10, 10)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOk)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnOkActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOk;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelNombre;
    private javax.swing.JTextArea textAreaAviso;
    // End of variables declaration//GEN-END:variables
}
