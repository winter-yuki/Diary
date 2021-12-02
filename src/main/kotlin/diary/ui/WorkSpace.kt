package diary.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import diary.ui.tabs.Tab
import diary.utils.forEachIndexedCo
import diary.utils.makeAlertDialogStateful

@Composable
fun WorkSpace(tabManager: TabManager) {
    TooManyTabsAlert(tabManager)
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
                TabBox(tab)
            }
        }
    }
}

@Composable
fun TooManyTabsAlert(tabManager: TabManager) =
    makeAlertDialogStateful(
        title = "Too many tabs opened",
        text = "No more then ${tabManager.maxNTabs} can be opened at once",
    )

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
fun TabBox(tab: Tab) {
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
        tab()
    }
}
