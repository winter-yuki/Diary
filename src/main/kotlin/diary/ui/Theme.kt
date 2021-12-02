package diary.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val Navy500 = Color(0xFF64869B)

val Navy700 = Color(0xFF37596D)
//val Navy700 = Color(0xffd11aff)

//val Navy900 = Color(0xFF073042)
val Navy900 = Color(0xfff0b3ff)

val Green300 = Color(0xFF3DDC84)
val Green900 = Color(0xFF00A956)

val LightColors = lightColors(
    primary = Navy700,
    primaryVariant = Navy900,
    secondary = Green300,
    secondaryVariant = Green900
)

val DarkColors = darkColors(
    primary = Navy500,
    primaryVariant = Navy900,
    secondary = Green300
)


val H6 = TextStyle(
    fontWeight = FontWeight.Medium,
    fontSize = 20.sp,
    letterSpacing = 0.15.sp
)
val Body2 = TextStyle(
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    letterSpacing = 0.25.sp
)
val Button = TextStyle(
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    letterSpacing = 1.25.sp
)

val Typography = Typography(
    h6 = H6,
    body2 = Body2,
    button = Button,
//    defaultFontFamily = FontFamily(Font(R.font.roboto_mono))
//     Using default values for subtitle1, caption, etc.
)


val Shapes = Shapes(
    small = RoundedCornerShape(percent = 20),
    medium = RoundedCornerShape(20),
    large = CutCornerShape(
        topStart = 16.dp,
        topEnd = 0.dp,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    )
)


@Composable
fun Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColors
    } else {
        LightColors
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}