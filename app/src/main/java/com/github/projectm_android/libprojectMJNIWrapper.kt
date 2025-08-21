package com.github.projectm_android

object libprojectMJNIWrapper {
    init {
        System.loadLibrary("jniwrapper")
    }

    @JvmStatic
    external fun onSurfaceCreated(window_width: Int, window_height: Int, assetPath: String?)

    @JvmStatic
    external fun onSurfaceChanged(width: Int, height: Int)

    @JvmStatic
    external fun onDrawFrame()

    @JvmStatic
    external fun addPCM(pcm_data: ShortArray?, nsamples: Short)

    @JvmStatic
    external fun nextPreset()
}
