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
import com.example.demanager.activity.EmployeeActivity;
import com.example.demanager.model.Departments;
import com.example.demanager.viewholder.DepartmentViewHolder;

import java.util.ArrayList;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentViewHolder> implements Filterable {

    private Context context;
    private ArrayList<Departments> listDepartments;
    private ArrayList<Departments> mArrayList;

    private SqliteDatabase mDatabase;

    public DepartmentAdapter(Context context, ArrayList<Departments> listDepartments) {
        this.context = context;
        this.listDepartments = listDepartments;
        this.mArrayList=listDepartments;
        mDatabase = new SqliteDatabase(context);
    }

    @Override
    public DepartmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.department_list_layout, parent, false);
        return new DepartmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DepartmentViewHolder holder, int position) {
        final Departments departments = listDepartments.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmployeeActivity.startSelf(context,departments.getId());
            }
        });
        holder.department.setText(departments.getDepartmentName());

        holder.editDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTaskDialog(departments);
            }
        });

        holder.deleteDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //delete row from database

                mDatabase.deleteDepartment(departments.getId());

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

                    listDepartments = mArrayList;
                } else {

                    ArrayList<Departments> filteredList = new ArrayList<>();

                    for (Departments contacts : mArrayList) {

                        if (contacts.getDepartmentName().toLowerCase().contains(charString)) {

                            filteredList.add(contacts);
                        }
                    }

                    listDepartments = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listDepartments;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listDepartments = (ArrayList<Departments>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    @Override
    public int getItemCount() {
        return listDepartments.size();
    }


    private void editTaskDialog(final Departments departments){
        LayoutInflater inflater = LayoutInflater.from(context);
        View subView = inflater.inflate(R.layout.add_department_layout, null);

        final EditText nameField = (EditText)subView.findViewById(R.id.enter_departmentname);


        if(departments != null){
            nameField.setText(departments.getDepartmentName());

        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit department");
        builder.setView(subView);
        builder.create();

        builder.setPositiveButton("EDIT DEPARTMENT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String name = nameField.getText().toString();


                if(TextUtils.isEmpty(name)){
                    Toast.makeText(context, "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                }
                else{
                    mDatabase.updateDepartments(new Departments(departments.getId(), name));
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
