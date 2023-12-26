package ipca.utility.bookinghousesapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

class PaymentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", 0)

        Backend.fetchReservationPayment(userId).observe(this) {
            it.onError { error ->
                Toast.makeText(
                    this@PaymentActivity,
                    "Erro:${error.error}",
                    Toast.LENGTH_LONG
                ).show()
            }
            it.onNetworkError {
                Toast.makeText(
                    this@PaymentActivity,
                    "Sem Ligação à Internet",
                    Toast.LENGTH_LONG
                ).show()
            }
            it.onSuccess {
                    reservation ->
                reservation?.let {
                    var idReservation = reservation.id_reservation
                    Log.d("testeeeep", idReservation.toString())
                }
            }

        }
    }
}