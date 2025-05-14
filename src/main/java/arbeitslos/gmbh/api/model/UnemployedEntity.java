package arbeitslos.gmbh.api.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Getter
@Setter
@Table("unemployed")
@Builder
public class UnemployedEntity {
    @Id
    @Column("id")
    private UUID id;
    @NotBlank(message = "First Name is required.")
    @Column("firstname")
    private String firstName;
    @NotBlank(message = "Last Name is required.")
    @Column("lastname")
    private String lastName;
    @Column("email")
    @NotBlank(message = "Email address is required.")
    @Email(message = "Please provide a valid Email address.")
    private String email;
    @Column("password")
    @NotBlank(message = "Password is required.")
    private String password;
    @Column("employmentstatus")
    private EmploymentStatus employmentStatus;

}
