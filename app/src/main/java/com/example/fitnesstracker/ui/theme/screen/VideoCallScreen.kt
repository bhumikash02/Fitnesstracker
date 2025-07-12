package com.example.fitnesstracker.ui.theme.screen

import android.Manifest
import android.util.Log
import android.view.SurfaceView
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import io.agora.rtc2.*
import io.agora.rtc2.video.VideoCanvas

@Composable
fun VideoCallScreen(onNavigateUp: () -> Unit) {
    val context = LocalContext.current
    val agoraEngine = remember { mutableStateOf<RtcEngine?>(null) }

    var hasPermissions by remember { mutableStateOf(false) }
    var remoteUserUid by remember { mutableStateOf<Int?>(null) }
    var isMicMuted by remember { mutableStateOf(false) }
    var isCameraMuted by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { perms ->
            hasPermissions = perms.values.all { it }
            if (!hasPermissions) {
                Toast.makeText(context, "Permissions are required.", Toast.LENGTH_SHORT).show()
            }
        }
    )

    LaunchedEffect(key1 = true) {
        launcher.launch(arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE))
    }

    DisposableEffect(hasPermissions) {
        if (!hasPermissions) return@DisposableEffect onDispose {}

        val engine: RtcEngine
        try {
            val config = RtcEngineConfig()
            config.mAppId = "d4612cfd789441abb1ef25c15efae152" // Your App ID
            config.mContext = context
            config.mEventHandler = object : IRtcEngineEventHandler() {
                override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
                    Log.d("VideoCallDebug", "Local user $uid joined successfully")
                }
                override fun onUserJoined(uid: Int, elapsed: Int) {
                    Log.d("VideoCallDebug", "Remote user $uid joined")
                    remoteUserUid = uid
                }
                override fun onUserOffline(uid: Int, reason: Int) {
                    Log.d("VideoCallDebug", "Remote user $uid left")
                    remoteUserUid = null
                }
            }
            engine = RtcEngine.create(config)
            engine.enableVideo()
            agoraEngine.value = engine

            val options = ChannelMediaOptions()
            options.channelProfile = Constants.CHANNEL_PROFILE_LIVE_BROADCASTING
            options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER
            val token = "007eJxTYHgbXRCi0THzc/TkM5nhedHvt7s7aV2efNfP6+XaF5X6DxkVGFJMzAyNktNSzC0sTUwME5OSDFPTjEyTDU1T0xJTDU2Nqt2KMhoCGRk0vWtZGRkgEMTnZ0jLLMlLLS6OT85IzMtLzWFgAAApwSSd"
            engine.joinChannel(token, "fitness_channel", 0, options)
        } catch (e: Exception) {
            Log.e("VideoCallDebug", "Engine setup failed", e)
        }

        onDispose {
            agoraEngine.value?.leaveChannel()
            RtcEngine.destroy()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Remote Video
        if (remoteUserUid != null) {
            AndroidView(
                factory = { ctx ->
                    val surfaceView = SurfaceView(ctx)
                    agoraEngine.value?.setupRemoteVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, remoteUserUid!!))
                    surfaceView
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        // Local Video
        if (!isCameraMuted) {
            AndroidView(
                factory = { ctx ->
                    val surfaceView = SurfaceView(ctx)
                    agoraEngine.value?.setupLocalVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, 0))
                    agoraEngine.value?.startPreview()
                    surfaceView
                },
                modifier = Modifier
                    .size(width = 120.dp, height = 160.dp)
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            )
        }

        // Controls
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .background(Color.Black.copy(alpha = 0.3f), shape = MaterialTheme.shapes.large)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IconButton(onClick = {
                isMicMuted = !isMicMuted
                agoraEngine.value?.muteLocalAudioStream(isMicMuted)
            }) {
                Icon(
                    imageVector = if (isMicMuted) Icons.Default.MicOff else Icons.Default.Mic,
                    contentDescription = "Mute Mic",
                    tint = Color.White
                )
            }
            Button(onClick = onNavigateUp, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                Icon(imageVector = Icons.Default.CallEnd, contentDescription = "End Call", tint = Color.White)
            }
            IconButton(onClick = {
                isCameraMuted = !isCameraMuted
                agoraEngine.value?.muteLocalVideoStream(isCameraMuted)
            }) {
                Icon(
                    imageVector = if (isCameraMuted) Icons.Default.VideocamOff else Icons.Default.Videocam,
                    contentDescription = "Mute Camera",
                    tint = Color.White
                )
            }
        }
    }
}