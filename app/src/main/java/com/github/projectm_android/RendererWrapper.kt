package com.github.projectm_android

import android.content.res.Resources
import android.opengl.GLSurfaceView.Renderer
import android.util.Log
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class RendererWrapper(private val mAssetPath: String) : Renderer {
    private var mNextPreset = false
    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        Log.d("projectM", "RenderWrapper onSurfaceCreated")
        val width = Resources.getSystem().displayMetrics.widthPixels
        val height = Resources.getSystem().displayMetrics.heightPixels
        libprojectMJNIWrapper.onSurfaceCreated(width, height, mAssetPath)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        Log.d("projectM", "RenderWrapper onSurfaceChanged")
        libprojectMJNIWrapper.onSurfaceChanged(width, height)
    }

    fun nextPreset() {
        mNextPreset = true
    }

    override fun onDrawFrame(gl: GL10) {
        if (mNextPreset) {
            mNextPreset = false
            libprojectMJNIWrapper.nextPreset()
        }
        libprojectMJNIWrapper.onDrawFrame()
    }
}
