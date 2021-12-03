package diary.ui.tabs.notes.cells

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import diary.ui.TabManager
import diary.ui.tabs.notes.NotesLink
import java.nio.file.Path
import kotlin.io.path.writeText

class RenderedTextCell(
    val textCell: TextCell,
    private val notesPath: Path?,
    private val tabManager: TabManager
) : AbstractCell() {

    override var name: CellName
        get() = textCell.name
        set(value) {
            textCell.name = value
        }

    private val text by textCell.text

    override fun save(path: Path) {
        // TODO
        path.writeText(text)
    }

    @Composable
    override fun invoke() = cell {
        val lt = linkedText()
        Column {
            var targetName by remember { mutableStateOf<Pair<String?, Int>>(null to 0) }
            ClickableText(
                modifier = Modifier.fillMaxWidth(),
                text = lt
            ) { offset ->
                // TODO
                val linkAnnotation = lt
                    .getStringAnnotations(start = offset, end = offset)
                    .lastOrNull()?.item
                targetName = linkAnnotation?.substring(1) to targetName.second.plus(1)
            }
            targetName.first?.let {
                println("Navigate 1")
                NotesLink(
                    id = notesPath,
                    path = notesPath ?: Path.of(""),
                    cellName = CellName(it)
                ).navigate(tabManager)
            }
        }
    }

    private fun linkedText() = buildAnnotatedString {
        val linkRegex = Regex("""#[a-zA-Z]+\S""")
        val matches = linkRegex.findAll(text)
        var cursor = 0
        for (match in matches) {
            append(text.substring(cursor until match.range.first))
            pushStringAnnotation(tag = match.value, annotation = match.value)
            withStyle(style = SpanStyle(color = Color.Blue, fontWeight = FontWeight.Bold)) {
                append(match.value)
            }
            cursor = match.range.last + 1
        }
        append(text.substring(cursor))
    }
}
