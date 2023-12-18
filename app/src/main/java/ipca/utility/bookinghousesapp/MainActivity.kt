package ipca.utility.bookinghousesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import ipca.utility.bookinghousesapp.Models.House
import ipca.utility.bookinghousesapp.Models.Reservation
import ipca.utility.bookinghousesapp.databinding.ActivityMainBinding
import java.util.Date

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    var houses = arrayListOf<House>(
        House(1, "T3 Braga", 3, 4, 120.0, null, 5, 5, "Rua do Queimado", null, false),
        House(1, "T3 Braga", 3, 4, 120.0, null, 5, 5, "Rua do Queimado", null, false),
        House(1, "T3 Braga", 3, 4, 120.0, null, 5, 5, "Rua do Queimado", null, false),
        House(1, "T3 Braga", 3, 4, 120.0, null, 5, 5, "Rua do Queimado", null, false),
        House(1, "T3 Braga", 3, 4, 120.0, null, 5, 5, "Rua do Queimado", null, false),
        House(1, "T3 Braga", 3, 4, 120.0, null, 5, 5, "Rua do Queimado", null, false),
        House(1, "T3 Braga", 3, 4, 120.0, null, 5, 5, "Rua do Queimado", null, false),
        House(1, "T3 Braga", 3, 4, 120.0, null, 5, 5, "Rua do Queimado", null, false),
        House(1, "T3 Braga", 3, 4, 120.0, null, 5, 5, "Rua do Queimado", null, false),
        House(1, "T3 Braga", 3, 4, 120.0, null, 5, 5, "Rua do Queimado", null, false),
        House(1, "T3 Braga", 3, 4, 120.0, null, 5, 5, "Rua do Queimado", null, false),
        House(1, "T3 Braga", 3, 4, 120.0, null, 5, 5, "Rua do Queimado", null, false),
        House(1, "T3 Braga", 3, 4, 120.0, null, 5, 5, "Rua do Queimado", null, false),
        House(1, "T3 Braga", 3, 4, 120.0, null, 5, 5, "Rua do Queimado", null, false),
        House(1, "T3 Braga", 3, 4, 120.0, null, 5, 5, "Rua do Queimado", null, false),
        )
    val houseAdapter = HouseAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.listViewHouses.adapter = houseAdapter

    }

    inner class HouseAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return houses.size
        }

        override fun getItem(position: Int): Any {
            return houses[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rootView = layoutInflater.inflate(R.layout.row_house,parent, false)
            rootView.findViewById<TextView>(R.id.textViewName).text = houses[position].name
            rootView.findViewById<TextView>(R.id.textViewGuests).text = houses[position].guestsNumber.toString()
            rootView.findViewById<TextView>(R.id.textViewLocalidade).text = houses[position].road
            rootView.findViewById<TextView>(R.id.textViewStars).text = "5"

            return rootView
        }
    }


}