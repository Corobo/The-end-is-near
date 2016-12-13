package com.grupo.the_end_is_near.gestores;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import java.util.HashMap;

public class GestorAudio implements MediaPlayer.OnPreparedListener {
    public static final int SONIDO_INICIO_COMBATE = 0;
    public static final int SONIDO_GANANCIA_COMBATE = 1;
    public static final int SONIDO_PERDIDA_COMBATE = 2;
    public static final int SONIDO_POCION_COMBATE = 3;
    public static final int SONIDO_MAGIA_MAGO_COMBATE = 4;
    public static final int SONIDO_MAGIA_THIEF_COMBATE = 5;
    public static final int SONIDO_MAGIA_WARRIOR_COMBATE = 6;
    public static final int SONIDO_GOLPE_COMBATE = 7;
    public static final int SONIDO_HABLAR_MAPA = 10;
    public static final int SONIDO_POCION_MAPA = 11;
    public static final int SONIDO_RECOGER_POCION_MAPA = 12;
    public static final int SONIDO_LEVELUP_MAPA = 13;
    public static final int SONIDO_LEVELDOWN_MAPA = 14;
    public static final int SONIDO_APARECE_LLAVE = 15;
    public static final int SONIDO_HUIR_COMBATE = 16;
    public static final int SONIDO_PERSONAJE_GOLPEADO = 17;


    // Pool de sonidos, para efectos de sonido.
    // Suele fallar el utilizar ficheros de sonido demasiado grandes
    private SoundPool poolSonidos;
    private HashMap<Integer, Integer> mapSonidos;
    private Context contexto;
    // Media Player para bucle de sonido de fondo1.
    private MediaPlayer sonidoAmbiente;
    private AudioManager gestorAudio;

    private static GestorAudio instancia = null;

    public static GestorAudio getInstancia(Context contexto,
                                           int idMusicaAmbiente) {
        synchronized (GestorAudio.class) {
            if (instancia == null) {
                instancia = new GestorAudio();
                instancia.initSounds(contexto, idMusicaAmbiente);
            }
            return instancia;
        }
    }


    public static GestorAudio getInstancia() {
        return instancia;
    }

    private GestorAudio() {
    }

    public void initSounds(Context contexto, int idMusicaAmbiente) {
        this.contexto = contexto;
        poolSonidos = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        mapSonidos = new HashMap<Integer, Integer>();
        gestorAudio = (AudioManager) contexto
                .getSystemService(Context.AUDIO_SERVICE);
        sonidoAmbiente = MediaPlayer.create(contexto, idMusicaAmbiente);
        sonidoAmbiente.setLooping(true);
        sonidoAmbiente.setVolume(1, 1);

    }

    public void reproducirMusicaAmbiente() {
        try {
            if (!sonidoAmbiente.isPlaying()) {
                try {
                    sonidoAmbiente.setOnPreparedListener(this);
                    sonidoAmbiente.prepareAsync();
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }
    }

    public void pararMusicaAmbiente() {
        if (sonidoAmbiente.isPlaying()) {
            sonidoAmbiente.stop();
        }
    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    public void registrarSonido(int index, int SoundID) {
        mapSonidos.put(index, poolSonidos.load(contexto, SoundID, 1));
    }

    public void reproducirSonido(int index) {
        float volumen =
                gestorAudio.getStreamVolume(AudioManager.STREAM_MUSIC);

        volumen =
                volumen / gestorAudio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        poolSonidos.play(
                (Integer) mapSonidos.get(index),
                volumen, volumen, 1, 0, 1f);
    }

}
