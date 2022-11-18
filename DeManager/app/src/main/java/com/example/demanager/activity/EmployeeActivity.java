package com.example.demanager.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.demanager.adapter.EmployeeAdapter;
import com.example.demanager.model.Employees;
import com.example.demanager.R;
import com.example.demanager.SqliteDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class EmployeeActivity extends AppCompatActivity {

    public static void startSelf(Context context, int departmentId) {
        Intent intent = new Intent(context, EmployeeActivity.class);
        intent.putExtra("departmentId", departmentId);
        context.startActivity(intent);
    }

    private int departmentId = -99;
    private SqliteDatabase mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        departmentId = getIntent().getIntExtra("departmentId", -99);
        if (departmentId == -99) {
            finish();
        }
        RecyclerView listView = findViewById(R.id.list_view);
        listView.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = new SqliteDatabase(this);
        ///  doc du lieu tu db
        ArrayList<Employees> employeeList = new ArrayList<>(mDatabase.listEmployees(departmentId));
        EmployeeAdapter employeeAdapter = new EmployeeAdapter(this,employeeList);
        listView.setAdapter(employeeAdapter);

        /// Them nut add vao man hinh
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> addEmployeeDialog());


        /// Lan dau tien , thi man hinh nay se ko co du lieu
        /// co nut insert cai nhan vien vafo ,
        // bang nhan viene , thi gom thong tin nhan vien , truong departmentID .
        // luc sau thi a lay tu db du lieu , roi hien thi


    }

    public void addEmployeeDialog(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View subView = inflater.inflate(R.layout.add_employee_layout, null);

        final EditText nameField = subView.findViewById(R.id.enter_employeename);
        final EditText phoneField = subView.findViewById(R.id.enter_phno);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add new Employee");
        builder.setView(subView);
        builder.create();

        builder.setPositiveButton("ADD Employee", (dialog, which) -> {
            final String name = nameField.getText().toString();
            final String phone = phoneField.getText().toString();

            if(TextUtils.isEmpty(name)){
                Toast.makeText(EmployeeActivity.this, "データを入れてください。", Toast.LENGTH_LONG).show();
            }
            else{
                Employees employees = new Employees();
                employees.setDepartmentId(departmentId);
                employees.setName(name);
                employees.setPhno(phone);
                mDatabase.addEmployees(employees);
                finish();
                startSelf(this,departmentId);
            }
        });

        builder.setNegativeButton("CANCEL", (dialog, which) -> Toast.makeText(EmployeeActivity.this, "Task cancelled", Toast.LENGTH_LONG).show());
        builder.show();
    }
}