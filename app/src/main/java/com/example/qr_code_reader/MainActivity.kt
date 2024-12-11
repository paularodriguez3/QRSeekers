package com.example.qr_code_reader

import android.os.Bundle
import android.Manifest
import android.util.Patterns
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.qr_code_reader.databinding.ActivityMainBinding
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private val CAMERA_PERMISSION_CODE = 101 //Constante con el codigo del permiso de la camara

    //Se usa ViewBinding, tecnologia moderna que mejora el acceso a los elementos del
    //layout definido en activity_main.xml
    private lateinit var binding: ActivityMainBinding

    //Funcion constructora que inicializa el binding y el contentview,
    //agregando tambien un listener al boton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Configura el listener del boton para que ejecute
        //la funcion de comprobar permiso de camara
        binding.btnScanQr.setOnClickListener {
            checkCameraPermission()
        }
    }

    //Funcion que se ejecuta una vez se hace click en el boton. Comprueba si se ha concedido
    //permiso de camara para, en caso positivo, proseguir iniciando el escaner o, en caso negativo
    //solicitar el permiso
    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
            //Si el permiso ya fue concedido, inicia el escaner
            startQRScanner()
        } else {
            //Si no, solicita el permiso
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
        }
    }

    //Funcion que se lanza automaticamente tras la eleccion en el dialogo de permisos que haga
    //el usuario. Lo que hace es comprobar si se ha dado o no permiso y actuar en consecuencia
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startQRScanner()
                } else {
                    binding.tvQrResult.text = "Permiso de cámara denegado."
                    //Se oculta la camara y se muestra label y textview
                    ocultarCamaraMostrarResultados(false)
                }
            }
        }
    }

    //Función para configurar la camara y realizar el escaneo. Se usa CameraX y Google MLKit
    private fun startQRScanner() {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        mostrarCamaraOcultarResultados()
        cameraProviderFuture.addListener({
            //Obtenemos el proveedor de la cámara
            val cameraProvider = cameraProviderFuture.get()
            //Seleccionamos la cámara trasera
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            //Caso de Uso 1: Configuramos la vista previa
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewElement.surfaceProvider)
            }

            //Caso de Uso 2: Configuramos el analizador de imagen
            //2.1.Especificar las opciones del escaner, donde se indica el tipo de codigo (QR)
            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                    Barcode.FORMAT_QR_CODE)
                .build()
            //2.2.Instanciar el Escaner pasandole las opciones anteriormente definidas
            val scanner = BarcodeScanning.getClient(options)

            //2.3.Se construye el analizador de imagenes
            val imageAnalyzer = ImageAnalysis.Builder().build()

            //2.4.Se establece, creando un nuevo hilo para evitar bloquear el hilo principal de la
            //app, la función que realiza el análisis de imagen
            imageAnalyzer.setAnalyzer(
                Executors.newSingleThreadExecutor(),
                { imageProxy ->
                    processImageProxy(scanner, imageProxy)
                })

            try {
                //2.5.Desvinculamos todas las configuraciones previas del lifecycle y
                //vinculamos la cámara y los casos de uso de vista previa y análisis de imagen
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer)
            } catch (e: Exception) {
                binding.tvQrResult.text = "Error iniciando la cámara"
                //Se oculta la camara y se muestra label y textview
                ocultarCamaraMostrarResultados(false)
            }
        }, ContextCompat.getMainExecutor(this))

    }


    @OptIn(ExperimentalGetImage::class)
    private fun processImageProxy(
        barcodeScanner: BarcodeScanner,
        imageProxy: ImageProxy
    ) {

        imageProxy.image?.let { image ->
            //Se crea un InputImage necesario para el procesamiento de la imagen
            val inputImage = InputImage.fromMediaImage( image, imageProxy.imageInfo.rotationDegrees)

            //Se procesa la imagen agregando 3 Listener para controlar escaneo correcto, fallido
            //y finalizado
            barcodeScanner.process(inputImage)
                .addOnSuccessListener { barcodeList ->
                    //Si el escaneo ha sido exitoso
                    val barcode = barcodeList.getOrNull(0)
                    //rawValue es el valor decodificado de la barcode
                    barcode?.rawValue?.let { value ->
                        //Se establece en la textview la url
                        binding.tvQrResult.text = barcode.rawValue.toString()
                        //Se oculta la camara y se muestra label, textview y boton del navegador
                        ocultarCamaraMostrarResultados(true)
                        //Configura el listener del boton para abrir el navegador
                        binding.btnBrowser.setOnClickListener {
                            openBrowser()
                        }
                    }
                }
                .addOnFailureListener {
                    //Si ha fallado el escaneo
                    binding.tvQrResult.text = "Error en el escaneo"
                    //Se oculta la camara y se muestra label y textview
                    ocultarCamaraMostrarResultados(false)
                }.addOnCompleteListener {
                    //Al completar el escaneo
                    //Cerrar el proxy con el frame actual una vez completado el análisis
                    imageProxy.image?.close()
                    imageProxy.close()
                }
        }
    }

    private fun mostrarCamaraOcultarResultados() {
        //Se muestra la camara
        binding.previewElement.visibility = View.VISIBLE

        //Se oculta la label, el textview y el boton del navegador
        binding.txtLabelResultado.visibility = View.GONE
        binding.tvQrResult.visibility = View.GONE
        binding.btnBrowser.visibility = View.GONE
    }

    private fun ocultarCamaraMostrarResultados(conBotonBrowser: Boolean) {
        //Se oculta la camara y se liberan sus recursos
        binding.previewElement.visibility = View.GONE
        ProcessCameraProvider.getInstance(this).get().unbindAll()

        //Se muestra la label, el textview y el boton del navegador (opcionalmente este ultimo)
        binding.txtLabelResultado.visibility = View.VISIBLE
        binding.tvQrResult.visibility = View.VISIBLE
        if (conBotonBrowser) binding.btnBrowser.visibility = View.VISIBLE
    }

    //Funcion que se ejecuta una vez se hace click en el boton de abrir el navegador.
    //Navega a la url ubicada en el textview si es una url valida
    private fun openBrowser() {
        val url = binding.tvQrResult.text.toString()
        //Si la URL es válida, se abre el navegador con un intent
        if (Patterns.WEB_URL.matcher(url).matches()) {
            //Abrir la URL en un navegador
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } else {
            Toast.makeText(this, "El código escaneado no se corresponde con una URL válida", Toast.LENGTH_SHORT).show()
        }
    }
}