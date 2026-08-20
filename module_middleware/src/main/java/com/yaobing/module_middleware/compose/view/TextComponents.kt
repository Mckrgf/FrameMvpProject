package com.yaobing.module_middleware.compose.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import com.yaobing.module_middleware.compose.theme.GreetingCardTheme

@Composable
fun CustomText(name: String, modifier: Modifier = Modifier) {
    Text(
        text = name,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun CustomTextPreview() {
    GreetingCardTheme {
        CustomText("Meghan")
    }
}
