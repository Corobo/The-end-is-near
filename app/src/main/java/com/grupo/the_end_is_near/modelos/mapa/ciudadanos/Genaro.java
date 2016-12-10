package com.grupo.the_end_is_near.modelos.mapa.ciudadanos;

import android.content.Context;

import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.graficos.Sprite;

/**
 * Created by sergiocueto on 08/12/2016.
 */

public class Genaro extends Ciudadano {

    public Genaro(Context context, double x, double y) {
        super(context, x, y, 40, 40);

        nombre = "Genaro";
        frases = new String[]{"buenos dias","caca de vaca"};

        Sprite sprite = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.ciudadano_3),
                ancho, altura,
                1, 1, true);
        inicializar(sprite,null);
    }
}
