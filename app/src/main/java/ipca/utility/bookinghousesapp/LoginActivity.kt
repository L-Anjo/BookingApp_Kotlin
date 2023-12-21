package ipca.utility.bookinghousesapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import android.util.Log
import android.content.Context
import android.widget.EditText
import io.swagger.client.apis.AuthApi
import ipca.utility.bookinghousesapp.databinding.ActivityHousedetailBinding
import ipca.utility.bookinghousesapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener{
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            Backend.login(this, lifecycleScope, email, password) { loginSuccessful ->
                if (loginSuccessful) {

                    val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                    val token = sharedPreferences.getString("access_token", null)
                    val userId = sharedPreferences.getInt("user_id", 0)
                    val userType = sharedPreferences.getInt("user_type", 0)
                    val intent = Intent(this, ProfilePageActivity::class.java )
                    startActivity(intent)
                    Log.d("LoginActivity", "Login bem-sucedido!")
                    Log.d("token do login", token.toString())
                    Log.d("id do utilizador", userId.toString())
                    Log.d("id do tipo de utilizador", userType.toString())
                } else {
                    binding.textViewError.text = "Credenciais inválidas. Tente novamente."
                }
            }
        }

        binding.textViewRegister.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java )
            startActivity(intent)
        }
    }
}