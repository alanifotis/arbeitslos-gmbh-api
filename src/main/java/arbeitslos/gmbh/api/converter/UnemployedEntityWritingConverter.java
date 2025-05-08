package arbeitslos.gmbh.api.converter;

import arbeitslos.gmbh.api.model.EmploymentStatus;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.r2dbc.convert.EnumWriteSupport;

@WritingConverter
public class UnemployedEntityWritingConverter extends EnumWriteSupport<EmploymentStatus> {
}