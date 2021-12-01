package diary.ui

interface Link {
    fun jump(workSpace: WorkSpace)
}

class LinkBuffer(var link: Link? = null)


//
//class PdfLink private constructor(
//    private val path: Path, private val anchor: Nothing
//) : Link {
//    override fun jump() {
//        TODO("Jump to PDF is not implemented") // #17
//    }
//
//    companion object {
//        fun of(path: Path) = PdfLink(path, TODO())
//    }
//}
//
//class CellLink : Link {
//    override fun jump() {
//        TODO("Not yet implemented") // #18
//    }
//}
