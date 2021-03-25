package seamcarving.util

data class Seam(val width: Int, val height: Int) : Iterable<EnergyPoint> {
    private val points = mutableListOf<EnergyPoint>()

    fun add(x: Int, y: Int, energy: Double, previousX: Int? = null) {
        require(x in 0 until width && y in 0 until height && energy >= 0.0) {
            "Illegal coordinate ($x, $y)!"
        }

        points.add(EnergyPoint(x, y, energy, previousX))
    }

    fun add(point: EnergyPoint) = add(point.x, point.y, point.energy, point.previousX)

    override fun iterator(): Iterator<EnergyPoint> = object: Iterator<EnergyPoint> {
        private val pointsIterator = points.iterator()

        override fun hasNext() = pointsIterator.hasNext()

        override fun next() = pointsIterator.next()
    }
}