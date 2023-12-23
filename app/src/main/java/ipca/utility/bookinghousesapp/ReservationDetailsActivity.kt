package ipca.utility.bookinghousesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ipca.utility.bookinghousesapp.databinding.ActivityHousedetailBinding
import ipca.utility.bookinghousesapp.databinding.ActivityReservationDetailsBinding

class ReservationDetailsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityReservationDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservationDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val houseId = intent.extras?.getInt("HOUSE_ID")?:-1



    }
}