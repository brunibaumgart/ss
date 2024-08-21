package ar.edu.itba.ss.models.parameters;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlotsParameters {
    private VideoParameters video;
    @JsonProperty("time_vs_va")
    private TimeVsVaParameters timeVsVa;
    @JsonProperty("va_vs_etha")
    private VaVsEthaParameters vaVsEtha;
}
