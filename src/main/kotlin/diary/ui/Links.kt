package diary.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import diary.ui.tabs.Tab
import diary.ui.tabs.TabId

interface Link {

    val id: TabId

    @Composable
    fun navigate(tabManager: TabManager)
}

abstract class AbstractLink : Link {

    @Composable
    protected fun navigateOrCreate(
        tabManager: TabManager,
        createTab: () -> Tab
    ) {
        val tab = tabManager[id]
        if (tab != null) {
            tab.navigate(this)
        } else {
            val newTab = createTab()
            var tooManyTabs by TooManyTabsAlert(tabManager)
            tabManager.add(newTab) { tooManyTabs = true }
            newTab.navigate(this)
        }
    }
}
