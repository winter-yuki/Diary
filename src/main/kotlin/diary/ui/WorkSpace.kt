package diary.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import diary.ui.tabs.Tab
import diary.utils.ui.forEachIndexedCo
import diary.utils.ui.makeAlertDialogStateful

@Composable
fun WorkSpace(tabManager: TabManager) {
    if (tabManager.tabs.isEmpty()) {
        Empty()
        return
    }
    Row(modifier = Modifier.padding(start = 5.dp, end = 5.dp, bottom = 5.dp)) {
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
        text = "No more than ${tabManager.maxNTabs} can be opened at once",
    )

@Composable
private fun Empty() {
    Box(Modifier.fillMaxSize()) {
        Column(Modifier.align(Alignment.Center)) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Welcome!", fontSize = 60.sp, textAlign = TextAlign.Center)
            }
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    buildAnnotatedString {
                        append(
                            "Press "
                        )
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("New")
                        }
                        append(
                            " to create new notes \nor "
                        )
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Open")
                        }
                        append(
                            " to open existing notes of pdf"
                        )
                    },
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun ColumnScope.RemoveButton(tab: Tab, onRemove: (Tab) -> Unit) {
    Button(
        onClick = { onRemove(tab) },
        modifier = Modifier
            .align(Alignment.End)
            .height(25.dp)
    ) {
        Text("Close tab", fontSize = 8.sp)
    }
}

@Composable
fun TabBox(tab: Tab) {
    val border = BorderStroke(
        1.dp,
        MaterialTheme
            .colors.primary
            .copy(alpha = 0.2f)
    )
    Surface(shape = MaterialTheme.shapes.large, elevation = 5.dp) {
        Box(
            modifier = Modifier
                .border(border)
        ) {
            tab()
        }
    }
}
