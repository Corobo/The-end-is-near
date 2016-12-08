package com.grupo.the_end_is_near.modelos.combate.enemigos;

import android.content.Context;
import android.graphics.Canvas;

import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.global.Estado;
import com.grupo.the_end_is_near.graficos.Sprite;
import com.grupo.the_end_is_near.modelos.Modelo;
import com.grupo.the_end_is_near.modelos.combate.jugadores.Personaje;

import java.util.HashMap;

/**
 * Created by jaime on 05/12/2016.
 */

public class Enemigo extends Modelo {

    public int vida=300;
    public int daño=30;
    public int tipo=0;
    public int ultimoDañoRecibido=0;
    public int estado = Estado.ACTIVO;

    public boolean atacando=false;
    public int acelera=0;
    public long millis;
    private double xInicial=0;

    public boolean ataco=false;

    public boolean utilizado=false;

    public boolean golpeado=false;

    public Sprite sprite;

    public static final String PARADO = "Parado";
    public static final String DAÑADO = "Dañado";

    public HashMap<String,Sprite> sprites = new HashMap<String,Sprite> ();

    public Enemigo(Context context, double xInicial, double yInicial) {
        super(context, xInicial, yInicial, 62, 63);
        inicializar();
        sprite = sprites.get("Parado");
        this.xInicial=xInicial;
    }

    public void inicializar(){

    }

    public void dibujar(Canvas canvas){
        sprite.dibujarSprite(canvas, (int) x , (int) y, false);
    }

    public int golpear(int tipoJugador,int nivelJugador){
            millis = System.currentTimeMillis();
            acelera = 3;
            atacando = true;
            ataco = true;

            if (this.tipo == 0 && tipoJugador == 0) {
                return daño * nivelJugador;
            }
            if (this.tipo == 0 && tipoJugador == 1) {
                return (daño + 15) * nivelJugador;
            }
            if (this.tipo == 0 && tipoJugador == 2) {
                return (daño - 15) * nivelJugador;
            }
            if (this.tipo == 1 && tipoJugador == 0) {
                return daño * nivelJugador;
            }
            if (this.tipo == 1 && tipoJugador == 1) {
                return (daño + 15) * nivelJugador;
            }
            if (this.tipo == 1 && tipoJugador == 2) {
                return (daño - 15) * nivelJugador;
            }
            if (this.tipo == 2 && tipoJugador == 0) {
                return daño * nivelJugador;
            }
            if (this.tipo == 2 && tipoJugador == 1) {
                return (daño + 15) * nivelJugador;
            }
            if (this.tipo == 2 && tipoJugador == 2) {
                return (daño - 15) * nivelJugador;
            }
            if (this.tipo == 3 && tipoJugador == 0) {
                return daño * nivelJugador;
            }
            if (this.tipo == 3 && tipoJugador == 1) {
                return (daño + 15) * nivelJugador;
            }
            if (this.tipo == 3 && tipoJugador == 2) {
                return (daño - 15) * nivelJugador;
            }
        return -1;

    }

    @Override
    public void actualizar (long tiempo){
        boolean finSprite = sprite.actualizar(tiempo);
        long s = System.currentTimeMillis();
        if (finSprite) {
            sprite.setFrameActual(0);
            sprite = sprites.get("Parado");
        }

        if(atacando) {
            if (s - millis > 550 && acelera > 0) {
                acelera = -3;
                millis = s;
            } else if (s - millis > 550 && acelera < 0) {
                acelera = 0;
                millis = 0;
                x = xInicial;
                atacando=false;
            }
            x = x + acelera;
        }

        if(golpeado){
            if (s - millis > 550) {
                sprite = sprites.get("Parado");
                millis = 0;
                golpeado=false;
            }
        }
    }

    public void golpeado(int tipoJugador,int dañoJugador){
        millis = System.currentTimeMillis();
        atacando=false;
        ataco = false;
        golpeado=true;
        sprite=sprites.get("Dañado");

        if(this.tipo==0 && tipoJugador==0){
            vida-= dañoJugador;
            ultimoDañoRecibido=dañoJugador;
        }
        if(this.tipo==0 && tipoJugador==1){
            vida-= (dañoJugador-15);
            ultimoDañoRecibido=dañoJugador-15;
        }
        if(this.tipo==0 && tipoJugador==2){
            vida-= (dañoJugador+15);
            ultimoDañoRecibido=dañoJugador+15;
        }
        if(this.tipo==1 && tipoJugador==0){
            vida-= dañoJugador;
            ultimoDañoRecibido=dañoJugador;
        }
        if(this.tipo==1 && tipoJugador==1){
            vida-= (dañoJugador-15);
            ultimoDañoRecibido=dañoJugador-15;
        }
        if(this.tipo==1 && tipoJugador==2){
            vida-= (dañoJugador+15);
            ultimoDañoRecibido=dañoJugador+15;
        }
        if(this.tipo==2 && tipoJugador==0){
            vida-= dañoJugador;
            ultimoDañoRecibido=dañoJugador;
        }
        if(this.tipo==2 && tipoJugador==1){
            vida-= (dañoJugador-15);
            ultimoDañoRecibido=dañoJugador-15;
        }
        if(this.tipo==2 && tipoJugador==2){
            vida-= (dañoJugador+15);
            ultimoDañoRecibido=dañoJugador+15;
        }
        if(this.tipo==3 && tipoJugador==0){
            vida-= dañoJugador;
            ultimoDañoRecibido=dañoJugador;
        }
        if(this.tipo==3 && tipoJugador==1){
            vida-= (dañoJugador-15);
            ultimoDañoRecibido=dañoJugador-15;
        }
        if(this.tipo==3 && tipoJugador==2){
            vida-= (dañoJugador+15);
            ultimoDañoRecibido=dañoJugador+15;
        }
    }

    public boolean estaOcupado() {
        return atacando || golpeado;
    }
}
