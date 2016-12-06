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

public class EnemigoInteligente extends Enemigo {

    public static final String UP = "up";
    public static final String FRONT = "front";

    public double velocidadY;

    public EnemigoInteligente(Context context, double xInicial, double yInicial) {
        super(context, xInicial, yInicial, 60, 60);
        setVelocidadX(0);
        setVelocidadY(0);
    }

    @Override
    public void inicializar() {

        Sprite caminandoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemy_fly_right),
                ancho, altura,
                5, 3, true);
        _getSprites().put(CAMINANDO_DERECHA, caminandoDerecha);

        Sprite caminandoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemy_fly_left),
                ancho, altura,
                5, 3, true);
        _getSprites().put(CAMINANDO_IZQUIERDA, caminandoIzquierda);

        Sprite front = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemy_fly_front),
                ancho, altura,
                5, 3, true);
        _getSprites().put(FRONT, front);

        Sprite up = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemy_fly_back),
                ancho, altura,
                5, 3, true);
        _getSprites().put(UP, up);

        setSprite(caminandoDerecha);
    }

    @Override
    public void actualizar(long tiempo) {
        Sprite sprite = _getSprite();
        boolean finSprite = sprite.actualizar(tiempo);

        if (estado == EState.INACTIVO) {
            estado = EState.ELIMINAR;
        } else {

            if (velocidadX > 0)
                setSprite(_getSprites().get(CAMINANDO_DERECHA));

            if (velocidadX < 0)
                setSprite(_getSprites().get(CAMINANDO_IZQUIERDA));

            if (velocidadX == 0){
                if(velocidadY < 0)
                    setSprite(_getSprites().get(UP));
                else{
                    setSprite(_getSprites().get(FRONT));
                }
                }
            }

    }

    @Override
    public void mover(Nivel nivel) {
        Tile[][] mapaTiles = nivel.getMapaTiles();

        int anchoMapaTiles = mapaTiles.length;
        int largoMapaTiles = mapaTiles[0].length;

        int tileXEnemigoIzquierda =
                (int) (x - (ancho / 2 - 1)) / Tile.ancho;
        int tileXEnemigoDerecha =
                (int) (x + (ancho / 2 - 1)) / Tile.ancho;
        int tileXEnemigoCentro =
                (int) x / Tile.ancho;

        int tileYEnemigoInferior =
                (int) (y + (altura / 2 - 1)) / Tile.altura;
        int tileYEnemigoCentro =
                (int) y / Tile.altura;
        int tileYEnemigoSuperior =
                (int) (y - (altura / 2 - 1)) / Tile.altura;

        //distancia entre el muñeco y el jugador
        double distancia = Math.sqrt(Math.pow(x - nivel.getJugador().x, 2) + Math.pow(y - nivel.getJugador().y, 2));
        if (distancia < 180) {

            if (x + 5 < nivel.getJugador().x)
                velocidadX = 1.2;
            else if (x - 5 > nivel.getJugador().x)
                velocidadX = -1.2;
            else
                velocidadX = 0;

            if (y + 5 < nivel.getJugador().y)
                velocidadY = 1.2;
            else if (y - 5 > nivel.getJugador().y)
                velocidadY = -1.2;
            else velocidadY = 0;
        } else {
            velocidadX = 0;
            velocidadY = 0;
        }

        if (velocidadX > 0) {
            //  No es elfin del mapa y el tile de delante es pasable
            if (tileXEnemigoDerecha + 1 <= anchoMapaTiles - 1 &&
                    mapaTiles[tileXEnemigoDerecha + 1][tileYEnemigoCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXEnemigoDerecha + 1][tileYEnemigoSuperior].tipoDeColision ==
                            Tile.PASABLE) {

                x += velocidadX;

                // Sino, me acerco al borde del que estoy
            } else if (tileXEnemigoDerecha + 1 <= anchoMapaTiles - 1) {

                int TileEnemigoDerecho = tileXEnemigoDerecha * Tile.ancho + Tile.ancho;
                double distanciaX = TileEnemigoDerecho - (x + ancho / 2);

                if (distanciaX > 0) {
                    double velocidadNecesaria = Math.min(distanciaX, velocidadX);
                    x += velocidadNecesaria;
                }
                // No hay Tile, o es el final del mapa
            }
        }

        if (velocidadY > 0) {
            //  No es elfin del mapa y el tile de delante es pasable
            if (tileYEnemigoInferior + 1 <= largoMapaTiles - 1 &&
                    mapaTiles[tileXEnemigoCentro][tileYEnemigoInferior].tipoDeColision ==
                            Tile.PASABLE /*&&
                    mapaTiles[tileXEnemigoDerecha][tileYEnemigoInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXEnemigoIzquierda][tileYEnemigoInferior].tipoDeColision ==
                            Tile.PASABLE*/) {

                y += velocidadY;

                // Sino, me acerco al borde del que estoy
            } else if (tileYEnemigoInferior + 1 <= largoMapaTiles - 1) {

                int TileEnemigoInferior = tileYEnemigoInferior * Tile.altura + Tile.altura;
                double distanciaY = TileEnemigoInferior - (y + altura / 2);

                if (distanciaY > 0) {
                    double velocidadNecesaria = Math.min(distanciaY, velocidadY);
                    y += velocidadNecesaria;
                }
            }
        }

        if (velocidadX < 0) {
            // Solo una condición para pasar: Tile izquierda pasable y suelo solido.
            if (tileXEnemigoIzquierda - 1 >= 0 &&
                    mapaTiles[tileXEnemigoIzquierda - 1][tileYEnemigoCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXEnemigoIzquierda - 1][tileYEnemigoSuperior].tipoDeColision ==
                            Tile.PASABLE) {

                x += velocidadX;

                // me acerco al borde del tile.
            } else if (tileXEnemigoIzquierda - 1 >= 0) {

                int TileEnemigoIzquierdo = tileXEnemigoIzquierda * Tile.ancho;
                double distanciaX = (x - ancho / 2) - TileEnemigoIzquierdo;

                if (distanciaX > 0) {
                    double velocidadNecesaria =
                            Utilidades.proximoACero(-distanciaX, velocidadX);
                    x += velocidadNecesaria;
                }
            }
        }

        if (velocidadY < 0) {
            // Solo una condición para pasar: Tile izquierda pasable y suelo solido.
            if (tileYEnemigoSuperior - 1 >= 0 &&
                    mapaTiles[tileXEnemigoCentro][tileYEnemigoSuperior - 1].tipoDeColision ==
                            Tile.PASABLE /*&&
                    mapaTiles[tileXEnemigoDerecha][tileYEnemigoSuperior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXEnemigoIzquierda][tileYEnemigoSuperior].tipoDeColision ==
                            Tile.PASABLE */) {

                y += velocidadY;

                // me acerco al borde del tile.
            } else if (tileYEnemigoSuperior - 1 >= 0) {

                int TileEnemigoSuperior = tileYEnemigoSuperior * Tile.altura;
                double distanciaY = (y - altura / 2) - TileEnemigoSuperior;

                if (distanciaY > 0) {
                    double velocidadNecesaria =
                            Utilidades.proximoACero(-distanciaY, velocidadY);
                    y += velocidadNecesaria;
                }
            }
        }
    }

    protected void setVelocidadY(double velocidadY) {
        this.velocidadY = velocidadY;
    }
}
