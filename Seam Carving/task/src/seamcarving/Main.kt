package seamcarving

import java.awt.Color
import java.io.File
import javax.imageio.ImageIO

fun main(args: Array<String>) {

    /* Onur Sına
    val img = ImageIO.read(File(args[args.indexOf("-in") + 1]))
    for (x in 0 until img.width) for (y in 0 until img.height) img.setRGB(x, y, 16777215 - img.getRGB(x, y))
    ImageIO.write(img, "png", File(args[args.indexOf("-out") + 1]))
    */

    val inFile = File(args[args.indexOf("-in") + 1])
//    var file = File(args[1])
//    val image = ImageIO.read(file)
    val outFile = File(args[args.indexOf("-out") + 1])

    val bufferedImage = ImageIO.read(inFile)
    val W_MAX = bufferedImage.width - 1
    val H_MAX = bufferedImage.height - 1
//    val width = image.width
//    val height = image.height

    for (i in 0..W_MAX) {
        for (j in 0..H_MAX) {
//    for (w in 0 until width) {
//        for (h in 0 until height) {
            val color = Color(bufferedImage.getRGB(i, j))
            bufferedImage.setRGB(i, j, color.negative().rgb)
//            val pixel: Int = image.getRGB(w, h)
//            val color = Color(pixel, true)
//            val red = 255 - color.red
//            val green = 255 - color.green
//            val blue = 255 - color.blue
//            val rgb= Color(red, green, blue).rgb
//            image.setRGB(w, h, rgb)
        }
    }

    ImageIO.write(bufferedImage, "png", outFile)
//    file = File(args[3])
//    ImageIO.write(image, "png", file)
}

fun Color.negative() = Color(255 - red, 255 - green, 255 - blue)



