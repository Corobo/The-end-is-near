package com.grupo.the_end_is_near.modelos.enemigos;

import android.content.Context;

import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.escenario.Nivel;
import com.grupo.the_end_is_near.escenario.Tile;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.gestores.Utilidades;
import com.grupo.the_end_is_near.graficos.Sprite;

/**
 * Created by sergiocueto on 06/11/2016.
 */

public class Enemigo1 extends Enemigo {

    public Enemigo1(Context context, double xInicial, double yInicial, int idEnemigo) {
        super(context, xInicial, yInicial, 40, 40, idEnemigo);
    }

    @Override
    public void inicializar() {
        Sprite caminandoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemyrunright),
                ancho, altura,
                4, 4, true);
        _getSprites().put(CAMINANDO_DERECHA, caminandoDerecha);

        Sprite caminandoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemyrun),
                ancho, altura,
                4, 4, true);
        _getSprites().put(CAMINANDO_IZQUIERDA, caminandoIzquierda);

        Sprite muerteDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemydieright),
                ancho, altura,
                4, 8, false);
        _getSprites().put(MUERTE_DERECHA, muerteDerecha);

        Sprite muerteIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemydie),
                ancho, altura,
                4, 8, false);
        _getSprites().put(MUERTE_IZQUIERDA, muerteIzquierda);


        setSprite(caminandoDerecha);
    }

    @Override
    public void mover(Nivel nivel) {
        Tile[][] mapaTiles = nivel.getMapaTiles();
        int anchoMapaTiles = mapaTiles.length;

        int tileXEnemigoIzquierda =
                (int) (x - (ancho / 2 - 1)) / Tile.ancho;
        int tileXEnemigoDerecha =
                (int) (x + (ancho / 2 - 1)) / Tile.ancho;

        int tileYEnemigoInferior =
                (int) (y + (altura / 2 - 1)) / Tile.altura;
        int tileYEnemigoCentro =
                (int) y / Tile.altura;
        int tileYEnemigoSuperior =
                (int) (y - (altura / 2 - 1)) / Tile.altura;

        if (velocidadX > 0) {
            //  Solo una condicion para pasar:  Tile delante libre, el de abajo solido
            if (tileXEnemigoDerecha + 1 <= anchoMapaTiles - 1 &&
                    mapaTiles[tileXEnemigoDerecha + 1][tileYEnemigoInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXEnemigoDerecha + 1][tileYEnemigoCentro].tipoDeColision ==
                            Tile.PASABLE) {
                x += velocidadX;

                // Sino, me acerco al borde del que estoy
            } else if (tileXEnemigoDerecha + 1 <= anchoMapaTiles - 1) {

                int TileEnemigoDerecho = tileXEnemigoDerecha * Tile.ancho + Tile.ancho;
                double distanciaX = TileEnemigoDerecho - (x + ancho / 2);

                if (distanciaX > 0) {
                    double velocidadNecesaria = Math.min(distanciaX, velocidadX);
                    x += velocidadNecesaria;
                } else {
                    girar();
                }

                // No hay Tile, o es el final del mapa
            } else {
                girar();
            }
        }

        if (velocidadX < 0) {
            // Solo una condición para pasar: Tile izquierda pasable y suelo solido.
            if (tileXEnemigoIzquierda - 1 >= 0 &&
                    mapaTiles[tileXEnemigoIzquierda - 1][tileYEnemigoInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXEnemigoIzquierda - 1][tileYEnemigoCentro].tipoDeColision ==
                            Tile.PASABLE) {

                x += velocidadX;

                // Solido / borde del tile acercarse.
            } else if (tileXEnemigoIzquierda - 1 >= 0) {

                int TileEnemigoIzquierdo = tileXEnemigoIzquierda * Tile.ancho;
                double distanciaX = (x - ancho / 2) - TileEnemigoIzquierdo;

                if (distanciaX > 0) {
                    double velocidadNecesaria =
                            Utilidades.proximoACero(-distanciaX, velocidadX);
                    x += velocidadNecesaria;
                } else {
                    girar();
                }
            } else {
                girar();
            }
        }
    }

    private void girar() {
        velocidadX = velocidadX * -1;
    }

}
