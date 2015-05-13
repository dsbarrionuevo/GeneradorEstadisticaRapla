package interfaz;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author MatiasNB
 */
public class Render extends DefaultTableCellRenderer {

    Color backgroundColor = getBackground();

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        JLabel c = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        ModeloTablaHorarios model = (ModeloTablaHorarios) table.getModel();
        
        if( (double)model.getValueAt2(row, column) >= 0 && (double)model.getValueAt2(row, column) <= 2) 
        {
            c.setBackground(Color.GREEN);
            c.setToolTipText(model.getValueAt2(row, column).toString() + " Aulas");
        } 
        else if ((double)model.getValueAt2(row, column) >= 3 && (double)model.getValueAt2(row, column) <= 4) 
        {
            c.setBackground(Color.YELLOW);
            c.setToolTipText(model.getValueAt2(row, column).toString() + " Aulas");
        }
        else
        {
            c.setBackground(Color.RED);
            c.setToolTipText(model.getValueAt2(row, column).toString() + " Aulas");
        }
        return c;
    }
}


