package com.filiplike.powermask

import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.IBinder
import android.os.PersistableBundle
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CloudService : JobService() {

    override fun onStartJob(params: JobParameters?): Boolean {
        JobAsyncTask().execute(params!!.extras)
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {

        Log.d(ContentValues.TAG,"Cloud service job stopped")
        return true
    }

    private class JobAsyncTask: AsyncTask<PersistableBundle, Int, Int>() {
        override fun doInBackground(vararg params: PersistableBundle): Int? {
            val firebase = Firebase.firestore
            val bundle = params[0]

            val strings = bundle.getString("data")!!.split('|')
            val map:Map<String, Int> = strings.mapIndexed { index, s -> s to index }.toMap()
                //upload map to Firestore
            firebase.collection("users").document(bundle.getString("user", "notLoggedUser")).set(map, SetOptions.merge())
                .addOnSuccessListener {
                    //Toast.makeText(cont, "uploaded", Toast.LENGTH_SHORT).show()
                    Log.d(ContentValues.TAG,"Uploading array succeed")
                }
                .addOnFailureListener {
                    //throwing exception
                    Log.d(ContentValues.TAG,"Uploading array failed")
                    //   Toast.makeText(cont, "upload failed", Toast.LENGTH_SHORT).show()
                }
            return 0
        }
    }
}