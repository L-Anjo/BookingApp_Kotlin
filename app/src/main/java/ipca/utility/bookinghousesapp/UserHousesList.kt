package ipca.utility.bookinghousesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import ipca.utility.bookinghousesapp.databinding.ActivityAdminHousesListBinding
import ipca.utility.bookinghousesapp.databinding.ActivityUserHousesListBinding
import org.w3c.dom.Text

class UserHousesList : AppCompatActivity() {

    private lateinit var binding : ActivityUserHousesListBinding
    private var houses = arrayListOf<io.swagger.client.models.House>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserHousesListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageView5.setOnClickListener {
            onBackPressed()
        }

        Backend.GetUserHouses(this, lifecycleScope) { fetchedHouses ->
            houses.addAll(fetchedHouses)
            if (!houses.isEmpty()) {
                binding.textViewNoHouses.visibility = View.GONE
                setupListView()
            }
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
            val avatar = rootView.findViewById<ImageView>(R.id.imageViewHouse)
            if (houses[position].images != null ) {


                val firstImage = houses[position].images?.firstOrNull()
                if (firstImage != null) {
                    val imageUrl = "${Backend.BASE_API}/Houses/${firstImage.image}${firstImage.formato}"

                    Glide.with(this@UserHousesList)
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
            val buttonRemove = rootView.findViewById<Button>(R.id.buttonRemove)

            if(houses[position].statusHouse?.id==1 || houses[position].statusHouse?.id==2)
                buttonRemove.visibility = View.VISIBLE
            else
                buttonRemove.visibility = View.GONE
            buttonRemove.setOnClickListener{
                val houseIdToRemove = houses[position].id_house
                Backend.DeleteHouse(this@UserHousesList, lifecycleScope,houseIdToRemove.toString().toInt()) { isSuccess ->
                    if (isSuccess) {
                        notifyDataSetChanged()
                    }
                }
            }

            val seeReservationsButton = rootView.findViewById<TextView>(R.id.textViewSeeReservations)
            seeReservationsButton.setOnClickListener {
                val intent = Intent(this@UserHousesList,HouseReservationsLIst::class.java )
                intent.putExtra(HouseReservationsLIst.DATA_HOUSE, houses[position].id_house)
                startActivity(intent)
            }


            return rootView
        }

    }
}