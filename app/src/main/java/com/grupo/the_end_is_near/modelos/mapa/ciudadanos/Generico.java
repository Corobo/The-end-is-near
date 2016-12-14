package com.grupo.the_end_is_near.modelos.mapa.ciudadanos;

import android.content.Context;

import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.graficos.Sprite;

/**
 * Created by sergiocueto on 12/12/2016.
 */

public class Generico extends Ciudadano {
    public Generico(Context context, double x, double y) {
        super(context, x, y, 41, 29);

        nombre = "";
        frases = new String[]{""};

        int aldeano = new Double(Math.random() * 6).intValue();
        Sprite sprite = null;
        switch (aldeano) {
            case 0:
                sprite = new Sprite(
                        CargadorGraficos.cargarDrawable(context, R.drawable.aldeano1),
                        ancho, altura,
                        1, 1, true);
                break;
            case 1:
                sprite = new Sprite(
                        CargadorGraficos.cargarDrawable(context, R.drawable.aldeano2),
                        ancho, altura,
                        1, 1, true);
                break;
            case 2:
                sprite = new Sprite(
                        CargadorGraficos.cargarDrawable(context, R.drawable.aldeano3),
                        ancho, altura,
                        1, 1, true);
                break;
            case 3:
                sprite = new Sprite(
                        CargadorGraficos.cargarDrawable(context, R.drawable.aldeana1),
                        ancho, altura,
                        1, 1, true);
                break;
            case 4:
                sprite = new Sprite(
                        CargadorGraficos.cargarDrawable(context, R.drawable.aldeana2),
                        ancho, altura,
                        1, 1, true);
                break;
            case 5:
                sprite = new Sprite(
                        CargadorGraficos.cargarDrawable(context, R.drawable.aldeana3),
                        ancho, altura,
                        1, 1, true);
                break;
        }

        inicializar(sprite, null);
    }
}
