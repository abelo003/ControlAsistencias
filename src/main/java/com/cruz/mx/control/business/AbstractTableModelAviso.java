/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cruz.mx.control.business;

import com.cruz.mx.control.dao.beans.AvisoBean;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author acruzb
 */
public class AbstractTableModelAviso extends AbstractTableModel {
    
    private final String[] columnNames = {"Tipo", "Clave", "Inicio", "Fin", "Contenido"};
    private ArrayList<AvisoBean> avisos;

    public AbstractTableModelAviso() {
        avisos = new ArrayList<>();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return avisos.size();
    }

    @Override
    public Object getValueAt(int row, int col) {
        AvisoBean staff = avisos.get(row);
        switch (col) {
            case 0:
                return staff.getTipo();
            case 1:
                return (staff.getClave() == null)? "": staff.getClave();
            case 2:
                return staff.getInicio();
            case 3:
                return staff.getFin();
            case 4:
                return staff.getContenido();
        }
        return "";
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public void addData(AvisoBean data) {
        avisos.add(data);
    }
    
    public AvisoBean getAviso(int row){
        return avisos.get(row);
    }
    
    public boolean eliminarAviso(AvisoBean aviso){
        Iterator<AvisoBean> iteratorBand = avisos.iterator();
        while (iteratorBand.hasNext()) {
            if (aviso.getId().equals(iteratorBand.next().getId())) {
                iteratorBand.remove();
                return true;
            }
        }
        return false;
    }

    public void emptyData() {
        this.avisos = new ArrayList<>();
    }    
}
