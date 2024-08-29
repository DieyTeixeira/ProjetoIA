package br.com.dieyteixeira.projetoia

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.dieyteixeira.projetoia.ui.screens.ChatScreen
import br.com.dieyteixeira.projetoia.ui.viewmodels.ChatViewModel
import br.com.dieyteixeira.projetoia.ui.theme.ProjetoIATheme

@RequiresApi(Build.VERSION_CODES.P)
class MainActivity : ComponentActivity() {

    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private val chatViewModel: ChatViewModel by viewModels() // Obtém a instância do ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as Bitmap
                chatViewModel.setImageBitmap(imageBitmap) // Atualiza o ViewModel com a imagem capturada
            }
        }
        setContent {
            ProjetoIATheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    ChatScreen(
                        chatViewModel = chatViewModel, // Passe o ViewModel para a ChatScreen
                        onCaptureImage = { openCamera() }
                    )
                }
            }
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)
    }
}