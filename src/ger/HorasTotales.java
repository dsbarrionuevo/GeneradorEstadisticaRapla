/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ger;

import java.sql.Time;

/**
 *
 * @author MatiasNB
 */
public class HorasTotales {
    private Time horasTotales;

    public Time getHorasTotales() {
        return horasTotales;
    }

    public void setHorasTotales(Time horasTotales) {
        this.horasTotales = horasTotales;
    }

    public int getCantidadDias() {
        return cantidadDias;
    }

    public void setCantidadDias(int cantidadDias) {
        this.cantidadDias = cantidadDias;
    }
    private int cantidadDias;
}
