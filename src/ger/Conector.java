package ger;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Barrionuevo Diego
 */
public class Conector {

    private Connection conexion;
    private Statement sentencia;
    private String baseDatos, usuario, clave;

    public Conector(String baseDatos, String usuario, String clave) {
        this.baseDatos = baseDatos;
        this.usuario = usuario;
        this.clave = clave;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Conector() {
        this("rapla", "admin", "admin");
    }

    public void conectar() {
        try {
            conexion = DriverManager.getConnection("jdbc:mysql://127.0.0.1/" + baseDatos, usuario, clave);
        } catch (SQLException ex) {
            Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ResultSet ejecutarProcedimiento(String nombreProcedimiento) {
        try {
            sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery("CALL " + nombreProcedimiento);
            return resultado;
        } catch (SQLException ex) {
            Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void cerrar() {
        try {
            this.conexion.close();
        } catch (SQLException ex) {
            Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void consultarCantidadCursosPorMateriaPorAnio(int anio) {
        conectar();
        ResultSet resultado1, resultado2 = null;
        resultado1 = ejecutarProcedimiento("consultar_materias_sistemas();");
        try {
            while (resultado1.next()) {
                //System.out.println(resultado.getInt(1));
                resultado2 = ejecutarProcedimiento("consultar_cantidad_cursos_por_materia_anio(" + resultado1.getInt(1) + "," + anio + ");");
                resultado2.first();
                System.out.println("La materia con id: " + resultado1.getInt(1) + ", posee " + resultado2.getInt(1) + " cursos");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (resultado1 != null) {
                    resultado1.close();
                }
                if (resultado2 != null) {
                    resultado1.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
            }
            cerrar();
        }
    }
}
