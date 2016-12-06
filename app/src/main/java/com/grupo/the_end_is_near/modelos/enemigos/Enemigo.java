package com.grupo.the_end_is_near.modelos.enemigos;

import android.content.Context;
import android.graphics.Canvas;

import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.escenario.Nivel;
import com.grupo.the_end_is_near.escenario.Tile;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.gestores.Utilidades;
import com.grupo.the_end_is_near.graficos.Sprite;
import com.grupo.the_end_is_near.modelos.Modelo;
import com.grupo.the_end_is_near.modelos.enemigos.EState;

import java.util.HashMap;

/**
 * Created by cueto on 18/10/2016.
 */

public abstract class Enemigo extends Modelo {
    public EState estado = EState.ACTIVO;

    public static final String CAMINANDO_DERECHA = "Caminando_derecha";
    public static final String CAMINANDO_IZQUIERDA = "caminando_izquierda";
    public static final String MUERTE_DERECHA = "muerte_derecha";
    public static final String MUERTE_IZQUIERDA = "muerte_izquierda";

    private Sprite sprite;
    private HashMap<String,Sprite> sprites = new HashMap<String,Sprite> ();

    public double velocidadX = 1.2;

    private int cadenciaDisparo;
    private long milisegundosDisparo;

    public Enemigo(Context context, double xInicial, double yInicial,int altura, int ancho) {
        super(context, 0, 0, altura, ancho);

        this.x = xInicial;
        this.y = yInicial - altura/2;

        cDerecha = 15;
        cIzquierda = 15;
        cArriba = 20;
        cAbajo = 20;

        inicializar();
    }

    public void inicializar (){

        Sprite caminandoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemyrunright),
                ancho, altura,
                4, 4, true);
        sprites.put(CAMINANDO_DERECHA, caminandoDerecha);

        Sprite caminandoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemyrun),
                ancho, altura,
                4, 4, true);
        sprites.put(CAMINANDO_IZQUIERDA, caminandoIzquierda);

        Sprite muerteDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemydieright),
                ancho, altura,
                4, 8, false);
        sprites.put(MUERTE_DERECHA, muerteDerecha);

        Sprite muerteIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemydie),
                ancho, altura,
                4, 8, false);
        sprites.put(MUERTE_IZQUIERDA, muerteIzquierda);


        sprite = caminandoDerecha;
    }

    @Override
    public void actualizar (long tiempo) {
        boolean finSprite = sprite.actualizar(tiempo);

        if (estado == EState.INACTIVO){
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

        if ( estado == EState.INACTIVO && finSprite == true){
            estado = EState.ELIMINAR;
        }

    }

    public void mover(Nivel nivel){
        Tile[][] mapaTiles = nivel.getMapaTiles();
        int anchoMapaTiles= mapaTiles.length;

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
                            Tile.PASABLE &&
                    mapaTiles[tileXEnemigoDerecha + 1][tileYEnemigoSuperior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXEnemigoDerecha + 1][tileYEnemigoInferior + 1].tipoDeColision ==
                            Tile.SOLIDO) {

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
        if (velocidadX > 0) {
            //  Solo una condicion para pasar:  Tile delante libre, el de abajo solido
            if (tileXEnemigoDerecha + 1 <= anchoMapaTiles - 1 &&
                    mapaTiles[tileXEnemigoDerecha + 1][tileYEnemigoInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXEnemigoDerecha + 1][tileYEnemigoCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXEnemigoDerecha + 1][tileYEnemigoSuperior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXEnemigoDerecha + 1][tileYEnemigoInferior + 1].tipoDeColision ==
                            Tile.SOLIDO) {

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
                            Tile.PASABLE &&
                    mapaTiles[tileXEnemigoIzquierda - 1][tileYEnemigoSuperior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXEnemigoIzquierda - 1][tileYEnemigoInferior + 1].tipoDeColision
                            == Tile.SOLIDO) {

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

    public void girar(){
        velocidadX = velocidadX*-1;
    }

    public void destruir (){
        velocidadX = 0;
        estado = EState.INACTIVO;
    }

    public void dibujar(Canvas canvas){
        sprite.dibujarSprite(canvas, (int) x - Nivel.scrollEjeX, (int) y -Nivel.scrollEjeY);
    }

    protected HashMap<String, Sprite> _getSprites() {
        return sprites;
    }

    protected void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    protected Sprite _getSprite() {
        return sprite;
    }

    public void setVelocidadX(double velocidadX) {
        this.velocidadX = velocidadX;
    }

    public long getMilisegundosDisparo() {
        return milisegundosDisparo;
    }

    public int getCadenciaDisparo() {
        return cadenciaDisparo;
    }

    public void setCadenciaDisparo(int cadenciaDisparo) {
        this.cadenciaDisparo = cadenciaDisparo;
    }

    public void setMilisegundosDisparo(long milisegundosDisparo) {
        this.milisegundosDisparo = milisegundosDisparo;
    }
}
