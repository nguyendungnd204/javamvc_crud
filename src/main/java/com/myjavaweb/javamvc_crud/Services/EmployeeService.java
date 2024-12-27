package com.myjavaweb.javamvc_crud.Services;
import com.myjavaweb.javamvc_crud.Models.Employee;
import com.myjavaweb.javamvc_crud.dto.EmployeeDto;
import com.myjavaweb.javamvc_crud.dto.ProductDto;
import com.myjavaweb.javamvc_crud.repo.EmployeeRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository repo;
    public EmployeeService(EmployeeRepository repo) {
        this.repo = repo;
    }

    public List<Employee> getAllEmployee() {
        try{
            return repo.findAll();
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void createEmployee(EmployeeDto employeeDto) {
        Employee employee = new Employee();
        employee.setName(employeeDto.getName());
        employee.setAge(employeeDto.getAge());
        employee.setEmail(employeeDto.getEmail());
        employee.setPhone(employeeDto.getPhone());
        repo.save(employee);
    }

    public void updateEmployee( EmployeeDto employeeDto, Employee employee) {
        employee.setName(employeeDto.getName());
        employee.setAge(employeeDto.getAge());
        employee.setEmail(employeeDto.getEmail());
        employee.setPhone(employeeDto.getPhone());
        repo.save(employee);
    }

    public void getEmployeeInfo( EmployeeDto employeeDto, Employee employee) {
        employeeDto.setName(employee.getName());
        employeeDto.setAge(employee.getAge());
        employeeDto.setEmail(employee.getEmail());
        employeeDto.setPhone(employee.getPhone());
    }

    public void deleteEmployee(Employee employee) {
        repo.delete(employee);
    }
}
