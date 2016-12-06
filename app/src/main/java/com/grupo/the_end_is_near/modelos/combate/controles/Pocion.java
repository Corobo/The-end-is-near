package com.grupo.the_end_is_near.modelos.combate.controles;

import android.content.Context;

import com.grupo.the_end_is_near.GameView;
import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.modelos.Modelo;

/**
 * Created by Corobo on 06/12/2016.
 */

public class Pocion extends Modelo {
    public Pocion(Context context) {
        super(context, GameView.pantallaAncho*0.36 , GameView.pantallaAlto*0.925 ,
                GameView.pantallaAlto, GameView.pantallaAncho);

        altura = 40;
        ancho = 40;

        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.boton_pocion);
    }

    public boolean estaPulsado(float clickX, float clickY) {
        boolean estaPulsado = false;

        if (clickX <= (x + ancho / 2) && clickX >= (x - ancho / 2)
                && clickY <= (y + altura / 2) && clickY >= (y - altura / 2)
                ) {
            estaPulsado = true;
        }
        return estaPulsado;
    }
}
