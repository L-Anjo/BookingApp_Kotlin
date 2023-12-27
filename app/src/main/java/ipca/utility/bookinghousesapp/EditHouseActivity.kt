package ipca.utility.bookinghousesapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import ipca.utility.bookinghousesapp.Backend
import ipca.utility.bookinghousesapp.ResultWrapper
import ipca.utility.bookinghousesapp.databinding.ActivityEditHouseBinding
import io.swagger.client.models.House
import ipca.utility.bookinghousesapp.ProfilePageActivity
import kotlinx.coroutines.launch

class EditHouseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditHouseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditHouseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val houseId = intent.extras?.getInt("HOUSE_ID")?:-1 // Assuming you pass house ID via Intent

        // Fetch house details
        fetchHouseDetails(houseId)

        binding.imageViewBackH2.setOnClickListener {
            onBackPressed()
        }

        binding.buttonEditHouse.setOnClickListener {
            val houseName = binding.editTextEditNameHouse.text.toString()
            val numbPessoas = binding.editTextEditNumberPHouse.text.toString().toInt()
            val district = binding.editTextEditDistrictHouse.text.toString()
            val concelho = binding.editTextEditConcelhoHouse.text.toString()
            val street = binding.editTextEditStreetHouse.text.toString()
            val postalCodeValue = binding.editTextEditPostalCodeHouse.text.toString().toInt()


            // Crie um objeto PostalCode do tipo io.swagger.client.models.PostalCode
            val postalCode = io.swagger.client.models.PostalCode(
                // Preencha os campos do PostalCode
                postalCode = postalCodeValue,
                concelho = concelho,
                district = district,
            )

            val numbRooms = binding.editTextEditNumberRoomsHouse.text.toString().toInt()
            val numbFloor = binding.editTextEditNumberFloorHouse.text.toString().toInt()
            val doorNumber = binding.editTextEditNumberDoorHouse.text.toString().toInt()
            val price = binding.editTextEditNumberPriceHouse.text.toString().toDouble()
            val propertyAssessment = binding.editTextEditpropertyAssessment.text.toString()

            // Verifique se a CheckBox está marcada para preço anual
            val isAnnualPrice = binding.checkBoxEditAnualPrice.isChecked
            val isSharedRoom = binding.checkBoxEditSharedRoom.isChecked

            val sharedPreferences = this.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            val id_user = sharedPreferences.getInt("user_id", 0)
            //val id_user = 2 // ou qualquer outra maneira de obter o ID do usuário

            Backend.fetchUserDetail(this, lifecycleScope, id_user) { user ->
                // ... Seus dados anteriores ...
                println("USER:")
                println(id_user)
                println(user)
                println(user.id_user)

                // Crie um objeto House com os dados coletados da UI e o usuário obtido
                val houseEdited = io.swagger.client.models.House(
                    name = houseName,
                    doorNumber = doorNumber,
                    guestsNumber = numbPessoas,
                    postalCode = postalCode,
                    floorNumber = numbFloor,
                    rooms = numbRooms,
                    road = street,
                    user = user,
                    propertyAssessment = propertyAssessment,

                    sharedRoom = isSharedRoom,

                    // Decida qual campo de preço atualizar com base no estado da CheckBox
                    price = if (isAnnualPrice) null else price,
                    priceyear = if (isAnnualPrice) price else null,
                )

                println(houseEdited)
                // Agora, você precisa passar esses valores para a API
                lifecycleScope.launch {
                    try {
                        Backend.editHouse(houseId, houseEdited)
                    } catch (e: Exception) {
                        // Tratar exceções aqui
                        Log.e("EditHouseActivity", "Erro ao editar a casa: ${e.message}")
                    }
                }

                val intent = Intent(this,UserHousesList::class.java)
                startActivity(intent)
            }
        }
    }
    private fun fetchHouseDetails(houseId: Int) {

        val houseDetailLiveData: LiveData<ResultWrapper<House>> = Backend.fetchHouseDetail(houseId)

        houseDetailLiveData.observe(this) { result ->
            when (result) {
                is ResultWrapper.Success -> {
                    val houseDetails = result.value

                    // Fill EditText fields with house details
                    fillEditTextFields(houseDetails)
                }
                is Error -> {

                }
                is ResultWrapper.Error -> {
                    // Handle error case if needed
                }
                is ResultWrapper.NetworkError -> {
                    // Lidar com erros de rede, se necessário
                }
            }
        }
    }

    private fun fillEditTextFields(houseDetails: House) {
        binding.editTextEditNameHouse.text = Editable.Factory.getInstance().newEditable(houseDetails.name)
        binding.editTextEditNumberPHouse.text = Editable.Factory.getInstance().newEditable(houseDetails.guestsNumber.toString())
        binding.editTextEditDistrictHouse.text = Editable.Factory.getInstance().newEditable(houseDetails.postalCode?.district.toString())
        binding.editTextEditConcelhoHouse.text = Editable.Factory.getInstance().newEditable(houseDetails.postalCode?.concelho.toString())
        binding.editTextEditPostalCodeHouse.text = Editable.Factory.getInstance().newEditable(houseDetails.postalCode?.postalCode.toString())
        binding.editTextEditStreetHouse.text = Editable.Factory.getInstance().newEditable(houseDetails.road.toString())
        binding.editTextEditNumberRoomsHouse.text = Editable.Factory.getInstance().newEditable(houseDetails.rooms.toString())
        binding.editTextEditNumberFloorHouse.text = Editable.Factory.getInstance().newEditable(houseDetails.floorNumber.toString())
        binding.editTextEditNumberDoorHouse.text = Editable.Factory.getInstance().newEditable(houseDetails.doorNumber.toString())
        binding.editTextEditpropertyAssessment.text = Editable.Factory.getInstance().newEditable(houseDetails.propertyAssessment.toString())

        if(houseDetails.price == null) {
            binding.checkBoxEditAnualPrice.isChecked = true
            binding.editTextEditNumberPriceHouse.text = Editable.Factory.getInstance().newEditable(houseDetails.priceyear.toString())
        }
        else{
            binding.checkBoxEditAnualPrice.isChecked = false
            binding.editTextEditNumberPriceHouse.text = Editable.Factory.getInstance().newEditable(houseDetails.price.toString())
        }

        if(houseDetails.sharedRoom == true){
            binding.checkBoxEditSharedRoom.isChecked = true
        }
        else{
            binding.checkBoxEditSharedRoom.isChecked = false
        }

    }


}