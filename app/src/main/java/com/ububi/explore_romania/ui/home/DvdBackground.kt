package com.ububi.explore_romania.ui.home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.delay
import androidx.compose.ui.draw.clipToBounds
import kotlin.random.Random
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp

fun decodeScaledBitmap(context: Context, resId: Int, maxSize: Int = 2048): Bitmap {

    val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    BitmapFactory.decodeResource(context.resources, resId, options)

    var sampleSize = 1


    while (options.outWidth / sampleSize > maxSize ||
        options.outHeight / sampleSize > maxSize) {
        sampleSize *= 2
    }


    val finalOptions = BitmapFactory.Options().apply { inSampleSize = sampleSize }

    return BitmapFactory.decodeResource(context.resources, resId, finalOptions)
}

@Composable
fun DvdBackground(imageRes: Int) {

    val context = LocalContext.current

    val bitmap = remember {
        decodeScaledBitmap(context, imageRes, maxSize = 2048)
    }

    val screenWidthPx = with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.dp.roundToPx()
    }
    val screenHeightPx = with(LocalDensity.current) {
        LocalConfiguration.current.screenHeightDp.dp.roundToPx()
    }

    val imgWidth = bitmap.width
    val imgHeight = bitmap.height


    var offsetX by remember { mutableStateOf(((imgWidth - screenWidthPx) / 2f)) }
    var offsetY by remember { mutableStateOf(((imgHeight - screenHeightPx) / 2f)) }

    var dirX by remember { mutableStateOf(if (Random.nextBoolean()) 1f else -1f) }
    var dirY by remember { mutableStateOf(if (Random.nextBoolean()) 1f else -1f) }

    val speed = 20f

    val minSeconds = 6
    val maxSeconds = 10


    LaunchedEffect(Unit) {
        while (true) {
            delay(Random.nextInt(minSeconds, maxSeconds) * 1000L)

            val possible = listOf(
                1f to 1f,
                1f to -1f,
                -1f to 1f,
                -1f to -1f
            ).filter { it.first != dirX || it.second != dirY }

            val newDir = possible.random()
            dirX = newDir.first
            dirY = newDir.second
        }
    }


    LaunchedEffect(Unit) {
        var lastTime = 0L

        while (true) {
            withFrameNanos { time ->

                if (lastTime != 0L) {
                    val dt = (time - lastTime) / 1_000_000_000f

                    offsetX += dirX * speed * dt
                    offsetY += dirY * speed * dt

                    val maxX = (imgWidth - screenWidthPx).toFloat()
                    val maxY = (imgHeight - screenHeightPx).toFloat()

                    if (offsetX < 0) { offsetX = 0f; dirX = 1f }
                    if (offsetX > maxX) { offsetX = maxX; dirX = -1f }

                    if (offsetY < 0) { offsetY = 0f; dirY = 1f }
                    if (offsetY > maxY) { offsetY = maxY; dirY = -1f }
                }

                lastTime = time
            }
        }
    }

    val shader = remember(bitmap) {
        androidx.compose.ui.graphics.ImageShader(
            bitmap.asImageBitmap(),
            androidx.compose.ui.graphics.TileMode.Clamp,
            androidx.compose.ui.graphics.TileMode.Clamp
        )
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .clipToBounds()
    ) {
        val androidMatrix = android.graphics.Matrix().apply {
            setTranslate(-offsetX, -offsetY)
        }

        shader.setLocalMatrix(androidMatrix)

        drawRect(
            brush = androidx.compose.ui.graphics.ShaderBrush(shader),
            size = Size(size.width, size.height)
        )
    }
}

