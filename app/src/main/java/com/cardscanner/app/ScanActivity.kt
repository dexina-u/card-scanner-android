package com.cardscanner.app

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.cardscanner.app.databinding.ActivityScanBinding
import com.cardscanner.app.utils.TextParser
import com.cardscanner.app.viewmodel.CardViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ScanActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityScanBinding
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cardViewModel: CardViewModel
    private var imageCapture: ImageCapture? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        cardViewModel = ViewModelProvider(this)[CardViewModel::class.java]
        cameraExecutor = Executors.newSingleThreadExecutor()
        
        startCamera()
        
        binding.btnCapture.setOnClickListener {
            captureAndProcess()
        }
        
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
    
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }
            
            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()
            
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
            } catch (e: Exception) {
                Toast.makeText(this, "Camera initialization failed", Toast.LENGTH_SHORT).show()
            }
            
        }, ContextCompat.getMainExecutor(this))
    }
    
    @SuppressLint("UnsafeOptInUsageError")
    private fun captureAndProcess() {
        val imageCapture = imageCapture ?: return
        
        binding.btnCapture.isEnabled = false
        binding.tvStatus.text = "Processing..."
        
        imageCapture.takePicture(
            cameraExecutor,
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(imageProxy: ImageProxy) {
                    val mediaImage = imageProxy.image
                    if (mediaImage != null) {
                        val image = InputImage.fromMediaImage(
                            mediaImage,
                            imageProxy.imageInfo.rotationDegrees
                        )
                        processImage(image)
                    }
                    imageProxy.close()
                }
                
                override fun onError(exception: ImageCaptureException) {
                    runOnUiThread {
                        binding.btnCapture.isEnabled = true
                        binding.tvStatus.text = "Ready to scan"
                        Toast.makeText(
                            this@ScanActivity,
                            "Capture failed: ${exception.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        )
    }
    
    private fun processImage(image: InputImage) {
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        
        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val text = visionText.text
                if (text.isNotBlank()) {
                    val parsed = TextParser.parseCardText(text)
                    runOnUiThread {
                        showResultDialog(parsed.name, parsed.address, text)
                    }
                } else {
                    runOnUiThread {
                        binding.btnCapture.isEnabled = true
                        binding.tvStatus.text = "Ready to scan"
                        Toast.makeText(
                            this@ScanActivity,
                            "No text detected",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            .addOnFailureListener { e ->
                runOnUiThread {
                    binding.btnCapture.isEnabled = true
                    binding.tvStatus.text = "Ready to scan"
                    Toast.makeText(
                        this@ScanActivity,
                        "Text recognition failed: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
    
    private fun showResultDialog(name: String, address: String, rawText: String) {
        AlertDialog.Builder(this)
            .setTitle("Card Scanned")
            .setMessage("Name: $name\n\nAddress: $address\n\nRaw Text:\n$rawText")
            .setPositiveButton("Save") { _, _ ->
                cardViewModel.saveCard(name, address, rawText)
                Toast.makeText(this, "Card saved!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .setNegativeButton("Retry") { _, _ ->
                binding.btnCapture.isEnabled = true
                binding.tvStatus.text = "Ready to scan"
            }
            .setCancelable(false)
            .show()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}