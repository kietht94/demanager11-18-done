package com.example.demanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.demanager.model.Departments;
import com.example.demanager.model.Employees;

import java.util.ArrayList;

public class SqliteDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "department";
    //table departments
    private static final String TABLE_DEPARTMENTS = "departments";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "departmentname";
    //table employees
    private static final String TABLE_EMPLOYEES = "employees";

    private static final String COLUMN_NAME1 = "employeename";
    private static final String COLUMN_NO = "phno";


    public SqliteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_DEPARTMENTS_TABLE = "CREATE	TABLE " + TABLE_DEPARTMENTS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_NAME + " TEXT" + ")";
        String CREATE_EMPLOYEES_TABLE = "CREATE TABLE " + TABLE_EMPLOYEES + "(" + COLUMN_ID + " INTEGER PRIMARY KEY ," + COLUMN_NAME1 + " TEXT," + "departmentId" + " INTEGER," + COLUMN_NO + " TEXT" + ")";
        db.execSQL(CREATE_DEPARTMENTS_TABLE);
        db.execSQL(CREATE_EMPLOYEES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEPARTMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEES);
        onCreate(db);
    }

    public ArrayList<Departments> listDepartments() {
        String sql = "select * from " + TABLE_DEPARTMENTS;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Departments> storeDepartments = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String name = cursor.getString(1);
                storeDepartments.add(new Departments(id, name));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return storeDepartments;
    }

    public ArrayList<Employees> listEmployees(int departmentId) {
        String sql = "select * from " + TABLE_EMPLOYEES + " WHERE departmentId = " + departmentId;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Employees> storeEmployees = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                int id1 = Integer.parseInt(cursor.getString(0));
                String name = cursor.getString(1);
                String phno = cursor.getString(3);
                int departmentID = Integer.parseInt(cursor.getString(2));
                storeEmployees.add(new Employees(id1, name, phno, departmentID));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return storeEmployees;
    }


    public void addDepartments(Departments departments) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, departments.getDepartmentName());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_DEPARTMENTS, null, values);
    }

    public void addEmployees(Employees employees) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME1, employees.getName());
        values.put(COLUMN_NO, employees.getPhno());
        values.put("departmentId", employees.getDepartmentId());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_EMPLOYEES, null, values);
    }

    public boolean checkduplicate(SQLiteDatabase db, String departmentname) {

        return false;
    }

    public void updateDepartments(Departments departments) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, departments.getDepartmentName());

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_DEPARTMENTS, values, COLUMN_ID + "	= ?", new String[]{String.valueOf(departments.getId())});
    }

    public void updateEmployees(Employees employees) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME1, employees.getName());
        values.put(COLUMN_NO, employees.getPhno());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_EMPLOYEES, values, COLUMN_ID + "	= ?", new String[]{String.valueOf(employees.getId())});
    }


    public Employees findEmployees(String name) {
        String query = "Select * FROM " + TABLE_EMPLOYEES + " WHERE " + COLUMN_NAME1 + " = " + "employeename";
        SQLiteDatabase db = this.getWritableDatabase();
        Employees employees = null;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            int id = Integer.parseInt(cursor.getString(0));
            String employeesName = cursor.getString(1);
            String employeesNo = cursor.getString(3);
            int departmentId = Integer.parseInt(cursor.getString(2));
            employees = new Employees(id, employeesName, employeesNo, departmentId);
        }
        cursor.close();
        return employees;
    }

    public void deleteDepartment(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DEPARTMENTS, COLUMN_ID + "	= ?", new String[]{String.valueOf(id)});
    }

    public void deleteEmployee(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EMPLOYEES, COLUMN_ID + "	= ?", new String[]{String.valueOf(id)});
    }

    // ham moi


    public Departments findDepartments(String name) {
        String query = "Select * FROM " + TABLE_DEPARTMENTS + " WHERE " + COLUMN_NAME + " = " + name;
        SQLiteDatabase db = this.getWritableDatabase();
        Departments departments = null;
        Cursor cursor = db.query(TABLE_DEPARTMENTS,
                null,
                COLUMN_NAME + "= ?", // the WHERE clause less WHERE
                new String[]{name}, // the arguments that replace ?'s
                null, null, null);
        if (cursor.moveToFirst()) {
            int id = Integer.parseInt(cursor.getString(0));
            String departmentsName = cursor.getString(1);

            departments = new Departments(id, departmentsName);
        }
        cursor.close();
        return departments;
    }

    public Departments addDepartmentIfNotExit(String departmentname) {
        Departments department = findDepartments(departmentname);
        if (department == null) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, departmentname);
            SQLiteDatabase db = this.getWritableDatabase();
            db.insert(TABLE_DEPARTMENTS, null, values);
            return findDepartments(departmentname);
        } else {
            return department;
        }
    }

    public boolean isExit(String name, int departmentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor csr = db.query(TABLE_EMPLOYEES,
                null,    // null equates to all columns
                COLUMN_NAME1 + "=? And "+
                        "departmentId"+"=?", // the WHERE clause less WHERE
                new String[]{name,departmentId+""}, // the arguments that replace ?'s
                null, null, null);
        boolean rv = (csr.getCount() > 0);
        csr.close();
        return rv;
    }

    public void addEmployeeIfNotExit(Employees employees) {
        if (!isExit(employees.getName(),employees.getDepartmentId())) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME1, employees.getName());
            values.put(COLUMN_NO, employees.getPhno());
            values.put("departmentId", employees.getDepartmentId());
            SQLiteDatabase db = this.getWritableDatabase();
            db.insert(TABLE_EMPLOYEES, null, values);
        }
    }
}

