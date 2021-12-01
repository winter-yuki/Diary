package diary.ui.tabs.pdf

import diary.ui.AbstractLink
import diary.ui.TabManager
import diary.ui.tabs.Tab
import java.nio.file.Path

data class PdfLink(
    val path: Path,
    val page: Int? = null
) : AbstractLink() {

    override val id: Tab.Id by lazy { Tab.Id(path) }

    override fun navigate(tabManager: TabManager) = navigateOrCreate(tabManager) {
        PdfTab.from(
            path,
            tabManager = tabManager,
            currPage = page ?: 0
        )
    }
}
