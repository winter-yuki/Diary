package diary.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import diary.ui.tabs.Tab

class TabManager(val tabs: SnapshotStateList<Tab> = mutableStateListOf()) {

    val linkBuffer = LinkBuffer() // TODO remember
    val maxNTabs = 3

    fun add(tab: Tab, onTooManyTabs: () -> Unit) {
        if (tabs.size < maxNTabs) {
            tabs += tab
        } else {
            onTooManyTabs()
        }
    }

    operator fun get(id: Tab.Id): Tab? = tabs.find { it.id == id }
}

@Composable
fun rememberTabManager(
    tabs: SnapshotStateList<Tab> = mutableStateListOf()
) = remember(tabs) {
    TabManager(tabs)
}
