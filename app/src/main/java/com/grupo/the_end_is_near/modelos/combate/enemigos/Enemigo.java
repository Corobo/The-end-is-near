package com.grupo.the_end_is_near.modelos.combate.enemigos;

import android.content.Context;

import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.graficos.Sprite;
import com.grupo.the_end_is_near.modelos.Modelo;
import com.grupo.the_end_is_near.modelos.combate.jugadores.Personaje;

/**
 * Created by jaime on 05/12/2016.
 */

public class Enemigo extends Modelo {

    public int vida=300;
    public int daño=30;
    public int tipo=0;

    public boolean atacando=false;
    public boolean siendoGolpeado=false;
    public int acelera=0;
    public long millis;
    private double xInicial=0;

    public boolean ataco=false;

    public boolean utilizado=false;

    public Enemigo(Context context, double xInicial, double yInicial) {
        super(context, xInicial, yInicial, 54, 63);

        this.xInicial=xInicial;
        imagen = CargadorGraficos.cargarDrawable(context,R.drawable.enemy_01);
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
        long s = System.currentTimeMillis();
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
    }

    public void golpeado(int tipoJugador,int dañoJugador){
        millis = System.currentTimeMillis();
        acelera = 5;
        siendoGolpeado = true;

        ataco = false;

        if(this.tipo==0 && tipoJugador==0){
            vida-= dañoJugador;
        }
        if(this.tipo==0 && tipoJugador==1){
            vida-= (dañoJugador-15);
        }
        if(this.tipo==0 && tipoJugador==2){
            vida-= (dañoJugador+15);
        }
        if(this.tipo==1 && tipoJugador==0){
            vida-= dañoJugador;
        }
        if(this.tipo==1 && tipoJugador==1){
            vida-= (dañoJugador-15);
        }
        if(this.tipo==1 && tipoJugador==2){
            vida-= (dañoJugador+15);
        }
        if(this.tipo==2 && tipoJugador==0){
            vida-= dañoJugador;
        }
        if(this.tipo==2 && tipoJugador==1){
            vida-= (dañoJugador-15);
        }
        if(this.tipo==2 && tipoJugador==2){
            vida-= (dañoJugador+15);
        }
        if(this.tipo==3 && tipoJugador==0){
            vida-= dañoJugador;
        }
        if(this.tipo==3 && tipoJugador==1){
            vida-= (dañoJugador-15);
        }
        if(this.tipo==3 && tipoJugador==2){
            vida-= (dañoJugador+15);
        }
    }

    public boolean estaOcupado() {
        return atacando;
    }
}
