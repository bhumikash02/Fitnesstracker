package com.example.fitnesstracker.utils

import android.content.Context
import android.util.Log
import org.webrtc.*

object WebRtcManager {

    private const val TAG = "WebRtcManager"

    private var peerConnectionFactory: PeerConnectionFactory? = null
    private var eglBaseContext: EglBase? = null
    private var videoCapturer: VideoCapturer? = null
    private var localVideoSource: VideoSource? = null
    private var remoteVideoSource: VideoSource? = null
    private var localVideoTrack: VideoTrack? = null
    private var remoteVideoTrack: VideoTrack? = null
    private var localPeer: PeerConnection? = null

    private lateinit var iceServers: List<PeerConnection.IceServer>

    fun init(context: Context) {
        eglBaseContext = EglBase.create()

        PeerConnectionFactory.initialize(
            PeerConnectionFactory.InitializationOptions.builder(context)
                .createInitializationOptions()
        )

        val options = PeerConnectionFactory.Options()
        val encoderFactory = DefaultVideoEncoderFactory(
            eglBaseContext!!.eglBaseContext, true, true
        )
        val decoderFactory = DefaultVideoDecoderFactory(eglBaseContext!!.eglBaseContext)

        peerConnectionFactory = PeerConnectionFactory.builder()
            .setOptions(options)
            .setVideoEncoderFactory(encoderFactory)
            .setVideoDecoderFactory(decoderFactory)
            .createPeerConnectionFactory()

        iceServers = listOf(
            PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer()
        )
    }

    fun createVideoCapturer(context: Context): VideoCapturer {
        val enumerator = Camera2Enumerator(context)
        val devices = enumerator.deviceNames

        devices.forEach { device ->
            if (enumerator.isFrontFacing(device)) {
                val capturer = enumerator.createCapturer(device, null)
                if (capturer != null) {
                    return capturer
                }
            }
        }

        throw RuntimeException("No front-facing camera found")
    }

    fun startLocalVideo(surfaceView: SurfaceViewRenderer, context: Context) {
        videoCapturer = createVideoCapturer(context)
        videoCapturer?.let {
            localVideoSource = peerConnectionFactory?.createVideoSource(it.isScreencast)
            it.initialize(
                SurfaceTextureHelper.create("CaptureThread", eglBaseContext!!.eglBaseContext),
                context,
                localVideoSource?.capturerObserver
            )
            it.startCapture(1024, 720, 30)

            localVideoTrack = peerConnectionFactory?.createVideoTrack("LOCAL_TRACK", localVideoSource!!)
            surfaceView.init(eglBaseContext!!.eglBaseContext, null)
            surfaceView.setMirror(true)
            localVideoTrack?.addSink(surfaceView)
        }
    }

    fun release() {
        localPeer?.close()
        videoCapturer?.stopCapture()
        localVideoSource?.dispose()
        remoteVideoSource?.dispose()
        peerConnectionFactory?.dispose()
        eglBaseContext?.release()
    }
}
