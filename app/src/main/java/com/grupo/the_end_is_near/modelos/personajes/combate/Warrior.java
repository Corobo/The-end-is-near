package com.grupo.the_end_is_near.modelos.personajes.combate;

import android.content.Context;

import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.graficos.Sprite;

/**
 * Created by jaime on 05/12/2016.
 */

public class Warrior extends Personaje {
    public Warrior(Context context, double xInicial, double yInicial) {
        super(context, xInicial, yInicial,32,48);

        this.vida=278;
    }

    @Override
    public void inicializar() {
        Sprite parado = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.warrior_01),
                ancho, altura,
                1, 1, true);
        sprites.put(PARADO, parado);

        //TODO false el bucle del sprite?
        Sprite avanza = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.warrior_avanza),
                ancho, altura,
                3, 3,true);
        sprites.put(AVANZA, avanza);

        Sprite retrocede = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.warrior_retrocede),
                ancho, altura,
                3, 3, true);
        sprites.put(RETROCEDE,retrocede);

        //TODO
        Sprite ataque = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.warrior_01),
                ancho, altura,
                1, 1, true);
        sprites.put(ATAQUE,ataque);

        Sprite magia = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.warrior_magia),
                ancho, altura,
                3, 3, true);
        sprites.put(MAGIA,magia);

        Sprite defensa = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.warrior_09),
                ancho, altura,
                3, 1, true);
        sprites.put(DEFENSA,defensa);

        Sprite dañado = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.warrior_10),
                ancho, altura,
                3, 1, true);
        sprites.put(DAÑADO,dañado);

        Sprite morir = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.warrior_11),
                ancho, altura,
                3, 1, true);
        sprites.put(MORIR,morir);
    }


}
