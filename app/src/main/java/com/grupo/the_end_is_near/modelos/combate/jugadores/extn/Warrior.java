package com.grupo.the_end_is_near.modelos.combate.jugadores.extn;

import android.content.Context;

import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.graficos.Sprite;
import com.grupo.the_end_is_near.modelos.combate.jugadores.Personaje;

/**
 * Created by jaime on 05/12/2016.
 */

public class Warrior extends Personaje {
    public Warrior(Context context, double xInicial, double yInicial) {
        super(context, xInicial, yInicial,32,48);
        this.tipo=1;
        this.nivel=10;
        this.vida=278;
        this.vidaMaxima = 278;
        this.mana = 20;
        this.manaMaximo = 20;
    }

    @Override
    public void inicializar() {
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
                3, 1, true);
        sprites.put(DAÑADO,dañado);

        Sprite morir = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.warrior_11),
                48, 32,
                3, 1, true);
        sprites.put(MORIR,morir);
    }


}
