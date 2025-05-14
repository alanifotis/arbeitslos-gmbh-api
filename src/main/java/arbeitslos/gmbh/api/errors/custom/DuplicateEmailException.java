package arbeitslos.gmbh.api.errors.custom;

import lombok.Getter;
import org.springframework.dao.DuplicateKeyException;

@Getter
public class DuplicateEmailException extends DuplicateKeyException {
    private final String email;

    public DuplicateEmailException(String email, String message, Throwable cause) {
        super(message, cause);
        this.email = email;
    }

    public DuplicateEmailException(String email, String message) {
        super(message);
        this.email = email;
    }

}
