package seamcarving.util

import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.sqrt

object Converter {
    fun convert(inputFileName: String, outputFileName: String,
                transformationType: TransformationType) {

        val transformation = when (transformationType) {
            TransformationType.NEGATIVE -> BufferedImage::negative
            TransformationType.ENERGY -> BufferedImage::energy
            TransformationType.ADD_VERTICAL_SEAM -> BufferedImage::addVerticalSeam
            TransformationType.ADD_HORIZONTAL_SEAM -> BufferedImage::addHorizontalSeam
        }

        process(inputFileName, outputFileName, transformation)
    }

    private fun process(inputFileName: String, outputFileName: String,
                        transformation: BufferedImage.() -> BufferedImage) {

        val image = readImage(inputFileName)

        val processedImage = image.transformation()

        saveImage(outputFileName, processedImage)
    }

    private fun readImage(inputFileName: String): BufferedImage {
        val inputFile = File(inputFileName)

        require(inputFile.exists() && inputFile.canRead()) { "Can't read input file!" }

        return ImageIO.read(inputFile)
    }

    private fun saveImage(outputFileName: String, image: BufferedImage) {
        val outputFile = File(outputFileName)

        ImageIO.write(image, "png", outputFile)
    }
}