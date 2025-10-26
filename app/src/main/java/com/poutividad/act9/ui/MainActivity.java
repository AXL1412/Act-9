package com.poutividad.act9.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.poutividad.act9.R;
import com.poutividad.act9.data.EmployeeDao;
import com.poutividad.act9.model.Employee;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements EmployeeAdapter.OnItemClick {

    private EmployeeDao dao;
    private EmployeeAdapter adapter;
    private List<Employee> employeeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dao = new EmployeeDao(this);

        RecyclerView rvEmployees = findViewById(R.id.rvEmployees);
        rvEmployees.setLayoutManager(new LinearLayoutManager(this));
        
        adapter = new EmployeeAdapter(employeeList, this);
        rvEmployees.setAdapter(adapter);

        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> showEmployeeDialog(null));

        loadEmployees();
    }

    private void loadEmployees() {
        List<Employee> updatedList = dao.getAll();
        employeeList.clear();
        employeeList.addAll(updatedList);
        adapter.notifyDataSetChanged();
    }

    private void showEmployeeDialog(final Employee employee) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_employee, null);
        builder.setView(dialogView);

        final EditText etName = dialogView.findViewById(R.id.etName);
        final EditText etPosition = dialogView.findViewById(R.id.etPosition);
        final EditText etSalary = dialogView.findViewById(R.id.etSalary);

        if (employee != null) {
            builder.setTitle("Edit Employee");
            etName.setText(employee.getName());
            etPosition.setText(employee.getPosition());
            etSalary.setText(String.valueOf(employee.getSalary()));
        } else {
            builder.setTitle("Add Employee");
        }

        builder.setPositiveButton(employee == null ? "Add" : "Save", (dialog, which) -> {
            String name = etName.getText().toString().trim();
            String position = etPosition.getText().toString().trim();
            String salaryStr = etSalary.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            double salary = 0.0;
            if (!salaryStr.isEmpty()) {
                try {
                    salary = Double.parseDouble(salaryStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Invalid salary. Please enter a valid number.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            if (employee == null) {
                Employee newEmployee = new Employee(0, name, position, salary);
                dao.insert(newEmployee);
                Toast.makeText(this, "Employee added", Toast.LENGTH_SHORT).show();
            } else {
                employee.setName(name);
                employee.setPosition(position);
                employee.setSalary(salary);
                dao.update(employee);
                Toast.makeText(this, "Employee updated", Toast.LENGTH_SHORT).show();
            }
            loadEmployees();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    @Override
    public void onEdit(Employee e) {
        showEmployeeDialog(e);
    }

    @Override
    public void onDelete(long id) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Employee")
                .setMessage("Are you sure you want to delete this employee?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    dao.delete(id);
                    Toast.makeText(this, "Employee deleted", Toast.LENGTH_SHORT).show();
                    loadEmployees();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
