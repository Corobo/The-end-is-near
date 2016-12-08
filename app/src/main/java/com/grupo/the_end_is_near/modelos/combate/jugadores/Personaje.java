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


    public int acelera=0;
    public int daño;
    public int dañoMagico;

    public int vida;
    public int vidaMaxima;
    public int  mana ;
    public int  manaMaximo;

    public int tipo=0;
    public int nivel=1;

    public int estado = Estado.ACTIVO;

    public boolean estaMuerto=false;

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

    public long millis;

    private double xInicial=0;
    public boolean atacando=false;
    public boolean magia=false;
    public boolean dañado=false;
    public boolean estaBloqueando=false;

    public boolean utilizado=false;

    public Personaje(Context context, double xInicial, double yInicial,int ancho,int alto) {
        super(context, xInicial, yInicial, alto,ancho);
        inicializar();
        this.xInicial=xInicial;
        sprite = sprites.get("Parado");
        calcularDaño();
        calcularMana();
        calcularVida();
    }

    public abstract void inicializar();

    public void dibujar(Canvas canvas){
        sprite.dibujarSprite(canvas, (int) x , (int) y, false);
    }

    public void actualizar (long tiempo) {
        boolean finSprite = sprite.actualizar(tiempo);
        long s = System.currentTimeMillis();
        if (finSprite) {
            sprite.setFrameActual(0);
            sprite = sprites.get("Parado");
        }
        if(atacando) {
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

        if(magia){
            if (s - millis > 550 && s - millis < 1000 && acelera < 0) {
                sprite = sprites.get("Magia");
                acelera = 0;
                millis = s;
            }
            else if(s - millis > 2000 && acelera == 0){
                acelera=3;
                millis=s;
            }else if (s - millis > 550 && acelera > 0) {
                acelera = 0;
                millis = 0;
                x = xInicial;
                magia=false;
                sprite=sprites.get("Parado");
            }
            x = x + acelera;
        }

        if(dañado){
            if (s - millis > 1000) {
                sprite = sprites.get("Parado");
                millis = 0;
                dañado=false;
            }
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
    }

    public void magia(){
        millis=System.currentTimeMillis();
        acelera=-3;
        magia=true;
    }

    public void bloquear(){
        sprite = sprites.get("Defensa");
        estaBloqueando=true;
    }

    public void dejarBloquear(){
        sprite = sprites.get("Parado");
        estaBloqueando=false;
    }

    public void golpeado(int daño){
        this.vida=this.vida-daño;
        millis=System.currentTimeMillis();
        if(this.vida<=0) {
            this.vida=0;
            morir();
        }
        else {
            sprite = sprites.get("Dañado");
            dañado = true;
        }
    }

    public void morir(){
        estado= Estado.INACTIVO;
        sprite = sprites.get("Morir");
        estaMuerto = true;
    }

    public boolean estaOcupado(){
        return atacando || magia || dañado;
    }

    public void accionAleatoria() {
        if(!utilizado) {
            int x = new Double(Math.random() * 2).intValue();
            switch (x) {
                case 0:
                    atacar();
                    break;
                case 1:
                    magia();
                    break;
            }
            utilizado=true;
        }

    }

    public void calcularVida(){
        this.vidaMaxima = this.nivel*100 ;
        this.vida = this.nivel*100;
    }

    public void calcularMana(){
        this.mana = this.nivel*5;
        this.manaMaximo = this.nivel*5;
    }

    public void calcularDaño(){
         this.daño =  nivel*35;
         this.dañoMagico = nivel*35;
    }
}