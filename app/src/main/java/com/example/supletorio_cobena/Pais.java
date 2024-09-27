package com.example.supletorio_cobena;

import java.io.Serializable;

public class Pais implements Serializable {
    private String codigoAlpha2;
    private String nombre;

    public Pais(String codigoAlpha2, String nombre) {
        this.codigoAlpha2 = codigoAlpha2;
        this.nombre = nombre;
    }

    public String getCodigoAlpha2() {
        return codigoAlpha2;
    }

    public String getNombre() {
        return nombre;
    }
}
