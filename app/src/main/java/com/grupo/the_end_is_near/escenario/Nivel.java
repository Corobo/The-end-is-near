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
import com.grupo.the_end_is_near.factorias.EnemiesFactory;
import com.grupo.the_end_is_near.factorias.ItemsFactory;
import com.grupo.the_end_is_near.factorias.PaisanosFactory;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.gestores.GestorAudio;
import com.grupo.the_end_is_near.global.Estado;
import com.grupo.the_end_is_near.modelos.mapa.Conversation;
import com.grupo.the_end_is_near.modelos.mapa.ciudadanos.Ciudadano;
import com.grupo.the_end_is_near.modelos.mapa.enemigos.Boss1;
import com.grupo.the_end_is_near.modelos.mapa.enemigos.Enemigo;
import com.grupo.the_end_is_near.modelos.mapa.escenarios.Fondo;
import com.grupo.the_end_is_near.modelos.mapa.items.Item;
import com.grupo.the_end_is_near.modelos.mapa.jugador.Jugador;

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
    private List<Item> items;
    private Item portal;
    private List<Ciudadano> buenasGentes;
    private Conversation conver;

    public boolean inicializado;
    public int ganoJefe = -1;
    private Tile[][] mapaTiles;

    int cindex = 0;
    int xKey=0;
    int yKey =0;
    private int key=0;
    private boolean hasKey;

    private int[] convinaciones;

    public float orientacionPadX = 0;
    public float orientacionPadY = 0;

    public static int scrollEjeX = 0;
    public static int scrollEjeY = 0;

    public boolean padArribaPulsado = false;
    public boolean padAbajoPulsado = false;
    public boolean btAccionPulsado = false;

    public GameView gameView;

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

        convinaciones = new int[]{-1,-1,-1};
        conver = null;
        hasKey = false;
        enemigos = new LinkedList<Enemigo>();
        items = new LinkedList<Item>();
        portal = null;

        buenasGentes = new LinkedList<Ciudadano>();
        nivelPausado = false;
        inicializarMapaTiles();
        //TODO ajustar scroll
        scrollEjeY = (int) (altoMapaTiles() - tilesEnDistanciaY(GameView.pantallaAlto)) * Tile.altura;
    }


    public void actualizar(long tiempo) throws Exception {
        if (inicializado && !nivelPausado && !GameView.combate.enCombate && ganoJefe==-1) {

            for (Enemigo enemigo : enemigos) {
                enemigo.actualizar(tiempo);
            }

            for (Item i : items)
                i.actualizar(tiempo);

            if(portal != null)
                portal.actualizar(tiempo);

            for (Ciudadano p : buenasGentes)
                p.actualizar(tiempo);

            jugador.procesarOrdenes(orientacionPadX, orientacionPadY);
            jugador.actualizar(tiempo);

            if(ganoJefe==0){
              GameView.gestorAudio.pararMusicaAmbiente();

            }
            if(ganoJefe==1){

            }

            aplicarReglasMovimiento();

            if (btAccionPulsado)
                btAccionPulsado = false;
        }
    }


    public void dibujar(Canvas canvas) {
        if (inicializado && !nivelPausado && !GameView.combate.enCombate && ganoJefe==-1) {
            fondo.dibujar(canvas);
            dibujarTiles(canvas);

            for (Item i : items)
                i.dibujar(canvas);

            for (Ciudadano p : buenasGentes)
                p.dibujar(canvas);

            jugador.dibujar(canvas);

            for (Enemigo enemigo : enemigos) {
                enemigo.dibujar(canvas);
            }

            if (conver != null)
                conver.dibujar(canvas);

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
                int xCentroAbajoTile = x * Tile.ancho + Tile.ancho;
                int yCentroAbajoTile = y * Tile.altura + Tile.altura;
                jugador = new Jugador(context, xCentroAbajoTile, yCentroAbajoTile, 3);

                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_verde_1), Tile.PASABLE);
            case '2':
                // Ciudadano Genaro con suelo verde
                int xCentroAbajoTileG = x * Tile.ancho + Tile.ancho;
                int yCentroAbajoTileG = y * Tile.altura + Tile.altura;

                buenasGentes.add(PaisanosFactory.getGenaro(context, xCentroAbajoTileG, yCentroAbajoTileG));
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_verde_1), Tile.PASABLE);
            case '0':
                // Ciudadano Genaro con suelo de piedra
                int xCentroAbajoTileG2 = x * Tile.ancho + Tile.ancho;
                int yCentroAbajoTileG2 = y * Tile.altura + Tile.altura;

                buenasGentes.add(PaisanosFactory.getGenaro(context, xCentroAbajoTileG2, yCentroAbajoTileG2));
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_piedra), Tile.PASABLE);
            case '5':
                // Ciudadano Manolo
                // Ciudadano Genaro
                int xCentroAbajoTileM = x * Tile.ancho + Tile.ancho;
                int yCentroAbajoTileM = y * Tile.altura + Tile.altura;

                buenasGentes.add(PaisanosFactory.getManolo(context, xCentroAbajoTileM, yCentroAbajoTileM));
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_verde_1), Tile.PASABLE);

            case '7':
                // Ciudadano Manolo
                int xCentroAbajoTileMa = x * Tile.ancho + Tile.ancho;
                int yCentroAbajoTileMa = y * Tile.altura + Tile.altura;

                buenasGentes.add(PaisanosFactory.getMariPepa(context, xCentroAbajoTileMa, yCentroAbajoTileMa));
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_piedra), Tile.PASABLE);
            case '9':
                // Ciudadano Rigoverto
                int xCentroAbajoTileRi = x * Tile.ancho + Tile.ancho;
                int yCentroAbajoTileRi = y * Tile.altura + Tile.altura;

                buenasGentes.add(PaisanosFactory.getRigoverto(context, xCentroAbajoTileRi, yCentroAbajoTileRi));
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_verde_1), Tile.PASABLE);
            case '¡':
                // Ciudadano Genérico piedra
                int xCentroAbajoTileGe = x * Tile.ancho + Tile.ancho;
                int yCentroAbajoTileGe = y * Tile.altura + Tile.altura;

                buenasGentes.add(PaisanosFactory.getGenericCiu(context, xCentroAbajoTileGe, yCentroAbajoTileGe));
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_piedra), Tile.PASABLE);
            case '&':
                // Ciudadano Genérico piedra
                int xCentroAbajoTileGe2 = x * Tile.ancho + Tile.ancho;
                int yCentroAbajoTileGe2 = y * Tile.altura + Tile.altura;

                buenasGentes.add(PaisanosFactory.getGenericCiu(context, xCentroAbajoTileGe2, yCentroAbajoTileGe2));
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_marron), Tile.PASABLE);
            case 'a':
                // antorcha
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.antorcha_marron), Tile.SOLIDO);
            case '3':
                //Enemigo
                int xCentroAbajoTileE1 = x * Tile.ancho + Tile.ancho;
                int yCentroAbajoTileE1 = y * Tile.altura + Tile.altura;
                enemigos.add(EnemiesFactory.getEnemigo1(context, xCentroAbajoTileE1,
                        yCentroAbajoTileE1));
                return new Tile(CargadorGraficos.cargarDrawable(context, R.drawable.suelo_marron),
                        Tile.PASABLE);
            case '4':
                //Enemigo Intelligente suelo verde
                int xCentroAbajoTileEI = x * Tile.ancho + Tile.ancho;
                int yCentroAbajoTileEI = y * Tile.altura + Tile.altura;
                enemigos.add(EnemiesFactory.getEnemigoInteligente(context, xCentroAbajoTileEI,
                        yCentroAbajoTileEI));
                return new Tile(CargadorGraficos.cargarDrawable(context, R.drawable.suelo_verde_1),
                        Tile.PASABLE);
            case '8':
                //Enemigo Inteligente suelo marron
                int xCentroAbajoTileEI2 = x * Tile.ancho + Tile.ancho;
                int yCentroAbajoTileEI2 = y * Tile.altura + Tile.altura;
                enemigos.add(EnemiesFactory.getEnemigoInteligente(context, xCentroAbajoTileEI2,
                        yCentroAbajoTileEI2));
                return new Tile(CargadorGraficos.cargarDrawable(context, R.drawable.suelo_marron),
                        Tile.PASABLE);
            case '6':
                //Enemigo
                int xCentroAbajoTileB = x * Tile.ancho + Tile.ancho;
                int yCentroAbajoTileB = y * Tile.altura + Tile.altura;
                enemigos.add(EnemiesFactory.getBoss1(context, xCentroAbajoTileB,
                        yCentroAbajoTileB));
                return new Tile(CargadorGraficos.cargarDrawable(context, R.drawable.suelo_marron),
                        Tile.PASABLE);
            //Dibujar la puerta
            case 'I':
                // parte arriba izquierda puerta
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.puerta_arriba_izquierda_abierta), Tile.SOLIDO);
            case 'i':
                // parte abajo izquierda puerta
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.puerta_abajo_izquierda_abierta), Tile.SOLIDO);
            case 'O':
                // parte arriba derecha puerta
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.puerta_arriba_derecha_abierta), Tile.SOLIDO);
            case 'o':
                // parte abajo derecha puerta
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.puerta_abajo_derecha_abierta), Tile.SOLIDO);
            case 'K':
                // Parte central de la puerta
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.puerta_parte_central), Tile.PASABLE);
            case 'k':
                // fondo de la puerta
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.puerta_fondo), Tile.PASABLE);
            //Fin dibujoar puerta
            case 'p':
                // pocion suelo marron
                int xCentroAbajoTileP = x * Tile.ancho + Tile.ancho;
                int yCentroAbajoTileP = y * Tile.altura + Tile.altura;
                items.add(ItemsFactory.getPocion(context, xCentroAbajoTileP, yCentroAbajoTileP));

                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_marron), Tile.PASABLE);
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
            case 'B':
                // parte Arriba izquierda agua
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_azul_esquina_izquierda_arriba), Tile.SOLIDO);
            case 'b':
                // parte abajo izquierda agua
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_azul_esquina_izquierda_abajo), Tile.SOLIDO);
            case 'n':
                // parte abajo derecha agua
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_azul_esquina_derecha_abajo), Tile.SOLIDO);
            case 'N':
                // parte Arriba derecha agua
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_azul_esquina_derecha_arriba), Tile.SOLIDO);
            case 'M':
                // linea Arriba  agua
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_azul_linea_arriba), Tile.SOLIDO);
            case 'm':
                // linea abajo  agua
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_azul_linea_abajo), Tile.SOLIDO);
            case 'V':
                // linea derecha  agua
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_azul_linea_derecha), Tile.SOLIDO);
            case 'v':
                // linea izquierda  agua
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_azul_linea_izquierda), Tile.SOLIDO);
            case 'c':
                // suelo azul
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_azul), Tile.SOLIDO);
            case 'G':
                // Cascada izquierda
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.cascada_izquierda), Tile.SOLIDO);
            case 'g':
                // Cascada derecha
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.cascada_derecha), Tile.SOLIDO);
            case 'H':
                // Cascada
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.cascada_1), Tile.SOLIDO);
            case 'h':
                // Cascada rota centro
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.cascada_rota_centro), Tile.SOLIDO);
            case 'J':
                // Cascada rota derecha
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.cascada_rota_derecha), Tile.SOLIDO);
            case 'j':
                // Cascada rota izquierda
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.cascada_rota_izquierda), Tile.SOLIDO);
            case 'l':
                // puente izquierda
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.puente_izquierda), Tile.PASABLE);
            case 'L':
                // puente derecha
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.puente_derecha), Tile.PASABLE);
            case 'ñ':
                // puente centro
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.puente_centro), Tile.PASABLE);
            case ';':
                // suelo de piedra
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_piedra), Tile.PASABLE);
            case 'd':
                // Tejado casa Arriba parte atras izquierda
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.tejado_atras_izquierda), Tile.SOLIDO);
            case 'f':
                // Tejado casa Arriba parte atras derecha
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.tejado_atras_derecha), Tile.SOLIDO);
            case 'Ñ':
                // Tejado medio izquierda
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.tejado_medio_izquierda), Tile.SOLIDO);
            case 'P':
                // Tejado medio derecha
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.tejado_medio_derecha), Tile.SOLIDO);
            case 'Q':
                // Tejado alante izquierda
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.tejado_alante_izquierda), Tile.SOLIDO);
            case 's':
                // Tejado alante derecha
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.tejado_alante_derecha), Tile.SOLIDO);
            case 'z':
                // Puerta casa izquierda
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.puerta_casa_izquierda), Tile.PASABLE);
            case '!':
                // Puerta casa derecha
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.puerta_casa_derecha), Tile.PASABLE);
            case '%':
                // Runa 1
                int xCentroAbajoTileR1 = x * Tile.ancho + Tile.ancho;
                int yCentroAbajoTileR1 = y * Tile.altura + Tile.altura;
                items.add(ItemsFactory.getRuna1(context, xCentroAbajoTileR1, yCentroAbajoTileR1));
                return new Tile(null, Tile.PASABLE);
            case '/':
                // Runa 2
                int xCentroAbajoTileR2 = x * Tile.ancho + Tile.ancho;
                int yCentroAbajoTileR2 = y * Tile.altura + Tile.altura;
                items.add(ItemsFactory.getRuna2(context, xCentroAbajoTileR2, yCentroAbajoTileR2));
                return new Tile(null, Tile.PASABLE);
            case '(':
                // Runa 3
                int xCentroAbajoTileR3 = x * Tile.ancho + Tile.ancho;
                int yCentroAbajoTileR3 = y * Tile.altura + Tile.altura;
                items.add(ItemsFactory.getRuna3(context, xCentroAbajoTileR3, yCentroAbajoTileR3));
                return new Tile(null, Tile.PASABLE);
            case ')':
                // Verja horizontal
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.verja_horizontal), Tile.SOLIDO);
            case '=':
                // Verja hacia abajo
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.verja_hacia_arriba), Tile.SOLIDO);
            case '?':
                // Verja hacia arriba
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.verja_hacia_abajo), Tile.SOLIDO);
            case 'x':
                xKey =x * Tile.ancho + Tile.ancho;
                yKey = y * Tile.altura + Tile.altura;
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.suelo_piedra), Tile.PASABLE);
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

        //enemigos
        for (Iterator<Enemigo> iterator = enemigos.iterator(); iterator.hasNext(); ) {
            Enemigo enemigo = iterator.next();

            if (enemigo.estado == Estado.ELIMINAR) {
                iterator.remove();
                continue;
            }

            if (enemigo.estado != Estado.ACTIVO)
                continue;

            int tileXEnemigoIzquierda =
                    (int) (enemigo.x - (enemigo.ancho / 2 - 1)) / Tile.ancho;

            enemigo.mover(this);
            //comprobamos las colisiones de los enemigos
            int rango = 4;
            if (tileXJugadorIzquierda - rango < tileXEnemigoIzquierda &&
                    tileXJugadorIzquierda + rango > tileXEnemigoIzquierda) {

                if (jugador.colisiona(enemigo)) {
                    //TODO Lanzar Pelea
                    //eliminamos al enemigo del mapa
                    GameView.gestorAudio.pararMusicaAmbiente();
                    GameView.combate.jX=(int) jugador.x;
                    GameView.combate.jY=(int) jugador.y;
                    if(enemigo instanceof Boss1)
                        GameView.combate.iniciaCombate(true);
                    else
                        GameView.combate.iniciaCombate(false);
                    enemigo.destruir();
                    iterator.remove();
                }
            }
        }

        //movemos el jugador sin no está en medio de una apasionante conversación con un pueblerino
        if (conver == null)
            jugador.mover(this);

        for (Ciudadano ciu : buenasGentes)
            ciu.mover(this);

        if(key ==1){
            items.add(ItemsFactory.getKey(context,xKey,yKey));
            key++;
        }
        //elimina los items recolectados y captura la colisión
        for (Iterator<Item> iterator = items.iterator(); iterator.hasNext(); ) {
            Item r = iterator.next();

            if (r.estado == Estado.ELIMINAR)
                iterator.remove();
            if (jugador.colisiona(r))
                r.doSomething(this);
        }

        for (Ciudadano p : buenasGentes) {
            if (jugador.colisiona(p)) {
                p.hablar(this);

            }
        }

        if(portal!= null && jugador.colisiona(portal))
            portal.doSomething(this);
    }

    //usa el módulo para insertar siepre en la siguiente posición a la última que insertó o en la
    // primera si la última inserción fué en la ultima posicion
    public void addConvinacion(int convinacion) {
        int pos = cindex % (convinaciones.length);
        if(pos ==0 || convinaciones[pos-1] != convinacion){
            convinaciones[cindex % convinaciones.length] = convinacion;
            cindex++;

            //comprobar si se cumple la secuencia deseada en convinaciones
            for (int i = 0; i < convinaciones.length; i++) {
                if (convinaciones[i] == 1){
                    if(convinaciones[(i + 1) % convinaciones.length]==2 &&
                            convinaciones[(i + 2) % convinaciones.length]==3 ){
                        //si la variable key es 1 se añade una llave desde update y de actualiza el valor.
                        // Esto asegura que solo se podrá generar una llave en el juego
                        key ++;
                        GameView.gestorAudio.reproducirSonido(GestorAudio.SONIDO_APARECE_LLAVE);
                    }

                }
            }
        }
    }

    public void pararJugador(){
        this.jugador.setVelocidadY(0);
        this.jugador.setVelocidadX(0);
    }

    public void setJugador(Jugador j){
        this.jugador=j;
    }

    public void addPortal(double x , double y){
        portal = ItemsFactory.getPortal1(context,x,y);
    }

    public Tile[][] getMapaTiles() {
        return mapaTiles;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public List<Item> getItems() {
        return items;
    }

    public List<Ciudadano> getBuenasGentes() {
        return buenasGentes;
    }

    public Conversation getConver() {
        return conver;
    }

    public void setConver(Conversation conver) {
        this.conver = conver;
    }

    public boolean isKey() {
        return hasKey;
    }

    public void setKey(boolean key) {
        this.hasKey = key;
    }

    private float tilesEnDistanciaX(double distanciaX) {
        return (float) distanciaX / Tile.ancho;
    }

    private float tilesEnDistanciaY(double distanciaY) {
        return (float) distanciaY / Tile.altura;
    }

}