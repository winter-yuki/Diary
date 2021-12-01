import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.nio.file.Path

class Notes(private val cells: SnapshotStateList<Cell>) {

    constructor(vararg cells: Cell) : this(cells.toMutableList())
    constructor(cells: Iterable<Cell>) : this(cells.toMutableList())
    constructor(cells: MutableList<Cell>) : this(cells.toMutableStateList())

    @Composable
    operator fun invoke() {
        if (cells.isEmpty()) {
            cells += TextCell()
        }
        Box(modifier = Modifier.fillMaxSize().padding(10.dp)) {
            val state = rememberLazyListState()
            Column {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(end = 12.dp),
                    state = state
                ) {
                    itemsIndexed(cells) { i, cell ->
                        CellBox(i) { cell() }
                    }
                }
            }
            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                adapter = rememberScrollbarAdapter(
                    scrollState = state
                )
            )
        }

        // TODO move cell up/down buttons #3
        // TODO show buttons only on focus #4
    }

    @Composable
    private fun CellBox(iCell: Int, block: @Composable () -> Unit) {
        Row(
            modifier = Modifier.fillMaxWidth().fillMaxHeight()
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().fillMaxHeight()
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .fillMaxWidth(0.75F)
                ) {
                    Button(
                        modifier = Modifier
                            .padding(5.dp)
                            .size(width = 30.dp, height = 25.dp)
                            .align(Alignment.End),
                        onClick = { cells.removeAt(iCell) }
                    ) {}

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(modifier = Modifier.align(Alignment.CenterVertically)) {
                            Text((iCell + 1).toString() + ".")
                        }
                        block()
                    }


                    Row(
                        modifier = Modifier.padding(start = 16.dp)

                    ) {
                        val size = 10.sp
                        Button(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(10.dp),
                            onClick = { cells.add(iCell, TextCell()) },
                        ) {
                            Text("Add text", fontSize = size)
                        }
                        Button(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(10.dp),
                            onClick = { cells.add(iCell, SketchCell()) },
                        ) {
                            Text("Add sketch", fontSize = size)
                        }
                    }
                }
            }
        }
    }

    fun save(path: Path) {
        TODO() // #12
    }

    companion object {
        fun from(path: Path): Notes {
            // TODO
            return Notes()
        }
    }
}
