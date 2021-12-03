package diary.ui.tabs

import androidx.compose.runtime.Composable
import diary.ui.Link
import java.util.*

typealias TabId = UUID

interface Tab {

    val id: TabId

    @Composable
    fun navigate(link: Link)

    @Composable
    operator fun invoke()
}
