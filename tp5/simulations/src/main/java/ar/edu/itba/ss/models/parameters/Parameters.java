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
public class Parameters implements Cloneable{
    private int nj;

    private double width; // m
    private double height; // m

    private double mass; // kg
    private double radius; // m

    // granular force
    private double kn; // kg/s

    // avoidance force
    private double a; // N
    private double b; // m

    // driving force
    @JsonProperty("tau_red")
    private double tauRed; // s
    @JsonProperty("tau_blue")
    private double tauBlue; // s
    @JsonProperty("desired_velocity_red")
    private double desiredVelocityRed; // m/s
    @JsonProperty("desired_velocity_blue")
    private double desiredVelocityBlue; // m/s

    @JsonProperty("delta_t")
    private double deltaT; // s

    private PlotsParameters plots;

    @Override
    public Parameters clone() {
        try {
            Parameters clone = (Parameters) super.clone();
            clone.setNj(nj);
            clone.setWidth(width);
            clone.setHeight(height);
            clone.setMass(mass);
            clone.setRadius(radius);
            clone.setKn(kn);
            clone.setA(a);
            clone.setB(b);
            clone.setTauRed(tauRed);
            clone.setTauBlue(tauBlue);
            clone.setDesiredVelocityRed(desiredVelocityRed);
            clone.setDesiredVelocityBlue(desiredVelocityBlue);
            clone.setDeltaT(deltaT);
            clone.setPlots(plots.clone());

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}