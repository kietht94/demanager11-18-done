package com.example.demanager.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demanager.R;
import com.example.demanager.SqliteDatabase;
import com.example.demanager.model.Employees;
import com.example.demanager.viewholder.EmployeeViewHolder;

import java.util.ArrayList;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeViewHolder> implements Filterable {

    private Context context;
    private ArrayList<Employees> listEmployees;
    private ArrayList<Employees> mArrayList;
    public void setData(ArrayList<Employees> data){
        mArrayList = data;
        listEmployees = data;
        notifyDataSetChanged();
    }
    private SqliteDatabase mDatabase;

    public EmployeeAdapter(Context context, ArrayList<Employees> listEmployees) {
        this.context = context;
        this.listEmployees = listEmployees;
        this.mArrayList=listEmployees;
        mDatabase = new SqliteDatabase(context);
    }

    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_list_layout, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmployeeViewHolder holder, int position) {
        final Employees employees = listEmployees.get(position);

        holder.name.setText(employees.getName());
        holder.ph_no.setText(employees.getPhno());

        holder.editEmployee.setOnClickListener(view -> editTaskDialog(employees));

        holder.deleteEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //delete row from database
                mDatabase.deleteEmployee(employees.getId());
                //refresh the activity page.
                ((Activity)context).finish();
                context.startActivity(((Activity) context).getIntent());
            }
        });
    }
    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    listEmployees = mArrayList;
                } else {

                    ArrayList<Employees> filteredList = new ArrayList<>();

                    for (Employees employees : mArrayList) {

                        if (employees.getName().toLowerCase().contains(charString)) {

                            filteredList.add(employees);
                        }
                    }

                    listEmployees = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listEmployees;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listEmployees = (ArrayList<Employees>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    @Override
    public int getItemCount() {
        return listEmployees.size();
    }


    private void editTaskDialog(final Employees contacts){
        LayoutInflater inflater = LayoutInflater.from(context);
        View subView = inflater.inflate(R.layout.add_employee_layout, null);

        final EditText nameField = subView.findViewById(R.id.enter_employeename);
        final EditText contactField = subView.findViewById(R.id.enter_phno);

        if(contacts != null){
            nameField.setText(contacts.getName());
            contactField.setText(String.valueOf(contacts.getPhno()));
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Employee");
        builder.setView(subView);
        builder.create();

        builder.setPositiveButton("EDIT EMPLOYEES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String name = nameField.getText().toString();
                final String ph_no = contactField.getText().toString();

                if(TextUtils.isEmpty(name)){
                    Toast.makeText(context, "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                }
                else{
                    mDatabase.updateEmployees(new Employees(contacts.getId(), name, ph_no,contacts.getDepartmentId()));
                    //refresh the activity
                    ((Activity)context).finish();
                    context.startActivity(((Activity)context).getIntent());
                }
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "Task cancelled", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }
}
