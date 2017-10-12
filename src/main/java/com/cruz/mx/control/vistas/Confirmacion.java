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
import java.awt.BorderLayout;
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
    private ImageIcon defautlPerson;

    /**
     * Creates new form Confirmacion
     *
     * @param parent
     * @param modal
     */
    public Confirmacion(java.awt.Frame parent, boolean modal, ImageIcon defautlPerson) {
        super(parent, modal);
        initComponents();
        this.defautlPerson = defautlPerson;
        this.setResizable(false);
        this.setTitle("Información del empleado");
        init();
    }

    private void init() {
        panelImagen.setLayout(new BorderLayout());
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
        panelImagen.removeAll();
        if(null != personal.getFoto()){
            panelImagen.add(new PanelImagen(panelImagen, base64ToImageIcon(personal.getFoto())), BorderLayout.CENTER);
        }
        else{
            panelImagen.add(new PanelImagen(panelImagen, defautlPerson), BorderLayout.CENTER);
        }
        panelImagen.updateUI();
        this.setVisible(true);
    }
    
    private void checarAvisosInvidivual(){
        String stringAviso = "";
        List<AvisoBean> avisos = avisoDao.consultarAvisosIndividual(personal.getClave(), FechaUtils.getFechaHoy());
        if(null != avisos){
            for(AvisoBean aviso : avisos){
                System.out.println(aviso);
                stringAviso += aviso.getTipo() + " --- " + aviso.getContenido()+ "\n";
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
                stringAviso += aviso.getTipo() + " --- " + aviso.getContenido()+ "\n";
            }
            textAreaAviso.setText(textAreaAviso.getText() + stringAviso);
        }
    }
    
    private ImageIcon base64ToImageIcon(String base64){
        return new ImageIcon(base64ToBufferedImage(base64));
    }
    
    public BufferedImage base64ToBufferedImage(String imageB64){
        try{
            return ImageIO.read(new ByteArrayInputStream(Base64.decodeBase64(imageB64.getBytes())));
        }catch(IOException e){}
        return null;
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
        panelImagen = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        labelNombre.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labelNombre.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelNombre.setText("Abel Cruz Baños");

        textAreaAviso.setEditable(false);
        textAreaAviso.setColumns(20);
        textAreaAviso.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        textAreaAviso.setRows(5);
        textAreaAviso.setText("Hola mundo cruel");
        textAreaAviso.setEnabled(false);
        jScrollPane1.setViewportView(textAreaAviso);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Avisos");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Empleado");

        btnOk.setText("Ok");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        panelImagen.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelImagen.setPreferredSize(new java.awt.Dimension(100, 100));

        javax.swing.GroupLayout panelImagenLayout = new javax.swing.GroupLayout(panelImagen);
        panelImagen.setLayout(panelImagenLayout);
        panelImagenLayout.setHorizontalGroup(
            panelImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 196, Short.MAX_VALUE)
        );
        panelImagenLayout.setVerticalGroup(
            panelImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 196, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel1)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                                .addComponent(labelNombre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelNombre)
                        .addGap(46, 46, 46)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panelImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnOk)
                .addGap(6, 6, 6))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
    private javax.swing.JPanel panelImagen;
    private javax.swing.JTextArea textAreaAviso;
    // End of variables declaration//GEN-END:variables
}
