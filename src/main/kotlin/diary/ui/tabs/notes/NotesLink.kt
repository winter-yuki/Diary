package diary.ui.tabs.notes

import diary.ui.AbstractLink
import diary.ui.TabManager
import diary.ui.tabs.Tab
import diary.ui.tabs.notes.cells.CellName
import java.nio.file.Path

data class NotesLink(
    val path: Path,
    val cellName: CellName? = null
) : AbstractLink() {

    override val id: Tab.Id by lazy { Tab.Id(path) }

    override fun navigate(tabManager: TabManager) = navigateOrCreate(tabManager) {
        NotesTab.from(path, tabManager)
    }
}
