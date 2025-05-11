package com.it.Payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class StudentDto {

    private Long id;

    @NotBlank(message = "This Filed is required")
    private String fullName;

    @NotBlank(message = "This Filed is required")
    @Email(message = "Enter the Valid mail id")
    private String email;

    @NotBlank(message = "This Filed is required")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$", message = "Password must have of minimum 8 Characters and at least one uppercase letter, one lowercase letter, one number and one special character")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull(message = "This Filed is required")
    @Size(min = 10, max = 10, message = "size must be 10 digits.")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be exactly 10 digits Only.")
    private String mobile;

    @NotNull(message = "This Filed is required")
    @Min(18)
    @Max(60)
    private Long age;

    @NotBlank(message = "This Filed is required")
    private String gender;
}
