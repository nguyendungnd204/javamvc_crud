package com.myjavaweb.javamvc_crud.Controllers;
import com.myjavaweb.javamvc_crud.Models.Employee;
import com.myjavaweb.javamvc_crud.Services.EmployeeService;
import com.myjavaweb.javamvc_crud.dto.EmployeeDto;
import com.myjavaweb.javamvc_crud.repo.EmployeeRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeService employeeService, EmployeeRepository employeeRepository) {
        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping({"","/"})
    public String index(Model model) {
        List<Employee> employees = employeeService.getAllEmployee();
        model.addAttribute("employees", employees);
        return "employees/index";
    }

    @GetMapping("/create")
    public String showPagecreate(Model model) {
        EmployeeDto employeeDto = new EmployeeDto();
        model.addAttribute("employeeDto", employeeDto);
        return "employees/createEmployee";
    }

    @PostMapping("/create")
    public String createEmployee(@Valid @ModelAttribute EmployeeDto employeeDto,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "employees/createEmployee";
        }
        try {
            employeeService.createEmployee(employeeDto);
            return "redirect:/employees";
        }catch (Exception e) {
            System.out.println("Cannot create: " + e.getMessage());
            return "employees/createEmployee";
        }
    }

    @GetMapping("/edit")
    public String showPageEdit(Model model, @RequestParam Long id) {
        try{
            EmployeeDto employeeDto = new EmployeeDto();
            Employee employee = employeeRepository.findById(id).orElseThrow(()->new RuntimeException("Employee not found"));
            model.addAttribute("employee", employee);
            employeeService.getEmployeeInfo(employeeDto,employee);
            model.addAttribute("employeeDto", employeeDto);
            return "employees/editEmployee";
        }catch (Exception e) {
            System.out.println("Message: " + e.getMessage());
            return "redirect:/employees";
        }
    }

    @PostMapping("/edit")
    public String editEmployee(Model model, @RequestParam Long id ,@Valid @ModelAttribute
    EmployeeDto employeeDto, BindingResult bindingResult) {
        try {
            Employee employee = employeeRepository.findById(id).orElseThrow(()-> new RuntimeException("Employee not found"));
            model.addAttribute("employee", employee);
            employeeService.updateEmployee(employeeDto, employee);
            if (bindingResult.hasErrors()) {
                model.addAttribute("errors", bindingResult.getAllErrors());
                return "employees/editEmployee";
            }
            return "redirect:/employees";
        }catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating employee: " + e.getMessage());
            return "employees/editEmployee";
        }

    }
    @GetMapping("/delete")
    public String deleteEmployee(@RequestParam Long id) {
        try{
            Employee employee = employeeRepository.findById(id).orElseThrow(()->new RuntimeException("Employee not found"));
            employeeService.deleteEmployee(employee);
        }catch (Exception e) {
            System.out.println("Message: " + e.getMessage());
        }
        return "redirect:/employees";
    }


}
