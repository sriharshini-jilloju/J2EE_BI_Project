package org.digital.online.ejb.tools;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeSerializer extends StdSerializer<LocalTime> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");

    public LocalTimeSerializer() {
        super(LocalTime.class);
    }

    @Override
    public void serialize(LocalTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(value.format(formatter));
    }
}

