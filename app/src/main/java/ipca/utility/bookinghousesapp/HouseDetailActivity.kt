package ipca.utility.bookinghousesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ipca.utility.bookinghousesapp.Models.Feedback
import ipca.utility.bookinghousesapp.Models.House
import ipca.utility.bookinghousesapp.databinding.ActivityHousedetailBinding

class HouseDetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHousedetailBinding
    var house : House? = null
    var feedback = arrayListOf<Feedback>(
        Feedback(2,4,"Muito Bom"),
        Feedback(3,3,"Muito Bom"),
        Feedback(4,5,"Muito Bom"),
    )
    val feedbackAdapter = FeedbackAdapter()

    var imageList = listOf(R.drawable.baseline_house_24,R.drawable.baseline_person_outline_24,R.drawable.test)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHousedetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.listViewFeebackDetails.adapter = feedbackAdapter

        val viewPager: ViewPager2 = binding.viewPager
        val tabLayout: TabLayout = binding.tabLayout
        val pagerAdapter = ImagePagerAdapter(imageList, this)
        viewPager.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = "Tab ${position + 1}"
        }.attach()

        Backend.fetchHouseDetail(lifecycleScope) { house,postalCode,imageListt ->
            house?.let {
                val displayText = if (it.priceYear != 0.0) {
                    "${it.priceYear}€ / Ano"
                } else {
                    "${it.price}€ / Noite"
                }
                binding.textViewNameDetail.text = it.name
                binding.textViewGuestsDetail.text = "${it.guestsNumber}  Pessoas"
                binding.textViewFloorDetail.text = "${it.floorNumber}  Andar"
                binding.textViewRoomsDetail.text = "${it.rooms}  Quartos"
                binding.textViewNMaximoPessoasDetail.text = it.guestsNumber.toString()
                binding.textViewprecoDetail.text = displayText
                binding.textViewAndarDetailD.text = it.floorNumber.toString()

                binding.textViewPrecoNoiteDetail.text = displayText
            }
            postalCode?.let{
                binding.textViewLocationDetail.text = it.district
                binding.textViewConcelhoDetail.text = it.concelho
                binding.textViewDistrictDetail.text = it.district
            }
            imageListt?.let{
                Log.d("sdfsd",it[0].image)


            }
        }
    }

    inner class FeedbackAdapter : BaseAdapter(){
        override fun getCount(): Int {
            return feedback.size
        }

        override fun getItem(position: Int): Any {
            return feedback[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rootView = layoutInflater.inflate(R.layout.row_feeback_housedetail,parent, false)
            rootView.findViewById<TextView>(R.id.textViewNomeFeedback).text = feedback[position].id_feedback.toString()
            rootView.findViewById<TextView>(R.id.textViewClassiFeedback).text = feedback[position].classification.toString()
            rootView.findViewById<TextView>(R.id.textViewComentFeedback).text = feedback[position].comment

            return rootView
        }

    }

    inner class ImagePagerAdapter(private val images: List<Int>, activity: AppCompatActivity) :
        FragmentStateAdapter(activity) {

        override fun getItemCount(): Int = images.size

        override fun createFragment(position: Int): Fragment {
            return ImageFragment(images[position])
        }
    }

       class ImageFragment(private val imageResId: Int) : Fragment() {

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.image_view_carrousel, container, false)
            val imageView = view.findViewById<ImageView>(R.id.idIVImage)
            imageView.setImageResource(imageResId)
            return view
        }
    }

}
