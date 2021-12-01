package diary.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import diary.ui.tabs.Tab
import diary.utils.forEachIndexedCo
import diary.utils.makeAlertDialog

class TabManager(
    private val tabs: MutableList<Tab> = mutableStateListOf()
) : UIComponent {

    val linkBuffer = LinkBuffer()
    private var tooManyTabsDialog = mutableStateOf(false)
    private val maxNTabs = 4

    fun add(tab: Tab) {
        if (tabs.size < maxNTabs) {
            tabs += tab
        } else {
            tooManyTabsDialog.value = true
        }
    }

    operator fun get(id: Tab.Id): Tab? =
        tabs.find { it.id == id }

    @Composable
    override operator fun invoke() {
        makeAlertDialog(
            title = "Too many tabs opened",
            text = "No more then $maxNTabs can be opened at once",
            state = tooManyTabsDialog
        )
        if (tabs.isEmpty()) {
            EmptyTab()
            return
        }
        Row {
            tabs.forEachIndexedCo { i, tab ->
                val fraction = 1F / (tabs.size - i)
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction)
                        .padding(5.dp)
                        .border(
                            border = BorderStroke(1.dp, MaterialTheme.colors.primary.copy(alpha = 0.2f)),
                            shape = RoundedCornerShape(1)
                        )
                ) {
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
