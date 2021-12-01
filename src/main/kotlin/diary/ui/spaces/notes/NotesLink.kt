package diary.ui.spaces.notes

import diary.ui.Link
import diary.ui.Tab
import diary.ui.WorkSpace
import java.nio.file.Path

data class NotesLink(
    val path: Path,
    val cellName: CellName? = null
) : Link {
    override fun jump(workSpace: WorkSpace) {
        val notes = NotesSpace.from(path, workSpace)
        workSpace.add(Tab(notes))
        // TODO navigate to cell
    }
}
