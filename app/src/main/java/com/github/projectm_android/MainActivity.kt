package com.github.projectm_android

import android.annotation.SuppressLint
import android.content.Context
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import java.io.File

class MainActivity : FragmentActivity() {
    private var audioThread: AudioThread? = null
    private var glSurfaceView: ProjectMGLView? = null
    @SuppressLint("ClickableViewAccessibility")
    inner class ProjectMGLView(context: Context?, assetPath: String) :
        GLSurfaceView(context) {
        private val mRenderer: RendererWrapper
        override fun onTouchEvent(e: MotionEvent): Boolean {
            mRenderer.nextPreset()
            return true
        }

        override fun onPause() {
            super.onPause()
        }

        override fun onResume() {
            super.onResume()
        }

        init {
            setEGLContextClientVersion(2)
            mRenderer = RendererWrapper(assetPath)
            setRenderer(mRenderer)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val assetPath = File(cacheDir, "projectM").toString()
        glSurfaceView = ProjectMGLView(this, assetPath)
        setContentView(glSurfaceView)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onPause() {
        super.onPause()
        glSurfaceView!!.onPause()
        audioThread!!.stop_recording()
        try {
            audioThread!!.join()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        glSurfaceView!!.onResume()
        audioThread = AudioThread()
        audioThread!!.start()
    }
}
