package diary.ui.spaces.pdf

import diary.ui.Link
import diary.ui.Tab
import diary.ui.WorkSpace
import java.nio.file.Path

data class PdfLink(
    val path: Path,
    val page: Int? = null
) : Link {
    override fun jump(workSpace: WorkSpace) {
        val pdf = PdfSpace.from(
            path,
            workSpace = workSpace,
            currPage = page ?: 0
        )
        workSpace.add(Tab(pdf))
    }
}
