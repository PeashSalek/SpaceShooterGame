package com.example.spaceshootergame

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect

class Enemy(context: Context, drawableResource: Int, screenWidth: Int, screenHeight: Int) {
    private val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, drawableResource)
    var x = (Math.random() * screenWidth).toFloat()
    var y = -bitmap.height.toFloat()
    private val variableSpeed = 3 + (Math.random() * 3).toInt()  // Speed between 3-5
    private val screenHeight = screenHeight
    private val screenWidth = screenWidth

    init {
        // Set initial y position with randomized vertical spacing
        y = -bitmap.height.toFloat() - (Math.random() * 400).toFloat()
    }

    fun update() {
        y += variableSpeed

        // Check if the enemy goes off-screen (bottom)
        if (y > screenHeight) {
            // Reset to top of screen with random position
            resetPosition()
        }
    }

    // Reset enemy position and ensure consistent speed
    fun resetPosition() {
        y = -bitmap.height.toFloat() - (Math.random() * 400).toFloat()
        x = (Math.random() * (screenWidth - bitmap.width)).toFloat()
    }

    // When restarting the game, we need to ensure consistent speeds
    fun resetSpeed() {
        // Ensure speed remains in the proper range
        val newSpeed = 3 + (Math.random() * 3).toInt()
    }

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(bitmap, x, y, null)
    }

    fun collidesWith(player: Player): Boolean {
        return Rect.intersects(getBounds(), player.getBounds())
    }

    fun getBounds(): Rect {
        return Rect(x.toInt(), y.toInt(), (x + bitmap.width).toInt(), (y + bitmap.height).toInt())
    }

    fun getBitmapHeight(): Float {
        return bitmap.height.toFloat()
    }

    fun distanceTo(other: Enemy): Float {
        val dx = x - other.x
        val dy = y - other.y
        return Math.sqrt((dx * dx + dy * dy).toDouble()).toFloat()
    }

    fun destroy() {
        resetPosition()
    }
}