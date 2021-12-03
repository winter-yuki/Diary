package diary.ui.tabs.pdf

import androidx.compose.runtime.Composable
import diary.ui.AbstractLink
import diary.ui.TabManager
import diary.ui.tabs.TabId
import java.nio.file.Path

data class PdfLink(
    val path: Path,
    val page: Int,
    override val id: TabId
) : AbstractLink() {

    @Composable
    override fun navigate(tabManager: TabManager) = navigateOrCreate(tabManager) {
        PdfTab.from(
            path,
            tabManager = tabManager,
            currPage = page
        )
    }
}
