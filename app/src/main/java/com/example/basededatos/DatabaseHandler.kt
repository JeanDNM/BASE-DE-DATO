package com.example.basededatos

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler (context : Context) : SQLiteOpenHelper(context, DATABASE_NAME, null,DATABASE_VERSION) {

    companion object{
        private const val DATABASE_VERSION = 2 //*ir actualizando*/
        private const val DATABASE_NAME = "MyDataBase"
        private  const val TABLE_NAME = "Contact"
        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_EMAIL = "email"
        private const val KEY_PROVINCIA = "provincia"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_NAME($KEY_ID INTEGER PRIMARY KEY, $KEY_NAME TEXT, $KEY_EMAIL TEXT, $KEY_PROVINCIA TEXT)")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
    fun addContact(name: String, email: String, provincia: String): Long {
        val db = this.writableDatabase
        val value = ContentValues()
        value.put(KEY_NAME,name)
        value.put(KEY_EMAIL,email)
        value.put(KEY_PROVINCIA, provincia)

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

                   val provinciaIndex = cursor.getColumnIndex(KEY_PROVINCIA)
                   val provincia = if (provinciaIndex != -1) cursor.getString(provinciaIndex) else ""


                   val contact = Contact(id,name,email,provincia)
                   contactList.add(contact)

               }while(it.moveToNext())
           }






       }


       return contactList


   }

    fun queryProvinciaContacts(provincia:String) : List<Contact> {
        val contactList = mutableListOf<Contact>()
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $KEY_PROVINCIA = ?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, arrayOf(provincia))

        if (cursor.moveToFirst()) {
            do {
                val contact = Contact(
                    cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                    cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                    cursor.getString(cursor.getColumnIndex(KEY_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(KEY_PROVINCIA))

                )
                contactList.add(contact)
            } while (cursor.moveToNext())

        }
        cursor.close()
        return contactList
    }

    fun getDistinctProvinces(): List<String> {
        val provinceList = mutableListOf<String>()
        val selectDistinctQuery = "SELECT DISTINCT $KEY_PROVINCIA FROM $TABLE_NAME"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectDistinctQuery, null)

        cursor.use {
            if (it.moveToFirst()) {
                do {
                    val provincia = it.getString(it.getColumnIndex(KEY_PROVINCIA))
                    if (!provincia.isNullOrBlank()) {
                        provinceList.add(provincia)
                    }
                } while (it.moveToNext())
            }
        }

        return provinceList
    }

}