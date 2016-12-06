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

        this.tipo=3;
        this.nivel=10;
        calcularVida();
        calcularMana();
    }

    @Override
    public void inicializar() {
        Sprite parado = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.mage_01),
                ancho, altura,
                1, 1, true);
        sprites.put(PARADO, parado);

        Sprite avanza = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.mage_avanza),
                38,48,
                3, 3,true);
        sprites.put(AVANZA, avanza);

        Sprite retrocede = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.mage_retrocede),
                38, 48,
                3, 3, true);
        sprites.put(RETROCEDE,retrocede);

        Sprite ataque = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.mage_01),
                ancho, altura,
                1, 1, true);
        sprites.put(ATAQUE,ataque);

        Sprite magia = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.mage_magia),
                38, 48,
                3, 3, false);
        sprites.put(MAGIA,magia);

        Sprite defensa = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.mage_09),
                42, 40,
                3, 1, true);
        sprites.put(DEFENSA,defensa);

        Sprite dañado = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.mage_10),
                39, 48,
                3, 1, true);
        sprites.put(DAÑADO,dañado);

        Sprite morir = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.mage_11),
                48, 31,
                3, 1, true);
        sprites.put(MORIR,morir);
    }

    @Override
    public void calcularVida() {
        this.vida = nivel*75;
        this.vidaMaxima = nivel*75;
    }
    @Override
    public void calcularMana(){
        this.mana = this.nivel*(15/2);
        this.manaMaximo = this.nivel*(15/2);
    }
}