package com.grupo.the_end_is_near.modelos.personajes.combate;

import android.content.Context;

import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.graficos.Sprite;

/**
 * Created by jaime on 05/12/2016.
 */

public class Enemigo extends Personaje {
    public Enemigo(Context context, double xInicial, double yInicial) {
        super(context, xInicial, yInicial, 54, 63);
    }

    @Override
    public void inicializar() {
        Sprite parado = null;
        int x = new Double(Math.random() * 10).intValue();
        switch (x){
            case 0:
                parado = new Sprite(
                        CargadorGraficos.cargarDrawable(context, R.drawable.enemy_01),
                        ancho, altura,
                        1, 1, true);
                break;
            case 1:
                parado = new Sprite(
                        CargadorGraficos.cargarDrawable(context, R.drawable.enemy_02),
                        ancho, altura,
                        1, 1, true);
                break;
            case 2:
                parado = new Sprite(
                        CargadorGraficos.cargarDrawable(context, R.drawable.enemy_03),
                        ancho, altura,
                        1, 1, true);
                break;
            case 3:
                parado = new Sprite(
                        CargadorGraficos.cargarDrawable(context, R.drawable.enemy_04),
                        ancho, altura,
                        1, 1, true);
                break;
            case 4:
                parado = new Sprite(
                        CargadorGraficos.cargarDrawable(context, R.drawable.enemy_05),
                        ancho, altura,
                        1, 1, true);
                break;
            case 5:
                parado = new Sprite(
                        CargadorGraficos.cargarDrawable(context, R.drawable.enemy_56),
                        ancho, altura,
                        1, 1, true);
                break;
            case 6:
                parado = new Sprite(
                        CargadorGraficos.cargarDrawable(context, R.drawable.enemy_32),
                        ancho, altura,
                        1, 1, true);
                break;
            case 7:
                parado = new Sprite(
                        CargadorGraficos.cargarDrawable(context, R.drawable.enemy_10),
                        ancho, altura,
                        1, 1, true);
                break;
            case 8:
                parado = new Sprite(
                        CargadorGraficos.cargarDrawable(context, R.drawable.enemy_23),
                        ancho, altura,
                        1, 1, true);
                break;
            case 9:
                parado = new Sprite(
                        CargadorGraficos.cargarDrawable(context, R.drawable.enemy_77),
                        ancho, altura,
                        1, 1, true);
                break;
        }
        sprites.put(PARADO, parado);
    }

    @Override
    public void atacar(){
        acelera=2;
        sprite = sprites.get("Avanza");
    }
}
