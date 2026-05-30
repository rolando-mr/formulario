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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
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
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
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
    var context= LocalContext.current
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


    Column(modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement =Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
         ) {
        Text(text = "FORMULARIO DE VENTAS",
            modifier = Modifier.padding(top = 20.dp),
            fontSize = 25.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.padding(7.dp))
        OutlinedTextField(value = producto, onValueChange = {producto=it},label={
            Text(text = "Nombre del producto")},
            placeholder={Text(text = "Ingrese el nombre del producto")}

        )
        Spacer(modifier = Modifier.padding(8.dp))
        OutlinedTextField(value =cantidad, onValueChange = {cantidad=it},label={
            Text(text = "Cantidad del producto")},
            placeholder={Text(text = "0")}

        )
        Spacer(modifier = Modifier.padding(8.dp))

        OutlinedTextField(value = precioUnitario, onValueChange = {precioUnitario=it},label={
            Text(text = "Precio Unitario")},
            placeholder={Text(text = "0.00")}

        )
        Spacer(modifier = Modifier.padding(8.dp))

        Row() {
            Checkbox(checked=checkDescuento, onCheckedChange = {checkDescuento=it})
            Text(text = "¿Desea aplicar un descuento?", modifier = Modifier.padding(top=13.dp))
        }

        OutlinedTextField(value = descuento,
            onValueChange = {descuento=it},
            label={
            Text(text = "Descuento")},
            placeholder={Text(text = "0.00")}, enabled = checkDescuento

        )
        Spacer(modifier = Modifier.padding(8.dp))
        Text(text="Total es: $total ", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.padding(8.dp))
        Text(text="Elija el metodo de pago")
        Row(modifier = Modifier.fillMaxWidth().padding(25.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column() {
                RadioButton(
                    selected = metodoPago == "Efectivo",
                    onClick = { metodoPago = "Efectivo" }
                )
                Text(text = "Efectivo")
            }
            Column() {
                RadioButton(
                    selected = metodoPago == "QR",
                    onClick = { metodoPago = "QR" }
                )
                Text(text = "QR")
            }

            Column() {
                RadioButton(
                    selected = metodoPago == "Tarjeta",
                    onClick = { metodoPago = "Tarjeta" }
                )

                Text(text = "Tarjeta")
            }
        }
        Row() {
            ElevatedButton(onClick = {producto="";cantidad="";precioUnitario="";descuento="";checkDescuento=false;
            guardarCompraCSV(context,producto,cantidad,precioUnitario,descuento,total,metodoPago);
                guardarOAgregarCSV(context,producto,cantidad,precioUnitario,descuento,total,metodoPago)}) {
                Text(text = "Guardar Compra")
            }
            Spacer(modifier = Modifier.width(15.dp))
            ElevatedButton(onClick = {producto="";cantidad="";precioUnitario="";descuento="";checkDescuento=false}, colors = ButtonDefaults.elevatedButtonColors(containerColor = Color(
                0xFFAF4848
            )
            )) {
                Text(text = "Limpiar")
            }
        }

        }
}

fun guardarCompraCSV(context: Context, Producto:String, Cantidad:String, PrecioUnitario: String, Descuento: String, total:String, MetodoPago:String){
    var linea="$Producto,$Cantidad,$PrecioUnitario,$Descuento,$total,$MetodoPago\n"
    try {
        val filename="compras.csv"
        val file= File(context.filesDir, filename)
        if (!file.exists()){
            file.createNewFile()
        }
        val writer= FileWriter(file,true)
        writer.append(linea)
        writer.flush()
        writer.close()
        Toast.makeText(context,"Compra guardada exitosamente", Toast.LENGTH_LONG).show()
    }catch (e: Exception) {
        Toast.makeText(context,"Error al guardar", Toast.LENGTH_LONG).show()

    }


}

fun guardarOAgregarCSV(context: Context, producto: String, cantidad: String, precioUnitario: String, descuento: String, total: String, metodoPago: String) {
    val resolver = context.contentResolver
    val fileName = "compras.csv"

    val uri = buscarArchivoCSV(context, fileName)

    if (uri == null) {
        //  Crear archivo por primera vez
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
        }

        val newUri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

        newUri?.let {
            resolver.openOutputStream(it)?.use { outputStream ->
                val texto = "Producto,Cantidad,PrecioUnitario,Descuento,Total,MetodoPago\n$producto,$cantidad,$precioUnitario,$descuento,$total,$metodoPago\n"
                outputStream.write(texto.toByteArray())
            }
        }

    } else {
        //  Ya existe → leer + agregar + reescribir
        try {
            val contenidoActual = resolver.openInputStream(uri)?.bufferedReader().use { it?.readText() } ?: ""

            val nuevoContenido = contenidoActual + "$producto,$cantidad,$precioUnitario,$descuento,$total,$metodoPago\n"

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







