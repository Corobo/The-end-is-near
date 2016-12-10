package com.grupo.the_end_is_near.modelos.combate.controles;

import android.content.Context;

import com.grupo.the_end_is_near.GameView;
import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.modelos.Modelo;

/**
 * Created by Corobo on 06/12/2016.
 */

public class BarraVida extends Modelo {
    public BarraVida(Context context) {
        super(context, GameView.pantallaAncho*0.74 , GameView.pantallaAlto*0.88 ,
                GameView.pantallaAlto, GameView.pantallaAncho);

        altura = 70;
        ancho = 240;

        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.barra_vida);
    }
}
