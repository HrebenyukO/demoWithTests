package com.example.demowithtests.dto;

import com.example.demowithtests.domain.Gender;
import com.example.demowithtests.util.annotations.dto.BlockedEmailDomains;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class EmployeeDto {

    public Integer id;

    @NotNull
    @Size(min = 2, max = 32, message = "Name must be between 2 and 32 characters long")
    @Schema(description = "Name of an employee.", example = "Billy", required = true)
    public String name;

    @Schema(description = "Name of the country.", example = "England", required = true)
    public String country;

    @Email
    @NotNull
    @BlockedEmailDomains(contains = {".com1", ".ru", ".su"})
    @Schema(description = "Email address of an employee.", example = "billys@mail.com", required = true)
    public String email;

    public Instant startDate = Instant.now();

    public Gender gender;

    @Valid
    public Set<AddressDto> addresses = new HashSet<>();
}
