package com.grupo.the_end_is_near.modelos.mapa.ciudadanos;

import android.content.Context;
import android.text.util.Linkify;

import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.graficos.Sprite;

/**
 * Created by sergiocueto on 08/12/2016.
 */

public class CiudadanoEspecifico extends Ciudadano {

    public CiudadanoEspecifico(Context context, double x, double y) {
        super(context, x, y, 41, 29);

        nombre = "Genaro";
        frases = new String[]{"Buenos dias", "viste a mi corderito", "por aquí?"};

        Sprite sprite = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.aldeano1),
                ancho, altura,
                1, 1, true);
        ;

        inicializar(sprite, null);
    }
}
