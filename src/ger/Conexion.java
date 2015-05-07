package ger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Barrionuevo Diego
 */
public class Conexion 
{

    private Connection conexion;
    private Statement sentencia;
    private String baseDatos, usuario, clave;

    public Conexion(String baseDatos, String usuario, String clave) {
        this.baseDatos = baseDatos;
        this.usuario = usuario;
        this.clave = clave;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Conexion() {
        this("rapla", "admin", "admin");
    }

    public void conectar() {
        try {
            setConexion(DriverManager.getConnection("jdbc:mysql://127.0.0.1/" + baseDatos, usuario, clave));
        } catch (SQLException ex) {
            Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ResultSet ejecutarProcedimiento(String nombreProcedimiento) {
        try {
            setSentencia(getConexion().createStatement());
            ResultSet resultado = getSentencia().executeQuery("CALL " + nombreProcedimiento);
            return resultado;
        } catch (SQLException ex) {
            Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void cerrar() {
        try {
            this.getConexion().close();
        } catch (SQLException ex) {
            Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the conexion
     */
    public Connection getConexion() {
        return conexion;
    }

    /**
     * @param conexion the conexion to set
     */
    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }

    /**
     * @return the sentencia
     */
    public Statement getSentencia() {
        return sentencia;
    }

    /**
     * @param sentencia the sentencia to set
     */
    public void setSentencia(Statement sentencia) {
        this.sentencia = sentencia;
    }

}
