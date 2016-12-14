package com.grupo.the_end_is_near.modelos.mapa.ciudadanos;

import android.content.Context;

import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.graficos.Sprite;

/**
 * Created by sergiocueto on 12/12/2016.
 */

public class CiudadanoEspecifico2 extends Ciudadano {
    public CiudadanoEspecifico2(Context context, double x, double y) {
        super(context, x, y, 41, 29);

        nombre = "Rigoverto";
        frases = new String[]{"ñiiiiiii...", "No me molestes"};

        int aldeano = new Double(Math.random() * 3).intValue();
        Sprite sprite = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.aldeano2),
                ancho, altura,
                1, 1, true);

        inicializar(sprite, null);
    }
}
