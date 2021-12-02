package diary.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import diary.ui.tabs.Tab
import diary.utils.AlertDialog
import diary.utils.forEachIndexedCo

class TabManager(val tabs: SnapshotStateList<Tab> = mutableStateListOf()) {

    val linkBuffer = LinkBuffer() // TODO remember
    val maxNTabs = 3

    fun add(tab: Tab, onTooManyTabs: () -> Unit = {}) {
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

@Composable
fun WorkSpace(tabManager: TabManager) {
    val tooManyTabsDialog = remember { mutableStateOf(false) }
    AlertDialog(
        title = "Too many tabs opened",
        text = "No more then ${tabManager.maxNTabs} can be opened at once",
        open = tooManyTabsDialog
    )
    if (tabManager.tabs.isEmpty()) {
        Empty()
        return
    }
    Row {
        tabManager.tabs.forEachIndexedCo { i, tab ->
            val fraction = 1F / (tabManager.tabs.size - i)
            Column(
                modifier = Modifier
                    .fillMaxWidth(fraction)
                    .fillMaxHeight()
                    .padding(5.dp)
            ) {
                RemoveButton(tab) { tabManager.tabs.remove(tab) }
                TabBox {
                    tab()
                }
            }
        }
    }
}

@Composable
private fun Empty() {
    Box(Modifier.fillMaxSize()) {
        Column(Modifier.align(Alignment.Center)) {
            Text("Welcome!", fontSize = 20.sp)
            Text("Press New to create new notes \nor Open to open existing notes of pdf")
        }
    }
}

@Composable
fun ColumnScope.RemoveButton(tab: Tab, onRemove: (Tab) -> Unit) {
    TextButton(
        onClick = { onRemove(tab) },
        modifier = Modifier
            .align(Alignment.End)
            .padding(bottom = 5.dp)
            .border(
                BorderStroke(
                    1.dp, MaterialTheme.colors.primary.copy(alpha = 0.2f)
                )
            )
    ) {
        Text("X")
    }
}

@Composable
fun TabBox(block: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .border(
                border = BorderStroke(
                    1.dp, MaterialTheme.colors.primary.copy(alpha = 0.2f)
                ),
                shape = RoundedCornerShape(1)
            )
            .padding(5.dp)
    ) {
        block()
    }
}
