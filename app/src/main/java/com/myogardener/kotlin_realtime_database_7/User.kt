package com.myogardener.kotlin_realtime_database_7

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize


class User {
    var key:String?= ""
    var age: String? = ""

    constructor() {}

    constructor(key:String,age: String?) {
        this.key= key
        this.age = age

    }
}

