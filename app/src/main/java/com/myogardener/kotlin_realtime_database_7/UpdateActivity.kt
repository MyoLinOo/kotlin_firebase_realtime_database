package com.myogardener.kotlin_realtime_database_7

import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_next.*
import kotlinx.android.synthetic.main.activity_update.*

class UpdateActivity : AppCompatActivity() {
    lateinit var ref: DatabaseReference
    private var fuser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        fuser = FirebaseAuth.getInstance().currentUser
        ref = FirebaseDatabase.getInstance().getReference("users")

        val data =intent.getStringExtra("key")

btn_update_next.setOnClickListener {
updateData(data.toString())
        }

        val passedArg = intent.extras!!.getString("arg")

        edt_update.setText(passedArg)
    }
    fun updateData(key:String){
        var age = edt_update.text.toString()

if(age.isEmpty()){
    Toast.makeText(applicationContext,"Please Enter your age",Toast.LENGTH_SHORT).show()
}else{
    Toast.makeText(applicationContext,"update successfully",Toast.LENGTH_LONG).show()

    ref!!.child(fuser!!.uid).child("Data").child(key).child("age").setValue(age)
finish()
}



    }


}