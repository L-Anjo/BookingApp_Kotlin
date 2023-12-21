package ipca.utility.bookinghousesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import ipca.utility.bookinghousesapp.databinding.ActivityAdminHousesListBinding
import ipca.utility.bookinghousesapp.databinding.ActivityAdminUsersListBinding

class AdminHousesList : AppCompatActivity() {

    private lateinit var binding : ActivityAdminHousesListBinding
    private var houses = arrayListOf<io.swagger.client.models.House>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminHousesListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Backend.GetAllHouses(this, lifecycleScope) { fetchedHouses ->
            houses.addAll(fetchedHouses)
            setupListView()
        }
    }


    private fun setupListView() {
        val adapter = HouseListAdapter()
        binding.listViewHouses.adapter = adapter
    }

    inner class HouseListAdapter : BaseAdapter() {

        override fun getCount(): Int {
            return houses.size
        }

        override fun getItem(position: Int): Any {
            return houses[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rootView = layoutInflater.inflate(R.layout.row_housesadmin,parent, false)
            rootView.findViewById<TextView>(R.id.TextViewNomeHouse).text = houses[position].name
            rootView.findViewById<TextView>(R.id.TextViewLocalidadeHouse).text = houses[position].road


            return rootView
        }
    }
}