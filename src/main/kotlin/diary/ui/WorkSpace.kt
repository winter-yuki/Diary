package diary.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import diary.ui.spaces.Space
import diary.utils.forEachIndexedCo
import diary.utils.makeAlertDialog

class WorkSpace(
    private val spaces: MutableList<Space> = mutableStateListOf()
) : UIComponent {

    val linkBuffer = LinkBuffer()
    private var tooManyTabsDialog = mutableStateOf(false)
    private val maxNTabs = 4

    fun add(space: Space) {
        if (spaces.size < 4) {
            spaces += space
        } else {
            tooManyTabsDialog.value = true
        }
    }

    operator fun get(id: Space.Id): Space? =
        spaces.find { it.id == id }

    @Composable
    override operator fun invoke() {
        makeAlertDialog(
            title = "Too many tabs opened",
            text = "No more then $maxNTabs can be opened at once",
            state = tooManyTabsDialog
        )
        if (spaces.isEmpty()) {
            EmptyTab()
            return
        }
        Row {
            spaces.forEachIndexedCo { i, tab ->
                val fraction = 1F / (spaces.size - i)
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
