package com.grupo.the_end_is_near.modelos.mapa.enemigos;

import android.content.Context;

import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.escenario.Nivel;
import com.grupo.the_end_is_near.escenario.Tile;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.gestores.Utilidades;
import com.grupo.the_end_is_near.graficos.Sprite;
import com.grupo.the_end_is_near.modelos.mapa.ciudadanos.Ciudadano;

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
                CargadorGraficos.cargarDrawable(context, R.drawable.enemigo_derecha_moviendo),
                ancho, altura,
                3, 3, true);
        _getSprites().put(CAMINANDO_DERECHA, caminandoDerecha);

        Sprite caminandoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemigo_izquierda_moviendo),
                ancho, altura,
                3, 3, true);
        _getSprites().put(CAMINANDO_IZQUIERDA, caminandoIzquierda);


        setSprite(caminandoDerecha);
    }

    @Override
    public void mover(Nivel nivel) {
        //comento porque tal como está el mapa hay un zombie que siempre choca con un caramelo
        //colisiones con items
//        for(Item item :nivel.getItems()){
//            if(colisiona(item))
//                girar();
//        }

        //colisiones enemigo ciudadano
        for(Ciudadano ciu : nivel.getBuenasGentes()){
            if(colisiona(ciu))
                girar();
        }
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
