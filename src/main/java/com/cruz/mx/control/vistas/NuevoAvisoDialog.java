/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cruz.mx.control.vistas;

import com.cruz.mx.control.business.DateLabelFormatter;
import com.cruz.mx.control.dao.AvisoDao;
import com.cruz.mx.control.dao.PersonalDao;
import com.cruz.mx.control.dao.beans.AvisoBean;
import com.cruz.mx.control.dao.beans.PersonalBean;
import com.cruz.mx.control.enums.EstadoEmpleadoNuevoEnum;
import com.cruz.mx.control.utils.FechaUtils;
import static com.cruz.mx.control.utils.FechaUtils.getFecha;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.apache.log4j.Logger;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

/**
 *
 * @author acruzb
 */
public class NuevoAvisoDialog extends javax.swing.JDialog {
    
    private static final Logger LOGGER = Logger.getLogger(NuevoAvisoDialog.class);
    
    private AvisoDao avisoDao;
    private PersonalDao personalDao;
    private EstadoEmpleadoNuevoEnum modo;
    
    private UtilDateModel modeloFechaInicio;
    private UtilDateModel modeloFechaFin;

    /**
     * Creates new form NuevoAvisoDialog
     */
    public NuevoAvisoDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initDatePicker();
        setTitle("Nuevo aviso");
        buttonGroup1.add(radioIndividual);
        buttonGroup1.add(radioGeneral);
        textAreaContenido.setLineWrap(true);
        personalDao = Principal.getObject(PersonalDao.class);
        avisoDao = Principal.getObject(AvisoDao.class);
    }
    
    /**
     * 
     * @param modo
     * @param aviso
     * @param clave se agrega la clave cuando se selecciona de la tabla de empleados
     */
    public void setModo(EstadoEmpleadoNuevoEnum modo, AvisoBean aviso, String clave){
        this.modo = modo;
        if(modo == EstadoEmpleadoNuevoEnum.NUEVO){
            if(null != clave){
                textFieldClave.setText(clave);
            }
            LOGGER.info("Se abre en modo NUEVO");
        }
        else{
            LOGGER.info("Se abre en modo EDICION");
        }
        this.setVisible(true);
    }
    
    private void initDatePicker(){
        modeloFechaInicio = new UtilDateModel(new Date());
        modeloFechaInicio.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                String fechaInicio = getFecha(modeloFechaInicio);
                String fechaHoy = FechaUtils.getFechaHoy();
                if(!(fechaInicio.compareTo(fechaHoy) >= 0)){
                    modeloFechaInicio.setValue(new Date());
                }
            }
        });
        modeloFechaFin = new UtilDateModel(new Date());
        modeloFechaFin.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                String fechaFin = getFecha(modeloFechaFin);
                String fechaHoy = FechaUtils.getFechaHoy();
                if(!(fechaFin.compareTo(fechaHoy) >= 0)){
                    modeloFechaInicio.setValue(new Date());
                }
            }
        });
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanelInicio = new JDatePanelImpl(modeloFechaInicio, p);
        JDatePanelImpl datePanelFin = new JDatePanelImpl(modeloFechaFin, p);
        // Don't know about the formatter, but there it is...
        JDatePickerImpl datePickerInicio = new JDatePickerImpl(datePanelInicio, new DateLabelFormatter());
        JDatePickerImpl datePickerFin = new JDatePickerImpl(datePanelFin, new DateLabelFormatter());
        final NuevoAvisoDialog nuevoAvisoView = this;
        datePickerInicio.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!verificarRangoFechas()){
                    restablecerFechasASemana();
                    JOptionPane.showMessageDialog(nuevoAvisoView, "Favor de validar el rango de fechas.");
                }
            }
        });
        datePickerFin.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!verificarRangoFechas()){
                    restablecerFechasASemana();
                    JOptionPane.showMessageDialog(nuevoAvisoView, "Favor de validar el rango de fechas.");
                }
            }
        });
        panelDe.setLayout(new BorderLayout());
        panelDe.add(datePickerInicio, BorderLayout.CENTER);
        panelHasta.setLayout(new BorderLayout());
        panelHasta.add(datePickerFin, BorderLayout.CENTER);
    }
    
     public void restablecerFechasASemana(){
        Calendar c = Calendar.getInstance();
        modeloFechaInicio.setDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        c.add(Calendar.DAY_OF_YEAR, 1);
        modeloFechaFin.setDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
    }
    
    private boolean verificarRangoFechas(){
        Calendar cInicio = Calendar.getInstance();
        cInicio.set(modeloFechaInicio.getYear(), modeloFechaInicio.getMonth(), modeloFechaInicio.getDay());
        Calendar cFin = Calendar.getInstance();
        cFin.set(modeloFechaFin.getYear(), modeloFechaFin.getMonth(), modeloFechaFin.getDay());
        Date startDate = cInicio.getTime();
        Date endDate = cFin.getTime();
        long diff = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);
        return diff >= 0;
    }
    
    public void limpiarCampos(){
        textAreaContenido.setText("");
        textFieldClave.setText("");
        radioIndividual.doClick();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        btnCancelar = new javax.swing.JButton();
        btnAceptar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        textAreaContenido = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        radioIndividual = new javax.swing.JRadioButton();
        radioGeneral = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        panelHasta = new javax.swing.JPanel();
        panelDe = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        textFieldClave = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnAceptar.setText("Aceptar");
        btnAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarActionPerformed(evt);
            }
        });

        textAreaContenido.setColumns(20);
        textAreaContenido.setRows(2);
        jScrollPane1.setViewportView(textAreaContenido);

        jLabel2.setText("Contenido");

        jLabel1.setText("Tipo de aviso");

        radioIndividual.setSelected(true);
        radioIndividual.setText("Individual");
        radioIndividual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioIndividualActionPerformed(evt);
            }
        });

        radioGeneral.setText("General");
        radioGeneral.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioGeneralActionPerformed(evt);
            }
        });

        jLabel3.setText("  De  ");

        jLabel4.setText("Hasta");

        panelHasta.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        javax.swing.GroupLayout panelHastaLayout = new javax.swing.GroupLayout(panelHasta);
        panelHasta.setLayout(panelHastaLayout);
        panelHastaLayout.setHorizontalGroup(
            panelHastaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 102, Short.MAX_VALUE)
        );
        panelHastaLayout.setVerticalGroup(
            panelHastaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );

        panelDe.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        javax.swing.GroupLayout panelDeLayout = new javax.swing.GroupLayout(panelDe);
        panelDe.setLayout(panelDeLayout);
        panelDeLayout.setHorizontalGroup(
            panelDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelDeLayout.setVerticalGroup(
            panelDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );

        jLabel5.setText("  Clave  ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(radioIndividual, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                                    .addComponent(radioGeneral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel5))
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(textFieldClave, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                                    .addComponent(panelDe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(20, 20, 20)
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(panelHasta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 207, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnCancelar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAceptar)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioIndividual)
                    .addComponent(jLabel5)
                    .addComponent(textFieldClave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(radioGeneral)
                        .addComponent(jLabel3)
                        .addComponent(jLabel4))
                    .addComponent(panelDe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelHasta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAceptar)
                    .addComponent(btnCancelar))
                .addContainerGap())
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

    private void radioGeneralActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioGeneralActionPerformed
        textFieldClave.setEnabled(false);
        textFieldClave.setText("");
    }//GEN-LAST:event_radioGeneralActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void radioIndividualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioIndividualActionPerformed
        textFieldClave.setEnabled(true);
    }//GEN-LAST:event_radioIndividualActionPerformed

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
        String clave = textFieldClave.getText().trim();
        if(radioIndividual.isSelected()){
            if(!clave.matches("\\d\\d\\d\\d")){
                JOptionPane.showMessageDialog(this, "Favor de validar el formato de la clave.", "Clave inválida", JOptionPane.WARNING_MESSAGE);
                textFieldClave.selectAll();
                textFieldClave.requestFocus();
                return;
            }
            PersonalBean personal = new PersonalBean(clave);
            if(null == personalDao.existPersonal(personal)){
                JOptionPane.showMessageDialog(this, "El empleado con la clave "+ clave +" no existe.", "Clave inválida", JOptionPane.WARNING_MESSAGE);
                textFieldClave.selectAll();
                textFieldClave.requestFocus();
                return;
            }
        }
        String contenido = textAreaContenido.getText();
        if("".equals(contenido)){
            JOptionPane.showMessageDialog(this, "Favor de ingresar el contenido del aviso.", "Aviso vacío", JOptionPane.WARNING_MESSAGE);
            textAreaContenido.requestFocus();
            return;
        }
        AvisoBean aviso = new AvisoBean();
        aviso.setContenido(contenido);
        aviso.setInicio(getFecha(modeloFechaInicio));
        aviso.setFin(getFecha(modeloFechaFin));
        if(radioIndividual.isSelected()){
            aviso.setTipo(AvisoBean.TIPO_INDIVIDUAL);
            aviso.setClave(clave);
        }
        else{
            aviso.setTipo(AvisoBean.TIPO_GENERAL);
        }
        try{
            avisoDao.agregarAviso(aviso);
            JOptionPane.showMessageDialog(this, "Se ha agregado el aviso exitosamente.", "Aviso agregado", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
        }catch(RuntimeException e){
            JOptionPane.showMessageDialog(this, "No se pudo agregar el aviso.", "Aviso no creado", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnAceptarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panelDe;
    private javax.swing.JPanel panelHasta;
    private javax.swing.JRadioButton radioGeneral;
    private javax.swing.JRadioButton radioIndividual;
    private javax.swing.JTextArea textAreaContenido;
    private javax.swing.JTextField textFieldClave;
    // End of variables declaration//GEN-END:variables
}
