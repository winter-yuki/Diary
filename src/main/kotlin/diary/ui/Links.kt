package diary.ui

import diary.ui.tabs.Tab

interface Link {
    val id: Tab.Id
    fun navigate(tabManager: TabManager)
}

abstract class AbstractLink : Link {
    protected fun navigateOrCreate(tabManager: TabManager, createTab: () -> Tab) {
        val tab = tabManager[id]
        if (tab != null) {
            tab.navigate(this)
        } else {
            val newTab = createTab()
//            tabManager.add(newTab) TODO
            newTab.navigate(this)
        }
    }
}

class LinkBuffer(var link: Link? = null)
