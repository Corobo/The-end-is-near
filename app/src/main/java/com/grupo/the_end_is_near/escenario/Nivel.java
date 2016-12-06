package com.grupo.the_end_is_near.escenario;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.grupo.the_end_is_near.GameView;
import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.gestores.Opciones;
import com.grupo.the_end_is_near.gestores.Utilidades;
import com.grupo.the_end_is_near.modelos.Enemigo;
import com.grupo.the_end_is_near.modelos.escenarios.Fondo;
import com.grupo.the_end_is_near.modelos.jugador.mapa.Jugador;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Nivel {
    private Context context = null;
    private int numeroNivel;
    private Jugador jugador;
    private Fondo fondo;
    private List<Enemigo> enemigos;

    public boolean inicializado;
    private Tile[][] mapaTiles;

    public float orientacionPadX = 0;
    public float orientacionPadY = 0;

    public static int scrollEjeX = 0;
    public static int scrollEjeY = 0;

    public boolean padArribaPulsado = false;
    public boolean padAbajoPulsado = false;

    public GameView gameView;

    public Bitmap mensaje;
    public boolean nivelPausado;

    public Nivel(Context context, int numeroNivel) throws Exception {
        inicializado = false;
        this.context = context;
        this.numeroNivel = numeroNivel;
        inicializar();

        //generamos un bitmap negro
        Bitmap blackBitmap = Bitmap.createBitmap(altoMapaTiles(), anchoMapaTiles(),
                Bitmap.Config.ARGB_8888);
        blackBitmap.eraseColor(Color.BLACK);
        fondo = new Fondo(context, blackBitmap, 1);
        inicializado = true;
    }

    public void inicializar() throws Exception {
        scrollEjeX = 0;
        scrollEjeY = 0;

        mensaje = CargadorGraficos.cargarBitmap(context, R.drawable.description);
        enemigos = new LinkedList<Enemigo>();
        nivelPausado = true;
        inicializarMapaTiles();
        //TODO ajustar scroll
        scrollEjeY = (int) (altoMapaTiles() - tilesEnDistanciaY(GameView.pantallaAlto)) * Tile.altura;
    }


    public void actualizar(long tiempo) throws Exception {
        if (inicializado) {
            for (Enemigo enemigo : enemigos) {
                enemigo.actualizar(tiempo);
            }
            jugador.procesarOrdenes(orientacionPadX, orientacionPadY);
            jugador.actualizar(tiempo);

            aplicarReglasMovimiento();
        }
    }


    public void dibujar(Canvas canvas) {
        if (inicializado) {
            fondo.dibujar(canvas);
            dibujarTiles(canvas);
            jugador.dibujar(canvas);

            for (Enemigo enemigo : enemigos) {
                enemigo.dibujar(canvas);
            }

            if (nivelPausado) {
                // la foto mide 480x320
                Rect orgigen = new Rect(0, 0,
                        480, 320);

                Paint efectoTransparente = new Paint();
                efectoTransparente.setAntiAlias(true);

                Rect destino = new Rect((int) (GameView.pantallaAncho / 2 - 480 / 2),
                        (int) (GameView.pantallaAlto / 2 - 320 / 2),
                        (int) (GameView.pantallaAncho / 2 + 480 / 2),
                        (int) (GameView.pantallaAlto / 2 + 320 / 2));
                canvas.drawBitmap(mensaje, orgigen, destino, null);
            }
        }
    }

    public int anchoMapaTiles() {
        return mapaTiles.length;
    }

    public int altoMapaTiles() {

        return mapaTiles[0].length;
    }

    private void inicializarMapaTiles() throws Exception {
        InputStream is = context.getAssets().open(numeroNivel + ".txt");
        int anchoLinea;

        List<String> lineas = new LinkedList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        {
            String linea = reader.readLine();
            anchoLinea = linea.length();
            while (linea != null) {
                lineas.add(linea);
                if (linea.length() != anchoLinea) {
                    Log.e("ERROR", "Dimensiones incorrectas en la línea");
                    throw new Exception("Dimensiones incorrectas en la línea.");
                }
                linea = reader.readLine();
            }
        }

        // Inicializar la matriz
        mapaTiles = new Tile[anchoLinea][lineas.size()];
        // Iterar y completar todas las posiciones
        for (int y = 0; y < altoMapaTiles(); ++y) {
            for (int x = 0; x < anchoMapaTiles(); ++x) {
                char tipoDeTile = lineas.get(y).charAt(x);//lines[y][x];
                mapaTiles[x][y] = inicializarTile(tipoDeTile, x, y);
            }
        }
    }

    private Tile inicializarTile(char codigoTile, int x, int y) {
        switch (codigoTile) {
            case '1':
                // Jugador
                // Posicion centro abajo
                int xCentroAbajoTile = x * Tile.ancho + Tile.ancho / 2;
                int yCentroAbajoTile = y * Tile.altura + Tile.altura;
                jugador = new Jugador(context, xCentroAbajoTile, yCentroAbajoTile, 3);

                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_verde_1), Tile.PASABLE);
            case '2':
                // Ciudadano
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.ciudadano_3_fondo_verde), Tile.SOLIDO);
            case 'a':
                // antorcha
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.antorcha_marron), Tile.SOLIDO);
            case '9':
                //Enemigo
                int xCentroAbajoTileE = x * Tile.ancho + Tile.ancho / 2;
                int yCentroAbajoTileE = y * Tile.altura + Tile.altura;
                enemigos.add(new Enemigo(context, xCentroAbajoTileE, yCentroAbajoTileE));

                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_marron), Tile.PASABLE);
            case 'p':
                // pocion suelo marron
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.pocion_suelo_marron), Tile.SOLIDO);
            case 'Y':
                // parte diagonal izquierda de la cabaña con suelo marron
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.cabana_diagonal_izquierda_marron), Tile.SOLIDO);
            case 'U':
                // parte diagonal derecha de la cabaña con suelo marron
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.cabana_diagonal_derecha_marron), Tile.SOLIDO);
            case 'y':
                // parte abajo izquierda de la cabaña con suelo marron
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.cabana_abajo_izquierda_marron), Tile.SOLIDO);
            case 'u':
                // parte abajo derecha de la cabaña con suelo marron
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.cabana_abajo_derecha_marron), Tile.SOLIDO);
            case '.':
                //Fondo general del mapa, Aire Libre.
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_verde_1), Tile.PASABLE);
            case 'W':
                // suelo verde con linea marron Arriba
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_verde_marron_linea_arriba), Tile.PASABLE);
            case 'w':
                // suelo verde con esquina marron Arriba derecha
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_verde_marron_esquina_derecha_arriba), Tile.PASABLE);
            case 'e':
                // suelo verde con esquina marron Arriba izquierda
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_verde_marron_esquina_izquierda_arriba), Tile.PASABLE);
            case 'E':
                // suelo verde con linea marron izquierda
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_verde_marron_linea_izquierda), Tile.PASABLE);
            case 'r':
                // suelo verde con esquina marron abajo izquierda
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_verde_marron_esquina_izquierda_down), Tile.PASABLE);
            case 'R':
                // suelo verde con linea marron abajo
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_verde_marron_linea_abajo), Tile.PASABLE);
            case 't':
                // suelo verde con esquina marron abajo derecha
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_verde_marron_esquina_derecha_down), Tile.PASABLE);
            case 'T':
                // suelo verde con linea marron derecha
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_verde_marron_linea_derecha), Tile.PASABLE);
            case '#':
                // Arbol redondo, no se puede pasar
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.arbol_redondo_1), Tile.SOLIDO);
            case '$':
                // Arbol pico, no se puede pasar
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.arbol_pico_1), Tile.SOLIDO);
            case 'A':
                // Inicio camino de tierra borde izquierda Arriba
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_marron_verde_esquina_izquierda_up), Tile.PASABLE);
            case 'Z':
                // Inicio camino de tierra borde izquierda Abajo
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_marron_verde_esquina_izquierda_down), Tile.PASABLE);
            case 'X':
                // camino de tierra con linea Abajo
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_marron_verde_linea_abajo), Tile.PASABLE);
            case 'S':
                // camino de tierra con linea Arriba
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_marron_verde_linea_arriba), Tile.PASABLE);
            case 'C':
                // camino de tierra con esquina derecha abajo
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_marron_verde_esquina_derecha_down), Tile.PASABLE);
            case 'q':
                // camino de tierra con mueca verde Arriba izq
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_marron_mueca_verde_izq), Tile.PASABLE);
            case ',':
                // camino de tierra
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_marron), Tile.PASABLE);
            case 'D':
                // camino de tierra con linea Derecha
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_marron_verde_linea_derecha), Tile.PASABLE);
            case 'F':
                // camino de tierra con linea Izquierda
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_marron_verde_linea_izquierda), Tile.PASABLE);
            default:
                //cualquier otro caso
                return new Tile(null, Tile.PASABLE);
        }
    }

    private void dibujarTiles(Canvas canvas) {

        int tileXJugador = (int) jugador.x / Tile.ancho;
        int izquierda = (int) (tileXJugador - tilesEnDistanciaX(jugador.x - scrollEjeX));
        izquierda = Math.max(0, izquierda); // Que nunca sea < 0, ej -1
        if (jugador.x <
                (anchoMapaTiles() - tilesEnDistanciaX(GameView.pantallaAncho * 0.3)) * Tile.ancho)
            if (jugador.x - scrollEjeX > GameView.pantallaAncho * 0.7) {
                int catidad = (int) ((jugador.x - scrollEjeX) - GameView.pantallaAncho * 0.7);
                scrollEjeX += catidad;
                Log.v("Fondo.mover", "Fondo.mover: Scroll aumentado");

            }

        if (jugador.x >
                tilesEnDistanciaX(GameView.pantallaAncho * 0.3) * Tile.ancho)
            if (jugador.x - scrollEjeX < GameView.pantallaAncho * 0.3) {
                int cantidad = -(int) (GameView.pantallaAncho * 0.3 - (jugador.x - scrollEjeX));
                scrollEjeX += cantidad;
                Log.v("Fondo.mover", "Fondo.mover: Scroll reducido");
            }

        int derecha = izquierda +
                GameView.pantallaAncho / Tile.ancho + 1;

        derecha = Math.min(derecha, anchoMapaTiles() - 1);

        int tileYJugador = (int) jugador.y / Tile.altura;

        int arriba = (int) (tileYJugador - tilesEnDistanciaY(jugador.y - scrollEjeY));
        arriba = Math.max(0, arriba);

        if (jugador.y <
                altoMapaTiles() * Tile.altura - GameView.pantallaAlto * 0.3)
            if (jugador.y - scrollEjeY > GameView.pantallaAlto * 0.7)
                scrollEjeY += (int) ((jugador.y - scrollEjeY) - GameView.pantallaAlto * 0.7);


        if (jugador.y > GameView.pantallaAlto * 0.3)
            if (jugador.y - scrollEjeY < GameView.pantallaAlto * 0.3)
                scrollEjeY -= (int) (GameView.pantallaAlto * 0.3 - (jugador.y - scrollEjeY));


        int abajo = arriba +
                GameView.pantallaAlto / Tile.altura + 1;

        abajo = Math.min(abajo, altoMapaTiles() - 1);


        for (int y = arriba; y <= abajo; ++y) {
            for (int x = izquierda; x <= derecha; ++x) {
                if (mapaTiles[x][y].imagen != null) {
                    // Calcular la posición en pantalla correspondiente
                    // izquierda, arriba, derecha , abajo

                    mapaTiles[x][y].imagen.setBounds(
                            (x * Tile.ancho) - scrollEjeX,
                            (y * Tile.altura) - scrollEjeY,
                            (x * Tile.ancho) + Tile.ancho - scrollEjeX,
                            (y * Tile.altura + Tile.altura) - scrollEjeY);

                    mapaTiles[x][y].imagen.draw(canvas);
                }
            }
        }
    }


    private void aplicarReglasMovimiento() throws Exception {

        int tileXJugadorIzquierda
                = (int) (jugador.x - (jugador.ancho / 2 - 1)) / Tile.ancho;
        int tileXJugadorDerecha
                = (int) (jugador.x + (jugador.ancho / 2 - 1)) / Tile.ancho;
        int tileXJugadorCentro
                = (int) jugador.x / Tile.ancho;

        int tileYJugadorInferior
                = (int) (jugador.y + (jugador.altura / 2 - 1)) / Tile.altura;
        int tileYJugadorCentro
                = (int) jugador.y / Tile.altura;
        int tileYJugadorSuperior
                = (int) (jugador.y - (jugador.altura / 2 - 1)) / Tile.altura;


       /* for (Iterator<Enemigo> iterator = enemigos.iterator(); iterator.hasNext(); ) {
            Enemigo enemigo = iterator.next();

            if (enemigo.estado == Enemigo.ELIMINAR) {
                iterator.remove();
                continue;
            }
            if (enemigo.estado != Enemigo.ACTIVO)
                continue;

            int tileXEnemigoIzquierda =
                    (int) (enemigo.x - (enemigo.ancho / 2 - 1)) / Tile.ancho;
            int tileXEnemigoDerecha =
                    (int) (enemigo.x + (enemigo.ancho / 2 - 1)) / Tile.ancho;

            int tileYEnemigoInferior =
                    (int) (enemigo.y + (enemigo.altura / 2 - 1)) / Tile.altura;
            int tileYEnemigoCentro =
                    (int) enemigo.y / Tile.altura;
            int tileYEnemigoSuperior =
                    (int) (enemigo.y - (enemigo.altura / 2 - 1)) / Tile.altura;
//Se comprueba la colision del enemigo y el jugador
            int rango = 4;
            if (tileXJugadorIzquierda - rango < tileXEnemigoIzquierda &&
                    tileXJugadorIzquierda + rango > tileXEnemigoIzquierda) {

          *//*      if (jugador.colisiona(enemigo)) {
                    if (jugador.golpeado() <= 0) {
                        jugador.restablecerPosicionInicial();
                        scrollEjeX = 0;
                        nivelPausado = true;
                        marcador.setPuntos(0);
                        mensaje = CargadorGraficos.cargarBitmap(context, R.drawable.you_lose);
                        return;
                    }
                }*//*
            }

//Movimiento Derecha del enemigo
            if (enemigo.velocidadX > 0) {
                //  Solo una condicion para pasar:  Tile delante libre, el de abajo solido
                if (tileXEnemigoDerecha + 1 <= anchoMapaTiles() - 1 &&
                        mapaTiles[tileXEnemigoDerecha + 1][tileYEnemigoInferior].tipoDeColision ==
                                Tile.PASABLE &&
                        mapaTiles[tileXEnemigoDerecha + 1][tileYEnemigoCentro].tipoDeColision ==
                                Tile.PASABLE &&
                        mapaTiles[tileXEnemigoDerecha + 1][tileYEnemigoSuperior].tipoDeColision ==
                                Tile.PASABLE &&
                        mapaTiles[tileXEnemigoDerecha + 1][tileYEnemigoInferior + 1].tipoDeColision ==
                                Tile.SOLIDO) {

                    enemigo.x += enemigo.velocidadX;

                    // Sino, me acerco al borde del que estoy
                } else if (tileXEnemigoDerecha + 1 <= anchoMapaTiles() - 1) {

                    int TileEnemigoDerecho = tileXEnemigoDerecha * Tile.ancho + Tile.ancho;
                    double distanciaX = TileEnemigoDerecho - (enemigo.x + enemigo.ancho / 2);

                    if (distanciaX > 0) {
                        double velocidadNecesaria = Math.min(distanciaX, enemigo.velocidadX);
                        enemigo.x += velocidadNecesaria;
                    } else {
                        enemigo.girar();
                    }

                    // No hay Tile, o es el final del mapa
                } else {
                    enemigo.girar();
                }
            }
            //Movimiento Izquierda del enemigo
            if (enemigo.velocidadX < 0) {
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

                    enemigo.x += enemigo.velocidadX;

                    // Solido / borde del tile acercarse.
                } else if (tileXEnemigoIzquierda - 1 >= 0) {

                    int TileEnemigoIzquierdo = tileXEnemigoIzquierda * Tile.ancho;
                    double distanciaX = (enemigo.x - enemigo.ancho / 2) - TileEnemigoIzquierdo;

                    if (distanciaX > 0) {
                        double velocidadNecesaria =
                                Utilidades.proximoACero(-distanciaX, enemigo.velocidadX);
                        enemigo.x += velocidadNecesaria;
                    } else {
                        enemigo.girar();
                    }
                } else {
                    enemigo.girar();
                }
            }

        } */

        // derecha o parado
        if (jugador.getVelocidadX() > 0) {
            // Tengo un tile delante y es PASABLE
            // El tile de delante está dentro del Nivel
            if (tileXJugadorDerecha + 1 <= anchoMapaTiles() - 1 &&
                    tileYJugadorInferior <= altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorDerecha + 1][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha + 1][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE) {
                jugador.x += jugador.getVelocidadX();

                // No tengo un tile PASABLE delante
                // o es el FINAL del nivel o es uno SOLIDO
            } else if (tileXJugadorDerecha <= anchoMapaTiles() - 1 &&
                    tileYJugadorInferior <= altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorSuperior].tipoDeColision ==
                            Tile.PASABLE) {

                // Si en el propio tile del jugador queda espacio para
                // avanzar más, avanzo
                int TileJugadorBordeDerecho = tileXJugadorDerecha * Tile.ancho + Tile.ancho;
                double distanciaX = TileJugadorBordeDerecho - (jugador.x + jugador.ancho / 2);

                if (distanciaX > 0) {
                    double velocidadNecesaria = Math.min(distanciaX, jugador.getVelocidadX());
                    jugador.x += velocidadNecesaria;
                } else {
                    // Opcional, corregir posición
                    jugador.x = TileJugadorBordeDerecho - jugador.ancho / 2;
                }
            }
        }

        // izquierda
        if (jugador.getVelocidadX() <= 0) {
            // Tengo un tile detrás y es PASABLE
            // El tile de delante está dentro del Nivel
            if (tileXJugadorIzquierda - 1 >= 0 &&
                    tileYJugadorInferior < altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorIzquierda - 1][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda - 1][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE) {
                jugador.x += jugador.getVelocidadX();

                // No tengo un tile PASABLE detrás
                // o es el INICIO del nivel o es uno SOLIDO
            } else if (tileXJugadorIzquierda >= 0 && tileYJugadorInferior <= altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorInferior].tipoDeColision
                            == Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorCentro].tipoDeColision
                            == Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorSuperior].tipoDeColision
                            == Tile.PASABLE) {

                // Si en el propio tile del jugador queda espacio para
                // avanzar más, avanzo
                int TileJugadorBordeIzquierdo = tileXJugadorIzquierda * Tile.ancho;
                double distanciaX = (jugador.x - jugador.ancho / 2) - TileJugadorBordeIzquierdo;

                if (distanciaX > 0) {
                    double velocidadNecesaria = Utilidades.proximoACero(-distanciaX, jugador.getVelocidadX());
                    jugador.x += velocidadNecesaria;
                } else {
                    // Opcional, corregir posición
                    jugador.x = TileJugadorBordeIzquierdo + jugador.ancho / 2;
                }
            }
        }
        // Hacia arriba
        if (jugador.getVelocidadY() < 0) {
            // Tile superior PASABLE
            // Podemos seguir moviendo hacia arriba
            if (tileYJugadorSuperior - 1 >= 0 &&
                    mapaTiles[tileXJugadorCentro][tileYJugadorInferior-1].tipoDeColision ==
                    Tile.PASABLE) {
                jugador.y += jugador.getVelocidadY();

            } else {

                int TileJugadorBordeSuperior = (tileYJugadorSuperior) * Tile.altura;
                double distanciaY = (jugador.y - jugador.altura / 2) - TileJugadorBordeSuperior;

                if (distanciaY > 0 &&
                        mapaTiles[tileXJugadorCentro][tileYJugadorInferior-1].tipoDeColision ==
                        Tile.PASABLE) {
                    jugador.y += Utilidades.proximoACero(-distanciaY, jugador.getVelocidadY());

                }

            }

        }

        // Hacia abajo
        if (jugador.getVelocidadY() >= 0) {
            // Tile inferior PASABLE
            // Podemos seguir moviendo hacia abajo
            if (tileYJugadorInferior + 1 <= altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorCentro][tileYJugadorInferior + 1].tipoDeColision ==
                            Tile.PASABLE) {

                jugador.y += jugador.getVelocidadY();

            } else if (tileYJugadorInferior + 1 <= altoMapaTiles() - 1) {

                // Con que uno de los dos sea solido ya no puede caer
                // Si en el propio tile del jugador queda espacio para bajar más, bajo
                int TileJugadorBordeInferior =
                        tileYJugadorInferior * Tile.altura + Tile.altura;

                double distanciaY =
                        TileJugadorBordeInferior - (jugador.y + jugador.altura / 2);

                if (distanciaY > 0) {
                    jugador.y += Math.min(distanciaY, jugador.getVelocidadY());

                } else {
                    // Toca suelo, nos aseguramos de que está bien
                    jugador.y = TileJugadorBordeInferior - jugador.altura / 2;
                    jugador.setVelocidadY(0);
                }

            }
        }
      /*  for (Enemigo enemigo : enemigos) {
            if (disparoJugador.colisiona(enemigo)) {
                enemigo.destruir();
                iterator.remove();
                break;
            }
        }*/

    }

    private float tilesEnDistanciaX(double distanciaX) {
        return (float) distanciaX / Tile.ancho;
    }

    private float tilesEnDistanciaY(double distanciaY) {
        return (float) distanciaY / Tile.altura;
    }

}