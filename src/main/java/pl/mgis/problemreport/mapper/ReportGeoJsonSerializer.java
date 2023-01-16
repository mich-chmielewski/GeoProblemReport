package pl.mgis.problemreport.mapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import pl.mgis.problemreport.model.dto.ReportDTO;
import pl.mgis.problemreport.model.dto.ReportGeoJson;
import pl.mgis.problemreport.model.dto.ReportGjFeature;

import java.io.IOException;

public class ReportGeoJsonSerializer extends JsonSerializer<ReportGeoJson> {

    @Override
    public void serialize(ReportGeoJson reportGeoJson, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("type","FeatureCollection");
                jsonGenerator.writeArrayFieldStart("features");
                for (ReportGjFeature report: reportGeoJson.getFeatures()) {
                    jsonGenerator.writeStartObject();
                            jsonGenerator.writeStringField("type","Feature");
                            jsonGenerator.writeObjectFieldStart("properties");
                                jsonGenerator.writeNumberField("id",report.getId());
                                jsonGenerator.writeNumberField("status",report.getStatus());
                                jsonGenerator.writeStringField("reportStatus",report.getReportStatus());
                                jsonGenerator.writeStringField("reportType",report.getReportType());
                                jsonGenerator.writeStringField("created",report.getCreated().toString());
                                jsonGenerator.writeObjectField("color",report.getColor());
                        jsonGenerator.writeEndObject();
                        jsonGenerator.writeObjectFieldStart("geometry");
                            jsonGenerator.writeStringField("type","Point");
                            jsonGenerator.writeArrayFieldStart("coordinates");
                                jsonGenerator.writeNumber(report.getLon());
                                jsonGenerator.writeNumber(report.getLat());
                            jsonGenerator.writeEndArray();
                        jsonGenerator.writeEndObject();
                    jsonGenerator.writeEndObject();
                }
                jsonGenerator.writeEndArray();
            jsonGenerator.writeEndObject();

    }
}
