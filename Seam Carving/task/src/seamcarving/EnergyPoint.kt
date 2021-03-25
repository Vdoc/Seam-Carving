package seamcarving.util

data class EnergyPoint(
        val x: Int, val y: Int,
        val energy: Double,
        val previousX: Int? = null
)