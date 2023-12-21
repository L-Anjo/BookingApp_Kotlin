package ipca.utility.bookinghousesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import ipca.utility.bookinghousesapp.databinding.ActivityLoginBinding
import ipca.utility.bookinghousesapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonRegister.setOnClickListener{
            val editTextName = binding.editTextName.text.toString()
            val editTextEmail = binding.editTextEmail.text.toString()
            val editTextPassword = binding.editTextPassword.text.toString()
            val editTextPhone = binding.editTextPhone.text.toString().toInt()

            Backend.CreateUser(lifecycleScope, editTextName, editTextEmail, editTextPassword, editTextPhone) { createSuccessful ->
                if (createSuccessful) {
                    val intent = Intent(this, LoginActivity::class.java )
                    startActivity(intent)
                    Log.d("CreateUserActivity", "Criação bem-sucedido!")

                } else {
                    binding.textViewErrorRegister.text = "Credenciais inválidas. Tente novamente."
                }
            }
        }

        binding.textViewLogin.setOnClickListener{
            val intent = Intent(this,LoginActivity::class.java )
            startActivity(intent)
        }
    }
}