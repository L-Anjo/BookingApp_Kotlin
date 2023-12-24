package ipca.utility.bookinghousesapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import ipca.utility.bookinghousesapp.Models.House
import ipca.utility.bookinghousesapp.Models.PostalCode
import ipca.utility.bookinghousesapp.Models.User
import ipca.utility.bookinghousesapp.databinding.ActivityAdminUsersListBinding
import ipca.utility.bookinghousesapp.databinding.ActivityHousedetailBinding
import android.widget.Button
import android.widget.ImageView
import ipca.utility.bookinghousesapp.Models.Image
import ipca.utility.bookinghousesapp.databinding.ActivityAdminHousesApproveListBinding


class AdminHousesListApprove : AppCompatActivity() {

    private lateinit var binding:ActivityAdminHousesApproveListBinding
    private var houses = arrayListOf<io.swagger.client.models.House>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminHousesApproveListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageViewBack.setOnClickListener{
            onBackPressed()
        }

        // Chamada para buscar todas as casas ativas
        Backend.fetchAllHousesSusp(lifecycleScope) { fetchedHouses ->
            houses.addAll(fetchedHouses)
            setupListView()
        }
    }

    private fun setupListView() {
        val adapter = HouseListAdapter()
        binding.listViewHousesSusp.adapter = adapter
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
            val rootView = layoutInflater.inflate(R.layout.row_house_approve,parent, false)
            rootView.findViewById<TextView>(R.id.textViewHouseName).text = houses[position].name
            rootView.findViewById<TextView>(R.id.textViewRuaHouse).text = houses[position].road
            rootView.findViewById<TextView>(R.id.textViewNumbP).text = "${houses[position].guestsNumber.toString()}  Pessoas"

            val approveButton = rootView.findViewById<ImageView>(R.id.buttonAproveHouse)
            approveButton.setOnClickListener {
                val id_house = houses[position].id_house ?: -1 // Assuming -1 as default ID if null

                print("ID DA HOUSE:")
                println(houses[position].id_house)
                println(id_house)

                // Perform the action to approve the house here
                Backend.updateHouseStateApproved(id_house, lifecycleScope) {
                    // Remove the house from the list upon successful approval
                    houses.removeAt(position)
                    notifyDataSetChanged() // Notify adapter that data set has changed
                }
            }

            val declineButton = rootView.findViewById<ImageView>(R.id.buttonDecHouse)
            declineButton.setOnClickListener {
                val id_house = houses[position].id_house ?: -1 // Assuming -1 as default ID if null

                // Perform the action to decline (delete) the house here
                Backend.deleteHouseById(id_house, lifecycleScope) {
                    // Remove the house from the list upon successful deletion
                    houses.removeAt(position)
                    notifyDataSetChanged() // Notify adapter that data set has changed
                }
            }

            val seeMoreButton = rootView.findViewById<Button>(R.id.buttonSeeMoreHouse)
            seeMoreButton.setOnClickListener {
                val houseId = houses[position].id_house ?: -1 // Assuming -1 as default ID if null

                val intent = Intent(rootView.context, HouseDetailActivity::class.java)
                intent.putExtra("HOUSE_ID", houseId)
                rootView.context.startActivity(intent)
            }

            return rootView
        }
    }

}
