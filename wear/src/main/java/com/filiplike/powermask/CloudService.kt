package com.filiplike.powermask

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.ContentValues
import android.os.AsyncTask
import android.os.PersistableBundle
import android.util.Log
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
            val map:Map<String, Int> = strings.mapIndexed { index, s -> s to index+1 }.toMap()

            val user = bundle.getString("user", "unregisteredUser")

            Log.d(ContentValues.TAG, "Uploading data: ${map.entries} user: $user")

            if (strings.isNotEmpty() &&  user != "") {
                //upload map to Firestore
                firebase.collection("users").document(user!!).collection("sessions")
                    .document(strings.first())
                    .set(map, SetOptions.merge())
                    .addOnSuccessListener {
                        //Toast.makeText(cont, "uploaded", Toast.LENGTH_SHORT).show()
                        Log.d(ContentValues.TAG, "Uploading array succeed")
                    }
                    .addOnFailureListener {
                        //throwing exception
                        Log.d(ContentValues.TAG, "Uploading array failed")
                        //   Toast.makeText(cont, "upload failed", Toast.LENGTH_SHORT).show()
                    }
            }
            return 0
        }
    }
}