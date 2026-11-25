package tv.originostv.live

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class MainActivity : Activity() {
    private var player: ExoPlayer? = null
    private lateinit var playerView: PlayerView
    private lateinit var statusText: TextView
    private lateinit var urlInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playerView = findViewById(R.id.playerView)
        statusText = findViewById(R.id.statusText)
        urlInput = findViewById(R.id.streamUrlInput)

        findViewById<Button>(R.id.playButton).setOnClickListener {
            startPlayback(urlInput.text.toString().trim())
        }
    }

    private fun startPlayback(rawUrl: String) {
        val uri = runCatching { Uri.parse(rawUrl) }.getOrNull()
        if (uri == null || uri.scheme != "https" || uri.host.isNullOrBlank()) {
            statusText.text = "地址无效：当前验证版仅接受 HTTPS 地址"
            return
        }

        val activePlayer = player ?: ExoPlayer.Builder(this).build().also { created ->
            created.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    statusText.text = when (playbackState) {
                        Player.STATE_BUFFERING -> "正在缓冲…"
                        Player.STATE_READY -> "播放器已就绪"
                        Player.STATE_ENDED -> "播放结束"
                        Player.STATE_IDLE -> "播放器空闲"
                        else -> "播放状态更新"
                    }
                }

                override fun onPlayerError(error: PlaybackException) {
                    statusText.text = "播放失败：${error.errorCodeName}"
                }
            })
            player = created
            playerView.player = created
        }

        statusText.text = "正在连接直播源…"
        activePlayer.setMediaItem(MediaItem.fromUri(uri))
        activePlayer.prepare()
        activePlayer.playWhenReady = true
    }

    override fun onStop() {
        super.onStop()
        player?.pause()
    }

    override fun onDestroy() {
        playerView.player = null
        player?.release()
        player = null
        super.onDestroy()
    }
}
