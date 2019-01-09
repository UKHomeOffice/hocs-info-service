package uk.gov.digital.ho.hocs.info.client.ingest;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class EuropeMember {

    @Getter
    @JacksonXmlProperty(localName = "fullName")
    private String name;

    @Getter
    @JacksonXmlProperty(localName = "id")
    private String id;


}