import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@Composable
fun PlayerScreen(videoUrl: String) {
    val context = LocalContext.current
    // Only create the player ONCE
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            prepare()
        }
    }
    val playerView = PlayerView(context)
    var playWhenReady by rememberSaveable { mutableStateOf(true) }
    var playbackPosition by rememberSaveable { mutableStateOf(0L) }
    var currentWindow by rememberSaveable { mutableStateOf(0) }

    playerView.player = player

    LaunchedEffect(player) {
        player.seekTo(currentWindow, playbackPosition)
        player.playWhenReady = playWhenReady
    }

    // Release player when Composable leaves the composition
    DisposableEffect(Unit) {
        onDispose {
            playWhenReady = player.playWhenReady
            playbackPosition = player.currentPosition
            currentWindow = player.currentMediaItemIndex
            player.release()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {


        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(700.dp)
                .padding(8.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(
                    width = 6.dp,
                    brush = Brush.sweepGradient(
                        0.4f to Color.Blue, 0.6f to Color.Black,
                    ),
                    shape = RoundedCornerShape(20.dp)
                ),
            factory = {
                playerView
            }
        )

//        Text(
//            text = "LIVE",
//            color = Color.White,
//            fontSize = 14.sp,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier
//                .align(Alignment.Start)
//                .padding(16.dp)
//                .background(Color.Red, shape = RoundedCornerShape(8.dp))
//                .padding(horizontal = 12.dp, vertical = 6.dp)
//        )

    }
}