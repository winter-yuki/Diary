package diary.ui

import androidx.compose.runtime.Composable

interface UIComponent {
    @Composable
    operator fun invoke()
}
