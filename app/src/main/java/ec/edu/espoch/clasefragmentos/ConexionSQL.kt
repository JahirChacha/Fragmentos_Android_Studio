import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ConexionSQL(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "mostrardatos.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "datos_usuario"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "nombre"
        private const val COLUMN_LASTNAME = "apellido"
        private const val COLUMN_GENDER = "genero"
        private const val COLUMN_TRANSPORT = "transporte"
        private const val COLUMN_AGE = "edad"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_NAME TEXT," +
                "$COLUMN_LASTNAME TEXT," +
                "$COLUMN_GENDER TEXT," +
                "$COLUMN_TRANSPORT TEXT," +
                "$COLUMN_AGE TEXT)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertData(nombre: String, apellido: String, genero: String, transporte: String, edad: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_NAME, nombre)
        contentValues.put(COLUMN_LASTNAME, apellido)
        contentValues.put(COLUMN_GENDER, genero)
        contentValues.put(COLUMN_TRANSPORT, transporte)
        contentValues.put(COLUMN_AGE, edad)
        val result = db.insert(TABLE_NAME, null, contentValues)
        db.close()
        return result
    }

    fun getAllData(): ArrayList<String> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        val dataList = ArrayList<String>()

        if (cursor.moveToFirst()) {
            do {
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val apellido = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LASTNAME))
                val genero = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENDER))
                val transporte = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TRANSPORT))
                val edad = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AGE))
                val data = "Nombre: $nombre\nApellido: $apellido\nGénero: $genero\nTransporte: $transporte\nEdad: $edad"
                dataList.add(data)
            } while (cursor.moveToNext())
        }
        cursor.close()
        //db.close()
        return dataList
    }
}
