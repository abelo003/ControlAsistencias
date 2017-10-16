/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cruz.mx.control.vistas;

import com.cruz.mx.control.business.AbstractTableModelAviso;
import com.cruz.mx.control.business.AbstractTableModelPersonal;
import com.cruz.mx.control.business.AbstractTableModelPersonalChequeo;
import com.cruz.mx.control.business.DateLabelFormatter;
import com.cruz.mx.control.dao.AvisoDao;
import com.cruz.mx.control.dao.ChequeoDao;
import com.cruz.mx.control.dao.PersonalDao;
import com.cruz.mx.control.dao.beans.AvisoBean;
import com.cruz.mx.control.dao.beans.ChequeoBean;
import com.cruz.mx.control.dao.beans.PersonalBean;
import com.cruz.mx.control.enums.EstadoEmpleadoNuevoEnum;
import com.cruz.mx.control.service.MailService;
import com.cruz.mx.control.utils.FechaUtils;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.apache.log4j.Logger;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

/**
 *
 * @author acruzb
 */
public class Administrador extends javax.swing.JFrame {

    private static final Logger LOGGER = Logger.getLogger(Administrador.class);
    private static final String FORMATO_ARCHIVO = ".xls";

    private final Principal principal;
    private Administrador admin;
    private final PersonalDao personalDao;
    private final ChequeoDao chequeoDao;
    private final AvisoDao avisoDao;
    private AbstractTableModelPersonal modeloPersonal;
    private final AbstractTableModelPersonalChequeo modeloChequeo;
    private final AbstractTableModelAviso modeloAviso;

    private UtilDateModel modeloFechaInicio;
    private UtilDateModel modeloFechaFin;

    private JPopupMenu menuEmpleados;
    private JPopupMenu menuAvisos;

    private NuevoEmpleado dialogNuevoEmpleado;
    private NuevoAvisoDialog dialogNuevoAviso;
    
    private MailService mailService;

    /**
     * Creates new form Administrador
     *
     * @param principal
     */
    public Administrador(Principal principal) {
        initComponents();
        initDatePicker();
        initListeners();
        initListenerAviso();
        initNuevoEmpleadoDialog();
        initNuevoAvisoDialog();
        this.principal = principal;
        this.personalDao = Principal.getObject(PersonalDao.class);
        this.chequeoDao = Principal.getObject(ChequeoDao.class);
        this.avisoDao = Principal.getObject(AvisoDao.class);
        this.mailService = Principal.getObject(MailService.class);
        setTitle("Administración del sistema.");

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        super.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        this.setIconImage(new ImageIcon(getClass().getClassLoader().getResource("images/huella3.png")).getImage());
        modeloPersonal = new AbstractTableModelPersonal();
        modeloChequeo = new AbstractTableModelPersonalChequeo();
        modeloAviso = new AbstractTableModelAviso();
        tablaPersonal.setModel(modeloPersonal);
        tablaPersonal.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaPersonal.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                JTable table = (JTable) me.getSource();
                if (me.getClickCount() == 2) {
                    Point p = me.getPoint();
                    int row = table.rowAtPoint(p);
                    String clave = modeloPersonal.getValueAt(row, 0).toString();
                    textFielClaveEmpleadoBusqueda.setText(clave);
                    realizarBusquedaChequeos();
                }
            }
        });
        tablaPersonal.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    int row = 0;
                    if (tablaPersonal.getSelectedRow() == 0) {
                        row = tablaPersonal.getRowCount() - 1;
                    } else {
                        row = tablaPersonal.getSelectedRow() - 1;
                    }
                    String clave = modeloPersonal.getValueAt(row, 0).toString();
                    textFielClaveEmpleadoBusqueda.setText(clave);
                    realizarBusquedaChequeos();
                }
            }
        });
        tablaChequeos.setModel(modeloChequeo);
        tablaChequeos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaAvisos.setModel(modeloAviso);
        tablaAvisos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        textFieldClave.requestFocus();
    }

    private void initDatePicker() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, -7);
        modeloFechaInicio = new UtilDateModel(c.getTime());
        modeloFechaFin = new UtilDateModel(new Date());
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanelInicio = new JDatePanelImpl(modeloFechaInicio, p);
        JDatePanelImpl datePanelFin = new JDatePanelImpl(modeloFechaFin, p);
        // Don't know about the formatter, but there it is...
        JDatePickerImpl datePickerInicio = new JDatePickerImpl(datePanelInicio, new DateLabelFormatter());
        JDatePickerImpl datePickerFin = new JDatePickerImpl(datePanelFin, new DateLabelFormatter());
        final Administrador adminView = this;
        datePickerInicio.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!verificarRangoFechas()) {
                    restablecerFechasASemana();
                    JOptionPane.showMessageDialog(adminView, "Favor de validar el rango de fechas.");
                }
            }
        });
        datePickerFin.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!verificarRangoFechas()) {
                    restablecerFechasASemana();
                    JOptionPane.showMessageDialog(adminView, "Favor de validar el rango de fechas.");
                }
            }
        });
        modeloFechaFin.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                String fechaFin = FechaUtils.getFecha(modeloFechaFin);
                String fechaHoy = FechaUtils.getFechaHoy();
                if (!(fechaFin.compareTo(fechaHoy) <= 0)) {
                    modeloFechaFin.setValue(new Date());
                }
            }
        });
        panelFechaInicial.setLayout(new BorderLayout());
        panelFechaInicial.add(datePickerInicio, BorderLayout.CENTER);
        panelFechaFinal.setLayout(new BorderLayout());
        panelFechaFinal.add(datePickerFin, BorderLayout.CENTER);
    }

    private void initListenerAviso() {
        menuAvisos = new JPopupMenu();
        JMenuItem menuItemEliminar = new JMenuItem("Eliminar aviso BD");

        menuItemEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = tablaAvisos.getSelectedRow();
                AvisoBean aviso = modeloAviso.getAviso(row);
                int dialogResult = JOptionPane.showConfirmDialog(admin, "¿Realmente desea eliminar el aviso?", "Confirmación", JOptionPane.WARNING_MESSAGE);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    try {
                        avisoDao.eliminarAviso(aviso);
                        modeloAviso.eliminarAviso(aviso);
                        modeloAviso.fireTableDataChanged();
                        tablaAvisos.repaint();
                        JOptionPane.showMessageDialog(admin, "El aviso se ha eliminado exitosamente.");
                    } catch (Exception ex) {
                        LOGGER.info("No se pudo eliminar el aviso: " + aviso, ex);
                        JOptionPane.showMessageDialog(admin, "No se pudo eliminar el aviso.", "Aviso NO eliminado", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        menuAvisos.add(menuItemEliminar);
        tablaAvisos.setComponentPopupMenu(menuAvisos);
    }

    private void initListeners() {
        menuEmpleados = new JPopupMenu();
        JMenuItem menuItemCopiar = new JMenuItem("Copiar clave al Clipboard");
        JMenuItem menuItemAsistencias = new JMenuItem("Ver asistencias");
        JMenuItem menuItemAviso = new JMenuItem("Agregar aviso a empleado");
        JMenuItem menuItemEditar = new JMenuItem("Editar información");
        JMenuItem menuItemEliminarLista = new JMenuItem("Eliminar empleado de la lista");
        JMenuItem menuItemEliminar = new JMenuItem("Eliminar empleado BD");

        menuItemCopiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = tablaPersonal.getSelectedRow();
                String clave = (String) modeloPersonal.getValueAt(row, 0);
                LOGGER.info("Se procede a copiar el texto: " + clave);
                copiarAlClipboard(clave);
                JOptionPane.showMessageDialog(admin, "La clave se ha copiado al Clipboard");
            }
        });
        menuItemAsistencias.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = tablaPersonal.getSelectedRow();
                String clave = (String) modeloPersonal.getValueAt(row, 0);
                LOGGER.info("Se procede a mostrar las asistencias: " + clave);
                textFielClaveEmpleadoBusqueda.setText(clave);
                realizarBusquedaChequeos();
            }
        });
        menuItemAviso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = tablaPersonal.getSelectedRow();
                String clave = (String) modeloPersonal.getValueAt(row, 0);
                mostrarDialogAviso(EstadoEmpleadoNuevoEnum.NUEVO, null, clave);
            }
        });
        menuItemEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = tablaPersonal.getSelectedRow();
                String clave = (String) modeloPersonal.getValueAt(row, 0);
                PersonalBean bean = modeloPersonal.getPersonal(new PersonalBean(clave));
                LOGGER.info(bean);
                LOGGER.info("Se abre en modo edición.");
                if (null != bean) {
                    mostrarDialogEmpleado(EstadoEmpleadoNuevoEnum.EDITAR, bean);
                }
            }
        });
        menuItemEliminarLista.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = tablaPersonal.getSelectedRow();
                String clave = (String) modeloPersonal.getValueAt(row, 0);
                modeloPersonal.eliminarPersonal(new PersonalBean(clave));
                modeloPersonal.fireTableDataChanged();
                tablaPersonal.repaint();
            }
        });
        menuItemEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = tablaPersonal.getSelectedRow();
                String clave = (String) modeloPersonal.getValueAt(row, 0);
                PersonalBean personal = new PersonalBean(clave);
                int dialogResult = JOptionPane.showConfirmDialog(admin, "¿Realmente desea eliminar el registro del empleado?", "Confirmación", JOptionPane.WARNING_MESSAGE);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    try {
                        personalDao.eliminarPersonal(personal);
                        modeloPersonal.eliminarPersonal(personal);
                        modeloPersonal.fireTableDataChanged();
                        tablaPersonal.repaint();
                        JOptionPane.showMessageDialog(admin, "El empleado se ha eliminado exitosamente.");
                    } catch (Exception ex) {
                        LOGGER.info("No se pudo eliminar el empleado: " + personal, ex);
                        JOptionPane.showMessageDialog(admin, "No se pudo eliminar el empleado.", "Empleado NO eliminado", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        menuEmpleados.add(menuItemCopiar);
        menuEmpleados.add(menuItemAsistencias);
        menuEmpleados.add(menuItemAviso);
        menuEmpleados.add(menuItemEditar);
        menuEmpleados.add(menuItemEliminarLista);
        menuEmpleados.add(menuItemEliminar);
        tablaPersonal.setComponentPopupMenu(menuEmpleados);
    }

    private void initNuevoEmpleadoDialog() {
        dialogNuevoEmpleado = new NuevoEmpleado(this, true);
        dialogNuevoEmpleado.setLocationRelativeTo(this);
        dialogNuevoEmpleado.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                dialogNuevoEmpleado.limpiarCampos();
            }
        });
    }

    private void initNuevoAvisoDialog() {
        dialogNuevoAviso = new NuevoAvisoDialog(this, true);
        dialogNuevoAviso.setLocationRelativeTo(this);
        dialogNuevoAviso.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                dialogNuevoAviso.limpiarCampos();
            }
        });
    }

    public void mostrarDialogEmpleado(EstadoEmpleadoNuevoEnum modo, PersonalBean personal) {
        dialogNuevoEmpleado.setModo(modo, personal);
    }

    public void ocultarNuevoEmpleado() {
        dialogNuevoEmpleado.dispose();
    }

    public void mostrarDialogAviso(EstadoEmpleadoNuevoEnum modo, AvisoBean aviso, String clave) {
        dialogNuevoAviso.setModo(modo, aviso, clave);
    }

    public void ocultarNuevoAviso() {
        dialogNuevoAviso.dispose();
    }

    public void mostrarAdmin() {
        this.setVisible(true);
    }

    public void ocultarAdmin() {
        this.dispose();
    }

    public void ocultarPrincipal() {
        this.principal.dispose();
    }

    public void mostrarPrincipal() {
        this.principal.setVisible(true);
    }

    private void addTablePersonalData(PersonalBean personal) {
        modeloPersonal.addData(personal, this);
    }

    private void removePersonalModel() {
        modeloPersonal.emptyData();
    }

    public void restablecerFechasASemana() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, modeloFechaFin.getYear());
        c.set(Calendar.MONTH, modeloFechaFin.getMonth());
        c.set(Calendar.DAY_OF_MONTH, modeloFechaFin.getDay());
        c.add(Calendar.DAY_OF_YEAR, -7);
        modeloFechaInicio.setDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
    }

    private boolean verificarRangoFechas() {
        Calendar cInicio = Calendar.getInstance();
        cInicio.set(modeloFechaInicio.getYear(), modeloFechaInicio.getMonth(), modeloFechaInicio.getDay());
        Calendar cFin = Calendar.getInstance();
        cFin.set(modeloFechaFin.getYear(), modeloFechaFin.getMonth(), modeloFechaFin.getDay());
        Date startDate = cInicio.getTime();
        Date endDate = cFin.getTime();
        long diff = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);
        return diff >= 0;
    }

    public void realizarBusquedaChequeos() {
        String fechaInicio = String.format("%02d", modeloFechaInicio.getDay()) + "/" + String.format("%02d", (modeloFechaInicio.getMonth() + 1)) + "/" + modeloFechaInicio.getYear();
        String fechaFin = String.format("%02d", modeloFechaFin.getDay()) + "/" + String.format("%02d", (modeloFechaFin.getMonth() + 1)) + "/" + modeloFechaFin.getYear();
        String clave = textFielClaveEmpleadoBusqueda.getText();
        List<ChequeoBean> chequeos = chequeoDao.consultarChequeo(clave, fechaInicio, fechaFin);
        modeloChequeo.emptyData();
        for (ChequeoBean chequeo : chequeos) {
            modeloChequeo.addData(chequeo, this);
        }
        modeloChequeo.fireTableDataChanged();
        tablaChequeos.repaint();
    }

    private static void copiarAlClipboard(String texto) {
        StringSelection stringSelection = new StringSelection(texto);
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(stringSelection, null);
    }

    private void exportTableExcel() {
        if (modeloChequeo.getRowCount() > 0) {
            JFileChooser chooser = new JFileChooser(".");
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de excel", "xls");
            chooser.setFileFilter(filter);
            chooser.setDialogTitle("Guardar archivo");
            chooser.setAcceptAllFileFilterUsed(false);
            if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                String fileName = chooser.getSelectedFile().toString().concat(FORMATO_ARCHIVO);
                crearArchivoExcel(fileName, false);
            }
        } else {
            JOptionPane.showMessageDialog(this, "La tabla de chequeos se encuentra vacía.", "Conversión a excel", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void crearArchivoExcel(String fileName, boolean enviarMail, String ... destino) {
        File file;
        List<JTable> tabla = new ArrayList<>();
        List<String> nom_files;
        file = new File(fileName);
        file.setWritable(true);
        file.setReadable(true);
        tabla.add(tablaChequeos);
        nom_files = Arrays.asList(modeloChequeo.getColumns());
        try {
            WritableFont cabeceraFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false);
            WritableFont cuerpoFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false);
            WritableFont titleFont = new WritableFont(WritableFont.ARIAL, 18, WritableFont.NO_BOLD, false);

            WritableCellFormat fCabecera = new WritableCellFormat(cabeceraFont);
            fCabecera.setAlignment(Alignment.JUSTIFY);
            fCabecera.setVerticalAlignment(VerticalAlignment.CENTRE);
            fCabecera.setBackground(Colour.SKY_BLUE);
            fCabecera.setBorder(Border.ALL, BorderLineStyle.THIN);

            WritableCellFormat fCuerpoTilte = new WritableCellFormat(titleFont);
            fCuerpoTilte.setAlignment(Alignment.CENTRE);

            WritableCellFormat fCuerpoOK = new WritableCellFormat(cuerpoFont);
            fCuerpoOK.setAlignment(Alignment.LEFT);
            fCuerpoOK.setBackground(Colour.VERY_LIGHT_YELLOW);

            WritableCellFormat fCuerpoOKEmpty = new WritableCellFormat(cuerpoFont);
            fCuerpoOKEmpty.setAlignment(Alignment.LEFT);

            WritableCellFormat fCuerpoFAIL = new WritableCellFormat(cuerpoFont);
            fCuerpoFAIL.setAlignment(Alignment.LEFT);
            fCuerpoFAIL.setBackground(Colour.LIGHT_ORANGE);
            boolean guardadoOK = false;
            PersonalBean persona = personalDao.existPersonal(new PersonalBean(String.valueOf(modeloChequeo.getValueAt(0, 0))));
            try (DataOutputStream out = new DataOutputStream(new FileOutputStream(file))) {
                WritableWorkbook w = Workbook.createWorkbook(out);
                JTable table = tabla.get(0);
                WritableSheet s = w.createSheet("Asistencias", 0);
                s.addCell(new Label(4, 0, "Reporte de asistencias", fCuerpoTilte));
                s.addCell(new Label(2, 3, "Nombre", fCuerpoOKEmpty));

                String nombreEmpleado = "";
                if (null != persona) {
                    nombreEmpleado = persona.getNombre() + " " + persona.getaPaterno() + " " + persona.getaMaterno();
                }
                s.addCell(new Label(3, 3, nombreEmpleado, fCuerpoOKEmpty));
                s.setColumnView(0, 10);
                s.setColumnView(1, 10);
                s.setColumnView(2, 15);
                s.setColumnView(3, 15);
                s.setColumnView(4, 15);
                s.setColumnView(5, 15);
                s.setColumnView(6, 15);
                s.setColumnView(7, 15);
                for (int i = 0; i < table.getColumnCount(); i++) {
                    for (int j = 0; j < table.getRowCount(); j++) {
                        Object titulo = table.getColumnName(i);
                        s.addCell(new Label(i + 2, j + 6, String.valueOf(titulo), fCabecera));
                    }
                }
                int i = 0;
                for (; i < table.getRowCount(); i++) {
                    WritableCellFormat localFormat = fCuerpoOK;
                    for (int j = 0; j < table.getColumnCount(); j++) {
                        Object object = table.getValueAt(i, j);
                        s.addCell(new Label(j + 2, i + 7, String.valueOf(object), localFormat));
                    }
                }
                w.setProtected(false);
                w.write();
                w.close();
                guardadoOK = true;
            }
            if(!enviarMail){
                JOptionPane.showMessageDialog(this, "Archivo guardado exitosamente como:\n" + fileName, "Conversión a excel", JOptionPane.INFORMATION_MESSAGE);
            }
            else{
                LOGGER.info("Inicia el proceso de envío del correo.");
                if(guardadoOK){
                    mailService.sendEmail(persona, file, destino[0]);
                    LOGGER.info("Se elimina el archivo del disco.");
                    Files.delete(file.toPath());
                }
                else{
                    LOGGER.info("No se pudo guardar el archivo correctamente, se cancela el envío.");
                }
            }
        } catch (IOException | WriteException e) {
            LOGGER.info("Ocurrio un error al generarl el excel: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Exportación fallida", "Conversión a excel", JOptionPane.ERROR_MESSAGE);
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

        tabledPanelPrincipal = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaPersonal = new javax.swing.JTable();
        btnLimpiarPersonal = new javax.swing.JButton();
        btnAgregarEmpleado = new javax.swing.JButton();
        panelMostrarTodo = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnBuscarClave = new javax.swing.JButton();
        textFieldClave = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        btnBuscarNombre = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        textFieldNombre = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        textFieldPaterno = new javax.swing.JTextField();
        textFieldMaterno = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        panelFechaFinal = new javax.swing.JPanel();
        panelFechaInicial = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        textFielClaveEmpleadoBusqueda = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaChequeos = new javax.swing.JTable();
        btnEnviarReporte = new javax.swing.JButton();
        btnGenerarArchivo = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaAvisos = new javax.swing.JTable();
        btnMostrarAvisos = new javax.swing.JButton();
        btnNuevoAviso = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setPreferredSize(new java.awt.Dimension(474, 300));

        jPanel5.setFocusTraversalPolicyProvider(true);

        tablaPersonal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tablaPersonal);

        btnLimpiarPersonal.setText("Limpiar tabla");
        btnLimpiarPersonal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarPersonalActionPerformed(evt);
            }
        });

        btnAgregarEmpleado.setText("Agregar empleado");
        btnAgregarEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarEmpleadoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(btnAgregarEmpleado)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnLimpiarPersonal))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLimpiarPersonal)
                    .addComponent(btnAgregarEmpleado)))
        );

        jLabel1.setText("Clave");

        btnBuscarClave.setText("Buscar");
        btnBuscarClave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarClaveActionPerformed(evt);
            }
        });

        textFieldClave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textFieldClaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(textFieldClave, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnBuscarClave))
                .addContainerGap(192, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(textFieldClave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 100, Short.MAX_VALUE)
                .addComponent(btnBuscarClave)
                .addContainerGap())
        );

        panelMostrarTodo.addTab("Por clave", jPanel4);

        btnBuscarNombre.setText("Buscar");
        btnBuscarNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarNombreActionPerformed(evt);
            }
        });

        jLabel2.setText("Nombre");

        jLabel3.setText("Apellido paterno");

        jLabel4.setText("Apellido materno");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBuscarNombre)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(textFieldNombre)
                            .addComponent(textFieldPaterno, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                            .addComponent(textFieldMaterno))))
                .addContainerGap(192, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(textFieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(textFieldPaterno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(textFieldMaterno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(btnBuscarNombre)
                .addContainerGap())
        );

        panelMostrarTodo.addTab("Por nombre", jPanel6);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 440, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 165, Short.MAX_VALUE)
        );

        panelMostrarTodo.addTab("Todos", jPanel10);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panelMostrarTodo)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelMostrarTodo, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setPreferredSize(new java.awt.Dimension(474, 300));

        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Selecciona el periodo de asistencia que deseas consultar");

        jLabel6.setText("Fecha Inicial");

        jLabel7.setText("Fecha Final");

        jButton1.setText("Buscar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelFechaFinalLayout = new javax.swing.GroupLayout(panelFechaFinal);
        panelFechaFinal.setLayout(panelFechaFinalLayout);
        panelFechaFinalLayout.setHorizontalGroup(
            panelFechaFinalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
        );
        panelFechaFinalLayout.setVerticalGroup(
            panelFechaFinalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 24, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panelFechaInicialLayout = new javax.swing.GroupLayout(panelFechaInicial);
        panelFechaInicial.setLayout(panelFechaInicialLayout);
        panelFechaInicialLayout.setHorizontalGroup(
            panelFechaInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
        );
        panelFechaInicialLayout.setVerticalGroup(
            panelFechaInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 24, Short.MAX_VALUE)
        );

        jLabel8.setText("Clave de empleado:");

        textFielClaveEmpleadoBusqueda.setEditable(false);
        textFielClaveEmpleadoBusqueda.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(panelFechaInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(18, 18, 18)
                                .addComponent(textFielClaveEmpleadoBusqueda)))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(panelFechaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 22, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(panelFechaInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelFechaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(textFielClaveEmpleadoBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );

        tablaChequeos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tablaChequeos);

        btnEnviarReporte.setText("Enviar por correo");
        btnEnviarReporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarReporteActionPerformed(evt);
            }
        });

        btnGenerarArchivo.setText("Generar excel");
        btnGenerarArchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarArchivoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(btnGenerarArchivo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEnviarReporte)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEnviarReporte)
                    .addComponent(btnGenerarArchivo)))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)))
        );

        tabledPanelPrincipal.addTab("Empleados", jPanel1);

        tablaAvisos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(tablaAvisos);

        btnMostrarAvisos.setText("Mostrar avisos activos");
        btnMostrarAvisos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarAvisosActionPerformed(evt);
            }
        });

        btnNuevoAviso.setText("Nuevo aviso");
        btnNuevoAviso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoAvisoActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Gestión de avisos a empleados");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 944, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(btnMostrarAvisos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnNuevoAviso)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnMostrarAvisos)
                    .addComponent(btnNuevoAviso))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabledPanelPrincipal.addTab("Gestión de avisos", jPanel9);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 964, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 449, Short.MAX_VALUE)
        );

        tabledPanelPrincipal.addTab("Usuarios", jPanel11);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabledPanelPrincipal)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabledPanelPrincipal, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarClaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarClaveActionPerformed
        String clave = textFieldClave.getText();
        if (!"".equals(clave)) {
            PersonalBean personal = personalDao.existPersonal(new PersonalBean(clave));
            if (null != personal) {
                addTablePersonalData(personal);
                modeloPersonal.sort();
                modeloPersonal.fireTableDataChanged();
                tablaPersonal.repaint();
                textFieldClave.setText("");
            } else {
                JOptionPane.showMessageDialog(this, String.format("El empleado con la clave %s no ha sido encontrado.", clave), "Empleado no encontrado", JOptionPane.WARNING_MESSAGE);
                textFieldClave.selectAll();
                textFieldClave.requestFocus();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor ingrese una clave para realizar la búsqueda.", "Clave no informada", JOptionPane.WARNING_MESSAGE);
            textFieldClave.requestFocus();
        }
    }//GEN-LAST:event_btnBuscarClaveActionPerformed

    private void textFieldClaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textFieldClaveActionPerformed
        btnBuscarClave.doClick();
    }//GEN-LAST:event_textFieldClaveActionPerformed

    private void btnLimpiarPersonalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarPersonalActionPerformed
        removePersonalModel();
        modeloPersonal.fireTableDataChanged();
        tablaPersonal.repaint();
    }//GEN-LAST:event_btnLimpiarPersonalActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        realizarBusquedaChequeos();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnBuscarNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarNombreActionPerformed
        PersonalBean personal = new PersonalBean();
        personal.setNombre(textFieldNombre.getText().trim().toUpperCase());
        personal.setaPaterno(textFieldPaterno.getText().trim().toUpperCase());
        personal.setaMaterno(textFieldMaterno.getText().trim().toUpperCase());
        List<PersonalBean> personas = personalDao.existPersonalNombre(personal);
        for (PersonalBean persona : personas) {
            modeloPersonal.addData(persona, this);
        }
        modeloPersonal.sort();
        modeloPersonal.fireTableDataChanged();
        tablaPersonal.repaint();
    }//GEN-LAST:event_btnBuscarNombreActionPerformed

    private void btnAgregarEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarEmpleadoActionPerformed
        mostrarDialogEmpleado(EstadoEmpleadoNuevoEnum.NUEVO, null);
    }//GEN-LAST:event_btnAgregarEmpleadoActionPerformed

    private void btnNuevoAvisoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoAvisoActionPerformed
        mostrarDialogAviso(EstadoEmpleadoNuevoEnum.NUEVO, null, null);
    }//GEN-LAST:event_btnNuevoAvisoActionPerformed

    private void btnMostrarAvisosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarAvisosActionPerformed
        modeloAviso.emptyData();
        List<AvisoBean> avisos = avisoDao.consultarTodo();
        for (AvisoBean aviso : avisos) {
            modeloAviso.addData(aviso);
        }
        modeloAviso.fireTableDataChanged();
        tablaAvisos.repaint();
    }//GEN-LAST:event_btnMostrarAvisosActionPerformed

    private void btnGenerarArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarArchivoActionPerformed
        exportTableExcel();
    }//GEN-LAST:event_btnGenerarArchivoActionPerformed

    private void btnEnviarReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarReporteActionPerformed
        String destino = JOptionPane.showInputDialog(this, "Por favor ingrese el correo al que se enviará el reporte.", "Correo destino", JOptionPane.INFORMATION_MESSAGE);
        if(null != destino && !"".equals(destino)){
            if(destino.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")){
                LOGGER.info("Se envía el correo a " + destino);
                String nombreArchivo = "asistencias_" + new Date().getTime() + FORMATO_ARCHIVO;
                crearArchivoExcel(nombreArchivo, true, destino);
                JOptionPane.showMessageDialog(this, "El correo se envió exitosamente.", "OK", JOptionPane.INFORMATION_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(this, "El correo no es válido.", "No válido", JOptionPane.ERROR_MESSAGE);
            }
        }
        else{
            JOptionPane.showMessageDialog(this, "El correo no es válido.", "No válido", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnEnviarReporteActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarEmpleado;
    private javax.swing.JButton btnBuscarClave;
    private javax.swing.JButton btnBuscarNombre;
    private javax.swing.JButton btnEnviarReporte;
    private javax.swing.JButton btnGenerarArchivo;
    private javax.swing.JButton btnLimpiarPersonal;
    private javax.swing.JButton btnMostrarAvisos;
    private javax.swing.JButton btnNuevoAviso;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPanel panelFechaFinal;
    private javax.swing.JPanel panelFechaInicial;
    private javax.swing.JTabbedPane panelMostrarTodo;
    private javax.swing.JTable tablaAvisos;
    private javax.swing.JTable tablaChequeos;
    private javax.swing.JTable tablaPersonal;
    private javax.swing.JTabbedPane tabledPanelPrincipal;
    private javax.swing.JTextField textFielClaveEmpleadoBusqueda;
    private javax.swing.JTextField textFieldClave;
    private javax.swing.JTextField textFieldMaterno;
    private javax.swing.JTextField textFieldNombre;
    private javax.swing.JTextField textFieldPaterno;
    // End of variables declaration//GEN-END:variables
}
