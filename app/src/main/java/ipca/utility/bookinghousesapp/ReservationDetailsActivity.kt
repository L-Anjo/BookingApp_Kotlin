package ipca.utility.bookinghousesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.util.Pair
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.datepicker.MaterialDatePicker
import ipca.utility.bookinghousesapp.databinding.ActivityHousedetailBinding
import ipca.utility.bookinghousesapp.databinding.ActivityReservationDetailsBinding
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class ReservationDetailsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityReservationDetailsBinding
    private var startDate: LocalDateTime? = null
    private var endDate: LocalDateTime? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservationDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val houseId = intent.extras?.getInt("HOUSE_ID")?:-1
        val houseName = intent.extras?.getString("HOUSE_NAME")?:""

        val medclass = intent.extras?.getDouble("HOUSE_FEEDM")?:0.0
        val price = intent.extras?.getDouble("HOUSE_PRICE")?:null
        Log.d("teste" ,price.toString())
        val imagelink = intent.extras?.getString("HOUSE_IMAGE")?:""

        //var startDate: LocalDateTime? = null
        //var endDate: LocalDateTime? = null
        val image = binding.imageViewHouse
        val date = binding.textViewCheckInOut2


        binding.textViewName.text = houseName
        binding.textViewMFeed.text = "☆${medclass}"

        Glide.with(this)
            .load(imagelink)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(image)

        //updateReservationPrice()
        binding.editTextNumber.addTextChangedListener {
           updateReservationPrice()
        }

        date.setOnClickListener {
            val picker = MaterialDatePicker.Builder.dateRangePicker()
                .setTheme(R.style.ThemeMaterialCalendar)
                .setTitleText("Selecione a Data de Estadia")
                .setSelection(Pair(null,null))
                .build()

            picker.show(this.supportFragmentManager, "TAG")

            picker.addOnPositiveButtonClickListener {
                date.setText(convertTimeToDate(it.first) + " - " +convertTimeToDate(it.second))
                startDate = convertTimeToDatee(it.first)
                endDate = convertTimeToDatee(it.second)
                updateReservationPrice()
            }
            picker.addOnNegativeButtonClickListener {
                picker.dismiss()
            }
        }

        binding.buttonBack2.setOnClickListener {
            onBackPressed()
        }

        binding.buttonConfirmtoPay.setOnClickListener {

        }


    }

    private fun convertTimeToDate(time: Long): String{
        val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        utc.timeInMillis = time
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return format.format(utc.time)
    }

    private fun convertTimeToDatee(time: Long): LocalDateTime{
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneOffset.UTC)
    }

    private fun updateReservationPrice() {
        val pricePerNight = intent.extras?.getDouble("HOUSE_PRICE") ?: 0.0

        val startDate = startDate
        val endDate = endDate
        Log.d("tested",startDate.toString())
        val numGuests = binding.editTextNumber.text.toString().toIntOrNull() ?: 0
        var days = Duration.between(startDate, endDate).toDays()

        if (startDate == null || endDate == null) {
            days = 0
        }
        val totalPrice = pricePerNight * days
        val totalTaxa = totalPrice * 0.05
        val valortotalReserva = totalPrice+totalTaxa

        binding.textViewValorReserva.text = totalPrice.toString()
        binding.textViewTaxaEz.text = totalTaxa.toString()
        binding.textViewTotalHR.text = "${valortotalReserva}€"
    }

}