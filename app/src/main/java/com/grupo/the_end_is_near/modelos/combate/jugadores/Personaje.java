package com.grupo.the_end_is_near.modelos.combate.jugadores;

import android.content.Context;
import android.graphics.Canvas;

import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.escenario.Nivel;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.global.Estado;
import com.grupo.the_end_is_near.graficos.Sprite;
import com.grupo.the_end_is_near.modelos.Modelo;

import java.util.HashMap;

/**
 * Created by jaime on 05/12/2016.
 */

public abstract class Personaje extends Modelo{

    public int vida;
    public int acelera=0;
    public int estado = Estado.ACTIVO;

    //Animaciones
    public static final String PARADO = "Parado";
    public static final String RETROCEDE = "Retrocede";
    public static final String AVANZA = "Avanza";
    public static final String ATAQUE = "Ataque";
    public static final String MAGIA = "Magia";
    public static final String DEFENSA = "Defensa";
    public static final String DAÑADO = "Dañado";
    public static final String MORIR = "Morir";

    //Puntero sprite actual
    public Sprite sprite;

    public HashMap<String,Sprite> sprites = new HashMap<String,Sprite> ();

    public long millis; //TODO

    private double xInicial=0;
    public boolean atacando=false;

    public Personaje(Context context, double xInicial, double yInicial,int ancho,int alto) {
        super(context, xInicial, yInicial, alto,ancho);
        inicializar();
        this.xInicial=xInicial;
        sprite = sprites.get("Parado");
    }

    public abstract void inicializar();

    public void dibujar(Canvas canvas){
        sprite.dibujarSprite(canvas, (int) x , (int) y, false);
    }

    public void actualizar (long tiempo) {
        boolean finSprite = sprite.actualizar(tiempo);
        if (finSprite) {
            sprite = sprites.get("Parado");
        }
        //TODO atacar
        if(atacando) {
            long s = System.currentTimeMillis();
            if (s - millis > 550 && acelera < 0) {
                acelera = 3;
                millis = s;
            } else if (s - millis > 550 && acelera > 0) {
                acelera = 0;
                millis = 0;
                x = xInicial;
                atacando=false;
                sprite=sprites.get("Parado");
            }
            x = x + acelera;
        }
    }

    public void accion(String s){
        sprite = sprites.get(s);
    }

    public void atacar(){
        millis=System.currentTimeMillis();
        acelera=-3;
        sprite = sprites.get("Avanza");
        atacando=true;
        //TODO moverse
    }

    public void golpeado(int daño){
        this.vida -= daño;
        if(vida<0){
            sprite = sprites.get("Morir");
            estado = Estado.INACTIVO;
        }
    }
}
