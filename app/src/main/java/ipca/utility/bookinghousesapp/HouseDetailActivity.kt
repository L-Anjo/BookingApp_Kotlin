package ipca.utility.bookinghousesapp

import android.graphics.drawable.Drawable
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
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ipca.utility.bookinghousesapp.Models.Feedback
import ipca.utility.bookinghousesapp.Models.House
import ipca.utility.bookinghousesapp.databinding.ActivityHousedetailBinding
import org.json.JSONArray
import org.json.JSONObject

class HouseDetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHousedetailBinding
    var house : io.swagger.client.models.House? = null
    var usersfeed = mutableListOf<io.swagger.client.models.User>()
    var feedbacks = arrayListOf<io.swagger.client.models.Feedback?>()

    val feedbackAdapter = FeedbackAdapter()
    val imageUrls = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHousedetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.listViewFeebackDetails.adapter = feedbackAdapter

        var count = 0
        val viewPager: ViewPager2 = binding.viewPager
        val tabLayout: TabLayout = binding.tabLayout
        val pagerAdapter = ImagePagerAdapter(imageUrls, this)
        viewPager.adapter = pagerAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
        }.attach()

        Backend.fetchHouseDetail(lifecycleScope) { house ->
            house?.let {
                val displayText = if (it.priceyear != null) {
                    "${it.priceyear}€ / Ano"
                } else {
                    "${it.price}€ / Noite"
                }
                binding.textViewNameDetail.text = it.name
                binding.textViewGuestsDetail.text = "${it.guestsNumber}  Pessoas"
                binding.textViewFloorDetail.text = "${it.floorNumber}  Andar"
                binding.textViewRoomsDetail.text = "${it.rooms}  Quartos"
                binding.textViewRuaDetail.text = it.road
                binding.textViewNMaximoPessoasDetail.text = it.guestsNumber.toString()
                binding.textViewprecoDetail.text = displayText
                binding.textViewAndarDetailD.text = it.floorNumber.toString()
                binding.textViewOwnerDetail.text = it.user?.name

                binding.textViewPrecoNoiteDetail.text = displayText
            }
            house.postalCode?.let {
                binding.textViewLocationDetail.text = it.concelho
                binding.textViewCodigoPostalDetail.text = it.postalCode.toString()
                binding.textViewConcelhoDetail.text = it.concelho
                binding.textViewDistrictDetail.text = it.district
            }
            house.images?.let {
                for (imageName in it) {
                    val imageUrl =
                        "${Backend.BASE_API}/Houses/${imageName.image}.${imageName.formato}"

                    imageUrls.add(imageUrl)
                }
                pagerAdapter.notifyDataSetChanged()
            }
            house.reservations?.let {
                for(reservation in it) {
                    if (count < 5) {
                        reservation.feedback?.let {
                            reservation.user?.let { user ->
                                usersfeed.add(user)
                            }
                            feedbacks.add(reservation.feedback)
                            count++
                        }
                    }
                }
            }
            feedbackAdapter.notifyDataSetChanged()
        }

    }

    inner class FeedbackAdapter : BaseAdapter(){
        override fun getCount(): Int {
            return feedbacks.size
        }

        override fun getItem(position: Int): Any {
            return feedbacks[position]!!
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rootView = layoutInflater.inflate(R.layout.row_feeback_housedetail,parent, false)
            rootView.findViewById<TextView>(R.id.textViewNomeFeedback).text = usersfeed[position]?.name
            rootView.findViewById<TextView>(R.id.textViewClassiFeedback).text = feedbacks[position]?.classification.toString()
            rootView.findViewById<TextView>(R.id.textViewComentFeedback).text = feedbacks[position]?.comment
            var image = "${Backend.BASE_API}/Users/${usersfeed[position].image}${usersfeed[position].imageFormat}"

            Glide.with(rootView)
                .load(image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(rootView.findViewById<ImageView>(R.id.imageView4))

            return rootView
        }

    }

    inner class ImagePagerAdapter(private val imageUrls: List<String>, activity: FragmentActivity) :
        FragmentStateAdapter(activity) {

        override fun getItemCount(): Int = imageUrls.size

        override fun createFragment(position: Int): Fragment {
            return ImageFragment(imageUrls[position])
        }
    }

    class ImageFragment(private val imageUrl: String) : Fragment() {

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            Log.d("ImageFragment", "onCreateView called")
            val view = inflater.inflate(R.layout.image_view_carrousel, container, false)
            val imageView = view.findViewById<ImageView>(R.id.idIVImage)


            // Carregar a imagem usando Glide a partir da URL
            Glide.with(this)
                .load(imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)


            return view
        }
    }

}
