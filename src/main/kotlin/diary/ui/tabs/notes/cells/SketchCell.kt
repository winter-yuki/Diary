package diary.ui.tabs.notes.cells

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.unit.dp
import diary.utils.ui.DrawCanvas
import diary.utils.ui.chooseSize
import org.jetbrains.skia.Image
import kotlin.io.path.readBytes
import java.nio.file.Path as FilePath

class SketchCell(
    override var name: CellName = CellName(""),
    private val pathState: MutableState<Path> = mutableStateOf(Path()),
    backgroundImage: FilePath? = null
) : AbstractCell() {

    private val bitmap = backgroundImage?.let { path ->
        val bytes = path.readBytes()
        val image = Image.makeFromEncoded(bytes)
        image.toComposeImageBitmap()
    }

    override fun save(path: FilePath) {
        println("Save sketch $path") // TODO
    }

    @Composable
    override operator fun invoke() = cell {
        val path = pathState // TODO remember helps?
        DrawCanvas(
            path,
            bitmap = bitmap,
            modifier = Modifier
                .height(250.dp)
                .chooseSize(bitmap)
                .fillMaxWidth()
        )
    }
}
