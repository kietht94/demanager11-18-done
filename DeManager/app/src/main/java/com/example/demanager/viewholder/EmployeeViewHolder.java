package com.example.demanager.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demanager.R;


public class EmployeeViewHolder extends RecyclerView.ViewHolder
{
    public TextView name,ph_no;
    public ImageView deleteEmployee;
    public  ImageView editEmployee;

    public EmployeeViewHolder(View itemView) {
        super(itemView);
        name = (TextView)itemView.findViewById(R.id.employee_name);
        ph_no = (TextView)itemView.findViewById(R.id.ph_no);
        deleteEmployee = (ImageView)itemView.findViewById(R.id.delete_employee);
        editEmployee = (ImageView)itemView.findViewById(R.id.edit_employee);
    }

}