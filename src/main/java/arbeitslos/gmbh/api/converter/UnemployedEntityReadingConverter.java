package arbeitslos.gmbh.api.converter;

import arbeitslos.gmbh.api.model.EmploymentStatus;
import arbeitslos.gmbh.api.model.UnemployedEntity;
import io.r2dbc.spi.Row;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.lang.NonNull;

import java.util.UUID;

@ReadingConverter
public class UnemployedEntityReadingConverter implements Converter<Row, UnemployedEntity> {

    @Override
    public UnemployedEntity convert(Row row) {
        return UnemployedEntity.builder()
                .id(row.get("id", UUID.class))
                .firstName(row.get("firstname", String.class))
                .lastName(row.get("lastname", String.class))
                .email(row.get("email", String.class))
                .password(row.get("password", String.class))
                .employmentStatus(row.get("employmentstatus", EmploymentStatus.class))
                .build();
    }
}
