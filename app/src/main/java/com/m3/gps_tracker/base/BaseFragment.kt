package com.m3.islami2.base

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

open class BaseFragment:Fragment() {



    fun makeToast(message:String){
        Toast.makeText(context,message, Toast.LENGTH_LONG).show()



    }
    fun makeToast(messageID:Int){
        Toast.makeText(context,messageID, Toast.LENGTH_LONG).show()



    }


    fun showDialoge(title:String?=null,
                    message: String,
                    posActionNames:String?=null,
                    posAction:DialogInterface.OnClickListener?=null,
                    negActionNames:String?=null,
                    negAction:DialogInterface.OnClickListener?=null){

        val builder= androidx.appcompat.app.AlertDialog.Builder(requireContext())
        builder.setMessage(message)
        builder.setTitle(title)
        builder.setPositiveButton(posActionNames,posAction)
        builder.setNegativeButton(negActionNames,negAction)
    }
    fun showDialoge(titleID: Int?=null,
                    messageID: Int,
                    posActionNames:Int?=null,
                    posAction:DialogInterface.OnClickListener?=null,
                    negActionNames:Int?=null,
                    negAction:DialogInterface.OnClickListener?=null){

        val builder= androidx.appcompat.app.AlertDialog.Builder(requireContext())
        builder.setMessage(messageID)
        if (titleID != null) {
            builder.setTitle(titleID)}

        if (posActionNames != null) {
            builder.setPositiveButton(posActionNames,posAction)
        }

        if (negActionNames != null) {
            builder.setNegativeButton(negActionNames,negAction)
        }
    }
///    fun makeSnackbar(messageID: Int,
//                     actionString:Int?=null,
//                     click:View.OnClickListener?=null){
//        val snackbar=Snackbar.make(root,messageID,Snackbar.LENGTH_LONG)
//        if (actionString!=null){
//            snackbar.setAction(actionString,click)
//            }
//        snackbar.show()}
//
//    fun makeSnackbar(message: String,
//                     actionString:String?=null,
//                     click:View.OnClickListener?=null){
//        val snackbar=Snackbar.make(root,message,Snackbar.LENGTH_LONG)
//        if (actionString!=null){
//            snackbar.setAction(actionString,click)
//        }
//        snackbar.show()}
        }

