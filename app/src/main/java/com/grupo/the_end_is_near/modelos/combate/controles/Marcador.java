package com.grupo.the_end_is_near.modelos.combate.controles;

import android.content.Context;

import com.grupo.the_end_is_near.GameView;
import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.modelos.Modelo;

/**
 * Created by Corobo on 06/12/2016.
 */

public class Marcador extends Modelo {
    public Marcador(Context context, double pos, double posY) {
        super(context,GameView.pantallaAncho / (3 + pos), GameView.pantallaAlto / (2.5 + posY),22,13);

        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.marcador);
    }

}
