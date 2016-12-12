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

public class Thief extends Personaje {
    public Thief(Context context, double xInicial, double yInicial) {
        super(context, xInicial, yInicial,36,46);

        this.tipo=1;
        this.nivel=10;
        calcularVida();
        calcularMana();
        calcularDaño();
    }

    @Override
    public void inicializar() {
        hechizo= new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.rayos_2),
                56,64,
                5,8, false);

        Sprite parado = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.thief_01),
                ancho, altura,
                1, 1, true);
        sprites.put(PARADO, parado);

        Sprite avanza = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.thief_avanza),
                39,46,
                3, 3,true);
        sprites.put(AVANZA, avanza);

        Sprite retrocede = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.thief_retrocede),
                39, 46,
                3, 3, true);
        sprites.put(RETROCEDE,retrocede);

        Sprite ataque = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.thief_01),
                ancho, altura,
                1, 1, true);
        sprites.put(ATAQUE,ataque);

        Sprite magia = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.thief_magia),
                41, 47,
                3, 3, false);
        sprites.put(MAGIA,magia);

        Sprite defensa = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.thief_09),
                36, 38,
                3, 1, true);
        sprites.put(DEFENSA,defensa);

        Sprite dañado = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.thief_10),
                35, 45,
                3, 1, false);
        sprites.put(DAÑADO,dañado);

        Sprite morir = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.thief_11),
                48, 31,
                3, 1, true);
        sprites.put(MORIR,morir);
    }

    @Override
    public void calcularVida() {
        this.vida = nivel*86;
        this.vidaMaxima = nivel*86;
    }
    @Override
    public void calcularMana(){
        this.mana = this.nivel*2;
        this.manaMaximo = this.nivel*2;
    }

    @Override
    public void calcularDaño() {
        this.daño= nivel*25;
        this.dañoMagico = nivel*10;
    }
    @Override
    public void magia(Enemigo enemigo) {
        enemigoX=enemigo.x;
        enemigoY=enemigo.y;

        if(mana==0) {
            atacar(enemigo);
        }
        else if(mana-3<=0) {
            mana = 0;
            millis=System.currentTimeMillis();
            acelera=-3;
            magia=true;
            GameView.gestorAudio.reproducirSonido(GestorAudio.SONIDO_MAGIA_THIEF_COMBATE);
        }
        else {
            mana -= 3;
            millis=System.currentTimeMillis();
            acelera=-3;
            magia=true;
            GameView.gestorAudio.reproducirSonido(GestorAudio.SONIDO_MAGIA_THIEF_COMBATE);
        }

    }
}
