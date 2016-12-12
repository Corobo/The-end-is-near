package com.grupo.the_end_is_near.modelos.mapa.ciudadanos;

import android.content.Context;
import android.text.util.Linkify;

import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.graficos.Sprite;

/**
 * Created by sergiocueto on 08/12/2016.
 */

public class Genaro extends Ciudadano {

    public Genaro(Context context, double x, double y) {
        super(context, x, y, 41, 29);

        nombre = "Genaro";
        frases = new String[]{"buenos dias","caca de vaca"};

        int aldeano = new Double(Math.random() * 3).intValue();
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
        }

        inicializar(sprite,null);
    }
}
