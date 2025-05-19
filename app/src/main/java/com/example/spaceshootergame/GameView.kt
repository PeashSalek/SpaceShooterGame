package com.example.spaceshootergame

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.SurfaceView
import android.widget.Toast

class GameView(context: Context) : SurfaceView(context), Runnable {

    // Game state variables
    private var isPlaying = false
    private var gameStarted = false
    private lateinit var player: Player
    private val enemies = ArrayList<Enemy>()
    private val bullets = ArrayList<Bullet>()
    private val enemiesLock = Object()
    private val bulletsLock = Object()
    private var background: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.background)
    private var isGameOver = false
    private var gameThread: Thread? = null
    private var score = 0

    // Button images and positions
    private var startButtonBitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.start)
    private var pauseButtonBitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.pause)
    private var restartButtonBitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.start) // Reusing start button for restart
    private var startButtonRect = Rect()
    private var pauseButtonRect = Rect()
    private var restartButtonRect = Rect()

    // Score display
    private val scorePaint = Paint().apply {
        color = Color.WHITE
        textSize = 60f
        textAlign = Paint.Align.LEFT
        isFakeBoldText = true
        setShadowLayer(3f, 2f, 2f, Color.BLACK) // Add shadow for better visibility
    }

    // Game Over display
    private val gameOverPaint = Paint().apply {
        color = Color.RED
        textSize = 120f
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
        setShadowLayer(5f, 3f, 3f, Color.BLACK) // Add shadow for better visibility
    }

    // Enemy spawning configuration
    private val ENEMY_LIMIT = 8
    private val SPAWN_INTERVAL: Long = 3000
    private val handler = Handler(Looper.getMainLooper())
    private val spawnRunnable = object : Runnable {
        override fun run() {
            if (gameStarted && !isGameOver) {
                spawnEnemy()
                handler.postDelayed(this, SPAWN_INTERVAL)
            }
        }
    }

    init {
        player = Player(context)
        setupButtons()
    }

    private fun setupButtons() {
        // Scale the button images
        val scaledStartButton = Bitmap.createScaledBitmap(startButtonBitmap, 300, 100, true)
        startButtonBitmap = scaledStartButton

        val scaledPauseButton = Bitmap.createScaledBitmap(pauseButtonBitmap, 150, 80, true)
        pauseButtonBitmap = scaledPauseButton

        // Scale restart button (same as start button)
        val scaledRestartButton = Bitmap.createScaledBitmap(restartButtonBitmap, 300, 100, true)
        restartButtonBitmap = scaledRestartButton

        val screenWidth = resources.displayMetrics.widthPixels
        val screenHeight = resources.displayMetrics.heightPixels

        // Center the start button in the middle of the screen
        val startButtonX = (screenWidth - scaledStartButton.width) / 2
        val startButtonY = (screenHeight - scaledStartButton.height) / 2

        startButtonRect.set(
            startButtonX,
            startButtonY,
            startButtonX + scaledStartButton.width,
            startButtonY + scaledStartButton.height
        )

        // Position pause button at the top right
        pauseButtonRect.set(
            screenWidth - scaledPauseButton.width - 20,
            20,
            screenWidth - 20,
            20 + scaledPauseButton.height
        )

        // Position restart button below the "Game Over" text (will be properly positioned in onSizeChanged)
        restartButtonRect.set(
            startButtonX,
            startButtonY + 200, // Positioned below game over text
            startButtonX + scaledRestartButton.width,
            startButtonY + 200 + scaledRestartButton.height
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // Update button positions when size changes
        setupButtons()

        // Update restart button position based on the center of the screen
        val restartButtonX = (w - restartButtonBitmap.width) / 2
        val restartButtonY = (h / 2) + 150 // Position below the "Game Over" text

        restartButtonRect.set(
            restartButtonX,
            restartButtonY,
            restartButtonX + restartButtonBitmap.width,
            restartButtonY + restartButtonBitmap.height
        )
    }

    override fun run() {
        while (isPlaying) {
            if (gameStarted && !isGameOver) {
                update()
            }
            draw()
            sleep()
        }
    }

    private fun sleep() {
        try {
            Thread.sleep(17) // ~60 FPS
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun update() {
        player.update()

        // Update bullets
        synchronized(bulletsLock) {
            val bulletsToRemove = mutableListOf<Bullet>()

            for (bullet in bullets) {
                bullet.update()

                // Remove bullets that go off-screen
                if (bullet.y < 0) {
                    bulletsToRemove.add(bullet)
                }
            }

            bullets.removeAll(bulletsToRemove)
        }

        // Create a temporary list to collect enemies to remove
        val enemiesToRemove = mutableListOf<Enemy>()

        // Synchronize access to the enemies list to prevent concurrent modification
        synchronized(enemiesLock) {
            // Update enemies and check for collisions
            for (enemy in enemies) {
                enemy.update()

                // Check for collision with player
                if (enemy.collidesWith(player)) {
                    isGameOver = true
                }

                // Remove enemies that go off-screen (bottom)
                if (enemy.y > height) {
                    enemiesToRemove.add(enemy)
                }

                // Check for bullet collisions
                synchronized(bulletsLock) {
                    val bulletsToRemove = mutableListOf<Bullet>()

                    for (bullet in bullets) {
                        if (bullet.collidesWith(enemy)) {
                            // Enemy hit by bullet
                            bulletsToRemove.add(bullet)
                            enemiesToRemove.add(enemy)
                            score++  // Increment score
                            break
                        }
                    }

                    bullets.removeAll(bulletsToRemove)
                }
            }

            // Remove the enemies after the loop has finished
            enemies.removeAll(enemiesToRemove)
        }
    }

    private fun draw() {
        val canvas = holder.lockCanvas()
        if (canvas != null && holder.surface.isValid) {
            val width = canvas.width
            val height = canvas.height

            // Draw background
            val scaledBackground = Bitmap.createScaledBitmap(background, width, height, true)
            canvas.drawBitmap(scaledBackground, 0f, 0f, null)

            // Update button positions based on actual canvas size if needed
            if (startButtonRect.width() == 0) {
                val buttonX = (width - startButtonBitmap.width) / 2
                startButtonRect.set(
                    buttonX,
                    50,
                    buttonX + startButtonBitmap.width,
                    50 + startButtonBitmap.height
                )

                pauseButtonRect.set(
                    width - pauseButtonBitmap.width - 20,
                    20,
                    width - 20,
                    20 + pauseButtonBitmap.height
                )

                // Update restart button position as well
                restartButtonRect.set(
                    buttonX,
                    height / 2 + 150,
                    buttonX + restartButtonBitmap.width,
                    height / 2 + 150 + restartButtonBitmap.height
                )
            }

            if (!gameStarted) {
                // Draw start button when game hasn't started
                canvas.drawBitmap(startButtonBitmap, startButtonRect.left.toFloat(), startButtonRect.top.toFloat(), null)
            } else {
                // Game is running
                player.draw(canvas)

                // Draw bullets
                synchronized(bulletsLock) {
                    for (bullet in bullets) {
                        bullet.draw(canvas)
                    }
                }

                // Draw enemies
                val enemiesCopy = ArrayList<Enemy>()
                synchronized(enemiesLock) {
                    enemiesCopy.addAll(enemies)
                }

                for (enemy in enemiesCopy) {
                    enemy.draw(canvas)
                }

                // Draw pause button
                canvas.drawBitmap(pauseButtonBitmap, pauseButtonRect.left.toFloat(), pauseButtonRect.top.toFloat(), null)

                // Draw score with better visibility - positioned more to the left
                canvas.drawText("Score: $score", 30f, pauseButtonRect.bottom + 50f, scorePaint)

                // Draw game over overlay when the game is over
                if (isGameOver) {
                    // Create a semi-transparent overlay
                    val overlayPaint = Paint().apply {
                        color = Color.BLACK
                        alpha = 180 // Semi-transparent
                    }
                    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), overlayPaint)

                    // Draw "GAME OVER" text in the center of the screen
                    canvas.drawText("GAME OVER", width / 2f, height / 2f - 50, gameOverPaint)

                    // Draw final score
                    val finalScorePaint = Paint().apply {
                        color = Color.WHITE
                        textSize = 70f
                        textAlign = Paint.Align.CENTER
                        isFakeBoldText = true
                    }
                    canvas.drawText("Final Score: $score", width / 2f, height / 2f + 50, finalScorePaint)

                    // Draw restart button
                    canvas.drawBitmap(
                        restartButtonBitmap,
                        restartButtonRect.left.toFloat(),
                        restartButtonRect.top.toFloat(),
                        null
                    )
                }
            }

            holder.unlockCanvasAndPost(canvas)
        }
    }

    private fun fireBullet() {
        if (gameStarted && !isGameOver) {
            synchronized(bulletsLock) {
                // Create bullet at player's position (centered horizontally)
                val bulletX = player.x + (player.bitmap.width / 2f) - 10f // Center the bullet on the player
                val bullet = Bullet(context, bulletX, player.y)
                bullets.add(bullet)
            }
        }
    }

    private fun spawnEnemy() {
        val screenWidth = width
        val screenHeight = height

        // Only spawn if we haven't reached the maximum
        synchronized(enemiesLock) {
            if (enemies.size < ENEMY_LIMIT) {
                val newEnemy = createEnemyWithSpacing(screenWidth, screenHeight)
                enemies.add(newEnemy)
            }
        }
    }

    private fun createEnemyWithSpacing(screenWidth: Int, screenHeight: Int): Enemy {
        // Create a new enemy
        val newEnemy = Enemy(context, R.drawable.enemy_1, screenWidth, screenHeight)

        // Try to find a suitable position with proper spacing
        var maxAttempts = 15
        var validPosition = false

        while (!validPosition && maxAttempts > 0) {
            // Randomize position
            newEnemy.x = (Math.random() * (screenWidth - 100)).toFloat()
            newEnemy.y = -newEnemy.getBitmapHeight() - (Math.random() * 300).toFloat()

            // Check spacing with existing enemies
            validPosition = true

            synchronized(enemiesLock) {
                for (existingEnemy in enemies) {
                    // Calculate horizontal and vertical distances
                    val horizontalDistance = Math.abs(newEnemy.x - existingEnemy.x)
                    val verticalDistance = Math.abs(newEnemy.y - existingEnemy.y)

                    // Check if spacing requirements are met
                    if (horizontalDistance < 100 || verticalDistance < 200) {
                        validPosition = false
                        break
                    }
                }
            }

            maxAttempts--
        }

        return newEnemy
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val touchX = event.x.toInt()
                val touchY = event.y.toInt()

                if (!gameStarted) {
                    // Check if the start button was clicked
                    if (startButtonRect.contains(touchX, touchY)) {
                        gameStarted = true
                        startGame()
                        return true
                    }
                } else {
                    // Check if the game is over and restart button is clicked
                    if (isGameOver && restartButtonRect.contains(touchX, touchY)) {
                        // Restart the game
                        restartGame()
                        return true
                    }

                    // Check if the pause button was clicked
                    if (!isGameOver && pauseButtonRect.contains(touchX, touchY)) {
                        togglePause()
                        return true
                    }

                    // Fire a bullet when touching the screen (except on buttons)
                    if (!isGameOver &&
                        !pauseButtonRect.contains(touchX, touchY) &&
                        !restartButtonRect.contains(touchX, touchY)) {
                        fireBullet()
                    }
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (gameStarted && !isGameOver) {
                    val touchX = event.x
                    val touchY = event.y

                    // Move player based on touch location
                    if (touchX < player.x) {
                        player.moveLeft()
                    } else if (touchX > player.x + player.bitmap.width) {
                        player.moveRight()
                    }

                    if (touchY < player.y) {
                        player.moveUp()
                    } else if (touchY > player.y + player.bitmap.height) {
                        player.moveDown()
                    }
                }
            }
        }
        return true
    }

    private fun togglePause() {
        if (isPlaying) {
            pause()
        } else {
            resume()
        }
    }

    private fun startGame() {
        // Initialize the game
        player = Player(context)
        synchronized(enemiesLock) {
            enemies.clear()
        }
        synchronized(bulletsLock) {
            bullets.clear()
        }
        score = 0
        isGameOver = false

        // Start the game thread and enemy spawning
        resume()

        // Ensure enemies start with proper speed
        handler.postDelayed({
            if (enemies.size < 3) {
                // Force spawn a few enemies to get the game started
                for (i in 0 until (3 - enemies.size)) {
                    spawnEnemy()
                }
            }
        }, 500)
    }

    private fun restartGame() {
        // Reset game state and start a new game
        gameStarted = true
        startGame()
    }

    fun pause() {
        isPlaying = false
        try {
            gameThread?.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        handler.removeCallbacks(spawnRunnable)
    }

    fun resume() {
        isPlaying = true
        gameThread = Thread(this)
        gameThread?.start()
        startEnemySpawning()
    }

    private fun startEnemySpawning() {
        handler.postDelayed(spawnRunnable, 1000)
    }
}