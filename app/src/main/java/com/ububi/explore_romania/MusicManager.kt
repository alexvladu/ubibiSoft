package com.ububi.explore_romania

import android.app.Application
import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class MusicTrack(val fileName: String) {
    HOME("chef_de_chef-versiunea_medievală.mp3"),
    BOARD("dragosteadintei.mp3"),
    QUESTION("questionmusic.mp3"),
    // Am schimbat numele în COLLECTION pentru a se potrivi cu ecranul de Colecție
    COLLECTION("onemorenight.mp3")
}

enum class SoundEffect(val fileName: String) {
    BUTTON("sounds/button.mp3"),
    CORRECT("sounds/good.mp3"),
    WRONG("sounds/wrong.mp3")
}

object MusicManager {
    private var musicPlayer: MediaPlayer? = null
    private var soundEffectPlayer: MediaPlayer? = null
    private var currentTrack: MusicTrack? = null
    private var application: Application? = null
    private val scope = CoroutineScope(Dispatchers.Main + Job())

    // Chei DataStore pentru salvarea poziției melodiilor
    private val KEY_HOME_POSITION = intPreferencesKey("music_home_position")
    private val KEY_BOARD_POSITION = intPreferencesKey("music_board_position")
    private val KEY_QUESTION_POSITION = intPreferencesKey("music_question_position")
    private val KEY_COLLECTION_POSITION = intPreferencesKey("music_collection_position")

    fun initialize(app: Application) {
        application = app
    }

    // AICI am corectat eroarea ta (unde scria doar "Key")
    private fun getPositionKey(track: MusicTrack) = when (track) {
        MusicTrack.HOME -> KEY_HOME_POSITION
        MusicTrack.BOARD -> KEY_BOARD_POSITION
        MusicTrack.QUESTION -> KEY_QUESTION_POSITION
        MusicTrack.COLLECTION -> KEY_COLLECTION_POSITION
    }

    private suspend fun savePosition(track: MusicTrack, position: Int) {
        application?.let { app ->
            app.dataStore.edit { prefs ->
                prefs[getPositionKey(track)] = position
            }
        }
    }

    private suspend fun getSavedPosition(track: MusicTrack): Int {
        return application?.let { app ->
            app.dataStore.data.first()[getPositionKey(track)] ?: 0
        } ?: 0
    }

    fun playTrack(track: MusicTrack) {
        scope.launch {
            // Salvăm poziția actuală înainte de a schimba melodia
            musicPlayer?.let { player ->
                if (player.isPlaying && currentTrack != null && currentTrack != MusicTrack.QUESTION) {
                    val position = player.currentPosition
                    withContext(Dispatchers.IO) {
                        savePosition(currentTrack!!, position)
                    }
                }
            }

            stopMusic()

            application?.let { app ->
                try {
                    val afd = app.assets.openFd("music/${track.fileName}")
                    musicPlayer = MediaPlayer().apply {
                        setAudioAttributes(
                            AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .build()
                        )

                        setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                        afd.close()

                        // Volum 0.4f pentru a nu fi prea deranjantă în meniu
                        setVolume(0.4f, 0.4f)

                        prepare()
                        isLooping = true

                        // Melodia de întrebări pornește mereu de la 0, restul restaurează poziția
                        val savedPosition = if (track == MusicTrack.QUESTION) {
                            0
                        } else {
                            withContext(Dispatchers.IO) {
                                getSavedPosition(track)
                            }
                        }
                        seekTo(savedPosition)
                        start()
                    }
                    currentTrack = track
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun playSoundEffect(effect: SoundEffect) {
        scope.launch {
            application?.let { app ->
                try {
                    soundEffectPlayer?.release()

                    val afd = app.assets.openFd(effect.fileName)
                    soundEffectPlayer = MediaPlayer().apply {
                        setAudioAttributes(
                            AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                                .build()
                        )
                        setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                        afd.close()
                        setVolume(0.9f, 0.9f)
                        prepare()
                        setOnCompletionListener { it.release() }
                        start()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun pauseMusic() {
        musicPlayer?.let { player ->
            if (player.isPlaying) {
                player.pause()
                currentTrack?.let { track ->
                    if (track != MusicTrack.QUESTION) {
                        scope.launch(Dispatchers.IO) {
                            savePosition(track, player.currentPosition)
                        }
                    }
                }
            }
        }
    }

    fun resumeMusic() {
        musicPlayer?.let { player ->
            if (!player.isPlaying) {
                player.start()
            }
        }
    }

    fun stopMusic() {
        musicPlayer?.let { player ->
            if (currentTrack != null && currentTrack != MusicTrack.QUESTION) {
                val position = player.currentPosition
                scope.launch(Dispatchers.IO) {
                    savePosition(currentTrack!!, position)
                }
            }

            if (player.isPlaying) {
                player.stop()
            }
            player.release()
        }
        musicPlayer = null
        currentTrack = null
    }

    fun release() {
        stopMusic()
        soundEffectPlayer?.release()
        soundEffectPlayer = null
        application = null
    }
}