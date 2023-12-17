package ipca.utility.bookinghousesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ipca.utility.bookinghousesapp.Models.User
import ipca.utility.bookinghousesapp.databinding.ActivityAdminReservationsListBinding
import ipca.utility.bookinghousesapp.databinding.ActivityAdminUsersListBinding

class AdminReservationsList : AppCompatActivity() {

    private lateinit var binding : ActivityAdminReservationsListBinding
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_reservations_list)
    }
}