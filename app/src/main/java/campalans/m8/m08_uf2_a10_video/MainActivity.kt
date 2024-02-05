package campalans.m8.m08_uf2_a10_video

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.MediaController
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import campalans.m8.m08_uf2_a10_video.databinding.ActivityMainBinding
import java.io.File
import java.time.LocalDate


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.button.setOnClickListener()
        {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).also{
                it.resolveActivity(packageManager).also{component ->
                    createPhotoFile()
                    val photoUri: Uri = FileProvider.getUriForFile(this,"campalans.m8.m08_uf2_a10_video.fileprovider", file)

                    it.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                }
            }
            startForResult.launch(intent)
        }

        //Al fer click al botó video
        binding.buttonVideo.setOnClickListener()
        {
            val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE).also{
                it.resolveActivity(packageManager).also{component ->
                    createVideoFile()
                    val photoUri: Uri = FileProvider.getUriForFile(this,"campalans.m8.m08_uf2_a10_video.fileprovider", file)

                    it.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                }
            }
            startForResult2.launch(intent)
        }
    }

    private lateinit var file: File
    private fun createPhotoFile(){

        val dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        //Modificació per a que guardi les imatges amb el nom, la data d'avui i en format png
        file = File.createTempFile("MarcC_${LocalDate.now()}_", ".png", dir)
    }

    private fun createVideoFile(){

        val dir = getExternalFilesDir(Environment.DIRECTORY_MOVIES)
        //Modificació per a que guardi els videos amb el nom, la data d'avui i en format mp4
        file = File.createTempFile("MarcC_${LocalDate.now()}_", ".mp4", dir)
        Log.i("ruta", file.toString())
    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {
            result: ActivityResult ->
        if(result.resultCode == Activity.RESULT_OK)
        {
            val intent = result.data
            val imatgeBitmap = BitmapFactory.decodeFile(file.toString())
            val imatgeView = binding.miniatureFoto
            imatgeView.setImageBitmap(imatgeBitmap)
        }
    }

    private val startForResult2 = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {
            result: ActivityResult ->
        if(result.resultCode == Activity.RESULT_OK)
        {
            val videoView = binding.videoView
            videoView.setVideoURI(Uri.parse(file.toString()))
            Log.i("ruta", file.toString())

            val mediaController = MediaController(this)
            mediaController.setAnchorView(videoView)
            videoView.setMediaController(mediaController)

            videoView.requestFocus()
            videoView.start()
        }
    }
}