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
        frases = new String[]{"No me seas faltoso", "y tomate unas marañuelas"};

        int aldeana = new Double(Math.random() * 3).intValue();
        Sprite sprite = null;
        switch (aldeana) {
            case 0:
                sprite = new Sprite(
                        CargadorGraficos.cargarDrawable(context, R.drawable.aldeana1),
                        ancho, altura,
                        1, 1, true);
                break;
            case 1:
                sprite = new Sprite(
                        CargadorGraficos.cargarDrawable(context, R.drawable.aldeana2),
                        ancho, altura,
                        1, 1, true);
                break;
            case 2:
                sprite = new Sprite(
                        CargadorGraficos.cargarDrawable(context, R.drawable.aldeana3),
                        ancho, altura,
                        1, 1, true);
                break;
        }
        inicializar(sprite, null);
    }
}
