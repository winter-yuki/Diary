package diary.ui.tabs.notes

import androidx.compose.runtime.Composable
import diary.ui.AbstractLink
import diary.ui.TabManager
import diary.ui.tabs.TabId
import diary.ui.tabs.notes.cells.CellName
import java.nio.file.Path

data class NotesLink(
    val path: Path,
    val cellName: CellName,
    override val id: TabId
) : AbstractLink() {

    @Composable
    override fun navigate(tabManager: TabManager): Unit =
        navigateOrCreate(tabManager) {
            NotesTab.from(path, tabManager)
        }
}
