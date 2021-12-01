package diary.ui

import diary.ui.tabs.Tab

interface Link {
    val id: Tab.Id
    fun navigate(tabManager: TabManager)
}

abstract class AbstractLink : Link {
    protected fun navigateOrCreate(tabManager: TabManager, block: () -> Tab) {
        val tab = tabManager[id]
        if (tab != null) {
            tab.navigate(this)
        } else {
            val newTab = block()
            tabManager.add(newTab)
            newTab.navigate(this)
        }
    }
}

class LinkBuffer(var link: Link? = null)
