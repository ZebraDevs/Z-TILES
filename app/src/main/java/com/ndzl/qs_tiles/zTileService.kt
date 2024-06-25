package com.ndzl.qs_tiles

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.service.quicksettings.TileService
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.service.quicksettings.Tile
import kotlin.math.PI
import kotlin.math.sin

class zTileService : TileService() {


    // Called when the user adds your tile.
    override fun onTileAdded() {
        super.onTileAdded()
    }
    // Called when your app can update your tile.
   // data class StateModel(val enabled: Boolean, val label: String, val icon: Icon)

    override fun onStartListening() {
        super.onStartListening()
    }

    // Called when your app can no longer update your tile.
    override fun onStopListening() {
        super.onStopListening()
    }

    fun generateNote(frequency: Double, durationInSeconds: Double, sampleRate: Int): ByteArray {

        val numSamples = (durationInSeconds * sampleRate).toInt()
        val samples = ByteArray(numSamples)

        for (i in 0 until numSamples) {
            val time = i.toDouble()/100.0
            samples[i] = ((Math.sin( frequency * time) * 127.0).toInt() + 0).toByte()
        }

        return samples
    }

    // Called when the user taps on your tile in an active or inactive state.
    override fun onClick() {
        super.onClick()
        qsTile.state = Tile.STATE_ACTIVE
        qsTile.updateTile()

        for (i in 0 until 16) {
            val random = (50..500).random()
                    playPCMData(generateNote(random.toDouble(), 1.0, 1000))
                }

        qsTile.state = Tile.STATE_INACTIVE
        qsTile.updateTile()
    }

    // Called when the user removes your tile.
    override fun onTileRemoved() {
        super.onTileRemoved()
    }

    //PLay PCM audio data
    fun playPCMData(data: ByteArray) {
        val audioTrack = AudioTrack(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build(),
            AudioFormat.Builder()
                .setEncoding(AudioFormat.ENCODING_PCM_8BIT)
                .setSampleRate(11025)
                .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
                .build(),
            data.size,
            AudioTrack.MODE_STATIC,
            AudioManager.AUDIO_SESSION_ID_GENERATE
        )
        audioTrack.write(data, 0, data.size)
        audioTrack.play()
    }

}
