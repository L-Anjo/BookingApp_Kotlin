package ipca.utility.bookinghousesapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import ipca.utility.bookinghousesapp.Models.House
import ipca.utility.bookinghousesapp.Models.PostalCode
import ipca.utility.bookinghousesapp.Models.User
import ipca.utility.bookinghousesapp.databinding.ActivityAdminUsersListBinding
import ipca.utility.bookinghousesapp.databinding.ActivityHousedetailBinding
import android.widget.Button
import android.widget.CheckBox
import androidx.lifecycle.LifecycleCoroutineScope
import ipca.utility.bookinghousesapp.Backend
import ipca.utility.bookinghousesapp.databinding.ActivityCreateHouseBinding



class CreateHouse : AppCompatActivity() {

    private lateinit var binding: ActivityCreateHouseBinding // Substitua pelo seu layout de criação de casas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateHouseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageViewBackH2.setOnClickListener{
            onBackPressed()
        }

        val buttonCreateHouse =
            binding.buttonCreateHouse // Supondo que o botão esteja definido no layout

        // Configurando o botão de criação de casa
        setupCreateHouseButton(buttonCreateHouse, lifecycleScope)
    }

    fun setupCreateHouseButton(buttonCreateHouse: Button, lifecycleScope: LifecycleCoroutineScope) {
        buttonCreateHouse.setOnClickListener {
            createHouseObjectFromUI { body ->
                Backend.createHouse(this,body, lifecycleScope) {
                    // Callback chamado ao concluir a criação da casa
                    // Se você chegou aqui, significa que a casa foi criada com sucesso
                    // Adicione aqui a lógica para lidar com o sucesso da criação
                }
                val intent = Intent(this,UserHousesList::class.java)
                startActivity(intent)
            }
        }

    }



    private fun createHouseObjectFromUI(onHouseCreated: (io.swagger.client.models.House) -> Unit) {
        // ... outros dados da interface ...

        val houseName = binding.editTextNameHouse.text.toString()
        val numbPessoas = binding.editTextNumberPHouse.text.toString().toInt()
        val district = binding.editTextDistrictHouse.text.toString()
        val concelho = binding.editTextConcelhoHouse.text.toString()
        val street = binding.editTextStreetHouse.text.toString()
        val postalCodeValue = binding.editTextPostalCodeHouse.text.toString().toInt()

        // Crie um objeto PostalCode do tipo io.swagger.client.models.PostalCode
        val postalCode = io.swagger.client.models.PostalCode(
            // Preencha os campos do PostalCode
            postalCode = postalCodeValue,
            concelho = concelho,
            district = district,
        )

        val numbRooms = binding.editTextNumberRoomsHouse.text.toString().toInt()
        val numbFloor = binding.editTextNumberFloorHouse.text.toString().toInt()
        val doorNumber = binding.editTextNumberDoorHouse.text.toString().toInt()
        val price = binding.editTextNumberPriceHouse.text.toString().toDouble()
        val propertyAssessment = binding.editTextpropertyAssessment.text.toString()

        println(houseName)
        println(numbPessoas)
        println(district)
        println(concelho)
        println(street)
        println(postalCodeValue)
        println(numbRooms)
        println(numbFloor)
        println(doorNumber)
        println(price)

        // Verifique se a CheckBox está marcada para preço anual
        val isAnnualPrice = binding.checkBoxAnualPrice.isChecked
        val isSharedRoom = binding.checkBoxSharedRoom.isChecked

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
            val body = io.swagger.client.models.House(
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

            println(body)

            // Chame o callback passando o objeto newHouse
            onHouseCreated(body)
        }
    }
}
