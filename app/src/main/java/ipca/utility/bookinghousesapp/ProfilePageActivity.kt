package ipca.utility.bookinghousesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ipca.utility.bookinghousesapp.databinding.ActivityLoginBinding
import ipca.utility.bookinghousesapp.databinding.ActivityProfilePageBinding

class ProfilePageActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProfilePageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.constraintLayoutLogout.setOnClickListener{
            val intent = Intent(this,EditProfileActivity::class.java )
            startActivity(intent)
        }
    }
}