package com.ububi.explore_romania

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ububi.explore_romania.ui.theme.Explore_romaniaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize music manager with application context
        MusicManager.initialize(application)

        enableEdgeToEdge()
        setContent {
            Explore_romaniaTheme {
                AppRouter()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        MusicManager.pauseMusic()
    }

    override fun onResume() {
        super.onResume()
        MusicManager.resumeMusic()
    }

    override fun onDestroy() {
        super.onDestroy()
        MusicManager.release()
    }
}