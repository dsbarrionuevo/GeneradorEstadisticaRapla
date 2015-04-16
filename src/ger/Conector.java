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

    public void consultarHorarioMasUsadoPorDia(String fecha, int rango) {
        ResultSet resultado1 = null;
        ArrayList listaCursosEnRango = new ArrayList();
        int horaInicio = 8;
        int i = 0;
        try {
            conectar();
            while (horaInicio < 24) {
                int horaFin = horaInicio + rango;
                String horaInicioString, horaFinString;
                horaInicioString = ((String.valueOf(horaInicio).length() == 1) ? ("0" + horaInicio) : horaInicio + "") + ":00:00";
                horaFinString = ((String.valueOf(horaFin).length() == 1) ? ("0" + horaFin) : horaFin + "") + ":00:00";
                //resultado1 = ejecutarProcedimiento("consultar_cantidad_cursos_por_rango_horario_fecha('" + fecha + "', '" + horaInicioString + "','" + horaFinString + "');");

                sentencia = conexion.createStatement();
                String consulta = "SELECT count(*) \n"
                        + "FROM rapla.appointment AS a \n"
                        + "INNER JOIN rapla.event_attribute_value AS eav ON a.event_id = eav.event_id \n"
                        + "JOIN allocation al ON al.APPOINTMENT_ID = a.ID\n"
                        + "JOIN rapla.category c ON c.ID = eav.ATTRIBUTE_VALUE\n"
                        + "JOIN rapla.resource_attribute_value rav ON rav.RESOURCE_ID = al.RESOURCE_ID\n"
                        + "WHERE rav.ATTRIBUTE_KEY = \"name\" \n"
                        + "AND eav.ATTRIBUTE_KEY = \"especialidad\" \n"
                        + "AND '" + fecha + "' BETWEEN a.APPOINTMENT_START AND a.REPETITION_END \n"
                        + "AND (\n"
                        + "\n"
                        + "( DATE_FORMAT(a.APPOINTMENT_START,'%T')>='" + horaInicioString + "' AND DATE_FORMAT(a.APPOINTMENT_START,'%T')<'" + horaFinString + "' )\n"
                        + "OR\n"
                        + "( DATE_FORMAT(a.APPOINTMENT_END,'%T')>'" + horaInicioString + "' AND DATE_FORMAT(a.APPOINTMENT_END,'%T')<='" + horaFinString + "' )\n"
                        + "OR\n"
                        + "( DATE_FORMAT(a.APPOINTMENT_START,'%T')<'" + horaInicioString + "' AND DATE_FORMAT(a.APPOINTMENT_END,'%T')>'" + horaFinString + "' )\n"
                        + "\n"
                        + ")"
                        + ""
                        + "AND DAYNAME(a.APPOINTMENT_START) = DAYNAME('" + fecha + "')\n"
                        + "AND a.ID NOT IN(\n"
                        + "	SELECT ae.APPOINTMENT_ID \n"
                        + "	FROM appointment_exception ae \n"
                        + "	WHERE DATE(ae.EXCEPTION_DATE) = '" + fecha + "'\n"
                        + ")";
                resultado1 = sentencia.executeQuery(consulta);

                resultado1.first();
                listaCursosEnRango.add(resultado1.getInt(1));
                System.out.println("Cantidad de cursos para la fecha " + fecha + " desde las " + horaInicioString + " y las " + horaFinString + ": " + resultado1.getInt(1));
                i++;
                horaInicio += rango;
            }
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
