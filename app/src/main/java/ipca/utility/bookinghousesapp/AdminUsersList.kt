package ipca.utility.bookinghousesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import ipca.utility.bookinghousesapp.Models.User
import ipca.utility.bookinghousesapp.databinding.ActivityAdminUsersListBinding

class AdminUsersList : AppCompatActivity() {

    private lateinit var binding : ActivityAdminUsersListBinding
    var users = arrayListOf<User>(
        User(1, "Pedro", "pedro@gmail.com", "password", 12345, "token", true),
        User(2, "Luis", "luis@gmail.com", "password2", 67890, "token2", true),
        User(3, "Diogo", "diogo@gmail.com", "password3", 19534, "token3", true),
        User(3, "Diogo", "diogo@gmail.com", "password3", 19534, "token3", true),
        User(3, "Diogo", "diogo@gmail.com", "password3", 19534, "token3", true),
        User(3, "Diogo", "diogo@gmail.com", "password3", 19534, "token3", true),
        User(3, "Diogo", "diogo@gmail.com", "password3", 19534, "token3", true),
        User(3, "Diogo", "diogo@gmail.com", "password3", 19534, "token3", true),
        User(3, "Diogo", "diogo@gmail.com", "password3", 19534, "token3", true),
        User(3, "Diogo", "diogo@gmail.com", "password3", 19534, "token3", true),
        User(3, "Diogo", "diogo@gmail.com", "password3", 19534, "token3", true),
        User(3, "Diogo", "diogo@gmail.com", "password3", 19534, "token3", true),
        User(3, "Diogo", "diogo@gmail.com", "password3", 19534, "token3", true),
        User(3, "Diogo", "diogo@gmail.com", "password3", 19534, "token3", true),

    )

    val listUserAdapter = UsersListAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminUsersListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.listViewUsers.adapter = listUserAdapter
    }

    inner class UsersListAdapter : BaseAdapter(){
        override fun getCount(): Int {
            return users.size
        }

        override fun getItem(position: Int): Any {
            return users[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rootView = layoutInflater.inflate(R.layout.row_user,parent, false)
            rootView.findViewById<TextView>(R.id.TextViewNomeUser).text = users[position].name
            rootView.findViewById<TextView>(R.id.TextViewEmailUser).text = users[position].email
            rootView.findViewById<TextView>(R.id.TextViewPhoneUser).text = users[position].phone.toString()

            return rootView
        }

    }
}