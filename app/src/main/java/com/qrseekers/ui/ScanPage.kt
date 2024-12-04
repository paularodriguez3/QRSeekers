
package com.qrseekers.ui
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.navigation.NavController
import com.qrseekers.viewmodels.AuthViewModel


@Composable
fun ScanPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {

    Text(text = "Scan page")

}
/*package com.qrseekers.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.journeyapps.barcodescanner.BarcodeScanner
import com.journeyapps.barcodescanner.BarcodeCallback

@Composable
fun ScanPage(navController: NavController) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Scan QR Code", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // QR Code Scanner
        val scanner = BarcodeScanner(context)
        scanner.decodeContinuous(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult) {
                // Handle the scanned result
                val scannedValue = result.text
                // Navigate to another page or handle the scanned value
                navController.navigate("resultPage/$scannedValue")
            }

            override fun possibleResultPoints(resultPoints: List<ResultPoint>) {
                // Handle possible result points if needed
            }
        })

        // Add a button to start scanning
        Button(onClick = {
            scanner.startScanning()
        }) {
            Text(text = "Start Scanning")
        }
    }
}
*/

