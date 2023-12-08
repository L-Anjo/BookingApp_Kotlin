package ipca.utility.bookinghousesapp

data class User (
    var id_user : Int,
    var name : String,
    var email : String,
    var password : String,
    var phone : Int,
    var token : String?,
    var status : Boolean
)
