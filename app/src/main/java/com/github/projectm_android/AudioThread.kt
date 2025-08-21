package com.github.projectm_android

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.SystemClock
import android.util.Log

class AudioThread : Thread() {
    @Volatile
    private var keep_recording = true

    @SuppressLint("MissingPermission")
    override fun run() {
        var bufferSize = AudioRecord.getMinBufferSize(
            SAMPLE_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        val record = AudioRecord(
            MediaRecorder.AudioSource.DEFAULT,
            SAMPLE_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize
        )
        if (bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE) {
            bufferSize = SAMPLE_RATE * 2
        }
        val audioBuffer = ShortArray(bufferSize)
        Log.d("AudioThread", String.format("bufferSize: %d", bufferSize))
        while (currently_recording) {
            Log.d("AudioThread", "currently recoring, waiting 50ms before attempting again")
            // bufferSize is usually worth 80ms of aurdio
            // So waiting 50ms - this means, next attempt to grab AudioRecord will succeed.
            SystemClock.sleep(50)
        }
        record.startRecording()
        currently_recording = true
        while (keep_recording) {
            record.read(audioBuffer, 0, audioBuffer.size)
            libprojectMJNIWrapper.addPCM(audioBuffer, bufferSize.toShort())
            //            short max = 0;
//            for (int ix=0;ix<audioBuffer.length; ix++) {
//                max = (short) Math.max(audioBuffer[ix], max);
//            }
//            Log.i("MAX:", String.valueOf(max));
        }
        record.stop()
        currently_recording = false
    }

    fun stop_recording() {
        keep_recording = false
    }

    companion object {
        @Volatile
        private var currently_recording = false
        private const val SAMPLE_RATE = 11025
    }
}
