package com.myogardener.kotlin_realtime_database_7

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_next.*


class NextActivity : AppCompatActivity() {

    private var fuser: FirebaseUser? = null
    lateinit var ref: DatabaseReference
    private var mAdapter: FirebaseRecyclerAdapter<User, UserHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next)

        fuser = FirebaseAuth.getInstance().currentUser
        ref = FirebaseDatabase.getInstance().getReference("users")

        btn_set.setOnClickListener {
            saveData()
        }
        btn_logout.setOnClickListener {
            logout()
        }
        btn_delete.setOnClickListener {
            ref!!.child(fuser!!.uid).child("Data").removeValue()
        }

//        ref.child("1").addValueEventListener(object : ValueEventListener {
//            override fun onCancelled(p0: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onDataChange(p0: DataSnapshot) {
//                if (p0!!.exists()) {
//
//
//                    val message = p0.getValue()
//                    txt_age.text = message.toString()
//
//            }
//            }
//        })

        val query = ref.child(fuser!!.uid).child("Data").limitToLast(8)

        val childEventListener: ChildEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val message = dataSnapshot.getValue(User::class.java)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val message = dataSnapshot.getValue(User::class.java)
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                val message = dataSnapshot.getValue(User::class.java)
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val message = dataSnapshot.getValue(User::class.java)
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }

        query.addChildEventListener(childEventListener)

        val options = FirebaseRecyclerOptions.Builder<User>().setQuery(query, User::class.java).setLifecycleOwner(this).build()

        mAdapter = object : FirebaseRecyclerAdapter<User, UserHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
                return UserHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false))
            }

            override fun onBindViewHolder(holder: UserHolder, position: Int, model: User) {
                holder.bind(model)
                holder.itemView.setOnClickListener{
//Toast.makeText(applicationContext,"delete successfully",Toast.LENGTH_LONG).show()
//                    deleteData(model.key.toString())
showDialogMenu(model)

//                    val intent = Intent(this@NextActivity,UpdateActivity::class.java)
//                    intent.putExtra("age",model.age)
//                    intent.putExtra("key",model.key)
//                    startActivity(intent)
                                   }
            }
        }

        val layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        layoutManager.reverseLayout = false
        recyclerview.setHasFixedSize(true)
        recyclerview.layoutManager = layoutManager
        recyclerview.adapter = mAdapter
        }

    override fun onStart() {
        super.onStart()
        mAdapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        mAdapter!!.stopListening()
    }

    fun saveData(){

        var age = edt_age.text.toString()

        if (age.isEmpty()) {
            Toast.makeText(applicationContext,"Please Enter your age", Toast.LENGTH_SHORT).show()
        }else{

            var key = ref!!.child(fuser!!.uid).child("Data").push().key.toString()
            var user = User(key,age)

            ref.child(fuser!!.uid).child("Data").child(key).setValue(user)
            edt_age.setText("")
        }
    }

    fun deleteData(key:String){

                ref!!.child(fuser!!.uid).child("Data").child(key).removeValue()

            }

    private fun showDialogMenu(users: User) {
        //dialog popup edit hapus
        val builder = AlertDialog.Builder(this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
        val option = arrayOf("Edit  ?", "Delete  ?")
        builder.setItems(option) { dialog, which ->
            when (which) {
                0 -> this.startActivity(Intent(applicationContext, UpdateActivity::class.java).apply {

                    putExtra("key",users.key)

                })
                1 -> deleteData(users.key.toString())
            }
        }

        builder.create().show()
    }

    fun logout(){
        FirebaseAuth.getInstance().signOut()
        finish()
    }
}

//class NextActivity : AppCompatActivity() {
//
//  private var fuser: FirebaseUser? = null
//    lateinit var ref:DatabaseReference
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_next)
//
//        fuser= FirebaseAuth.getInstance().currentUser
//        ref=FirebaseDatabase.getInstance() .getReference("users")
//
//    ref.child("1").addValueEventListener(object :ValueEventListener{
//    override fun onCancelled(p0: DatabaseError) {
//        TODO("Not yet implemented")
//    }
//
//    override fun onDataChange(p0: DataSnapshot) {
//    if(p0!!.exists()){
//
//
//val message= p0.getValue()
//        txt_age.text = message.toString()
//
//        }
//    }
//})
//
//        btn_logout.setOnClickListener {
//            logout()
//        }
//        btn_set.setOnClickListener {
//            saveData()
//        }
//        btn_update.setOnClickListener {
//            updateData()
//        }
//        btn_delete.setOnClickListener {
//            deleteData()
//        }
//    }
//
//


//
//    fun updateData(){
//        var genter = edt_genter.text.toString()
//
//        var map = mutableMapOf<String,Any>()
//
//        map["genter"]= genter
//
//        FirebaseDatabase.getInstance().reference
//            .child("users")
//            .child("1")
//            .updateChildren(map)
//    }

//}