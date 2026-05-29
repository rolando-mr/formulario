package com.example.formulario

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.benchmark.traceprocessor.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.formulario.ui.theme.FormularioTheme

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
    var producto by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("0") }
    var precioUnitario by remember { mutableStateOf("0.00") }
    var descuento by remember { mutableStateOf("0.00") }
    var total by remember { mutableStateOf("0.00") }
    var metodoPago by remember { mutableStateOf("") }


    var cantidadInt=cantidad.toIntOrNull()?:0
    var precioUnitarioDouble=precioUnitario.toDoubleOrNull()?:0.00
    var descuentoDouble=descuento.toDoubleOrNull()?:0.00
    total=((cantidadInt*precioUnitarioDouble)-descuentoDouble).toString()


    Column(modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement =Arrangement.Center,
         ) {
        Text(text = "FORMULARIO DE VENTAS", modifier = Modifier.padding(top = 20.dp))
        TextField(value = producto, onValueChange = {producto=it},label={
            Text(text = "Nombre del producto")},
            placeholder={Text(text = "Ingrese el nombre del producto")}

        )
        Spacer(modifier = Modifier.padding(8.dp))
        TextField(value =cantidad, onValueChange = {cantidad=it},label={
            Text(text = "Cantidad del producto")},
            placeholder={Text(text = "0")}

        )
        Spacer(modifier = Modifier.padding(8.dp))

        TextField(value = precioUnitario, onValueChange = {precioUnitario=it},label={
            Text(text = "Precio Unitario")},
            placeholder={Text(text = "0.00")}

        )
        Spacer(modifier = Modifier.padding(8.dp))

        TextField(value = descuento, onValueChange = {descuento=it},label={
            Text(text = "Descuento")},
            placeholder={Text(text = "0.00")}

        )
        Spacer(modifier = Modifier.padding(8.dp))
        Text(text="Total es: $total ")
        Text(text="Elija el metodo de pago")
        Row() {
            RadioButton(
                selected = metodoPago == "Efectivo",
                onClick = { metodoPago = "Efectivo" }
            )
            Text(text = "Efectivo")

            RadioButton(
                selected = metodoPago == "QR",
                onClick = { metodoPago = "QR" }
            )
            Text(text = "QR")

            RadioButton(
                selected = metodoPago == "Tarjeta",
                onClick = { metodoPago = "Tarjeta" }
            )
            Text(text = "Tarjeta")

            //Checkbox(checked = "")
        }


}







}