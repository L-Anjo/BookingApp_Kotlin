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
import ipca.utility.bookinghousesapp.Models.User
import ipca.utility.bookinghousesapp.databinding.ActivityAdminUsersListBinding

class AdminUsersList : AppCompatActivity() {

    private lateinit var binding : ActivityAdminUsersListBinding
    private var users = arrayListOf<io.swagger.client.models.User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminUsersListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Backend.GetAllUsers(this, lifecycleScope) { fetchedUsers ->
            users.addAll(fetchedUsers)
            setupListView()
        }

    }

    private fun setupListView() {
        val adapter = UserListAdapter()
        binding.listViewUsers.adapter = adapter
    }

    inner class UserListAdapter : BaseAdapter() {

        override fun getCount(): Int {
            return users.size
        }

        override fun getItem(position: Int): Any {
            return users[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rootView = layoutInflater.inflate(R.layout.row_user,parent, false)
            rootView.findViewById<TextView>(R.id.TextViewNomeUser).text = users[position].name
            rootView.findViewById<TextView>(R.id.TextViewEmailUser).text = users[position].email
            rootView.findViewById<TextView>(R.id.TextViewPhoneUser).text = users[position].phone.toString()
            val avatar = rootView.findViewById<ImageView>(R.id.imageView7)
            val imageUrl = "${Backend.BASE_API}/Users/${users[position].image}${users[position].imageFormat}"
            Glide.with(this@AdminUsersList)
                .asBitmap()
                .load(imageUrl)
                .transition(BitmapTransitionOptions.withCrossFade())
                .transform(CircleCrop())
                .into(avatar)
            return rootView
        }
    }
}