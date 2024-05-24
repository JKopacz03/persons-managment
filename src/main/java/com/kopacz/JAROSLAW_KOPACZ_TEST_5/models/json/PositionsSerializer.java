package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.json;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.Position;
import java.io.IOException;
import java.util.Set;

public class PositionsSerializer extends JsonSerializer<Set<Position>> {
    @Override
    public void serialize(Set<Position> positions, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeNumber(positions.size());
    }
}
