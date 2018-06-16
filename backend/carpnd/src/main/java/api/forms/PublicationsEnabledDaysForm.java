package api.forms;

import lombok.Data;
import me.geso.tinyvalidator.constraints.NotNull;
import model.PublicationsEnabledDays;
import org.joda.time.LocalDate;

import java.util.List;

@Data
public class PublicationsEnabledDaysForm {

    @NotNull
    public List<LocalDate> disabledDays;

    @NotNull
    public List<LocalDate> reservedDays;

    public PublicationsEnabledDays getModelInstance() {
        PublicationsEnabledDays pEnableDays = new PublicationsEnabledDays(this.reservedDays, this.disabledDays);
        return pEnableDays;
    }
}