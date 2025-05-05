package arbeitslos.gmbh.api.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Getter
@Setter
@Table("unemployed")
public class UnemployedEntity {
    @Id
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private EmploymentStatus employmentStatus;
}
