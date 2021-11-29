import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import java.nio.file.Path

class Notes(private val cells: List<Cell> = listOf()) {

    constructor(vararg cells: Cell) : this(cells.toList())

    @Composable
    operator fun invoke() {
        LazyColumn {
            items(cells) { it() }
        }
        // TODO add cell button
        // TODO move cell up button
        // TODO move cell down button
        // TODO show buttons only on focus
    }

    fun save(path: Path) {
        TODO()
    }

    companion object {
        fun from(path: Path): Notes {
            TODO()
        }
    }
}
