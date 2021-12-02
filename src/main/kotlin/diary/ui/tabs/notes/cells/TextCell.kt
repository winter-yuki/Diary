package diary.ui.tabs.notes.cells

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import java.nio.file.Path
import kotlin.io.path.writeText

class TextCell(
    override var name: CellName = CellName(""),
    var text: MutableState<String> = mutableStateOf("")
) : AbstractCell() {

    constructor(text: String) : this(text = mutableStateOf(text))

    override fun save(path: Path) {
        // TODO
        path.writeText(text.value)
    }

    @Composable
    override fun invoke() = cell {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            value = text.value,
            singleLine = false,
            onValueChange = {
                text.value = it
            }
        )
    }
}
