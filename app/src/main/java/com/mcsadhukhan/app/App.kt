package com.mcsadhukhan.app

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class App : Application(){
    override fun onCreate() {
        super.onCreate()
        firestoreDatabase = provideFirestoreDatabase()
        firebaseAuth = FirebaseAuth.getInstance()

    }


    private fun provideFirestoreDatabase(): FirebaseFirestore {
        synchronized(this) {
            return FirebaseFirestore.getInstance()
        }
    }


    companion object {
        lateinit var firestoreDatabase: FirebaseFirestore
        lateinit var firebaseAuth : FirebaseAuth
    }
}