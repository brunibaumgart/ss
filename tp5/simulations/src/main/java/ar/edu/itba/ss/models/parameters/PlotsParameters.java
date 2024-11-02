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
public class PlotsParameters implements Cloneable {
    @JsonProperty("position_vs_time")
    private PositionVsTimeParameters positionVsTime;
    @JsonProperty("max_distance")
    private MaxDistanceParameters maxDistance;
    @JsonProperty("max_distance_heatmap")
    private MaxDistanceHeatmapParameters maxDistanceHeatmap;
    @JsonProperty("fraction_tries_vs_nj")
    private FractionTriesVsNjParameters fractionTriesVsNj;

    @Override
    public PlotsParameters clone() {
        try {
            PlotsParameters clone = (PlotsParameters) super.clone();
            clone.setPositionVsTime(positionVsTime.clone());
            clone.setMaxDistance(maxDistance.clone());
            clone.setMaxDistanceHeatmap(maxDistanceHeatmap.clone());
            clone.setFractionTriesVsNj(fractionTriesVsNj.clone());

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
