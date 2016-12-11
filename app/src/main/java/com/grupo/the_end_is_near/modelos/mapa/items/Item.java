package com.grupo.the_end_is_near.modelos.mapa.items;

import android.content.Context;
import android.graphics.Canvas;

import com.grupo.the_end_is_near.GameView;
import com.grupo.the_end_is_near.escenario.Nivel;
import com.grupo.the_end_is_near.graficos.Sprite;
import com.grupo.the_end_is_near.modelos.Modelo;

import java.util.HashMap;

/**
 * Created by sergiocueto on 05/11/2016.
 */

public abstract class Item extends Modelo{
    public IStates estado = IStates.ACTIVO;
    public static final String DEFAULT = "default";
    public static final String RECOLECTANDO = "recolectando";

    private boolean pasable = false;
    private Sprite sprite;
    private HashMap<String,Sprite> sprites = new HashMap<String,Sprite>();

    public Item(Context context, double x, double y, int altura, int ancho, boolean pasable) {
        super(context, x, y, altura, ancho);
        this.x = x-ancho/2;
        this.y = y - altura/2;
        this.pasable = pasable;
    }


    /**
     * es necesario invocar el método inicializar desde la instancia.
     * @param def
     * @param recolectando
     */
    protected void inicializar(Sprite def, Sprite recolectando){
        sprites.put(DEFAULT,def);
        if(recolectando != null)
            sprites.put(RECOLECTANDO,recolectando);

        sprite = def;
    }

    /**
     * @param canvas
     */
    public void dibujar(Canvas canvas) {
        sprite.dibujarSprite(canvas, (int) x - Nivel.scrollEjeX, (int) y-Nivel.scrollEjeY);
    }

    @Override
    public void actualizar (long tiempo) {
        boolean finSprite = false;
        if(sprite != null)
            finSprite = sprite.actualizar(tiempo);

        if ( estado == IStates.RECOLECTADO){
            if(finSprite == true)
                estado = IStates.ELIMINAR;
            else{
                if(sprites.containsKey(RECOLECTANDO))
                    sprite = sprites.get(RECOLECTANDO);
                else
                    estado = IStates.ELIMINAR;
            }
        }
    }

    public boolean isPasable() {
        return pasable;
    }

    /*se le pasa directamente el nivel para que los Items puedan realizar cualquier tipo
         de acciones sobre el */
    public abstract void doSomething(Nivel nivel) throws Exception;
}
