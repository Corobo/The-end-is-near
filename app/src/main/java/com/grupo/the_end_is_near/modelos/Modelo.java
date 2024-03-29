package com.grupo.the_end_is_near.modelos;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

public class Modelo {

    public Context context;
    public double x;
    public double y;
    public int altura;
    public int ancho;
    protected Drawable imagen;
    public int cDerecha;
    public int cIzquierda;
    public int cArriba;
    public int cAbajo;


    public Modelo(Context context, double x, double y, int altura, int ancho){;
        this.context = context;
        this.x = x;
        this.y = y;
        this.altura = altura;
        this.ancho = ancho;

        cDerecha = ancho/2;
        cIzquierda = ancho/2;
        cArriba = altura/2;
        cAbajo = altura/2;

    }

    public void dibujar(Canvas canvas){
        int yArriva = (int)  y - altura / 2;
        int xIzquierda = (int) x - ancho / 2;

        imagen.setBounds(xIzquierda, yArriva, xIzquierda
                + ancho, yArriva + altura);
        imagen.draw(canvas);
    }

    // No Actualiza
    public void actualizar (long tiempo){

    }


    public boolean colisiona (Modelo modelo){
        boolean colisiona = false;

        if (modelo.x - modelo.cIzquierda / 2 <= (x + cDerecha)
                && (modelo.x + modelo.cDerecha / 2) >= (x - cIzquierda)
                && (y + cAbajo) >= (modelo.y - modelo.cArriba)
                && (y - cArriba) < (modelo.y + modelo.cAbajo)) {

            colisiona = true;
        }
        return colisiona;
    }



}


