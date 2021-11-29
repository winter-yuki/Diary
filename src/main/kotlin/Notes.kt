import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import java.nio.file.Path

class Notes(private val cells: List<Cell> = listOf()) {

    constructor(vararg cells: Cell) : this(cells.toList())

    @Composable
    operator fun invoke() {
        LazyColumn {
            items(cells) { it(this@Notes) }
        }
        // TODO add cell button #1
        // TODO move cell up/down buttons #3
        // TODO show buttons only on focus #4
    }

    fun save(path: Path) {
        TODO() // #12
    }

    companion object {
        fun from(path: Path): Notes {
            TODO()
        }
    }
}
