package diary.ui.tabs.notes.cells

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import java.nio.file.Path

class CellName(val name: String)

interface Cell {

    var name: CellName
    fun save(path: Path)

    @Composable
    operator fun invoke()

    companion object {
        fun of(path: Path): Cell {
            TODO()
        }
    }
}

abstract class AbstractCell : Cell {

    @Composable
    protected fun cell(block: @Composable () -> Unit) {
        Surface(shape = MaterialTheme.shapes.medium, elevation = 2.dp) {
            block()
        }
    }
}
