package campus_care.dto;

import campus_care.enums.AdminCategory;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AdminDto {

    @NotBlank(message = "Email is required.")
    @Email(message = "Please enter a valid email address.")
    private String email;

    @NotNull(message = "Category is required.")
    private AdminCategory category;

    public AdminDto() {
    }

    public AdminDto(
            String email,
            AdminCategory category
    ) {
        this.email = email;
        this.category = category;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(
            String email
    ) {
        this.email = email;
    }

    public AdminCategory getCategory() {
        return category;
    }

    public void setCategory(
            AdminCategory category
    ) {
        this.category = category;
    }
}