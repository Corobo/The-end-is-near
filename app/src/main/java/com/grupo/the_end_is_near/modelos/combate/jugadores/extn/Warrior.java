package com.grupo.the_end_is_near.modelos.combate.jugadores.extn;

import android.content.Context;

import com.grupo.the_end_is_near.GameView;
import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.gestores.GestorAudio;
import com.grupo.the_end_is_near.graficos.Sprite;
import com.grupo.the_end_is_near.modelos.combate.enemigos.Enemigo;
import com.grupo.the_end_is_near.modelos.combate.jugadores.Personaje;

/**
 * Created by jaime on 05/12/2016.
 */

public class Warrior extends Personaje {
    public Warrior(Context context, double xInicial, double yInicial) {
        super(context, xInicial, yInicial,32,48);
        this.tipo=0;
        this.nivel=10;
        calcularVida();
        calcularMana();
        calcularDaño();
    }

    @Override
    public void inicializar() {
        hechizo= new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.rayos_3),
                48,50,
                5,7, false);

        Sprite parado = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.warrior_01),
                ancho, altura,
                1, 1, true);
        sprites.put(PARADO, parado);

        Sprite avanza = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.warrior_avanza),
                37,48,
                3, 3,true);
        sprites.put(AVANZA, avanza);

        Sprite retrocede = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.warrior_retrocede),
                37, 48,
                3, 3, true);
        sprites.put(RETROCEDE,retrocede);

        Sprite ataque = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.warrior_01),
                ancho, altura,
                1, 1, true);
        sprites.put(ATAQUE,ataque);

        Sprite magia = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.warrior_magia),
                33, 48,
                3, 3, false);
        sprites.put(MAGIA,magia);

        Sprite defensa = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.warrior_09),
                38, 43,
                3, 1, true);
        sprites.put(DEFENSA,defensa);

        Sprite dañado = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.warrior_10),
                34, 48,
                3, 1, false);
        sprites.put(DAÑADO,dañado);

        Sprite morir = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.warrior_11),
                48, 32,
                3, 1, true);
        sprites.put(MORIR,morir);
    }

    @Override
    public void calcularVida() {
        this.vida = nivel*136;
        this.vidaMaxima = nivel*136;
    }
    @Override
    public void calcularMana(){
        this.mana = this.nivel*(3/2);
        this.manaMaximo = this.nivel*(3/2);
    }
    @Override
    public void calcularDaño() {
        this.daño= nivel*20;
        this.dañoMagico = nivel*5;
    }
    @Override
    public void magia(Enemigo enemigo) {
        enemigoX=enemigo.x;
        enemigoY=enemigo.y;

        if(mana==0) {
            atacar(enemigo);
        }
        else if(mana-6<=0) {
            mana = 0;
            millis=System.currentTimeMillis();
            acelera=-3;
            magia=true;
            GameView.gestorAudio.reproducirSonido(GestorAudio.SONIDO_MAGIA_WARRIOR_COMBATE);
        }
        else {
            mana -= 6;
            millis=System.currentTimeMillis();
            acelera=-3;
            magia=true;
            GameView.gestorAudio.reproducirSonido(GestorAudio.SONIDO_MAGIA_WARRIOR_COMBATE);
        }

    }
}
