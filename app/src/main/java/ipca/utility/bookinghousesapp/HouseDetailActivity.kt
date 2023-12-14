package ipca.utility.bookinghousesapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
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

    val imageList = listOf(R.drawable.baseline_house_24,R.drawable.baseline_person_outline_24)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHousedetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.listViewFeebackDetails.adapter = feedbackAdapter

        val viewPager: ViewPager = binding.viewPager
        val PagerAdapter = ImagePagerAdapter(imageList)
        viewPager.adapter = PagerAdapter

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

    inner class ImagePagerAdapter(private val images: List<Int>) : PagerAdapter() {

        override fun getCount(): Int {
            return images.size
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.image_view_carrousel, container, false)

            val imageView = view.findViewById<ImageView>(R.id.idIVImage)
            imageView.setImageResource(images[position])

            container.addView(view)
            return view
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view == obj
        }

        override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
            container.removeView(obj as View)
        }
    }
}
