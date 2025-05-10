package com.example.spaceshootergame

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect

class Bullet(context: Context, startX: Float, startY: Float) {

    private val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.laser)
    var x = startX
    var y = startY
    private val speed = 20

    fun update() {
        y -= speed
    }

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(bitmap, x, y, null)
    }

    fun collidesWith(enemy: Enemy): Boolean {
        return Rect.intersects(getBounds(), enemy.getBounds())
    }

    private fun getBounds(): Rect {
        return Rect(x.toInt(), y.toInt(), (x + bitmap.width).toInt(), (y + bitmap.height).toInt())
    }

    fun destroy() {
        // Logic to destroy the bullet or reset position
    }
}