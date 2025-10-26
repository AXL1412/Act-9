package com.poutividad.act9;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.poutividad.act9.model.Employee;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.VH> {

    public interface OnItemClick {
        void onEdit(Employee e);
        void onDelete(long id);
    }

    private final List<Employee> employees;
    private final OnItemClick listener;

    public EmployeeAdapter(List<Employee> employees, OnItemClick listener) {
        this.employees = employees;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Employee employee = employees.get(position);
        holder.tvName.setText(employee.getName());
        holder.tvPosition.setText(employee.getPosition());
        holder.tvSalary.setText(String.format("$%.2f", employee.getSalary()));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEdit(employee);
            }
        });
        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onDelete(employee.getId());
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

    public void refreshData(List<Employee> newEmployees) {
        employees.clear();
        employees.addAll(newEmployees);
        notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvPosition, tvSalary;

        VH(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPosition = itemView.findViewById(R.id.tvPosition);
            tvSalary = itemView.findViewById(R.id.tvSalary);
        }
    }
}
