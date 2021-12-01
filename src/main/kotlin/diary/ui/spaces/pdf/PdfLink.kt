package diary.ui.spaces.pdf

import diary.ui.AbstractLink
import diary.ui.WorkSpace
import diary.ui.spaces.Space
import java.nio.file.Path

data class PdfLink(
    val path: Path,
    val page: Int? = null
) : AbstractLink() {

    override val id: Space.Id by lazy { Space.Id(path) }

    override fun navigate(workSpace: WorkSpace) = navigateOrCreate(workSpace) {
        PdfSpace.from(
            path,
            workSpace = workSpace,
            currPage = page ?: 0
        )
    }
}
