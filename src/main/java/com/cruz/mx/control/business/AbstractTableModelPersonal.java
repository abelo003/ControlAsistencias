/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cruz.mx.control.business;

import com.cruz.mx.control.dao.beans.PersonalBean;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author acruzb
 */
public class AbstractTableModelPersonal extends AbstractTableModel{
    
    private final String[] columnNames = { "Clave", "RL", "Nombre", "Apellido paterno", "Apellido materno"};
    private ArrayList<PersonalBean> personal;

    public AbstractTableModelPersonal() {
        personal = new ArrayList<>();
    }
    
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return personal.size();
    }

    @Override
    public Object getValueAt(int row, int col) {
        PersonalBean staff = personal.get(row);
        switch (col){
            case 0:
                return staff.getClave();
            case 1:
                return staff.getRL();
            case 2:
                return staff.getNombre();
            case 3:
                return staff.getaPaterno();
            case 4:
                return staff.getaMaterno();
        }
        return "";
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public void setValueAt(Object value, int row, int col) {}

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }
    
    public void addData(PersonalBean data){
        personal.add(data);
    }
    
    public void emptyData(){
        this.personal = new ArrayList<>();
    }
    
    public void sort(){
        Collections.sort(personal);
    }
    
}
