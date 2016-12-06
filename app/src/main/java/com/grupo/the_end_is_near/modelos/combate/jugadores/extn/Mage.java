package com.grupo.the_end_is_near.modelos.combate.jugadores.extn;

import android.content.Context;

import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.graficos.Sprite;
import com.grupo.the_end_is_near.modelos.combate.jugadores.Personaje;

/**
 * Created by jaime on 05/12/2016.
 */

public class Mage extends Personaje {
    public Mage(Context context, double xInicial, double yInicial) {
        super(context, xInicial, yInicial,38,48);

        this.vida=222;
    }

    @Override
    public void inicializar() {
        Sprite parado = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.mage_01),
                ancho, altura,
                1, 1, true);
        sprites.put(PARADO, parado);

        //TODO false el bucle del sprite?
        Sprite avanza = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.mage_avanza),
                ancho, altura,
                3, 3,false);
        sprites.put(AVANZA, avanza);

        Sprite retrocede = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.mage_retrocede),
                ancho, altura,
                3, 3, false);
        sprites.put(RETROCEDE,retrocede);

        //TODO
        Sprite ataque = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.mage_01),
                ancho, altura,
                1, 1, false);
        sprites.put(ATAQUE,ataque);

        Sprite magia = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.mage_magia),
                ancho, altura,
                3, 3, false);
        sprites.put(MAGIA,magia);

        Sprite defensa = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.mage_09),
                ancho, altura,
                3, 1, false);
        sprites.put(DEFENSA,defensa);

        Sprite dañado = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.mage_10),
                ancho, altura,
                3, 1, false);
        sprites.put(DAÑADO,dañado);

        Sprite morir = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.mage_11),
                ancho, altura,
                3, 1, false);
        sprites.put(MORIR,morir);
    }
}
