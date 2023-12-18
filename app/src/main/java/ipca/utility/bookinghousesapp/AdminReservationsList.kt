package ipca.utility.bookinghousesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import ipca.utility.bookinghousesapp.Models.House
import ipca.utility.bookinghousesapp.Models.Reservation
import ipca.utility.bookinghousesapp.Models.User
import ipca.utility.bookinghousesapp.databinding.ActivityAdminReservationsListBinding
import ipca.utility.bookinghousesapp.databinding.ActivityAdminUsersListBinding
import java.time.LocalDateTime
import java.util.Date
import java.text.SimpleDateFormat
import java.util.*

class AdminReservationsList : AppCompatActivity() {

    private lateinit var binding : ActivityAdminReservationsListBinding
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    var reservations = arrayListOf<Reservation>(
        Reservation(1, dateFormat.format(Date()),dateFormat.format(Date()).plus(3), 4),
        Reservation(2, dateFormat.format(Date()),dateFormat.format(Date()).plus(3), 4),
        Reservation(3, dateFormat.format(Date()),dateFormat.format(Date()).plus(3), 4),
        Reservation(4, dateFormat.format(Date()),dateFormat.format(Date()).plus(3), 4),
        Reservation(5, dateFormat.format(Date()),dateFormat.format(Date()).plus(3), 4),
        Reservation(6, dateFormat.format(Date()),dateFormat.format(Date()).plus(3), 4),
        Reservation(7, dateFormat.format(Date()),dateFormat.format(Date()).plus(3), 4),
        Reservation(8, dateFormat.format(Date()),dateFormat.format(Date()).plus(3), 4),
        Reservation(9, dateFormat.format(Date()),dateFormat.format(Date()).plus(3), 4),
        Reservation(10, dateFormat.format(Date()),dateFormat.format(Date()).plus(3), 4),
        Reservation(11, dateFormat.format(Date()),dateFormat.format(Date()).plus(3), 4),
        Reservation(12, dateFormat.format(Date()),dateFormat.format(Date()).plus(3), 4),
        Reservation(13, dateFormat.format(Date()),dateFormat.format(Date()).plus(3), 4),

    )

    val listReservationsAdapter = ReservationsListAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminReservationsListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.listViewReservations.adapter = listReservationsAdapter
    }

    inner class ReservationsListAdapter : BaseAdapter(){
        override fun getCount(): Int {
            return reservations.size
        }

        override fun getItem(position: Int): Any {
            return reservations[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rootView = layoutInflater.inflate(R.layout.row_reservation,parent, false)
            rootView.findViewById<TextView>(R.id.textViewReservationName).text = "T3 Braga"
            rootView.findViewById<TextView>(R.id.textViewReservationInitialDate).text = reservations[position].initDate.toString()
            rootView.findViewById<TextView>(R.id.textViewReservationFinalDate).text = reservations[position].endDate.toString()
            rootView.findViewById<TextView>(R.id.textViewReservationGuestsNumber).text = reservations[position].guestsNumber.toString()

            return rootView
        }

    }
}