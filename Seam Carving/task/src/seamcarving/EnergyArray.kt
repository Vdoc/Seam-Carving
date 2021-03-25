package seamcarving.util

import java.lang.Math.max
import java.lang.Math.min

data class EnergyArray(val width: Int, val height: Int) {
    private val array = Array(height) { DoubleArray(width) }

    var maxEnergy: Double = 0.0
        private set

    operator fun get(x: Int, y: Int): Double {
        require(x in 0 until width && y in 0 until height) {  "Illegal coordinate ($x, $y)!" }

        return array[y][x]
    }

    operator fun set(x: Int, y: Int, energy: Double) {
        require(x in 0 until width && y in 0 until height) {  "Illegal coordinate ($x, $y)!" }

        array[y][x] = energy
        maxEnergy = if (energy > maxEnergy) energy else maxEnergy
    }

    val verticalSeam
        get(): Seam {
            val seam = Seam(width, height)

            // based on (implementation in Python):
            // https://avikdas.com/2019/05/14/real-world-dynamic-programming-seam-carving.html

            // as is it needs some refactoring, but for now it works :-)

            val seamEnergies = mutableListOf<List<EnergyPoint>>()
            var seamEnergiesRow = mutableListOf<EnergyPoint>()

            for (x in 0 until width) {
                seamEnergiesRow.add(EnergyPoint(x, 0, array[0][x]))
            }

            seamEnergies.add(seamEnergiesRow)

            for (y in 1 until height) {
                val rowEnergies = array[y]

                seamEnergiesRow = mutableListOf<EnergyPoint>()

                for (x in 0 until width) {
                    val xLeft = (x - 1).coerceAtLeast(0)
                    val xRight = (x + 1).coerceAtMost(width - 1)

                    var minParentX = xLeft
                    var minSeamEnergy = seamEnergies[y - 1][minParentX].energy

                    for (i in xLeft + 1 .. xRight) {
                        if (seamEnergies[y - 1][i].energy < minSeamEnergy) {
                            minParentX = i
                            minSeamEnergy = seamEnergies[y - 1][i].energy
                        }
                    }

                    seamEnergiesRow.add(EnergyPoint(x, y, rowEnergies[x] + minSeamEnergy, minParentX))
                }

                seamEnergies.add(seamEnergiesRow)
            }

            var minSeamEndX = 0
            var minSeamEndEnergy = seamEnergies[height - 1][0].energy
            for (x in 1 until width) {
                if (seamEnergies[height - 1][x].energy < minSeamEndEnergy) {
                    minSeamEndX = x
                    minSeamEndEnergy = seamEnergies[height - 1][x].energy
                }
            }

            val path = mutableListOf<EnergyPoint>()
            var seamPointX: Int? = minSeamEndX
            for (y in height - 1 downTo 0) {
                if (seamPointX != null) {
                    val energyPoint = seamEnergies[y][seamPointX]
                    path.add(EnergyPoint(energyPoint.x, energyPoint.y, energyPoint.energy, energyPoint.previousX))
                    seamPointX = energyPoint.previousX
                }
            }

            for (point in path.reversed()) {
                seam.add(point)
            }

            return seam
        }
}