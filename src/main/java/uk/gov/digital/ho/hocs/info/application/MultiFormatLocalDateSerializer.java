package uk.gov.digital.ho.hocs.info.application;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;

public class MultiFormatLocalDateSerializer extends JsonDeserializer<LocalDate> {
        DateTimeFormatter[] parseFormatters = Stream.of("yyyy-MM-dd", "yyyy-MM-d", "yyyy-M-dd", "yyyy-M-d")
                .map(DateTimeFormatter::ofPattern)
                .toArray(DateTimeFormatter[]::new);
    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        System.out.println("DATE: " + p.getText());
        for (DateTimeFormatter formatter : parseFormatters) {
            try {
                return LocalDate.parse(p.getText(), formatter);
            } catch (DateTimeParseException e) {
                //ignore error and try next formatter
            }
        }
        throw new DateTimeException("Unable to parse Date Time of " + p.getText());
    }
}
