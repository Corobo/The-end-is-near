package com.grupo.the_end_is_near.modelos.jugador.mapa;

import android.content.Context;
import android.graphics.Canvas;

import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.escenario.Nivel;
import com.grupo.the_end_is_near.escenario.Tile;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.gestores.Utilidades;
import com.grupo.the_end_is_near.graficos.Sprite;
import com.grupo.the_end_is_near.modelos.Modelo;
import com.grupo.the_end_is_near.modelos.items.Item;
import com.grupo.the_end_is_near.modelos.ciudadanos.Ciudadano;

import java.util.HashMap;


/**
 * Created by uo227602 on 05/10/2016.
 */
public class Jugador extends Modelo {

    //Animaciones
    public static final String PARADO_DERECHA = "Parado_derecha";
    public static final String PARADO_IZQUIERDA = "Parado_izquierda";
    public static final String PARADO_ARRIBA = "Parado_arriba";
    public static final String PARADO_ABAJO = "Parado_abajo";
    public static final String CAMINANDO_DERECHA = "Caminando_derecha";
    public static final String CAMINANDO_IZQUIERDA = "Caminando_izquierda";
    public static final String CAMINANDO_ARRIBA = "Caminando_arriba";
    public static final String CAMINANDO_ABAJO = "Caminando_abajo";

    private int vidas;
    // ACTUAL
    public double msInmunidad;
    public boolean golpeado = false;


    //Puntero sprite actual
    private Sprite sprite;

    private HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();

    double xInicial;
    double yInicial;


    double velocidadX;
    float velocidadY; // actual
    float velcidadSalto = -14; // velocidad que le da el salto

    public boolean saltoPendiente; // tiene que saltar
    public boolean enElAire; // está en el aire


    public int orientacion;
    public static final int DERECHA = 1;
    public static final int IZQUIERDA = -1;
    public static final int ARRIBA = 2;
    public static final int ABAJO = -2;

    public Jugador(Context context, double xInicial, double yInicial, int vidas) {
        super(context, 0, 0, 40, 40);

        // guardamos la posición inicial porque más tarde vamos a reiniciarlo
        this.xInicial = xInicial;
        this.yInicial = yInicial - altura / 2;

        this.x = this.xInicial;
        this.y = this.yInicial;
        this.vidas = vidas;

        inicializar();
    }

    public void inicializar() {
        Sprite paradoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.personaje_derecha_parado),
                ancho, altura,
                1, 1, true);
        sprites.put(PARADO_DERECHA, paradoDerecha);

        Sprite paradoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.personaje_izquierda_parado),
                ancho, altura,
                1, 1, true);
        sprites.put(PARADO_IZQUIERDA, paradoIzquierda);

        Sprite caminandoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.personaje_derecha_moviendo),
                ancho, altura,
                4, 2, true);
        sprites.put(CAMINANDO_DERECHA, caminandoDerecha);

        Sprite caminandoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.personaje_izquierda_moviendo),
                ancho, altura,
                4, 2, true);
        sprites.put(CAMINANDO_IZQUIERDA, caminandoIzquierda);

        Sprite arribaParado = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.personaje_arriba_parado),
                ancho, altura,
                1, 1, true);
        sprites.put(PARADO_ARRIBA, arribaParado);

        Sprite arribaMoviendo = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.personaje_arriba_moviendo),
                ancho, altura,
                4, 2, true);
        sprites.put(CAMINANDO_ARRIBA, arribaMoviendo);


        Sprite abajoParado = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.personaje_abajo_parado),
                ancho, altura,
                4, 2, true);
        sprites.put(PARADO_ABAJO, abajoParado);

        Sprite abajoMoviendo = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.personaje_abajo_moviendo),
                ancho, altura,
                4, 2, true);
        sprites.put(CAMINANDO_ABAJO, abajoMoviendo);


        // animación actual
        sprite = abajoParado;
    }

    public void actualizar(long tiempo) {

        boolean finSprite = sprite.actualizar(tiempo);

        if (msInmunidad > 0) {
            msInmunidad -= tiempo;
        }

        if (golpeado && finSprite) {
            golpeado = false;
        }
        if (velocidadX > 0) {
            sprite = sprites.get(CAMINANDO_DERECHA);
            orientacion = DERECHA;
        } else if (velocidadX < 0) {
            sprite = sprites.get(CAMINANDO_IZQUIERDA);
            orientacion = IZQUIERDA;
        }
        //velocidad x = 0
        else {
            if (velocidadY > 0) {
                sprite = sprites.get(CAMINANDO_ABAJO);
                orientacion = ABAJO;
            } else if (velocidadY < 0) {
                sprite = sprites.get(CAMINANDO_ARRIBA);
                orientacion = ARRIBA;
            }
            //velocidadY =0
            else {
                if (orientacion == DERECHA) {
                    sprite = sprites.get(PARADO_DERECHA);
                }
                if (orientacion == IZQUIERDA) {
                    sprite = sprites.get(PARADO_IZQUIERDA);
                }
                if (orientacion == ARRIBA) {
                    sprite = sprites.get(PARADO_ARRIBA);
                }
                if (orientacion == ABAJO) {
                    sprite = sprites.get(PARADO_ABAJO);
                }
            }
        }

    }

    public void mover(Nivel nivel) {
        Tile[][] mapaTiles = nivel.getMapaTiles();
        int tileXJugadorIzquierda
                = (int) (x - (ancho / 2 - 1)) / Tile.ancho;
        int tileXJugadorDerecha
                = (int) (x + (ancho / 2 - 1)) / Tile.ancho;
        int tileXJugadorCentro
                = (int) x / Tile.ancho;

        int tileYJugadorInferior
                = (int) (y + (altura / 2 - 1)) / Tile.altura;
        int tileYJugadorCentro
                = (int) y / Tile.altura;
        int tileYJugadorSuperior
                = (int) (y - (altura / 2 - 1)) / Tile.altura;

        //colisiones con los Itemas
        boolean colisionaArriba = false;
        boolean colisionaAbajo = false;
        boolean colisionaDerecha = false;
        boolean colisionaIzquierda = false;

        //hace a los items sólidos
        for (Item i : nivel.getItems()) {
            if(colisiona(i)){
                if(y-cAbajo < i.y)
                    colisionaAbajo=true;
                if(y-cArriba > i.y)
                    colisionaArriba=true;
                if(x+cDerecha<i.x)
                    colisionaDerecha =true;
                if(x-cIzquierda>i.x)
                    colisionaIzquierda=true;
            }
        }

        //hace a los ciudadanos sólidos
        for(Ciudadano c : nivel.getBuenasGentes()){
            if(colisiona(c)){
                if(y < c.y)
                    colisionaAbajo=true;
                if(y > c.y)
                    colisionaArriba=true;
                if(x<c.x)
                    colisionaDerecha =true;
                if(x>c.x)
                    colisionaIzquierda=true;
            }
        }

        // derecha o parado
        if (getVelocidadX() > 0) {
            // Tengo un tile delante y es PASABLE
            // El tile de delante está dentro del Nivel
            if (tileXJugadorDerecha + 1 <= nivel.anchoMapaTiles() - 1 &&
                    tileYJugadorInferior <= nivel.altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorDerecha + 1][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha + 1][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE) {

                if (!colisionaDerecha)
                    x += getVelocidadX();

                // No tengo un tile PASABLE delante
                // o es el FINAL del nivel o es uno SOLIDO
            } else if (tileXJugadorDerecha <= nivel.anchoMapaTiles() - 1 &&
                    tileYJugadorInferior <= nivel.altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE) {

                // Si en el propio tile del jugador queda espacio para
                // avanzar más, avanzo
                int TileJugadorBordeDerecho = tileXJugadorDerecha * Tile.ancho + Tile.ancho;
                double distanciaX = TileJugadorBordeDerecho - (x + ancho / 2);

                if (distanciaX > 0) {
                    double velocidadNecesaria = Math.min(distanciaX, getVelocidadX());
                    x += velocidadNecesaria;
                } else {
                    // Opcional, corregir posición
                    x = TileJugadorBordeDerecho - ancho / 2;
                }
            }
        }

        // izquierda
        if (getVelocidadX() <= 0) {
            // Tengo un tile detrás y es PASABLE
            // El tile de delante está dentro del Nivel
            if (tileXJugadorIzquierda - 1 >= 0 &&
                    tileYJugadorInferior < nivel.altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorIzquierda - 1][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda - 1][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE) {
                if (!colisionaIzquierda)
                    x += getVelocidadX();

                // No tengo un tile PASABLE detrás
                // o es el INICIO del nivel o es uno SOLIDO
            } else if (tileXJugadorIzquierda >= 0 && tileYJugadorInferior <= nivel.altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorInferior].tipoDeColision
                            == Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorCentro].tipoDeColision
                            == Tile.PASABLE) {

                // Si en el propio tile del jugador queda espacio para
                // avanzar más, avanzo
                int TileJugadorBordeIzquierdo = tileXJugadorIzquierda * Tile.ancho;
                double distanciaX = (x - ancho / 2) - TileJugadorBordeIzquierdo;

                if (distanciaX > 0) {
                    double velocidadNecesaria = Utilidades.proximoACero(-distanciaX, getVelocidadX());
                    x += velocidadNecesaria;
                } else {
                    // Opcional, corregir posición
                    x = TileJugadorBordeIzquierdo + ancho / 2;
                }
            }
        }
        // Hacia arriba
        if (getVelocidadY() < 0) {
            // Tile superior PASABLE
            // Podemos seguir moviendo hacia arriba
            if (tileYJugadorSuperior - 1 >= 0 &&
                    mapaTiles[tileXJugadorCentro][tileYJugadorInferior - 1].tipoDeColision ==
                            Tile.PASABLE) {
                if (!colisionaArriba)
                    y += getVelocidadY();

            } else {

                int TileJugadorBordeSuperior = (tileYJugadorSuperior) * Tile.altura;
                double distanciaY = (y - altura / 2) - TileJugadorBordeSuperior;

                if (distanciaY > 0 &&
                        mapaTiles[tileXJugadorCentro][tileYJugadorInferior - 1].tipoDeColision ==
                                Tile.PASABLE) {
                    y += Utilidades.proximoACero(-distanciaY, getVelocidadY());

                }

            }

        }

        // Hacia abajo
        if (getVelocidadY() >= 0) {
            // Tile inferior PASABLE
            // Podemos seguir moviendo hacia abajo
            if (tileYJugadorInferior + 1 <= nivel.altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorCentro][tileYJugadorInferior + 1].tipoDeColision ==
                            Tile.PASABLE) {

                if (!colisionaAbajo)
                    y += getVelocidadY();

            } else if (tileYJugadorInferior + 1 <= nivel.altoMapaTiles() - 1) {

                // Con que uno de los dos sea solido ya no puede caer
                // Si en el propio tile del jugador queda espacio para bajar más, bajo
                int TileJugadorBordeInferior =
                        tileYJugadorInferior * Tile.altura + Tile.altura;

                double distanciaY =
                        TileJugadorBordeInferior - (y + altura / 2);

                if (distanciaY > 0) {
                    y += Math.min(distanciaY, getVelocidadY());

                } else {
                    // Toca suelo, nos aseguramos de que está bien
                    y = TileJugadorBordeInferior - altura / 2;
                    setVelocidadY(0);
                }

            }
        }
    }


    public void dibujar(Canvas canvas) {
        sprite.dibujarSprite(canvas, (int) x - Nivel.scrollEjeX, (int) y - Nivel.scrollEjeY, msInmunidad > 0);
    }

    public void procesarOrdenes(float orientacionPadX, float orientacionPadY) {
        if (orientacionPadX > 0) {
            velocidadX = -5;
        } else if (orientacionPadX < 0) {
            velocidadX = 5;
        } else {
            velocidadX = 0;
        }
        if (orientacionPadY > 0) {
            velocidadY = -5;
        } else if (orientacionPadY < 0) {
            velocidadY = 5;
        } else {
            velocidadY = 0;
        }
    }

    public int getVidas() {
        return vidas;
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }

    public double getVelocidadX() {
        return velocidadX;
    }

    public float getVelocidadY() {
        return velocidadY;
    }

    public void setVelocidadY(float velocidadY) {
        this.velocidadY = velocidadY;
    }

    public void setPosicionInicial(double xInicial, double yInicial) {
        this.xInicial = xInicial;
        this.yInicial = yInicial;
    }

}
