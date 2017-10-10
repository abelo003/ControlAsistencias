/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cruz.mx.control.business;

import com.cruz.mx.control.dao.beans.PersonalBean;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author acruzb
 */
public class AbstractTableModelPersonal extends AbstractTableModel {

    private final String[] columnNames = {"Clave", "RL", "Nombre", "Apellido paterno", "Apellido materno"};
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
        switch (col) {
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

    public void addData(PersonalBean data, JFrame frame) {
        boolean agregar = true;
        for (PersonalBean bean : personal) {
            if (bean.getClave().equals(data.getClave())) {
                agregar = false;
                break;
            }
        }
        if (agregar) {
            personal.add(data);
        }
    }

    public PersonalBean getPersonal(PersonalBean per) {
        for (PersonalBean bean : personal) {
            if (bean.getClave().equals(per.getClave())) {
                return bean;
            }
        }
        return null;
    }

    public boolean eliminarPersonal(PersonalBean per) {
        Iterator<PersonalBean> iteratorBand = personal.iterator();
        while (iteratorBand.hasNext()) {
            if (per.getClave().equals(iteratorBand.next().getClave())) {
                iteratorBand.remove();
                return true;
            }
        }
        return false;
    }

    public void emptyData() {
        this.personal = new ArrayList<>();
    }

    public void sort() {
        Collections.sort(personal);
    }

}
