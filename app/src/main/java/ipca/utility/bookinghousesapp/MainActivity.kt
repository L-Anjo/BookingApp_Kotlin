package ipca.utility.bookinghousesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ipca.utility.bookinghousesapp.Models.House
import ipca.utility.bookinghousesapp.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var textShowData: TextView
    private lateinit var getData: TextView
    var houses = arrayListOf<io.swagger.client.models.House>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val recyclerView = binding.RecycleViewHouses
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = HouseAdapter(houses)
        recyclerView.adapter = adapter

        var editTextLocalidade = binding.editTextTextLocalidade.text.toString()?:null
        var editTextGuests: Int? = binding.editTextTextGuests.text.toString().toIntOrNull()
        var radiobuttonHouse = binding.radioButtonHouse
        var radiobuttonRoom = binding.radioButtonRoom
        var buttonSearch = binding.buttonSearchFilter
        var startDate: LocalDateTime? = null
        var endDate: LocalDateTime? = null

        textShowData = binding.textViewCheckInOut

        textShowData.setOnClickListener {
            val picker = MaterialDatePicker.Builder.dateRangePicker()
                .setTheme(R.style.ThemeMaterialCalendar)
                .setTitleText("Selecione a Data de Estadia")
                .setSelection(Pair(null,null))
                .build()

            picker.show(this.supportFragmentManager, "TAG")

            picker.addOnPositiveButtonClickListener {
                textShowData.setText(convertTimeToDate(it.first) + " - " +convertTimeToDate(it.second))
                startDate = convertTimeToDatee(it.first)
                endDate = convertTimeToDatee(it.second)

            }
            picker.addOnNegativeButtonClickListener {
                picker.dismiss()
            }
        }


        Backend.fetchAllHouses(lifecycleScope) { houses ->
            houses?.let {
                adapter.updateData(houses.toList())
            }

        }

        buttonSearch.setOnClickListener {
        Backend.filterHouses(lifecycleScope,editTextLocalidade,editTextGuests,startDate, endDate){houses ->
            houses?.let {
                adapter.updateData(houses.toList())
            }
        }
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

    class HouseAdapter(private var houses: List<io.swagger.client.models.House>) : RecyclerView.Adapter<HouseAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val nameTextView: TextView = itemView.findViewById(R.id.textViewNameHouse)
            val DCTextView: TextView = itemView.findViewById(R.id.textViewDC)
            val FeedTextView: TextView = itemView.findViewById(R.id.textViewMedFeedbackH)
            val HouseRoomTextView: TextView = itemView.findViewById(R.id.textViewQH)
            val priceTextView: TextView = itemView.findViewById(R.id.textViewPNY)
            val viewPager: ViewPager2 = itemView.findViewById(R.id.viewPager)
            val tabLayout : TabLayout = itemView.findViewById(R.id.tabLayout)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.row_house, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val house = houses[position]
            var DC = "${house.postalCode?.district}, ${house.postalCode?.concelho}"
            val displayText = if (house.priceyear != null) {
                "${house.priceyear}€ / Ano"
            } else {
                "${house.price}€ / Noite"
            }
            var imageUrls = house.images?.map { imageName ->
                "${Backend.BASE_API}/Houses/${imageName.image}${imageName.formato}"
            } ?: emptyList()
            val averageFeedback = calculateAverageFeedback(house.reservations)


            holder.nameTextView.text = house.name
            holder.DCTextView.text = DC
            holder.FeedTextView.text = if (averageFeedback == 0.0) {""}
            else averageFeedback.toString()
            holder.HouseRoomTextView.text = if (house.sharedRoom== false) "Casa" else "Quarto"
            holder.priceTextView.text = displayText

            val imagePagerAdapter = ImagePagerAdapter(imageUrls, holder.itemView.context as FragmentActivity)
            holder.viewPager.adapter = imagePagerAdapter
            TabLayoutMediator(holder.tabLayout, holder.viewPager) { tab, position ->
            }.attach()

        }


        override fun getItemCount(): Int {
            return houses.size
        }

        fun updateData(newHouses: List<io.swagger.client.models.House>) {
            houses = newHouses
            notifyDataSetChanged()
        }

        fun calculateAverageFeedback(reservations: Array<io.swagger.client.models.Reservation>?): Double {
            if (reservations.isNullOrEmpty()) {
                return 0.0
            }

            var totalFeedback = 0.0
            reservations.forEach { reservation ->
                reservation.feedback?.let {
                    totalFeedback += it.classification!!
                }
            }

            return totalFeedback.toDouble() / reservations.size
        }
    }

     class ImagePagerAdapter(private val imageUrls: List<String>, activity: FragmentActivity) :
        FragmentStateAdapter(activity) {

        override fun getItemCount(): Int = imageUrls.size

        override fun createFragment(position: Int): Fragment {
            return HouseDetailActivity.ImageFragment(imageUrls[position])
        }
    }

    inner class ImageFragment(private val imageUrl: String) : Fragment() {

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            Log.d("ImageFragment", "onCreateView called")
            val view = inflater.inflate(R.layout.image_view_carrousel, container, false)
            val imageView = view.findViewById<ImageView>(R.id.idIVImage)

            Glide.with(this)
                .load(imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)


            return view
        }
    }





}