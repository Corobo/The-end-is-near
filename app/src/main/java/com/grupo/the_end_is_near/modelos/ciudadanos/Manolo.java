package com.grupo.the_end_is_near.modelos.ciudadanos;

import android.content.Context;

import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.graficos.Sprite;

/**
 * Created by sergiocueto on 08/12/2016.
 */

public class Manolo extends Ciudadano {
    public Manolo(Context context, double x, double y) {
        super(context, x, y, 40, 40);

        nombre = "Manolo";
        frases = new String[]{"No puedes pasar!!","corred insensatos","Sin la llave no pasas"};

        Sprite sprite = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.ciudadano_13),
                ancho, altura,
                1, 1, true);
        inicializar(sprite,null);
    }
}
