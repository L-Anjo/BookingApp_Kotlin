package ipca.utility.bookinghousesapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import ipca.utility.bookinghousesapp.databinding.ActivityLoginBinding
import ipca.utility.bookinghousesapp.databinding.ActivityProfilePageBinding

class ProfilePageActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProfilePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val userType = sharedPreferences.getInt("user_type", 0)
        if (userType == 3) {
            binding.constraintLayoutAdminProfile.visibility = View.VISIBLE
        } else {
            binding.constraintLayoutAdminProfile.visibility = View.GONE
        }
        Backend.fetchUserDetail(this, lifecycleScope) { user ->
            user?.let {
                binding.textViewUserName.text = it.name
                binding.textViewUserEmail.text = it.email

                val imageUrl = "${Backend.BASE_API}/Users/${user.image}${user.imageFormat}"
                Glide.with(this@ProfilePageActivity)
                    .asBitmap()
                    .load(imageUrl)
                    .transition(BitmapTransitionOptions.withCrossFade())
                    .transform(CircleCrop())
                    .into(binding.imageView11)
            }
        }

        binding.constraintLayoutEditProfile.setOnClickListener{
            val intent = Intent(this,EditProfileActivity::class.java )
            startActivity(intent)
        }

        binding.constraintLayoutLogout.setOnClickListener {
            Backend.logout(this, lifecycleScope) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
                Log.d("LogoutActivity", "Logout bem-sucedido!")
            }
        }

        binding.constraintLayoutAdminProfile.setOnClickListener{
            val intent = Intent(this,AdminProfilePage::class.java )
            startActivity(intent)
        }
    }
}