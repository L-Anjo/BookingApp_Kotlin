package ipca.utility.bookinghousesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHousedetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.listViewFeebackDetails.adapter = feedbackAdapter

        Backend.fetchHouseDetail(lifecycleScope) { house,postalCode ->
            house?.let {
                binding.textViewNameDetail.text = it.name
                binding.textViewGuestsDetail.text = "${it.guestsNumber}  Pessoas"
                binding.textViewFloorDetail.text = "${it.floorNumber}  Andar"
                binding.textViewRoomsDetail.text = "${it.rooms}  Quartos"
                binding.textViewNMaximoPessoasDetail.text = it.guestsNumber.toString()
                binding.textViewprecoDetail.text = it.price.toString()
                binding.textViewAndarDetailD.text = it.floorNumber.toString()
                val displayText = if (it.priceYear != 0.0) {
                    "${it.priceYear} / Ano"
                } else {
                    "${it.price} / Noite"
                }
                binding.textViewPrecoNoiteDetail.text = displayText
            }
            postalCode?.let{
                binding.textViewLocationDetail.text = it.district
                binding.textViewConcelhoDetail.text = it.concelho
                binding.textViewDistrictDetail.text = it.district
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
}
