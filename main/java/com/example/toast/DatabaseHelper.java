package com.example.toast;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase helper para manejar la base de datos SQLite
 * Implementa las operaciones CRUD para el sistema de estacionamiento
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Constantes de la base de datos
    private static final String DATABASE_NAME = "estacionamiento.db";
    private static final int DATABASE_VERSION = 1;

    // Constantes de la tabla vehículos
    private static final String TABLE_VEHICULOS = "vehiculos";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PLACA = "placa";
    private static final String COLUMN_PROPIETARIO = "propietario";
    private static final String COLUMN_TELEFONO = "telefono";
    private static final String COLUMN_TIPO_VEHICULO = "tipo_vehiculo";
    private static final String COLUMN_FECHA_INGRESO = "fecha_ingreso";
    private static final String COLUMN_HORA_INGRESO = "hora_ingreso";

    // Script de creación de tabla
    private static final String CREATE_TABLE_VEHICULOS =
            "CREATE TABLE " + TABLE_VEHICULOS + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_PLACA + " TEXT NOT NULL UNIQUE," +
                    COLUMN_PROPIETARIO + " TEXT NOT NULL," +
                    COLUMN_TELEFONO + " TEXT," +
                    COLUMN_TIPO_VEHICULO + " TEXT NOT NULL," +
                    COLUMN_FECHA_INGRESO + " TEXT NOT NULL," +
                    COLUMN_HORA_INGRESO + " TEXT NOT NULL" +
                    ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla de vehículos
        db.execSQL(CREATE_TABLE_VEHICULOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Eliminar tabla si existe y crear nueva
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VEHICULOS);
        onCreate(db);
    }

    /**
     * Método CREATE - Agregar nuevo vehículo
     * @param vehiculo Objeto Vehiculo a insertar
     * @return ID del vehículo insertado, -1 si hay error
     */
    public long agregarVehiculo(Vehiculo vehiculo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_PLACA, vehiculo.getPlaca());
        values.put(COLUMN_PROPIETARIO, vehiculo.getPropietario());
        values.put(COLUMN_TELEFONO, vehiculo.getTelefono());
        values.put(COLUMN_TIPO_VEHICULO, vehiculo.getTipoVehiculo());
        values.put(COLUMN_FECHA_INGRESO, vehiculo.getFechaIngreso());
        values.put(COLUMN_HORA_INGRESO, vehiculo.getHoraIngreso());

        long id = db.insert(TABLE_VEHICULOS, null, values);
        db.close();
        return id;
    }

    /**
     * Método READ - Obtener todos los vehículos
     * @return Lista de todos los vehículos
     */
    public List<Vehiculo> obtenerTodosLosVehiculos() {
        List<Vehiculo> listaVehiculos = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_VEHICULOS + " ORDER BY " + COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Vehiculo vehiculo = new Vehiculo();
                vehiculo.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                vehiculo.setPlaca(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PLACA)));
                vehiculo.setPropietario(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROPIETARIO)));
                vehiculo.setTelefono(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TELEFONO)));
                vehiculo.setTipoVehiculo(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIPO_VEHICULO)));
                vehiculo.setFechaIngreso(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_INGRESO)));
                vehiculo.setHoraIngreso(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HORA_INGRESO)));

                listaVehiculos.add(vehiculo);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listaVehiculos;
    }

    /**
     * Método READ - Obtener vehículo por ID
     * @param id ID del vehículo
     * @return Objeto Vehiculo o null si no existe
     */
    public Vehiculo obtenerVehiculoPorId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_VEHICULOS, null, COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);

        Vehiculo vehiculo = null;
        if (cursor.moveToFirst()) {
            vehiculo = new Vehiculo();
            vehiculo.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            vehiculo.setPlaca(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PLACA)));
            vehiculo.setPropietario(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROPIETARIO)));
            vehiculo.setTelefono(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TELEFONO)));
            vehiculo.setTipoVehiculo(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIPO_VEHICULO)));
            vehiculo.setFechaIngreso(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_INGRESO)));
            vehiculo.setHoraIngreso(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HORA_INGRESO)));
        }

        cursor.close();
        db.close();
        return vehiculo;
    }

    /**
     * Método UPDATE - Actualizar vehículo existente
     * @param vehiculo Objeto Vehiculo con datos actualizados
     * @return Número de filas afectadas
     */
    public int actualizarVehiculo(Vehiculo vehiculo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_PLACA, vehiculo.getPlaca());
        values.put(COLUMN_PROPIETARIO, vehiculo.getPropietario());
        values.put(COLUMN_TELEFONO, vehiculo.getTelefono());
        values.put(COLUMN_TIPO_VEHICULO, vehiculo.getTipoVehiculo());
        values.put(COLUMN_FECHA_INGRESO, vehiculo.getFechaIngreso());
        values.put(COLUMN_HORA_INGRESO, vehiculo.getHoraIngreso());

        int filasAfectadas = db.update(TABLE_VEHICULOS, values, COLUMN_ID + "=?",
                new String[]{String.valueOf(vehiculo.getId())});
        db.close();
        return filasAfectadas;
    }

    /**
     * Método DELETE - Eliminar vehículo por ID
     * @param id ID del vehículo a eliminar
     * @return Número de filas eliminadas
     */
    public int eliminarVehiculo(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int filasEliminadas = db.delete(TABLE_VEHICULOS, COLUMN_ID + "=?",
                new String[]{String.valueOf(id)});
        db.close();
        return filasEliminadas;
    }

    /**
     * Método para obtener el número total de vehículos registrados
     * @return Cantidad total de vehículos
     */
    public int contarVehiculos() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_VEHICULOS, null);

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return count;
    }

    /**
     * Método para buscar vehículos por placa
     * @param placa Placa a buscar
     * @return Lista de vehículos que coinciden
     */
    public List<Vehiculo> buscarVehiculosPorPlaca(String placa) {
        List<Vehiculo> listaVehiculos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_VEHICULOS, null, COLUMN_PLACA + " LIKE ?",
                new String[]{"%" + placa + "%"}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Vehiculo vehiculo = new Vehiculo();
                vehiculo.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                vehiculo.setPlaca(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PLACA)));
                vehiculo.setPropietario(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROPIETARIO)));
                vehiculo.setTelefono(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TELEFONO)));
                vehiculo.setTipoVehiculo(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIPO_VEHICULO)));
                vehiculo.setFechaIngreso(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_INGRESO)));
                vehiculo.setHoraIngreso(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HORA_INGRESO)));

                listaVehiculos.add(vehiculo);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listaVehiculos;
    }
}