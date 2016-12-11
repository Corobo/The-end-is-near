package com.grupo.the_end_is_near.modelos.mapa.items;

import android.content.Context;

import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.escenario.Nivel;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.graficos.Sprite;

/**
 * Created by sergiocueto on 11/12/2016.
 */

public class Runa1 extends Item {
    private static final int IDENT =1;

    public Runa1(Context context, double x, double y) {
        super(context, x, y, 40, 40,true);

        Sprite sprite = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.runa_1),
                ancho, altura,
                1, 1, true);

        Sprite recolectando = null/*new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.recogiendo_pocion),
                ancho, altura,
                8, 8, false)*/;

        //TODO buscar un sprite para cuando lo coges
        inicializar(sprite,recolectando);
    }

    @Override
    public void doSomething(Nivel nivel) throws Exception {
        nivel.addConvinacion(IDENT);
    }
}
