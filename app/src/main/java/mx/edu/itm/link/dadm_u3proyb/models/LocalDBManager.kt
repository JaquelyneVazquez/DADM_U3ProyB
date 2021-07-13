package mx.edu.itm.link.dadm_u3proyb.models

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import kotlin.jvm.Throws

class LocalDBManager(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.let {
            var sql = """
               CREATE TABLE usuario(
                    id INTEGER PRIMARY KEY,
                    usuario TEXT,
                    nombre TEXT,
                    celular TEXT
               ) 
            """

            it.execSQL(sql)
        }
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {}

    @Throws
    fun removeUsr() {
        val db = writableDatabase

        var sql = "DELETE FROM usuario"

        db.execSQL(sql)

        db.close()
    }

    @Throws
    fun setUsr(usuario: Usuario) {
        val db = writableDatabase

        var sql = "DELETE FROM usuario"

        db.execSQL(sql)

        sql = "INSERT INTO usuario VALUES(${usuario.id},'${usuario.usr}','${usuario.name}','${usuario.celphone}')"

        db.execSQL(sql)

        db.close()
    }

    @Throws
    fun getUsr() : Usuario? {
        val db = readableDatabase

        var sql = "SELECT * FROM usuario"

        val cursor = db.rawQuery(sql, null)

        var usuario : Usuario? = null
        if(cursor.moveToNext()) {
            usuario = Usuario(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3)
            )
        }
        db.close()

        return usuario
    }

}