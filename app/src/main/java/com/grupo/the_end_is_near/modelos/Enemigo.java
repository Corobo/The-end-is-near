package com.grupo.the_end_is_near.modelos;

import android.content.Context;
import android.graphics.Canvas;

import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.escenario.Nivel;
import com.grupo.the_end_is_near.escenario.Tile;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.graficos.Sprite;

import java.util.HashMap;

import static android.R.attr.x;
import static android.R.attr.y;
import static com.grupo.the_end_is_near.escenario.Tile.altura;
import static com.grupo.the_end_is_near.escenario.Tile.ancho;

/**
 * Created by personal on 05/12/2016.
 */

public class Enemigo extends Modelo {
    public static final String CAMINANDO_DERECHA = "Caminando_derecha";
    public static final String CAMINANDO_IZQUIERDA = "caminando_izquierda";
    public static final String MUERTE_DERECHA = "muerte_derecha";
    public static final String MUERTE_IZQUIERDA = "muerte_izquierda";

    public int estado = ACTIVO;
    public static final int ACTIVO = 1;
    public static final int INACTIVO = 0;
    public static final int ELIMINAR = -1;

    private Sprite sprite;
    private HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();

    public double velocidadX = 1.2;

    public Enemigo(Context context, double xInicial, double yInicial) {
        super(context, 0, 0, 40, 40);

        this.x = xInicial;
        this.y = yInicial - Tile.altura / 2;

        cDerecha = 15;
        cIzquierda = 15;
        cArriba = 20;
        cAbajo = 20;

        inicializar();
    }

    public void inicializar() {

        Sprite caminandoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemyrunright),
                Tile.ancho, Tile.altura,
                4, 4, true);
        sprites.put(CAMINANDO_DERECHA, caminandoDerecha);

        Sprite caminandoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemyrun),
                Tile.ancho, Tile.altura,
                4, 4, true);
        sprites.put(CAMINANDO_IZQUIERDA, caminandoIzquierda);

        sprite = caminandoDerecha;
        Sprite muerteDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemydieright),
                Tile.ancho, Tile.altura,
                4, 8, false);
        sprites.put(MUERTE_DERECHA, muerteDerecha);

        Sprite muerteIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemydie),
                Tile.ancho, Tile.altura,
                4, 8, false);
        sprites.put(MUERTE_IZQUIERDA, muerteIzquierda);

    }

    public void actualizar(long tiempo) {
        boolean finSprite = sprite.actualizar(tiempo);
        if (estado == INACTIVO) {
            if (velocidadX > 0)
                sprite = sprites.get(MUERTE_DERECHA);
            else
                sprite = sprites.get(MUERTE_IZQUIERDA);
        } else {

            if (velocidadX > 0) {
                sprite = sprites.get(CAMINANDO_DERECHA);
            }
            if (velocidadX < 0) {
                sprite = sprites.get(CAMINANDO_IZQUIERDA);
            }
        }

        if (estado == INACTIVO && finSprite == true) {
            estado = ELIMINAR;
        }

    }

    public void girar() {
        velocidadX = velocidadX * -1;
    }

    public void dibujar(Canvas canvas) {
        sprite.dibujarSprite(canvas, (int) android.R.attr.x - Nivel.scrollEjeX, (int) android.R.attr.y - Nivel.scrollEjeY);
    }

    public void destruir() {
        velocidadX = 0;
        estado = INACTIVO;
    }
}
