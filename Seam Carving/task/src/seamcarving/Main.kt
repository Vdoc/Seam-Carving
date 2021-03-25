package seamcarving

import java.awt.Color
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.awt.Graphics2D
import java.io.File
import kotlin.math.sqrt


fun main(args: Array<String>) {
    require(args.size == 4) { "USAGE: -in inputFileName -out outputFileName" }

    val inputFileName = if (args[0] == "-in") {
        args[1]
    } else {
        throw IllegalStateException("ERROR: missing required '-in' parameter!")
    }

    val outputFileName = if (args[2] == "-out") {
        args[3]
    } else {
        throw IllegalStateException("ERROR: missing required '-out' parameter!")
    }

    try {
        val inputImage = readImage(inputFileName)

        val outputImage = energyImage(inputImage)

        saveImage(outputFileName, outputImage)
    } catch (exception: Exception) {
        exception.printStackTrace()
    }
}

private fun energyImage(inputImage: BufferedImage): BufferedImage {
    val width = inputImage.width
    val height = inputImage.height

    val outputImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

    val energyArray = Array(width) { DoubleArray(height) }
    var maxEnergyValue = 0.0

    for (x in 0 until width) {
        for (y in 0 until height) {
            val dx2 = deltaXSquared(inputImage, x, y)
            val dy2 = deltaYSquared(inputImage, x, y)

            val energy = sqrt((dx2 + dy2).toDouble())

            energyArray[x][y] = energy

            maxEnergyValue = if (energy > maxEnergyValue) energy else maxEnergyValue
        }
    }

    for (x in 0 until width) {
        for (y in 0 until height) {
            val energy = energyArray[x][y]

            val intensity = (255.0 * energy / maxEnergyValue).toInt()

            val r = intensity
            val g = intensity
            val b = intensity

            val outputColor = Color(r, g, b)

            outputImage.setRGB(x, y, outputColor.rgb)
        }
    }

    return outputImage
}

private fun deltaXSquared(inputImage: BufferedImage, x: Int, y: Int): Int {
    val width = inputImage.width

    val xx = when (x) {
        0 -> 1
        width - 1 -> width - 2
        else -> x
    }

    val inputColorX1 = Color(inputImage.getRGB(xx + 1, y), true)
    val inputColorX2 = Color(inputImage.getRGB(xx - 1, y), true)

    val rx = inputColorX1.red - inputColorX2.red
    val gx = inputColorX1.green - inputColorX2.green
    val bx = inputColorX1.blue - inputColorX2.blue

    return (rx * rx) + (gx * gx) + (bx * bx)
}

private fun deltaYSquared(inputImage: BufferedImage, x: Int, y: Int): Int {
    val height = inputImage.height

    val yy = when (y) {
        0 -> 1
        height - 1 -> height - 2
        else -> y
    }

    val inputPixelY1 = Color(inputImage.getRGB(x, yy + 1), true)
    val inputPixelY2 = Color(inputImage.getRGB(x, yy - 1), true)

    val ry = inputPixelY1.red - inputPixelY2.red
    val gy = inputPixelY1.green - inputPixelY2.green
    val by = inputPixelY1.blue - inputPixelY2.blue

    return (ry * ry) + (gy * gy) + (by * by)
}

private fun negativeImage(inputImage: BufferedImage): BufferedImage {
    val width = inputImage.width
    val height = inputImage.height

    val outputImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

    for (x in 0 until width) {
        for (y in 0 until height) {
            val inputPixel = inputImage.getRGB(x, y)
            var inputColor = Color(inputPixel, true)
            var r = 255 - inputColor.red
            var g = 255 - inputColor.green
            var b = 255 - inputColor.blue
            val outputColor = Color(r, g, b)
            outputImage.setRGB(x, y, outputColor.rgb)
        }
    }

    return outputImage
}

private fun readImage(inputFileName: String): BufferedImage =
        ImageIO.read(File(inputFileName))

private fun saveImage(outputFileName: String, image: BufferedImage) =
        ImageIO.write(image, "png", File(outputFileName))

private fun createImage(width: Int, height: Int): BufferedImage {
    val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

    val graphics : Graphics2D = bufferedImage.createGraphics()

    graphics.paint = Color.BLACK
    graphics.fillRect(0, 0, bufferedImage.width, bufferedImage.height)
    graphics.paint = Color.RED
    graphics.drawLine(0, 0, bufferedImage.width - 1, bufferedImage.height - 1)
    graphics.drawLine(0, bufferedImage.height - 1, bufferedImage.width - 1, 0)

    return bufferedImage
}