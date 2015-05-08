/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package interfaz;

import ger.Horario;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author MatiasNB
 */
public class ModeloTablaHorarios extends AbstractTableModel
{
    ArrayList<ArrayList<Horario>> lista;
    
    public ModeloTablaHorarios(ArrayList<ArrayList<Horario>> lista )
    {
        this.lista = lista;
    }
    
    @Override
    public int getRowCount() 
    {
        return lista.get(0).size();
    }

    @Override
    public int getColumnCount() 
    {
        return lista.size();
    }

    @Override
    public String getColumnName(int column) 
    {
        return lista.get(column).get(0).getFecha();
    }    
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) 
    {
        return "";
    }
    
    public Object getValueAt2(int rowIndex, int columnIndex) 
    {
        return lista.get(columnIndex).get(rowIndex).getCantidadHoras();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }
    
    
    
}
