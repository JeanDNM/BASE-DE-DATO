package com.example.basededatos

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler (context : Context) : SQLiteOpenHelper(context, DATABASE_NAME, null,DATABASE_VERSION) {

    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "MyDataBase"
        private  const val TABLE_NAME = "Contact"
        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_EMAIL = "email"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_NAME($KEY_ID INTEGER PRIMARY KEY, $KEY_NAME TEXT, $KEY_EMAIL TEXT)")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)

    }
    fun addcontact(name:String, email:String): Long{
        val db = this.writableDatabase
        val value = ContentValues()
        value.put(KEY_NAME,name)
        value.put(KEY_EMAIL,email)
        val sucess =  db.insert(TABLE_NAME, null,value)
        db.close()
        return(sucess)
    }
   fun getAllContacts() : List<Contact> {

       val contactList = mutableListOf<Contact>()
       val db = this.readableDatabase
       val selectquery = "SELECT * FROM $TABLE_NAME"
       val cursor = db.rawQuery(selectquery,null)

       cursor.use {

           if (it.moveToFirst()){
               do {
                   val  id = it.getInt(it.getColumnIndex(KEY_ID))
                   val name = it.getString(it.getColumnIndex(KEY_NAME))
                   val email = it.getString(it.getColumnIndex(KEY_EMAIL))

                   //guardamos estos valores del registro en una variable de la clase contact
                   val contact = Contact(id,name,email)
                   contactList.add(contact)

               }while(it.moveToNext())
           }






       }


       return contactList


   }

}