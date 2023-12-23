package ipca.utility.bookinghousesapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import android.util.Log
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.Manifest
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import io.swagger.client.apis.AuthApi
import ipca.utility.bookinghousesapp.databinding.ActivityHousedetailBinding
import ipca.utility.bookinghousesapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            askNotificationPermission()
            Backend.login(this, lifecycleScope, email, password) { loginSuccessful ->
                if (loginSuccessful) {

                    val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                    val token = sharedPreferences.getString("access_token", null)
                    val userId = sharedPreferences.getInt("user_id", 0)
                    val userType = sharedPreferences.getInt("user_type", 0)
                    val intent = Intent(this, ProfilePageActivity::class.java)
                    startActivity(intent)
                    Log.d("LoginActivity", "Login bem-sucedido!")
                    Log.d("token do login", token.toString())
                    Log.d("id do utilizador", userId.toString())
                    Log.d("id do tipo de utilizador", userType.toString())

                    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            Log.w("Teste", "Fetching FCM registration token failed", task.exception)
                            return@OnCompleteListener
                        }
                        val token = task.result

                        //FirebaseApp.initializeApp(this)
                        val db = FirebaseFirestore.getInstance()
                        val tokenData = hashMapOf("token" to token)
                        db.collection("tokens").document(userId.toString())
                            .set(tokenData)
                            .addOnSuccessListener {
                                Log.d("Teste", "Token salvo no Firestore com ID: ${userId}")
                            }
                            .addOnFailureListener { e ->
                                Log.w("Teste", "Erro ao salvar token no Firestore", e)
                            }
                    })
                } else {
                    binding.textViewError.text = "Credenciais inválidas. Tente novamente."
                }
            }
        }

        binding.textViewRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {

        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {


            }
        }
    }
}