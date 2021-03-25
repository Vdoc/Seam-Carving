package seamcarving

import seamcarving.util.Converter
import seamcarving.util.TransformationType

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
        Converter.convert(inputFileName, outputFileName, TransformationType.ADD_HORIZONTAL_SEAM)
    } catch (exception: Exception) {
        exception.printStackTrace()
    }
}