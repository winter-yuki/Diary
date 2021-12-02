package diary.ui.tabs.notes.cells

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.unit.dp
import diary.utils.ui.DrawCanvas
import org.jetbrains.skia.Image
import java.nio.file.Path
import kotlin.io.path.readBytes

class SketchCell(
    override var name: CellName = CellName(""),
    private val pathState: MutableState<androidx.compose.ui.graphics.Path> =
        mutableStateOf(androidx.compose.ui.graphics.Path()),
    backgroundImage: Path? = null
) : AbstractCell() {

    private val bitmap = backgroundImage?.let { path ->
        val bytes = path.readBytes()
        val image = Image.makeFromEncoded(bytes)
        image.toComposeImageBitmap()
    }

    override fun save(path: Path) {
        println("Save sketch $path") // TODO
    }

    @Composable
    override operator fun invoke() = cell {
        val path = pathState // TODO helps?
        DrawCanvas(path, modifier = Modifier.height(250.dp)) // TODO test mod
    }
}
