package diary.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import diary.utils.forEachIndexedCo
import diary.utils.makeAlertDialog

class Tab(private val space: UIComponent) : UIComponent by space

class WorkSpace(
    private val tabs: MutableList<Tab> = mutableStateListOf()
) : UIComponent {

    val linkBuffer = LinkBuffer()
    private var tooManyTabsDialog = mutableStateOf(false)
    private val maxNTabs = 4

    fun add(tab: Tab) {
        if (tabs.size < 4) {
            tabs += tab
        } else {
            tooManyTabsDialog.value = true
        }
    }

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
