package CyTrack.Services;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DeserializeDateService extends JsonDeserializer<LocalDate> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String date = jsonParser.getText();
        return LocalDate.parse(date, formatter);
    }
}