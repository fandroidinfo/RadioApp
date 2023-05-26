package info.fandroid.radioapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import info.fandroid.radioapp.R

@Composable
fun ErrorScreen(
    retryAction: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.loading_failed))
        Button(onClick = retryAction) {
            Text(text = stringResource(id = R.string.error_screen_retry))
            
        }
        
    }
}

@Preview
@Composable
fun ErrorScreenPreview(){
    ErrorScreen {
    }
}