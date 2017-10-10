/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cruz.mx.control.business;

import com.cruz.mx.control.dao.beans.ChequeoBean;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author acruzb
 */
public class AbstractTableModelPersonalChequeo extends AbstractTableModel{
    
    private final String[] columnNames = { "Clave", "Fecha", "Entrada", "Salida"};
    private ArrayList<ChequeoBean> chequeos;

    public AbstractTableModelPersonalChequeo() {
        chequeos = new ArrayList<>();
    }
    
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return chequeos.size();
    }

    @Override
    public Object getValueAt(int row, int col) {
        ChequeoBean staff = chequeos.get(row);
        switch (col){
            case 0:
                return staff.getClave();
            case 1:
                return staff.getFecha();
            case 2:
                return staff.getEntrada();
            case 3:
                return staff.getSalida();
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
    
    public void addData(ChequeoBean data, JFrame frame){
        chequeos.add(data);    
    }
    
    public void emptyData(){
        this.chequeos = new ArrayList<>();
    }
    
}
