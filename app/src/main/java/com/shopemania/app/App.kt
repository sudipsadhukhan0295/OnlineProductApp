package com.shopemania.app

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

class App : Application(){
    override fun onCreate() {
        super.onCreate()
        firestoreDatabase = provideFirestoreDatabase()

    }

    private fun provideFirestoreDatabase(): FirebaseFirestore {
        synchronized(this) {
            return FirebaseFirestore.getInstance()
        }
    }


    companion object {
        lateinit var firestoreDatabase: FirebaseFirestore
    }
}