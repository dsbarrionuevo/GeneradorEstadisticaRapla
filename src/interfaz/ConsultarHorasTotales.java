/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package interfaz;

import ger.EstadisticasRapla;
import ger.HorasTotales;
import ger.Periodo;
import java.util.ArrayList;

/**
 *
 * @author MatiasNB
 * EN ESTA VENTANA SE PUEDEN CONSULTAR LAS HORAS POR ALGUN PERIDO EN PARTICULAR
 * PARA UN SOLO DIA O PARA TODA LA SEMANA.
 * SE MUESTRA UN GRAFICO Y SE DEBERIA EXPORTAR UN PDF PARA SU IMPRESION
 */
public class ConsultarHorasTotales extends javax.swing.JFrame {

    /**
     * Creates new form ConsultarHorasTotales
     */
    public ConsultarHorasTotales() {
        initComponents();
        CargarComboDias();
        CargarComboPeriodos();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cmbDias = new javax.swing.JComboBox();
        cmbPeriodos = new javax.swing.JComboBox();
        lblDias = new javax.swing.JLabel();
        lblPeriodo = new javax.swing.JLabel();
        btnAceptar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblDias.setText("Dias");

        lblPeriodo.setText("Periodo");

        btnAceptar.setText("Aceptar");
        btnAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbDias, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDias))
                .addGap(62, 62, 62)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblPeriodo)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cmbPeriodos, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 116, Short.MAX_VALUE)
                        .addComponent(btnAceptar)
                        .addGap(36, 36, 36))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDias)
                    .addComponent(lblPeriodo))
                .addGap(2, 2, 2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbDias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbPeriodos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAceptar))
                .addContainerGap(390, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
        Periodo periodoSeleccionado = (Periodo)cmbPeriodos.getSelectedItem();
        String claveDiaSeleccionado = ((ComboItem)cmbDias.getSelectedItem()).getKey();
        ObtenerDatos(periodoSeleccionado, claveDiaSeleccionado);
    }//GEN-LAST:event_btnAceptarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ConsultarHorasTotales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ConsultarHorasTotales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ConsultarHorasTotales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ConsultarHorasTotales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ConsultarHorasTotales().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAceptar;
    private javax.swing.JComboBox cmbDias;
    private javax.swing.JComboBox cmbPeriodos;
    private javax.swing.JLabel lblDias;
    private javax.swing.JLabel lblPeriodo;
    // End of variables declaration//GEN-END:variables

    private void CargarComboDias() 
    {
        cmbDias.addItem(new ComboItem("Monday", "Lunes"));
        cmbDias.addItem(new ComboItem("Tuesday", "Martes"));
        cmbDias.addItem(new ComboItem("Wednesday", "Miercoles"));
        cmbDias.addItem(new ComboItem("Thursday", "Jueves"));
        cmbDias.addItem(new ComboItem("Friday", "Viernes"));
        cmbDias.addItem(new ComboItem("Saturday", "Sabado"));
        cmbDias.addItem(new ComboItem("TODOS", "TODOS"));
    }

    private void CargarComboPeriodos() 
    {
        EstadisticasRapla estadisticas = new EstadisticasRapla();
        for (Periodo periodo : estadisticas.obtenerPeriodos()) {
            cmbPeriodos.addItem(periodo);
        }
    }

    private void ObtenerDatos(Periodo periodo, String diaSeleccionado) {
        if (diaSeleccionado != "TODOS") {
            EstadisticasRapla estadisticas = new EstadisticasRapla();
            ArrayList<HorasTotales> listaHorasTotales = estadisticas.obtenerHorasTotalesPorDia(diaSeleccionado, periodo);
            long promedio = listaHorasTotales.get(0).getHorasTotales().getTime() / listaHorasTotales.get(0).getCantidadDias();
        }
        else
        {
            
        }
    }
}
