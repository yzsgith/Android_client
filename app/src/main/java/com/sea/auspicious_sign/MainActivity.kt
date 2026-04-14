
package com.sea.auspicious_sign

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sea.auspicious_sign.ui.theme.Auspicious_signTheme
import com.sea.auspicious_sign.webview.WebViewActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Auspicious_signTheme {
                MainScreen(onOpenWebView = {
                    startActivity(Intent(this, WebViewActivity::class.java))
                })
            }
        }
    }
}

@Composable
fun MainScreen(onOpenWebView: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = onOpenWebView) {
            Text("打开 WebView 测试页面")
        }
    }
}