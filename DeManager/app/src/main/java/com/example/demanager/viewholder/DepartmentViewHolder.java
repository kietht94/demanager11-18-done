package com.example.demanager.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demanager.R;


public class DepartmentViewHolder extends RecyclerView.ViewHolder
{
    public TextView department;
    public ImageView deleteDepartment;
    public  ImageView editDepartment;

    public DepartmentViewHolder(View itemView) {
        super(itemView);
        department = (TextView)itemView.findViewById(R.id.department_name);

        deleteDepartment = (ImageView)itemView.findViewById(R.id.delete_department);
        editDepartment = (ImageView)itemView.findViewById(R.id.edit_department);
    }


}
