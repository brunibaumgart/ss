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
    @JsonProperty("system_one")
    private SystemOneparameters systemOne;
    @JsonProperty("frecuency_vs_w")
    private FrecuencyVsWParameters frecuencyVsW;
    @JsonProperty("amplitude_vs_time")
    private AmplitudeVsTimeParameters amplitudeVsTime;
    @JsonProperty("w0_vs_k")
    private W0VsKParameters w0VsK;

}
