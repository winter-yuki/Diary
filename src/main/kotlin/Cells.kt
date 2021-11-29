import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.nio.file.Path

interface Cell {
    fun save(path: Path) {
        TODO()
    }

    @Composable
    operator fun invoke()

    @Composable
    fun cell(block: @Composable () -> Unit) {
        // TODO border rectangle
        Row(modifier = Modifier.padding(8.dp)) {
            Surface(shape = MaterialTheme.shapes.large, elevation = 2.dp) {
                block()
            }
        }
    }
}

class TextCell : Cell {
    @Composable
    override operator fun invoke() = cell {
        // TODO make text mutable
        Text("Text cell")
    }
}

class SketchCell : Cell {
    @Composable
    override operator fun invoke() = cell {
        // TODO resize cell bar appearing on focus
        // TODO use canvas
        // TODO implement drawing
        // TODO clickable link: click -> jump
        Text("Sketch cell")
    }
}
