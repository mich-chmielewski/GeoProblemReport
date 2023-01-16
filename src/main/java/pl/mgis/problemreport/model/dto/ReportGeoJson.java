package pl.mgis.problemreport.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import pl.mgis.problemreport.mapper.ReportGeoJsonSerializer;

import java.util.List;

@JsonSerialize(using = ReportGeoJsonSerializer.class)
public class ReportGeoJson {

    List<ReportGjFeature> features;

    public List<ReportGjFeature> getFeatures() {
        return features;
    }

    public void setFeatures(List<ReportGjFeature> features) {
        this.features = features;
    }
}
