package com.grupo.the_end_is_near;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.grupo.the_end_is_near.escenario.Nivel;
import com.grupo.the_end_is_near.gestores.GestorAudio;
import com.grupo.the_end_is_near.gestores.Opciones;
import com.grupo.the_end_is_near.modelos.controles.BotonAccion;
import com.grupo.the_end_is_near.modelos.controles.Pad;


public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    boolean iniciado = false;
    Context context;
    GameLoop gameloop;

    public static int pantallaAncho;
    public static int pantallaAlto;

    protected static Nivel nivel;

    private Pad pad;
    private BotonAccion btAccion;

    public GestorAudio gestorAudio;

    public boolean pausa = false;


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
                if(btAccion.estaPulsado(x[i],y[i])){
                    //nivel.accionJugador();
                }
            }
        }
        if (!pulsacionPadMover) {
            nivel.orientacionPadX = 0;
            nivel.orientacionPadY = 0;
        }
    }

    protected void inicializar() throws Exception {
        pad = new Pad(context);
        btAccion = new BotonAccion(context);
        cargarNivel(Maps.DEFAULT_WORLD);
        if (!Opciones.musica)
            gestorAudio.pararMusicaAmbiente();
    }
    
    public void cargarNivel(Maps mapa) throws Exception {
        int numeroMapa = mapa.ordinal();
        nivel = new Nivel(context, numeroMapa);
        nivel.gameView = this;
    }

    public void inicializarGestorAudio(Context context) {
        gestorAudio = GestorAudio.getInstancia(context, R.raw.musica_fondo);
        gestorAudio.reproducirMusicaAmbiente();
        gestorAudio.registrarSonido(GestorAudio.SONIDO_DISPARO_JUGADOR,
                R.raw.lanzar_objeto);
        gestorAudio.registrarSonido(GestorAudio.SONIDO_SALTO_JUGADOR, R.raw.salto_jugador);
        gestorAudio.registrarSonido(GestorAudio.SONIDO_ENEMIGO_GOLPEADO, R.raw.disparo_golpea);
        gestorAudio.registrarSonido(GestorAudio.SONIDO_JUGADOR_GOLPEADO, R.raw.jugador_golpeado);
    }

    public void actualizar(long tiempo) throws Exception {
        if (!nivel.nivelPausado) {
            nivel.actualizar(tiempo);
        }
    }

    protected void dibujar(Canvas canvas) {
        nivel.dibujar(canvas);
        if (!nivel.nivelPausado) {
            pad.dibujar(canvas);
            btAccion.dibujar(canvas);
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
        if(keyCode == 62){
            //nivel.accionJugador();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == 32 || keyCode == 29 || keyCode == 47 || keyCode == 51) {
            nivel.orientacionPadX = 0;
            nivel.orientacionPadY=0;
        }
        return super.onKeyDown(keyCode, event);
    }


}

