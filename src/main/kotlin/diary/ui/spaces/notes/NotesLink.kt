package diary.ui.spaces.notes

import diary.ui.AbstractLink
import diary.ui.WorkSpace
import diary.ui.spaces.Space
import java.nio.file.Path

data class NotesLink(
    val path: Path,
    val cellName: CellName? = null
) : AbstractLink() {

    override val id: Space.Id by lazy { Space.Id(path) }

    override fun navigate(workSpace: WorkSpace) = navigateOrCreate(workSpace) {
        NotesSpace.from(path, workSpace)
    }
}
