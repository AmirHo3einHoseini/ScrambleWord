package com.example.scrambleword.core.audio

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log

class PronunciationPlayer {


    private var mediaPlayer: MediaPlayer? = null

    fun play(url: String) {
        if (url.isBlank()) return
        stop()

        try {
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .build()
                )
                setDataSource(url)
                setOnPreparedListener { start() }
                setOnCompletionListener { stop() }
                setOnErrorListener { _, what, extra ->
                    Log.e("pronunciation","error what=$what extra=$extra")
                    stop()
                    true
                }
                prepareAsync()
            }

        }catch (e: Exception) {
            Log.e("PronunciationPlayer", "play failed", e)
            stop()
        }
    }

    fun stop() {
        try {
            mediaPlayer?.run {
                if (isPlaying) stop()
                reset()
                release()
            }
        } catch (_: Exception) {
        } finally {
            mediaPlayer = null
        }
    }

}