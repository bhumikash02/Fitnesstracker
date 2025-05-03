package com.example.fitnesstracker.ui.theme.screen

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import org.webrtc.SurfaceViewRenderer
import androidx.compose.ui.viewinterop.AndroidView
import com.example.fitnesstracker.utils.WebRtcManager


@Composable
fun VideoCallScreen() {
    val context = LocalContext.current
    val activity = context as Activity

    var localSurfaceView: SurfaceViewRenderer? = remember { null }

    LaunchedEffect(Unit) {
        WebRtcManager.init(context)
    }

    DisposableEffect(Unit) {
        onDispose {
            WebRtcManager.release()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Live Video Call", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        AndroidView(
            factory = { ctx ->
                SurfaceViewRenderer(ctx).apply {
                    localSurfaceView = this
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            localSurfaceView?.let {
                WebRtcManager.startLocalVideo(it, context)
            }
        }) {
            Text("Start Camera")
        }
    }
}
