import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.nio.file.Path

class Notes(private val cells: SnapshotStateList<Cell>) {

    constructor(vararg cells: Cell) : this(cells.toMutableList())
    constructor(cells: Iterable<Cell>) : this(cells.toMutableList())
    constructor(cells: MutableList<Cell>) : this(cells.toMutableStateList())

    @Composable
    operator fun invoke() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(0.75F),
            ) {
                itemsIndexed(cells) { i, cell ->
                    CellBox(i) { cell() }
                }
            }
            Row {
                Button(
                    modifier = Modifier
                        .wrapContentHeight()
                        .wrapContentWidth()
                        .height(50.dp)
                        .padding(10.dp),
                    onClick = { cells += TextCell() },
                ) {
                    Text("Add text")
                }
                Button(
                    modifier = Modifier
                        .wrapContentHeight()
                        .wrapContentWidth()
                        .height(50.dp)
                        .padding(10.dp),
                    onClick = { cells += SketchCell() },
                ) {
                    Text("Add scratch")
                }
            }
        }

        // TODO move cell up/down buttons #3
        // TODO show buttons only on focus #4
    }

    @Composable
    private fun CellBox(iCell: Int, block: @Composable () -> Unit) {
        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            Button(
                modifier = Modifier
                    .size(width = 30.dp, height = 25.dp),
                onClick = { cells.removeAt(iCell) }
            ) {}
            block()
        }
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
