package com.grupo.the_end_is_near;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.grupo.the_end_is_near.escenario.Combate;
import com.grupo.the_end_is_near.escenario.Nivel;
import com.grupo.the_end_is_near.gestores.GestorAudio;
import com.grupo.the_end_is_near.modelos.mapa.controles.BotonAccion;
import com.grupo.the_end_is_near.modelos.mapa.controles.Pad;
import com.grupo.the_end_is_near.global.Estado;
import com.grupo.the_end_is_near.global.Turno;
import com.grupo.the_end_is_near.modelos.combate.controles.Atacar;
import com.grupo.the_end_is_near.modelos.combate.controles.BarraVida;
import com.grupo.the_end_is_near.modelos.combate.controles.Defender;
import com.grupo.the_end_is_near.modelos.combate.controles.Enemigo;
import com.grupo.the_end_is_near.modelos.combate.controles.Huir;
import com.grupo.the_end_is_near.modelos.combate.controles.Magia;
import com.grupo.the_end_is_near.modelos.combate.controles.Marcador;
import com.grupo.the_end_is_near.modelos.combate.controles.Pocion;


public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    boolean iniciado = false;
    Context context;
    GameLoop gameloop;

    public static int pantallaAncho;
    public static int pantallaAlto;

    public static Nivel nivel;
    public static Combate combate;

    private Pad pad;
    private BotonAccion btAccion;
    private Huir huir;
    private Atacar atacar;
    private BarraVida barraVida;
    private Defender defender;
    private Magia magia;
    private Pocion pocion;
    private Enemigo enemigo1;
    private Enemigo enemigo2;
    private Enemigo enemigo3;
    private Marcador marcador1;
    private Marcador marcador2;
    private Marcador marcador3;

    public static GestorAudio gestorAudio;

    public boolean pausa = false;

    public boolean enAtaque;
    public boolean enMagia;
    public boolean dibujarMarcador;

    public static int enemigo;
    public static int pintarDaño = -1;
    public static int dañoActual = -1;


    public GameView(Context context) {
        super(context);
        iniciado = true;

        getHolder().addCallback(this);
        setFocusable(true);

        this.context = context;
        inicializarGestorAudio(context);
        gameloop = new GameLoop(this);
        gameloop.setRunning(true);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // valor a Binario
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        // Indice del puntero
        int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;

        int pointerId = event.getPointerId(pointerIndex);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                accion[pointerId] = ACTION_DOWN;
                x[pointerId] = event.getX(pointerIndex);
                y[pointerId] = event.getY(pointerIndex);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                accion[pointerId] = ACTION_UP;
                x[pointerId] = event.getX(pointerIndex);
                y[pointerId] = event.getY(pointerIndex);
                break;
            case MotionEvent.ACTION_MOVE:
                int pointerCount = event.getPointerCount();
                for (int i = 0; i < pointerCount; i++) {
                    pointerIndex = i;
                    pointerId = event.getPointerId(pointerIndex);
                    accion[pointerId] = ACTION_MOVE;
                    x[pointerId] = event.getX(pointerIndex);
                    y[pointerId] = event.getY(pointerIndex);
                }
                break;
        }

        procesarEventosTouch();
        return true;
    }

    int NO_ACTION = 0;
    int ACTION_MOVE = 1;
    int ACTION_UP = 2;
    int ACTION_DOWN = 3;
    int accion[] = new int[6];
    float x[] = new float[6];
    float y[] = new float[6];

    public void procesarEventosTouch() {
        if (!combate.enCombate) {
            boolean pulsacionPadMover = false;

            for (int i = 0; i < 6; i++) {
                if (accion[i] != NO_ACTION) {

                    if (accion[i] == ACTION_DOWN) {
                        if (nivel.nivelPausado)
                            nivel.nivelPausado = false;
                    }

                    if (pad.estaPulsado(x[i], y[i])) {

                        float orientacionX = pad.getOrientacionX(x[i]);
                        float orientacionY = pad.getOrientacionY(y[i]);
                        // Si almenosuna pulsacion está en el pad
                        if (accion[i] != ACTION_UP) {
                            if (orientacionX != 0) {
                                pulsacionPadMover = true;
                                nivel.orientacionPadX = orientacionX;
                                nivel.orientacionPadY = 0.0f;
                            } else if (orientacionY != 0) {
                                pulsacionPadMover = true;
                                nivel.orientacionPadY = orientacionY;
                                nivel.padAbajoPulsado = true;
                                nivel.orientacionPadX = 0.0f;
                            }
                        }
                    }
                    if (btAccion.estaPulsado(x[i], y[i])) {
                        if (accion[i] == ACTION_DOWN) {
                            nivel.btAccionPulsado = true;
                        }
                    }
                }
            }
            if (!pulsacionPadMover) {
                nivel.orientacionPadX = 0;
                nivel.orientacionPadY = 0;
            }
        } else {
            for (int i = 0; i < 6; i++) {
                if (accion[i] != NO_ACTION) {

                    if (accion[i] == ACTION_DOWN) {
                        if (nivel.nivelPausado)
                            nivel.nivelPausado = false;
                    }

                    if (atacar.estaPulsado(x[i], y[i])) {
                        enAtaque = true;
                        enMagia = false;
                        dibujarMarcador = true;
                    } else if (defender.estaPulsado(x[i], y[i])) {
                        enAtaque = false;
                        enMagia = false;
                        dibujarMarcador = false;
                        combate.defender();
                    } else if (magia.estaPulsado(x[i], y[i])) {
                        enAtaque = false;
                        enMagia = true;
                        dibujarMarcador = true;
                    } else if (pocion.estaPulsado(x[i], y[i])) {
                        combate.pocion();
                    } else if (huir.estaPulsado(x[i], y[i])) {
                        combate.huir();
                    } else if (enemigo1.estaPulsado(x[i], y[i])) {
                        if (combate.enemigos.size() > 0) {
                            enemigo = 0;
                            accionARealizar();
                            dibujarMarcador = false;
                        }
                    } else if (enemigo2.estaPulsado(x[i], y[i])) {
                        if (combate.enemigos.size() > 1) {
                            enemigo = 1;
                            accionARealizar();
                            dibujarMarcador = false;
                        }
                    } else if (enemigo3.estaPulsado(x[i], y[i])) {
                        if (combate.enemigos.size() > 2) {
                            enemigo = 2;
                            accionARealizar();
                            dibujarMarcador = false;
                        }
                    }
                }
            }
        }
    }

    protected void inicializar() throws Exception {
        pad = new Pad(context);
        btAccion = new BotonAccion(context);
        huir = new Huir(context);
        atacar = new Atacar(context);
        pocion = new Pocion(context);
        defender = new Defender(context);
        magia = new Magia(context);
        barraVida = new BarraVida(context);
        enemigo1 = new Enemigo(context, 0, 0);
        enemigo2 = new Enemigo(context, 1.5, 1.5);
        enemigo3 = new Enemigo(context, 1.5, -0.7);
        marcador1 = new Marcador(context, 0.75, 0);
        marcador2 = new Marcador(context, 3.5, 1.5);
        marcador3 = new Marcador(context, 3.5, -0.7);
        cargarNivel(Maps.DEFAULT_WORLD);
        combate = new Combate(context);
    }

    public void cargarNivel(Maps mapa) throws Exception {
        int numeroMapa = mapa.ordinal();
        nivel = new Nivel(context, numeroMapa);
        nivel.gameView = this;
    }

    public void inicializarGestorAudio(Context context) {
        gestorAudio = GestorAudio.getInstancia(context, R.raw.musica_fondo, R.raw.musica_combate);
        gestorAudio.reproducirMusicaAmbiente();
        gestorAudio.registrarSonido(GestorAudio.SONIDO_GOLPE_COMBATE,R.raw.golpe_combate);
        gestorAudio.registrarSonido(GestorAudio.SONIDO_MAGIA_MAGO_COMBATE,R.raw.magia_mago);
        gestorAudio.registrarSonido(GestorAudio.SONIDO_MAGIA_WARRIOR_COMBATE,R.raw.magia_warrior);
        gestorAudio.registrarSonido(GestorAudio.SONIDO_MAGIA_THIEF_COMBATE,R.raw.magia_thief);
        gestorAudio.registrarSonido(GestorAudio.SONIDO_POCION_COMBATE,R.raw.pocion_combate);
        gestorAudio.registrarSonido(GestorAudio.SONIDO_POCION_MAPA,R.raw.pocion_mapa);
        gestorAudio.registrarSonido(GestorAudio.SONIDO_RECOGER_POCION_MAPA,R.raw.sonido_recoger_pocion);
        gestorAudio.registrarSonido(GestorAudio.SONIDO_GANANCIA_COMBATE,R.raw.sonido_victoria);
        gestorAudio.registrarSonido(GestorAudio.SONIDO_PERDIDA_COMBATE,R.raw.sonido_perder);
        gestorAudio.registrarSonido(GestorAudio.SONIDO_LEVELDOWN_MAPA,R.raw.sonido_perder);
        gestorAudio.registrarSonido(GestorAudio.SONIDO_LEVELUP_MAPA,R.raw.pocion_mapa);
        gestorAudio.registrarSonido(GestorAudio.SONIDO_HABLAR_MAPA,R.raw.sonido_hablar);
        gestorAudio.registrarSonido(GestorAudio.SONIDO_INICIO_COMBATE,R.raw.sonido_inicio_combate);

    }

    public void actualizar(long tiempo) throws Exception {
        if (!nivel.nivelPausado) {
            nivel.actualizar(tiempo);
        }
        if(combate.enCombate){
            combate.actualizar(tiempo);
        }
    }

    protected void dibujar(Canvas canvas) {
        if (!nivel.nivelPausado && !combate.enCombate) {
            nivel.dibujar(canvas);
            pad.dibujar(canvas);
            btAccion.dibujar(canvas);
            barraVida.dibujar(canvas);
            Paint textoPersonajes = new Paint();
            textoPersonajes.setAntiAlias(true);
            textoPersonajes.setColor(Color.WHITE);
            canvas.drawText("Thief : ", (float) (pantallaAncho * 0.55), (float) (pantallaAlto * 0.85), textoPersonajes);
            canvas.drawText(combate.heroes.get(0).vida + " / " + combate.heroes.get(0).vidaMaxima, (float) (pantallaAncho * 0.65), (float) (pantallaAlto * 0.85), textoPersonajes);
            canvas.drawText(combate.heroes.get(0).mana + " / " + combate.heroes.get(0).manaMaximo, (float) (pantallaAncho * 0.80), (float) (pantallaAlto * 0.85), textoPersonajes);
            canvas.drawText("Warrior : ", (float) (pantallaAncho * 0.55), (float) (pantallaAlto * 0.89), textoPersonajes);
            canvas.drawText(combate.heroes.get(1).vida + " / " + combate.heroes.get(1).vidaMaxima, (float) (pantallaAncho * 0.65), (float) (pantallaAlto * 0.89), textoPersonajes);
            canvas.drawText(combate.heroes.get(1).mana + " / " + combate.heroes.get(1).manaMaximo, (float) (pantallaAncho * 0.80), (float) (pantallaAlto * 0.89), textoPersonajes);
            canvas.drawText("Mage : ", (float) (pantallaAncho * 0.55), (float) (pantallaAlto * 0.93), textoPersonajes);
            canvas.drawText(combate.heroes.get(2).vida + " / " + combate.heroes.get(2).vidaMaxima, (float) (pantallaAncho * 0.65), (float) (pantallaAlto * 0.93), textoPersonajes);
            canvas.drawText(combate.heroes.get(2).mana + " / " + combate.heroes.get(2).manaMaximo, (float) (pantallaAncho * 0.80), (float) (pantallaAlto * 0.93), textoPersonajes);
        }
        if (combate.enCombate) {
            //gestorAudio.reproducirMusicaCombate(); TODO No reproduce si tenemos esto asi
            combate.dibujar(canvas);
            atacar.dibujar(canvas);
            magia.dibujar(canvas);
            defender.dibujar(canvas);
            pocion.dibujar(canvas);
            huir.dibujar(canvas);
            barraVida.dibujar(canvas);
            Paint textoPersonajes = new Paint();
            textoPersonajes.setAntiAlias(true);
            textoPersonajes.setColor(Color.WHITE);
            canvas.drawText("Thief : ", (float) (pantallaAncho * 0.55), (float) (pantallaAlto * 0.85), textoPersonajes);
            canvas.drawText(combate.heroes.get(0).vida + " / " + combate.heroes.get(0).vidaMaxima, (float) (pantallaAncho * 0.65), (float) (pantallaAlto * 0.85), textoPersonajes);
            canvas.drawText(combate.heroes.get(0).mana + " / " + combate.heroes.get(0).manaMaximo, (float) (pantallaAncho * 0.80), (float) (pantallaAlto * 0.85), textoPersonajes);
            canvas.drawText("Warrior : ", (float) (pantallaAncho * 0.55), (float) (pantallaAlto * 0.89), textoPersonajes);
            canvas.drawText(combate.heroes.get(1).vida + " / " + combate.heroes.get(1).vidaMaxima, (float) (pantallaAncho * 0.65), (float) (pantallaAlto * 0.89), textoPersonajes);
            canvas.drawText(combate.heroes.get(1).mana + " / " + combate.heroes.get(1).manaMaximo, (float) (pantallaAncho * 0.80), (float) (pantallaAlto * 0.89), textoPersonajes);
            canvas.drawText("Mage : ", (float) (pantallaAncho * 0.55), (float) (pantallaAlto * 0.93), textoPersonajes);
            canvas.drawText(combate.heroes.get(2).vida + " / " + combate.heroes.get(2).vidaMaxima, (float) (pantallaAncho * 0.65), (float) (pantallaAlto * 0.93), textoPersonajes);
            canvas.drawText(combate.heroes.get(2).mana + " / " + combate.heroes.get(2).manaMaximo, (float) (pantallaAncho * 0.80), (float) (pantallaAlto * 0.93), textoPersonajes);
            if (dibujarMarcador) {
                com.grupo.the_end_is_near.modelos.combate.enemigos.Enemigo n1 = null;
                com.grupo.the_end_is_near.modelos.combate.enemigos.Enemigo n2 = null;
                com.grupo.the_end_is_near.modelos.combate.enemigos.Enemigo n3 = null;
                if (combate.enemigos.size() == 3) {
                    n1 = combate.enemigos.get(0);
                    n2 = combate.enemigos.get(1);
                    n3 = combate.enemigos.get(2);
                }
                if (combate.enemigos.size() == 2) {
                    n1 = combate.enemigos.get(0);
                    n2 = combate.enemigos.get(1);
                }
                if (combate.enemigos.size() == 1) {
                    n1 = combate.enemigos.get(0);
                }
                if (n1 != null && n1.estado == Estado.ACTIVO)
                    marcador1.dibujar(canvas);
                if (n2 != null && n2.estado == Estado.ACTIVO)
                    marcador2.dibujar(canvas);
                if (n3 != null && n3.estado == Estado.ACTIVO)
                    marcador3.dibujar(canvas);
            }
            Paint textoDaño = new Paint();
            textoDaño.setAntiAlias(true);
            textoDaño.setColor(Color.RED);
            if (combate.turno == Turno.ENEMIGO) {
                if (pintarDaño == 0) {
                    canvas.drawText("" + dañoActual, (float) (pantallaAncho / 1.45), (float) (pantallaAlto / 3.5), textoDaño);
                } else if (pintarDaño == 1) {
                    canvas.drawText("" + dañoActual, (float) (pantallaAncho / 1.40), (float) (pantallaAlto / 2.5), textoDaño);
                } else if (pintarDaño == 2) {
                    canvas.drawText("" + dañoActual, (float) (pantallaAncho / 1.35), (float) (pantallaAlto / 1.9), textoDaño);
                }
            }
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        pantallaAncho = width;
        pantallaAlto = height;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if (iniciado) {
            iniciado = false;
            if (gameloop.isAlive()) {
                iniciado = true;
                gameloop = new GameLoop(this);
            }

            gameloop.setRunning(true);
            gameloop.start();
        } else {
            iniciado = true;
            gameloop = new GameLoop(this);
            gameloop.setRunning(true);
            gameloop.start();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        iniciado = false;

        boolean intentarDeNuevo = true;
        gameloop.setRunning(false);
        while (intentarDeNuevo) {
            try {
                gameloop.join();
                intentarDeNuevo = false;
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.v("Tecla", "Tecla pulsada: " + keyCode);

        if (keyCode == 32) {
            nivel.orientacionPadX = -0.5f;
            nivel.orientacionPadY = 0.0f;
        }
        if (keyCode == 29) {
            nivel.orientacionPadX = 0.5f;
            nivel.orientacionPadY = 0.0f;
        }
        if (keyCode == 47) {
            nivel.orientacionPadY = -0.5f;
            nivel.padAbajoPulsado = true;
            nivel.orientacionPadX = 0.0f;
        }
        if (keyCode == 51) {
            nivel.orientacionPadY = 0.5f;
            nivel.padArribaPulsado = true;
            nivel.orientacionPadX = 0.0f;
        }
        if (keyCode == 62) {
            //nivel.accionJugador();
        }
        return super.onKeyDown(keyCode, event);
    }

    public static Nivel getNivel() {
        return nivel;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == 32 || keyCode == 29 || keyCode == 47 || keyCode == 51) {
            nivel.orientacionPadX = 0;
            nivel.orientacionPadY = 0;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Metodo que realizar la accion correspondiente al enemigo seleccionado segun que comando
     * a seleccionado.
     */
    private void accionARealizar() {
        if (enAtaque) {
            combate.atacar();
            enAtaque = false;
        } else if (enMagia) {
            combate.magia();
            enMagia = false;
        }
    }


}

