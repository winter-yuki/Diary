import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier

interface Tab : UIElem {
    @Composable
    override operator fun invoke()
}

class EmptyTab : Tab {
    @Composable
    override operator fun invoke() {
        TODO("Not yet implemented")
    }
}

// TODO delegate
class NotesTab(val notes: Notes) : Tab {
    @Composable
    override operator fun invoke() = notes()
}

class PdfTab : Tab {
    @Composable
    override operator fun invoke() {
        TODO("Not yet implemented")
    }
}

class TabManager(private val tabs: SnapshotStateList<Tab> = mutableStateListOf(EmptyTab())) {

    constructor(vararg tabs: Tab) : this(tabs.toMutableList().toMutableStateList())

    fun add(tab: Tab) {
        tabs += tab
    }

    @Composable
    operator fun invoke() {
        Row {
            tabs.forEachIndexedCo { i, tab ->
                val fraction = 1F / (tabs.size - i)
                Box(modifier = Modifier.fillMaxWidth(fraction)) {
                    tab()
                }
            }
        }
    }
}
