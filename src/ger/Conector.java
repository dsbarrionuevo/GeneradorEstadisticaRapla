package ger;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.*;

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

    public void consultarHorasPorDia(String fechaDesde, String fechaHasta) {
        ResultSet resultado1 = null;
        try {
            conectar();
            Matcher matcher = (Pattern.compile("(\\d{4}-\\d{1,2})-(\\d{1,2})")).matcher(fechaDesde);
            matcher.find();
            String restoFecha = matcher.group(1);
            int diaInicial = Integer.parseInt(matcher.group(2));
            Matcher matcher2 = (Pattern.compile("(\\d{4}-\\d{1,2})-(\\d{1,2})")).matcher(fechaHasta);
            matcher2.find();
            int diaFinal = Integer.parseInt(matcher2.group(2));
            while (diaInicial <= diaFinal) {
                resultado1 = ejecutarProcedimiento("consultar_total_horas_por_fecha_hora_inicio_fin('" + restoFecha + "-" + diaInicial + "', '08:00:00','23:00:00');");
                resultado1.first();
                System.out.println(resultado1.getInt(1));
                diaInicial++;
            }
            //
        } catch (SQLException ex) {
            Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (resultado1 != null) {
                    resultado1.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
            }
            cerrar();
        }
    }
    
        public void consultarHorarioMasUsadoPorDia(String fecha, int rango) 
        {
        ResultSet resultado1 = null;
        ArrayList listaCursosEnRango = new ArrayList();
        int horaInicio = 10;
        int i = 0;
        try 
        {
            conectar();
            
            while (horaInicio <= 20) 
            {
                resultado1 = ejecutarProcedimiento("consultar_cantidad_cursos_rango_horario_fecha('" + fecha + "', '" + horaInicio +":00:00','" + (horaInicio + 2) +":00:00');");
                resultado1.first();
                listaCursosEnRango.add(resultado1.getInt(1));
                
                System.out.println(listaCursosEnRango.get(i).toString());
                i++;
                horaInicio += 2;
            }
            //
        } catch (SQLException ex) {
            Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (resultado1 != null) {
                    resultado1.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
            }
            cerrar();
        }
    }
}
