package com.grupo.the_end_is_near.modelos.mapa.items;

import android.content.Context;

import com.grupo.the_end_is_near.GameView;
import com.grupo.the_end_is_near.Maps;
import com.grupo.the_end_is_near.escenario.Nivel;

/**
 * Created by sergiocueto on 11/12/2016.
 */

public class Portal1 extends Item {
    public Portal1(Context context, double x, double y) {
        super(context, x, y, 20, 160, true);
    }

    @Override
    public void doSomething(Nivel nivel) throws Exception {
        GameView gameView = nivel.gameView;
        gameView.cargarNivel(Maps.WORLD2);
    }
}
