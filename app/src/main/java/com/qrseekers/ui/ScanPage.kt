package com.qrseekers.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.qrseekers.viewmodels.AuthViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.Executors

@Composable
fun ScanPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()

    // State variables
    var scannedUrl by remember { mutableStateOf<String?>(null) }
    var isCameraVisible by remember { mutableStateOf(true) }
    var hasCameraPermission by remember { mutableStateOf(false) }

    // Permission handling
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        if (!isGranted) {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    // Request camera permission on first composition
    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFE3F2FD), Color(0xFFFFFFFF))
                )
            ),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            text = "QRseekers",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = Color(0xFF1E88E5),
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
            ),
            modifier = Modifier.padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (hasCameraPermission) {
            if (isCameraVisible) {
                // Camera Preview with instructions
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Point the camera at a QR code to scan.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.Gray
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f) // Ensures the preview is square
                            .background(Color.Black)
                    ) {
                        QRCodeScannerView(
                            onQRCodeScanned = { url ->
                                scannedUrl = url
                                isCameraVisible = false
                            }
                        )
                    }
                }
            } else {
                // Results View
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Scanned Result",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(16.dp)
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth(0.9f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = scannedUrl ?: "No URL found",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Open Browser Button
                        Button(
                            onClick = {
                                if (Patterns.WEB_URL.matcher(scannedUrl ?: "").matches()) {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(scannedUrl))
                                    context.startActivity(intent)
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Invalid URL",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5))
                        ) {
                            Icon(Icons.Default.Search, contentDescription = "Open Browser")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Open URL")
                        }

                        // Rescan Button
                        Button(
                            onClick = {
                                scannedUrl = null
                                isCameraVisible = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5))
                        ) {
                            Text("Rescan")
                        }
                    }
                }
            }
        } else {
            // No permission granted
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Camera permission is required to scan QR codes",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.Red
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5))
                ) {
                    Text("Request Permission")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun QRCodeScannerView(
    onQRCodeScanned: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                // Preview use case
                val preview = Preview.Builder()
                    .build()
                    .also { it.setSurfaceProvider(previewView.surfaceProvider) }

                // Barcode scanning use case
                val options = BarcodeScannerOptions.Builder()
                    .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                    .build()
                val scanner = BarcodeScanning.getClient(options)

                val imageAnalyzer = ImageAnalysis.Builder()
                    .build()
                    .also { imageAnalysis ->
                        imageAnalysis.setAnalyzer(
                            Executors.newSingleThreadExecutor()
                        ) { imageProxy ->
                            coroutineScope.launch {
                                processImageProxy(scanner, imageProxy) { url ->
                                    onQRCodeScanned(url)
                                    imageProxy.close()
                                }
                            }
                        }
                    }

                // Bind use cases to camera
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        imageAnalyzer
                    )
                } catch (exc: Exception) {
                    // Handle any errors
                    Toast.makeText(
                        ctx,
                        "Error initializing camera",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }, ContextCompat.getMainExecutor(ctx))

            previewView
        }
    )
}

// Image processing function
@OptIn(ExperimentalGetImage::class)
suspend fun processImageProxy(
    barcodeScanner: BarcodeScanner,
    imageProxy: ImageProxy,
    onQRCodeDetected: (String) -> Unit
) {
    val mediaImage = imageProxy.image
    if (mediaImage != null) {
        val inputImage = InputImage.fromMediaImage(
            mediaImage,
            imageProxy.imageInfo.rotationDegrees
        )

        try {
            val barcodes = barcodeScanner.process(inputImage).await()
            barcodes.firstOrNull()?.rawValue?.let { url ->
                onQRCodeDetected(url)
            }
        } catch (e: Exception) {
            // Handle scanning failure (optional logging or error handling)
        } finally {
            // Close the imageProxy to free resources
            imageProxy.close()
        }
    } else {
        // If mediaImage is null, ensure the ImageProxy is closed to avoid memory leaks
        imageProxy.close()
    }
}
