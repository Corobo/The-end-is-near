package com.grupo.the_end_is_near.modelos.mapa.ciudadanos;

import android.content.Context;

import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.graficos.Sprite;

/**
 * Created by sergiocueto on 08/12/2016.
 */

public class MariPepa extends Ciudadano {

    public MariPepa(Context context, double x, double y) {
        super(context, x, y, 38, 38);

        nombre = "MariPepa";
        frases = new String[]{"No me seas faltoso", "y tomate unas marañuelas"};

        Sprite sprite = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.ciudadano_2),
                ancho, altura,
                1, 1, true);
        inicializar(sprite, null);
    }
}
