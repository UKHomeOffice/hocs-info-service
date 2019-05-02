package uk.gov.digital.ho.hocs.info.application;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MultiFormatLocalDateSerializerTest {

    private MultiFormatLocalDateSerializer serializer;

    @Mock
    JsonParser parser;

    @Mock
    DeserializationContext context;

    @Before
    public void setUp() {
        serializer = new MultiFormatLocalDateSerializer();
    }

    @Test
    public void shouldDeserializeISODateFormat() throws IOException {
        String unFormattedDate = "2018-12-01";
        when(parser.getText()).thenReturn(unFormattedDate);
        LocalDate result = serializer.deserialize(parser,context );
        assertThat(result).isEqualTo(LocalDate.of(2018,12,1));
    }

    @Test
    public void shouldDeserializeSingleDigitMonthDateFormat() throws IOException {
        String unFormattedDate = "2018-1-01";
        when(parser.getText()).thenReturn(unFormattedDate);
        LocalDate result = serializer.deserialize(parser,context );
        assertThat(result).isEqualTo(LocalDate.of(2018,1,1));
    }

    @Test
    public void shouldDeserializeSingleDigitDayDateFormat() throws IOException {
        String unFormattedDate = "2018-01-1";
        when(parser.getText()).thenReturn(unFormattedDate);
        LocalDate result = serializer.deserialize(parser,context );
        assertThat(result).isEqualTo(LocalDate.of(2018,1,1));
    }

    @Test
    public void shouldDeserializeSingleDigitDayAndMonthDateFormat() throws IOException {
        String unFormattedDate = "2018-1-1";
        when(parser.getText()).thenReturn(unFormattedDate);
        LocalDate result = serializer.deserialize(parser,context );
        assertThat(result).isEqualTo(LocalDate.of(2018,1,1));
    }

    @Test
    public void shouldThrowExceptionWhenDateFormatNotSupported() throws IOException {
        String unFormattedDate = "2018/1/1";
        when(parser.getText()).thenReturn(unFormattedDate);
        assertThatThrownBy(() ->serializer.deserialize(parser,context )).isExactlyInstanceOf(DateTimeException.class);

    }
}