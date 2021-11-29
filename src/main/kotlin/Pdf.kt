import java.nio.file.Path

interface Link {
    fun jump()
}

class PdfLink private constructor(private val path: Path) : Link {
    override fun jump() {
        TODO("Jump to PDF is not implemented")
    }

    companion object {
        fun of(path: Path) = PdfLink(path)
    }
}

class CellLink : Link {
    override fun jump() {
        TODO("Not yet implemented")
    }
}
