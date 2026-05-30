package com.example.formulario

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.benchmark.traceprocessor.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.formulario.ui.theme.FormularioTheme
import java.io.File
import java.io.FileWriter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Formulario()
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun Formulario(){
    val context= LocalContext.current
    var producto by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var precioUnitario by remember { mutableStateOf("") }
    var descuento by remember { mutableStateOf("") }
    var total by remember { mutableStateOf("0.00") }
    var metodoPago by remember { mutableStateOf("") }
    var checkDescuento by remember { mutableStateOf(false) }


    var cantidadInt=cantidad.toIntOrNull()?:0
    var precioUnitarioDouble=precioUnitario.toDoubleOrNull()?:0.00
    var descuentoDouble=descuento.toDoubleOrNull()?:0.00
    total=((cantidadInt*precioUnitarioDouble)-descuentoDouble).toString()


    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        verticalArrangement =Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
         ) {
        Text(
            text = "FORMULARIO DE VENTAS",
            modifier = Modifier.padding(top = 20.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.padding(8.dp))

        OutlinedTextField(
            value = producto,
            onValueChange = { producto = it },
            label = { Text(text = "Nombre del producto") },
            placeholder = { Text(text = "Ingrese el nombre del producto") }
        )
        Spacer(modifier = Modifier.padding(8.dp))
        OutlinedTextField(
            value = cantidad,
            onValueChange = { cantidad = it },
            label = { Text(text = "Cantidad del producto") },
            placeholder = { Text(text = "0") }
        )
        Spacer(modifier = Modifier.padding(8.dp))

        OutlinedTextField(
            value = precioUnitario, onValueChange = { precioUnitario = it }, label = {
            Text(text = "Precio Unitario")
        },
            placeholder = { Text(text = "0.00") }

        )
        Spacer(modifier = Modifier.padding(8.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 25.dp))
        {
            Checkbox(checked = checkDescuento, onCheckedChange = { checkDescuento = it })
            Text(text = "Aplicar descuento", modifier = Modifier.padding(top = 14.dp))
        }
        OutlinedTextField(
            value = descuento, onValueChange = { descuento = it }, enabled = checkDescuento, label = {
            Text(text = "Descuento")
        },
            placeholder = { Text(text = "0.00") }

        )
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
            text = "Total es: $total ",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Text(text = "Elija el metodo de pago")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column() {
                RadioButton(
                    selected = metodoPago == "Efectivo",
                    onClick = { metodoPago = "Efectivo" }
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Efectivo")
            }
            Column() {
                RadioButton(
                    selected = metodoPago == "QR",
                    onClick = { metodoPago = "QR" }
                )
                Spacer(modifier = Modifier.width(4.dp))

                Text(text = "QR")
            }
            Column() {
                RadioButton(
                    selected = metodoPago == "Tarjeta",
                    onClick = { metodoPago = "Tarjeta" }
                )
                Spacer(modifier = Modifier.width(4.dp))

                Text(text = "Tarjeta")
            }


        }
        Row() {
            ElevatedButton(onClick = {formularioCSV(context, producto, cantidad, precioUnitario, descuento, total);
            guardarOAgregarCSV(context, "$producto,$cantidad,$precioUnitario,$descuento,$total,$metodoPago") }) {
                Text(text = "Registrar Venta")
            }
                Spacer(modifier = Modifier.width(12.dp))
            ElevatedButton(onClick = { }) {
                Text(text = "Limpiar")
            }
        }

    }
}

fun formularioCSV(context: Context, producto: String, cantidad: String, precioUnitario: String, descuento: String, total: String) {
    val csvLine = "$producto,$cantidad,$precioUnitario,$descuento,$total\n"
    try {
        val filename="ventas.csv"
        val file= File(context.filesDir, filename)
        val writer= FileWriter(file,true)
        if (!file.exists()) {
            file.createNewFile()
        }
        writer.append(csvLine)
        writer.flush()
        writer.close()
        Toast.makeText(context, "Datos guardados en CSV", Toast.LENGTH_SHORT).show()

    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Error al guardar en CSV", Toast.LENGTH_SHORT).show()
    }
}

fun guardarOAgregarCSV(context: Context, lineaTexto: String) {
    val resolver = context.contentResolver
    val fileName = "ventas.csv"


    val uri = buscarArchivoCSV(context, fileName)

    if (uri == null) {
        //  Crear archivo por primera vez
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        val newUri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

        newUri?.let {
            resolver.openOutputStream(it)?.use { outputStream ->
                val texto = "producto,cantidad,precioUnitario,descuento,total,metodoPago\n$lineaTexto\n"
                outputStream.write(texto.toByteArray())
            }
        }

    } else {
        //  Ya existe → leer + agregar + reescribir
        try {
            val contenidoActual = resolver.openInputStream(uri)?.bufferedReader().use { it?.readText() } ?: ""

            val nuevoContenido = contenidoActual + "$lineaTexto\n"

            resolver.openOutputStream(uri, "w")?.use { outputStream ->
                outputStream.write(nuevoContenido.toByteArray())
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun buscarArchivoCSV(context: Context, fileName: String): Uri? {
    val resolver = context.contentResolver

    val projection = arrayOf(
        MediaStore.MediaColumns._ID,
        MediaStore.MediaColumns.DISPLAY_NAME
    )

    val selection = "${MediaStore.MediaColumns.DISPLAY_NAME} = ?"
    val selectionArgs = arrayOf(fileName)

    val cursor = resolver.query(
        MediaStore.Downloads.EXTERNAL_CONTENT_URI,
        projection,
        selection,
        selectionArgs,
        null
    )

    cursor?.use {
        if (it.moveToFirst()) {
            val id = it.getLong(it.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
            return ContentUris.withAppendedId(MediaStore.Downloads.EXTERNAL_CONTENT_URI, id)
        }
    }

    return null
}





