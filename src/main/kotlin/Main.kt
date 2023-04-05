
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Point
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.util.Random
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.Timer

class SnakeGame : JPanel(), ActionListener { companion object {
    const val SCALE = 10
    const val WIDTH = 30
    const val HEIGHT = 30
    const val DELAY = 140
}

private val random = Random()
private val timer = Timer(DELAY, this)
private var snake = listOf(Point(WIDTH / 2, HEIGHT / 2), Point(WIDTH / 2, HEIGHT / 2 - 1), Point(WIDTH / 2, HEIGHT / 2 - 2))
private var food = newFood()
private var direction = Direction.UP
private var score = 0

init {
    preferredSize = Dimension(WIDTH * SCALE, HEIGHT * SCALE)
    background = Color.black
    isFocusable = true
    addKeyListener(object : KeyAdapter() {
        override fun keyPressed(e: KeyEvent?) {
            when (e?.keyCode) {
                KeyEvent.VK_UP -> direction = Direction.UP
                KeyEvent.VK_DOWN -> direction = Direction.DOWN
                KeyEvent.VK_LEFT -> direction = Direction.LEFT
                KeyEvent.VK_RIGHT -> direction = Direction.RIGHT
            }
        }
    })
    timer.start()
}

override fun paintComponent(g: Graphics?) {
    super.paintComponent(g)
    snake.forEach {
        g?.color = Color.white
        g?.fillRect(it.x * SCALE, it.y * SCALE, SCALE, SCALE)
    }
    g?.color = Color.green
    g?.fillRect(food.x * SCALE, food.y * SCALE, SCALE, SCALE)
    g?.color = Color.white
    g?.drawString("Score: $score", SCALE, SCALE * HEIGHT - SCALE / 2)
}

override fun actionPerformed(e: ActionEvent?) {
    move()
    checkCollisions()
    repaint()
}

private fun move() {
    val head = Point(snake.first())
    when (direction) {
        Direction.UP -> head.translate(0, -1)
        Direction.DOWN -> head.translate(0, 1)
        Direction.LEFT -> head.translate(-1, 0)
        Direction.RIGHT -> head.translate(1, 0)
    }
    snake = listOf(head) + snake.dropLast(1)
}

private fun checkCollisions() {
    if (snake.first() == food) {
        score++
        food = newFood()
        snake = listOf(newHead()) + snake
    }
    if (snake.drop(1).contains(snake.first())) {
        gameOver()
    }
    if (snake.any { it.x !in 0 until WIDTH || it.y !in 0 until HEIGHT }) {
        gameOver()
    }
}

private fun newHead(): Point {
    val head = Point(snake.first())
    when (direction) {
        Direction.UP -> head.translate(0, -1)
        Direction.DOWN -> head.translate(0, 1)
        Direction.LEFT -> head.translate(-1, 0)
        Direction.RIGHT -> head.translate(1, 0)
    }
    return head
}

private fun newFood(): Point {
    var food = Point(random.nextInt(WIDTH), random.nextInt(HEIGHT))
    while (snake.contains(food)) {
        food = Point(random.nextInt(WIDTH), random.nextInt(HEIGHT))
    }
    return food
}

private fun gameOver() {
    timer.stop()
    println("Game Over! Score:
