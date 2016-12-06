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

    public Enemigo(Context context, double xInicial, double yInicial) {
        super(context, xInicial, yInicial, 54, 63);

        imagen = CargadorGraficos.cargarDrawable(context,R.drawable.enemy_01);
    }

    public int golpear(int tipoJugador,int nivelJugador){
        if(this.tipo==0 && tipoJugador==0){
            return daño*nivelJugador;
        }
        if(this.tipo==0 && tipoJugador==1){
            return (daño+15)*nivelJugador;
        }
        if(this.tipo==0 && tipoJugador==2){
            return (daño-15)*nivelJugador;
        }
        if(this.tipo==1 && tipoJugador==0){
            return daño*nivelJugador;
        }
        if(this.tipo==1 && tipoJugador==1){
            return (daño+15)*nivelJugador;
        }
        if(this.tipo==1 && tipoJugador==2){
            return (daño-15)*nivelJugador;
        }
        if(this.tipo==2 && tipoJugador==0){
            return daño*nivelJugador;
        }
        if(this.tipo==2 && tipoJugador==1){
            return (daño+15)*nivelJugador;
        }
        if(this.tipo==2 && tipoJugador==2){
            return (daño-15)*nivelJugador;
        }
        if(this.tipo==3 && tipoJugador==0){
            return daño*nivelJugador;
        }
        if(this.tipo==3 && tipoJugador==1){
            return (daño+15)*nivelJugador;
        }
        if(this.tipo==3 && tipoJugador==2){
            return (daño-15)*nivelJugador;
        }

        return -1;
    }

    public void golpeado(int tipoJugador,int nivelJugador,int dañoJugador){
        if(this.tipo==0 && tipoJugador==0){
            vida-= dañoJugador*nivelJugador;
        }
        if(this.tipo==0 && tipoJugador==1){
            vida-= (dañoJugador-15)*nivelJugador;
        }
        if(this.tipo==0 && tipoJugador==2){
            vida-= (dañoJugador+15)*nivelJugador;
        }
        if(this.tipo==1 && tipoJugador==0){
            vida-= dañoJugador*nivelJugador;
        }
        if(this.tipo==1 && tipoJugador==1){
            vida-= (dañoJugador-15)*nivelJugador;
        }
        if(this.tipo==1 && tipoJugador==2){
            vida-= (dañoJugador+15)*nivelJugador;
        }
        if(this.tipo==2 && tipoJugador==0){
            vida-= dañoJugador*nivelJugador;
        }
        if(this.tipo==2 && tipoJugador==1){
            vida-= (dañoJugador-15)*nivelJugador;
        }
        if(this.tipo==2 && tipoJugador==2){
            vida-= (dañoJugador+15)*nivelJugador;
        }
        if(this.tipo==3 && tipoJugador==0){
            vida-= dañoJugador*nivelJugador;
        }
        if(this.tipo==3 && tipoJugador==1){
            vida-= (dañoJugador-15)*nivelJugador;
        }
        if(this.tipo==3 && tipoJugador==2){
            vida-= (dañoJugador+15)*nivelJugador;
        }

    }
}
