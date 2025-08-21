package com.github.projectm_android

import android.app.Application
import android.content.res.AssetManager
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class ProjectMApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Extracting assets...")
        val cacheDir = File(cacheDir, "projectM")
        if (cacheDir.exists()) {
            deleteFile(cacheDir)
        }
        val assetManager = assets
        val dirNames = arrayOf("fonts", "presets")
        try {
            for (dirName in dirNames) {
                val dir = File(cacheDir, dirName)
                dir.mkdirs()
                val assetDir = String.format("projectM%s%s", File.separator, dirName)
                for (fileName in assetManager.list(assetDir)!!) {
                    val assetName = String.format("projectM/%s/%s", dirName, fileName)
                    writeStreamToFile(assetManager.open(assetName), File(dir, fileName))
                }
            }
            Log.d(TAG, "Assets extracted")
        } catch (e: IOException) {
            Log.e(TAG, "error extracting assets", e)
        }
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private fun deleteFile(entry: File) {
            if (entry.isDirectory) {
                for (sub in entry.listFiles()!!) {
                    deleteFile(sub)
                }
            }
            entry.delete()
        }

        @Throws(IOException::class)
        private fun writeStreamToFile(inputStream: InputStream, file: File) {
            var os: OutputStream? = null
            try {
                os = FileOutputStream(file)
                val buf = ByteArray(1024)
                var read: Int
                while (inputStream.read(buf).also { read = it } != -1) {
                    os.write(buf, 0, read)
                }
            } finally {
                os?.close()
            }
        }
    }
}
