package com.myjavaweb.javamvc_crud.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

public class EmployeeDto {

    @NotEmpty(message = "Name can not empty!")
    private String name;
    @NotEmpty(message = "Email can not empty!")
    private String email;
    @Pattern(regexp = "\\d+", message = "Phone must be a valid number")
    private String phone;
    @NotNull(message = "Age can not empty!")
    @Min(value = 1, message = "Age must be greater than 0")
    @Max(value = 120, message = "Age must be less than or equal to 120")
    private Integer age;

    public Integer getAge() {
        return age;
    }

    public void setAge( Integer age) {
        this.age = age;
    }

    public  String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
