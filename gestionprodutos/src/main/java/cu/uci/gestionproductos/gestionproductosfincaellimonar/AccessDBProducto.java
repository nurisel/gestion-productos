package cu.uci.gestionproductos.gestionproductosfincaellimonar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by npalma on 30/01/18.
 */

public class AccessDBProducto extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "Productos";

    public static final String TABLE_NAME_IMPORTE = "Importe";
    public static final String IMPORTE_SALDO = "saldo";
    public static final String IMPORTE_ID = "id";


    public static final String PRODUCTO_ID = "_id";
    public static final String PRODUCTO_NOMBRE = "nombre";
    public static final String PRODUCTO_DISPONIBILIDAD = "disponibilidad";
    public static final String PRODUCTO_PRECIO = "precio";

    static final String DB_NAME = "PRODUCTO.DB";
    static final int DB_VERSION = 1;

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + PRODUCTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + PRODUCTO_NOMBRE + " TEXT NOT NULL, " + PRODUCTO_DISPONIBILIDAD + " INTEGER NOT NULL, " + PRODUCTO_PRECIO + " FLOAT NOT NULL );";
    private static final String CREATE_TABLE_NAME_IMPORTE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_IMPORTE + "(" + IMPORTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + IMPORTE_SALDO + " FLOAT NOT NULL );";

    public AccessDBProducto(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.onCreate(this.getWritableDatabase());

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(CREATE_TABLE);
        sqLiteDatabase.execSQL(CREATE_TABLE_NAME_IMPORTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_IMPORTE);
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

    public void updateImporte(float saldo) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cur = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_IMPORTE, null);

        LinkedList<Importe> list = new LinkedList<>();
        while (cur.moveToNext()) {
            Importe obj = new Importe(cur.getString(0), cur.getFloat(1));
            list.add(obj);
        }

        if(list.size()==0){
            ContentValues row = new ContentValues();
            row.put(IMPORTE_ID, "1");
            row.put(IMPORTE_SALDO, 0);
            sqLiteDatabase.insert(TABLE_NAME_IMPORTE, null, row);
        }else{
            Importe im = new Importe(list.get(0).getId(),list.get(0).getSaldo());
            float newSaldo = im.getSaldo()+saldo;
            ContentValues cv = new ContentValues();
            cv.put(IMPORTE_SALDO, newSaldo);
            sqLiteDatabase.update(TABLE_NAME_IMPORTE, cv, IMPORTE_ID + "=" + im.getId(), null);
        }
        cur.close();
        sqLiteDatabase.close();
    }
    public float getTotalSaldo() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cur = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_IMPORTE, null);

        LinkedList<Importe> list = new LinkedList<>();
        while (cur.moveToNext()) {
            Importe obj = new Importe(cur.getString(0), cur.getFloat(1));
            list.add(obj);
        }
        cur.close();
        sqLiteDatabase.close();
        if(list.size()==0){
            return 0;
        }else{
            Importe im = new Importe(list.get(0).getId(),list.get(0).getSaldo());
            return im.getSaldo();
        }
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
        if(disponibilidad>0) {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(PRODUCTO_DISPONIBILIDAD, (disponibilidad - 1));
            sqLiteDatabase.update(TABLE_NAME, cv, PRODUCTO_ID + "=" + id, null);
            sqLiteDatabase.close();
        }
    }
}
