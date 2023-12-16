package ipca.utility.bookinghousesapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import ipca.utility.bookinghousesapp.databinding.ActivityHousedetailBinding
import ipca.utility.bookinghousesapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)




        binding.buttonLogin.setOnClickListener{
            val editTextEmail = binding.editTextEmail.text.toString()
            val editTextPassword = binding.editTextPassword.text.toString()

            Log.d("email", editTextEmail)
            Log.d("password", editTextPassword)
        }

        binding.textViewRegister.setOnClickListener{
            val intent = Intent(this,RegisterActivity::class.java )
            startActivity(intent)
        }
    }
}