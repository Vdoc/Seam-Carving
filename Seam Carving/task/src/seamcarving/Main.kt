package seamcarving

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO

fun main() {
    val scanner = Scanner(System.`in`)
    println("Enter rectangle width:")
    val width = scanner.nextInt()
    println("Enter rectangle height:")
    val height = scanner.nextInt()
    println("Enter output image name:")
    val imageName = scanner.next()

    val W_MAX = width - 1
    val H_MAX = height - 1

    val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    val graphics = image.graphics
    graphics.color = Color.BLACK
    graphics.fillRect(0, 0, W_MAX, H_MAX)
    graphics.color = Color.RED
    graphics.drawLine(0, 0, W_MAX, H_MAX)
    graphics.drawLine(W_MAX, 0, 0, H_MAX)

    ImageIO.write(image, "png", File(imageName))
}