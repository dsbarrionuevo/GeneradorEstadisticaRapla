/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ger;

import java.util.Properties;

/**
 *
 * @author MatiasNB
 */
public class Horario 
{
    private String horaInicio;
    private String horaFin;
    private String fechaFin;
    private Double cantidadHoras;
    private int dia;
    //fecha a la que pertenece ese horario. Para tener una referencia de a que dia se refiere
    private String fecha;
    private int cantidadDiasFecha; //Cantidad de dias que se utiliza en obtenerPromedioHorasUnSoloDiaAño()
    

    public int getCantidadDiasFecha() {
        return cantidadDiasFecha;
    }

    public void setCantidadDiasFecha(int cantidadDiasFecha) {
        this.cantidadDiasFecha = cantidadDiasFecha;
    }

    /**
     * @return the horaInicio
     */
    public String getHoraInicio() {
        return horaInicio;
    }

    /**
     * @param horaInicio the horaInicio to set
     */
    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    /**
     * @return the horaFin
     */
    public String getHoraFin() {
        return horaFin;
    }

    /**
     * @param horaFin the horaFin to set
     */
    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    /**
     * @return the cantidadHoras
     */
    public Double getCantidadHoras() {
        return cantidadHoras;
    }

    /**
     * @param cantidadHoras the cantidadHoras to set
     */
    public void setCantidadHoras(Double cantidadHoras) {
        this.cantidadHoras = cantidadHoras;
    }

    /**
     * @return the dia
     */
    public int getDia() {
        return dia;
    }

    /**
     * @param dia the dia to set
     */
    public void setDia(int dia) {
        this.dia = dia;
    }

    /**
     * @return the fecha
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
