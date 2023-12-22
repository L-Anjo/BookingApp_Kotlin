package ipca.utility.bookinghousesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
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


            if (houses[position].postalCode != null) {
                val textViewLocalidade = rootView.findViewById<TextView>(R.id.TextViewLocalidadeHouse)
                val textViewCodPostal = rootView.findViewById<TextView>(R.id.textViewHouseCodPostal)
                textViewCodPostal.text = houses[position].postalCode?.postalCode.toString()
                textViewLocalidade.text = houses[position].postalCode?.district ?: ""
            }
            if (houses[position].statusHouse != null){
                val textViewStatusHouse = rootView.findViewById<TextView>(R.id.textViewHouseStatus)

                textViewStatusHouse.text = houses[position].statusHouse?.name ?: ""

            }
            if (houses[position].images != null ) {
                val avatar = rootView.findViewById<ImageView>(R.id.imageViewHouse)

                val firstImage = houses[position].images?.firstOrNull()
                if (firstImage != null) {
                    val imageUrl = "${Backend.BASE_API}/Houses/${firstImage.image}${firstImage.formato}"

                    Glide.with(this@AdminHousesList)
                        .asBitmap()
                        .load(imageUrl)
                        .transition(BitmapTransitionOptions.withCrossFade())
                        .into(avatar)
                }
            }


            //rootView.setOnClickListener {
            //    val intent = Intent(this@AdminHousesList, HouseDetailActivity::class.java)
            //    startActivity(intent)
            //}

            rootView.findViewById<Button>(R.id.buttonRemove).setOnClickListener{
                val houseIdToRemove = houses[position].id_house
                Backend.DeleteHouse(this@AdminHousesList, lifecycleScope,houseIdToRemove.toString().toInt()) { isSuccess ->
                    if (isSuccess) {
                        houses.removeAt(position)
                        notifyDataSetChanged()
                    }
                }
            }


            return rootView
        }

    }
}