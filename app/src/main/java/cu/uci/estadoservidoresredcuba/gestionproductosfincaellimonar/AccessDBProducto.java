package cu.uci.estadoPRODUCTOredcuba.gestionproductosfincaellimonar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

import cu.uci.estadoservidoresredcuba.gestionproductosfincaellimonar.Producto;

/**
 * Created by tatos on 30/01/18.
 */

public class AccessDBProducto extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "Productos";


    public static final String PRODUCTO_ID = "_id";
    public static final String PRODUCTO_NOMBRE = "nombre";
    public static final String PRODUCTO_DISPONIBILIDAD = "disponibilidad";
    public static final String PRODUCTO_PRECIO = "precio";

    static final String DB_NAME = "PRODUCTO.DB";
    static final int DB_VERSION = 1;

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + PRODUCTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + PRODUCTO_NOMBRE + " TEXT NOT NULL, " + PRODUCTO_DISPONIBILIDAD + " INTEGER NOT NULL, " + PRODUCTO_PRECIO + " FLOAT NOT NULL );";

    public AccessDBProducto(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.onCreate(this.getWritableDatabase());

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public List<Producto> getAll() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        if (sqLiteDatabase == null) {
            return null;
        }
        Cursor cur = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + PRODUCTO_NOMBRE, null);

        LinkedList<Producto> list = new LinkedList<>();
        while (cur.moveToNext()) {
            Producto obj = new Producto(cur.getString(0), cur.getString(1), cur.getInt(2), cur.getFloat(3));
            list.add(obj);
        }
        cur.close();
        sqLiteDatabase.close();
        return list;
    }

    public long add(Producto obj) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (db == null) {
            return 0;
        }
        ContentValues row = new ContentValues();
        row.put(PRODUCTO_NOMBRE, obj.getNombre());
        row.put(PRODUCTO_DISPONIBILIDAD, obj.getDisponibilidad());
        row.put(PRODUCTO_PRECIO, obj.getPrecio());
        long id = db.insert(TABLE_NAME, null, row);
        db.close();
        return id;
    }

    public boolean delete(String id) {

        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, PRODUCTO_ID + "=" + id, null) > 0;
    }

    public void vender(String id) {
        List<Producto> l = this.getAll();
        int disponibilidad=1;
        for (int i = 0; i < l.size(); i++) {
            if(l.get(i).getId().equals(id)){
                disponibilidad = l.get(i).getDisponibilidad();
            }
        }
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(PRODUCTO_DISPONIBILIDAD, (disponibilidad-1));
        sqLiteDatabase.update(TABLE_NAME, cv, PRODUCTO_ID + "=" + id, null);
        sqLiteDatabase.close();
    }
}
