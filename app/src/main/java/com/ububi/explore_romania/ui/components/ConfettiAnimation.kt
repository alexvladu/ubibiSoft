package com.ububi.explore_romania.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import kotlin.random.Random

data class ConfettiParticle(
    val startX: Float,
    val startY: Float,
    val color: Color,
    val size: Float,
    val velocityX: Float,
    val velocityY: Float,
    val rotation: Float,
    val rotationSpeed: Float
)

@Composable
fun ConfettiAnimation(
    isAnimating: Boolean,
    centerX: Float,
    centerY: Float,
    modifier: Modifier = Modifier
) {
    val particles = remember {
        List(50) {
            val angle = Random.nextFloat() * 2 * Math.PI.toFloat()
            val velocity = Random.nextFloat() * 200 + 100

            ConfettiParticle(
                startX = centerX,
                startY = centerY,
                color = listOf(
                    Color.Red, Color.Yellow, Color.Green,
                    Color.Blue, Color.Magenta, Color.Cyan
                ).random(),
                size = Random.nextFloat() * 8 + 4,
                velocityX = kotlin.math.cos(angle) * velocity,
                velocityY = kotlin.math.sin(angle) * velocity - 100, // Bias upward
                rotation = Random.nextFloat() * 360,
                rotationSpeed = Random.nextFloat() * 360 - 180
            )
        }
    }

    var animationTime by remember { mutableStateOf(0f) }
    var isActive by remember { mutableStateOf(false) }

    LaunchedEffect(isAnimating) {
        if (isAnimating) {
            isActive = true
            animationTime = 0f

            // Animate for 1.5 seconds
            val duration = 1500L
            val startTime = System.currentTimeMillis()

            while (System.currentTimeMillis() - startTime < duration) {
                animationTime = (System.currentTimeMillis() - startTime) / 1000f
                delay(16) // ~60 FPS
            }

            isActive = false
        }
    }

    if (isActive) {
        Canvas(modifier = modifier.fillMaxSize()) {
            val gravity = 300f // pixels per second squared

            particles.forEach { particle ->
                val time = animationTime

                // Physics calculations
                val x = particle.startX + particle.velocityX * time
                val y = particle.startY + particle.velocityY * time + 0.5f * gravity * time * time

                // Only draw if still visible
                if (y < size.height) {
                    drawCircle(
                        color = particle.color,
                        radius = particle.size,
                        center = Offset(x, y),
                        alpha = 1f - (time / 1.5f) // Fade out
                    )
                }
            }
        }
    }
}

