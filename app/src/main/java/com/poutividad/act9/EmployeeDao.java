package com.poutividad.act9;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.poutividad.act9.model.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDao {
    private final DBHelper helper;

    public EmployeeDao(Context context) {
        helper = new DBHelper(context);
    }

    public long insert(Employee e) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.EmployeeEntry.COL_NAME, e.getName());
        values.put(DBContract.EmployeeEntry.COL_POSITION, e.getPosition());
        values.put(DBContract.EmployeeEntry.COL_SALARY, e.getSalary());
        long id = db.insert(DBContract.EmployeeEntry.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public int update(Employee e) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.EmployeeEntry.COL_NAME, e.getName());
        values.put(DBContract.EmployeeEntry.COL_POSITION, e.getPosition());
        values.put(DBContract.EmployeeEntry.COL_SALARY, e.getSalary());
        int rows = db.update(DBContract.EmployeeEntry.TABLE_NAME, values, DBContract.EmployeeEntry._ID + " = ?", new String[]{String.valueOf(e.getId())});
        db.close();
        return rows;
    }

    public int delete(long id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int rows = db.delete(DBContract.EmployeeEntry.TABLE_NAME, DBContract.EmployeeEntry._ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rows;
    }

    public List<Employee> getAll() {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<Employee> employees = new ArrayList<>();
        Cursor cursor = db.query(DBContract.EmployeeEntry.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Employee employee = new Employee();
                employee.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DBContract.EmployeeEntry._ID)));
                employee.setName(cursor.getString(cursor.getColumnIndexOrThrow(DBContract.EmployeeEntry.COL_NAME)));
                employee.setPosition(cursor.getString(cursor.getColumnIndexOrThrow(DBContract.EmployeeEntry.COL_POSITION)));
                employee.setSalary(cursor.getDouble(cursor.getColumnIndexOrThrow(DBContract.EmployeeEntry.COL_SALARY)));
                employees.add(employee);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return employees;
    }
}
