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




        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            Log.d("email", email)
            Log.d("password", password)
            if (email.isNotEmpty() && password.isNotEmpty()) {
                Backend.login(email, password) { success, message ->

                    if (success) {
                        Log.d("LoginActivity", "Login bem-sucedido")
                    } else {
                        Log.d("LoginActivity", "Login falhou: $message")
                    }
                }
            } else {

            }
        }

        binding.textViewRegister.setOnClickListener{
            val intent = Intent(this,RegisterActivity::class.java )
            startActivity(intent)
        }
    }
}