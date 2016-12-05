package com.grupo.the_end_is_near.modelos.controles;

import android.content.Context;

import com.grupo.the_end_is_near.GameView;
import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.modelos.Modelo;

/**
 * Created by sergiocueto on 05/12/2016.
 */

public class BotonAccion extends Modelo{

    public BotonAccion(Context context) {
        super(context, GameView.pantallaAncho*0.88 , GameView.pantallaAlto*0.6,
                60,60);
        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.action_button);
    }

    public boolean estaPulsado(float clickX, float clickY) {
        boolean estaPulsado = false;

        if (clickX <= (x + ancho / 2) && clickX >= (x - ancho / 2)
                && clickY <= (y + altura / 2) && clickY >= (y - altura / 2)) {
            estaPulsado = true;
        }
        return estaPulsado;
    }
}
