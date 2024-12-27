package com.myjavaweb.javamvc_crud.repo;
import com.myjavaweb.javamvc_crud.Models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {


}
