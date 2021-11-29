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
    fun save(path: Path)

    fun load(path: Path): Cell

    @Composable
    operator fun invoke(notes: Notes)

    @Composable
    fun cell(notes: Notes, block: @Composable () -> Unit) {
        // TODO border rectangle #5
        // TODO remove cell button #2
        Row(modifier = Modifier.padding(8.dp)) {
            Surface(shape = MaterialTheme.shapes.large, elevation = 2.dp) {
                block()
            }
        }
    }

    companion object {
        fun of(path: Path): Cell {
            TODO()
        }
    }
}

class TextCell : Cell {
    override fun save(path: Path) {
        TODO("Not yet implemented") // #7
    }

    override fun load(path: Path): TextCell {
        TODO("Not yet implemented") // #7
    }

    @Composable
    override operator fun invoke(notes: Notes) = cell(notes) {
        // TODO make text mutable #6
        Text("Text cell")
    }
}

class SketchCell : Cell {
    override fun save(path: Path) {
        TODO("Not yet implemented") // #8
    }

    override fun load(path: Path): SketchCell {
        TODO("Not yet implemented") // #8
    }

    @Composable
    override operator fun invoke(notes: Notes) = cell(notes) {
        // TODO resize cell bar appearing on focus #9
        // TODO use canvas with drawing possibility #10
        // TODO clickable link: click -> jump #11
        Text("Sketch cell")
    }
}
