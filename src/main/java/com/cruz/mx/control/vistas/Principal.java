/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cruz.mx.control.vistas;

import com.cruz.mx.control.business.Properties;
import com.cruz.mx.control.dao.PersonalDao;
import com.cruz.mx.control.dao.beans.PersonalBean;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author acruzb
 */
public class Principal extends javax.swing.JFrame {

    private static final Logger LOGGER = Logger.getLogger(Principal.class);
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy,hh:mm:ss a");
    private final Timer timer;

    public static ApplicationContext applicationContext;
    public static String VERSION_SISTEMA = "1.0.0";

    private Administrador dialogAdmin;
    private Login dialogLogin;
    
    private PersonalDao personalDao;
    private ImageIcon fotoEmpleadoDefault;

    /**
     * Creates new form Principal
     */
    public Principal() {
        initComponents();
        init();
        timer = new Timer();
        //Se inicia el contador de hora
        TimerTask task = new TimerTask() {
            int tic = 0;

            @Override
            public void run() {
                actualizarHora();
            }
        };
        timer.schedule(task, 0, 1000);
        fieldClave.requestFocus();
    }

    private void init() {
        setResizable(false);
        //Se centra la ventana
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        super.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        super.setTitle("Control de asistencias " + VERSION_SISTEMA);
        this.setIconImage(new ImageIcon(getClass().getClassLoader().getResource("images/huella3.png")).getImage());
        fotoEmpleadoDefault = new ImageIcon(getClass().getClassLoader().getResource("images/personal.png"));
        //SE PREPARA LA SECCION ADMIN
        dialogAdmin = new Administrador(this);
        dialogAdmin.setLocationRelativeTo(this);
        dialogAdmin.addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent arg0) {
                LOGGER.info("Se abre la sección de administración.");
                dialogAdmin.ocultarPrincipal();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                LOGGER.info("Se cierra la sección de administración.");
                dialogAdmin.mostrarPrincipal();
            }
        });

        dialogLogin = new Login(this, false, dialogAdmin);
        dialogLogin.setLocationRelativeTo(this);
        dialogLogin.setModal(true);
        dialogLogin.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                LOGGER.info("Se cierra el login.");
                dialogLogin.limpiarCampos();
            }
        });
        
        //Se crean los beans
        personalDao = getObject(PersonalDao.class);
    }

    public void actualizarHora() {
        String fechHora[] = SDF.format(new Date()).split(",");
        textFieldFecha.setText(fechHora[0]);
        textFieldHora.setText(fechHora[1]);
    }

    private void mostrarConfirmacion(PersonalBean personal) {
        
        final Confirmacion dialogConfirmacion = new Confirmacion(this, true, fotoEmpleadoDefault, personal);
        dialogConfirmacion.setLocationRelativeTo(this);
        dialogConfirmacion.setModal(true);

        //Must schedule the close before the dialog becomes visible
        ScheduledExecutorService s = Executors.newSingleThreadScheduledExecutor();
        s.schedule(new Runnable() {
            @Override
            public void run() {
                dialogConfirmacion.setVisible(false); //should be invoked on the EDT
                dialogConfirmacion.dispose();
                fieldClave.requestFocus();
            }
        }, Properties.TIEMPO_DIALOG_CONFIRMACION, TimeUnit.SECONDS);
        dialogConfirmacion.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        btnChecar = new javax.swing.JButton();
        textFieldFecha = new javax.swing.JTextField();
        textFieldHora = new javax.swing.JTextField();
        btnAdmin = new javax.swing.JButton();
        fieldClave = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel4.setText("Clave de empleado");

        btnChecar.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnChecar.setText("Checar");
        btnChecar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChecarActionPerformed(evt);
            }
        });

        textFieldFecha.setEditable(false);
        textFieldFecha.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        textFieldFecha.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textFieldFecha.setText("27/09/2017");

        textFieldHora.setEditable(false);
        textFieldHora.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        textFieldHora.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textFieldHora.setText("04:56:00 PM");

        btnAdmin.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnAdmin.setText("Administrador");
        btnAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdminActionPerformed(evt);
            }
        });

        fieldClave.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        fieldClave.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldClave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldClaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(textFieldFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(textFieldHora, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 132, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(fieldClave)
                    .addComponent(btnChecar, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(fieldClave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textFieldFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFieldHora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnChecar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 110, Short.MAX_VALUE)
                .addComponent(btnAdmin)
                .addGap(20, 20, 20))
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("REGISTRAR HORA DE ENTRADA - SALIDA");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fieldClaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldClaveActionPerformed
        btnChecar.doClick();
    }//GEN-LAST:event_fieldClaveActionPerformed

    private void btnChecarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChecarActionPerformed
        if(fieldClave.getPassword().length > 0){        
            checarPersonar();
        }
        else{
            fieldClave.requestFocus();
        }
    }//GEN-LAST:event_btnChecarActionPerformed

    private void btnAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdminActionPerformed
        dialogLogin.setVisible(true);
    }//GEN-LAST:event_btnAdminActionPerformed

    /**
     * @param args the command line arguments
     */
    @PostConstruct
    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        /* Create and display the form */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Principal().setVisible(true);
            }
        });
    }

    private void checarPersonar(){
        String clave = new String(fieldClave.getPassword());
        PersonalBean personal = personalDao.existPersonal(new PersonalBean(clave));
        if(null != personal){
            LOGGER.info(personal);
            if(personalDao.existChequeoHoy(personal)){
                System.out.println("Ya hay chequeo de hoy, se actualiza la salida.");
                personalDao.actualizarSalida(personal);
                mostrarConfirmacion(personal);
            }
            else{
                personalDao.checarEntrada(personal);
            }
        }else{
            JOptionPane.showMessageDialog(this, String.format("El empleado con clave %s no existe.", clave), "No existe", JOptionPane.WARNING_MESSAGE);
        }
        fieldClave.setText("");
        fieldClave.requestFocus();
    }
    
    public static <R extends Object> R getObject(Class<? extends Object> clazz) {
        return (R) applicationContext.getBean(clazz);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdmin;
    private javax.swing.JButton btnChecar;
    private javax.swing.JPasswordField fieldClave;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField textFieldFecha;
    private javax.swing.JTextField textFieldHora;
    // End of variables declaration//GEN-END:variables
}