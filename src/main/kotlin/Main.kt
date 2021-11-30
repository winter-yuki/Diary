import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

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

    @Composable
    operator fun invoke() {
        Row {
            tabs.forEachCo { it() }
        }
    }
}

fun main() = application {
    val notes = Notes(
        List(10) { i ->
            if (i % 2 == 0) TextCell()
            else SketchCell()
        }
    )

    Window(title = "Notes", onCloseRequest = ::exitApplication) {
        DesktopMaterialTheme {
            // TODO context menu: save notes, load notes #13
            // TODO main menu #14
            // TODO double notes screen  #15
            // TODO drug-n-drop cells from one screen to another #16
            TabManager(
                NotesTab(notes),
                NotesTab(notes)
            )()
        }
    }
}
