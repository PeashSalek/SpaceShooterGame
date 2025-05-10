package com.example.spaceshootergame

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect

class Player(context: Context) {
    val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.player_ship)
    var x: Float = 500f  // Starting x position (center of screen)
    var y: Float = 1000f  // Starting y position

    // Player movement speed
    private val speed = 10

    fun update() {
        // Logic for moving the player based on input (already handled in GameView)
    }

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(bitmap, x, y, null)
    }

    // Get the bounds of the player for collision detection
    fun getBounds(): Rect {
        return Rect(x.toInt(), y.toInt(), (x + bitmap.width).toInt(), (y + bitmap.height).toInt())
    }

    // Logic to move the player left
    fun moveLeft() {
        if (x - speed >= 0) {
            x -= speed  // Ensure the player doesn't move off the screen
        }
    }

    // Logic to move the player right
    fun moveRight() {
        if (x + speed + bitmap.width <= 1080) {  // Assume screen width is 1080px
            x += speed  // Ensure the player doesn't move off the screen
        }
    }

    // Logic to move the player up
    fun moveUp() {
        if (y - speed >= 0) {
            y -= speed  // Ensure the player doesn't move off the screen
        }
    }

    // Logic to move the player down
    fun moveDown() {
        if (y + speed + bitmap.height <= 1920) {  // Assume screen height is 1920px
            y += speed  // Ensure the player doesn't move off the screen
        }
    }
}

