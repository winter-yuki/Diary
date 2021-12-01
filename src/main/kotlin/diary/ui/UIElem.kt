package diary.ui

import androidx.compose.runtime.Composable

interface UIElem {
    @Composable
    operator fun invoke()
}
