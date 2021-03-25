package seamcarving.util

import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import kotlin.math.sqrt

fun createImage(width: Int, height: Int): BufferedImage {
    val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

    val graphics : Graphics2D = bufferedImage.createGraphics()

    graphics.paint = Color.BLACK
    graphics.fillRect(0, 0, bufferedImage.width, bufferedImage.height)
    graphics.paint = Color.RED
    graphics.drawLine(0, 0, bufferedImage.width - 1, bufferedImage.height - 1)
    graphics.drawLine(0, bufferedImage.height - 1, bufferedImage.width - 1, 0)

    return bufferedImage
}

fun BufferedImage.negative(): BufferedImage {
    val outputImage = createImage(width, height)

    for (y in 0 until height) {
        for (x in 0 until width) {
            val inputPixel = getRGB(x, y)
            val inputColor = Color(inputPixel, true)
            val r = 255 - inputColor.red
            val g = 255 - inputColor.green
            val b = 255 - inputColor.blue
            val outputColor = Color(r, g, b)
            outputImage.setRGB(x, y, outputColor.rgb)
        }
    }

    return outputImage
}

fun BufferedImage.deltaXSquared(x: Int, y: Int): Int {
    val xx = when (x) {
        0 -> 1
        width - 1 -> width - 2
        else -> x
    }

    val inputColorX1 = Color(getRGB(xx + 1, y), true)
    val inputColorX2 = Color(getRGB(xx - 1, y), true)

    val rx = inputColorX1.red - inputColorX2.red
    val gx = inputColorX1.green - inputColorX2.green
    val bx = inputColorX1.blue - inputColorX2.blue

    return (rx * rx) + (gx * gx) + (bx * bx)
}

fun BufferedImage.deltaYSquared(x: Int, y: Int): Int {
    val yy = when (y) {
        0 -> 1
        height - 1 -> height - 2
        else -> y
    }

    val inputPixelY1 = Color(getRGB(x, yy + 1), true)
    val inputPixelY2 = Color(getRGB(x, yy - 1), true)

    val ry = inputPixelY1.red - inputPixelY2.red
    val gy = inputPixelY1.green - inputPixelY2.green
    val by = inputPixelY1.blue - inputPixelY2.blue

    return (ry * ry) + (gy * gy) + (by * by)
}

fun BufferedImage.energyArray(): EnergyArray {
    val energyArray = EnergyArray(width, height)

    for (y in 0 until height) {
        for (x in 0 until width) {
            val dx2 = deltaXSquared(x, y)
            val dy2 = deltaYSquared(x, y)

            val energy = sqrt((dx2 + dy2).toDouble())

            energyArray[x, y] = energy
        }
    }

    return energyArray
}

fun BufferedImage.energy(): BufferedImage {
    val outputImage = createImage(width, height)

    val energyArray = energyArray()

    for (y in 0 until height) {
        for (x in 0 until width) {
            val energy = energyArray[x, y]

            val intensity = (255.0 * energy / energyArray.maxEnergy).toInt()

            val r = intensity
            val g = intensity
            val b = intensity

            val outputColor = Color(r, g, b)

            outputImage.setRGB(x, y, outputColor.rgb)
        }
    }

    return outputImage
}

fun BufferedImage.addVerticalSeam(): BufferedImage {
    val outputImage = createImage(width, height)

    // rewrite original image

    for (y in 0 until height) {
        for (x in 0 until width) {
            val inputPixel = getRGB(x, y)
            val inputColor = Color(inputPixel, true)
            outputImage.setRGB(x, y, inputColor.rgb)
        }
    }

    // add vertical seam

    val energyArray = energyArray()
    val seam = energyArray.verticalSeam

    for ((x, y) in seam) {
        outputImage.setRGB(x, y, Color.RED.rgb)
    }

    return outputImage
}

private fun BufferedImage.transpose(): BufferedImage {
    val outputImage = createImage(height, width)

    for (y in 0 until height) {
        for (x in 0 until width) {
            val inputPixel = getRGB(x, y)
            val inputColor = Color(inputPixel, true)
            outputImage.setRGB(y, x, inputColor.rgb)
        }
    }

    return outputImage
}

fun BufferedImage.addHorizontalSeam(): BufferedImage =
        this.transpose().addVerticalSeam().transpose()