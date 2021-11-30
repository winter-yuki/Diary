import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

interface Tab : UIElem {
    @Composable
    override operator fun invoke()
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

class TabManager(private val tabs: SnapshotStateList<Tab> = mutableStateListOf()) {

    constructor(vararg tabs: Tab) : this(tabs.toMutableList().toMutableStateList())

    fun add(tab: Tab) {
        tabs += tab
    }

    @Composable
    operator fun invoke() {
        if (tabs.isEmpty()) {
            EmptyTab()
            return
        }
        Row {
            tabs.forEachIndexedCo { i, tab ->
                val fraction = 1F / (tabs.size - i)
                Box(modifier = Modifier.fillMaxWidth(fraction)) {
                    tab()
                }
            }
        }
    }

    @Composable
    private fun EmptyTab() {
        Box(Modifier.fillMaxSize()) {
            Column(Modifier.align(Alignment.Center)) {
                Text("Welcome!", fontSize = 20.sp)
                Text("Press New to create new notes \nor Open to open existing notes of pdf")
            }
        }
    }
}
