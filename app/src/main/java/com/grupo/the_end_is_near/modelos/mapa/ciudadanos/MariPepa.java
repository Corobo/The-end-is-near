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
        super(context, x, y, 41, 29);

        nombre = "MariPepa";
        frases = new String[]{"Cuenta la profecía", "que una persona", "de puro de corazón",
                "pisará las rúnas de", "la sabiduría", "en sentido anti horario", "y se le mostrará el ",
                "camino a la verdad", "Feliz pascua !!"};

        Sprite sprite = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.aldeana1),
                ancho, altura,
                1, 1, true);
        inicializar(sprite, null);
    }
}
