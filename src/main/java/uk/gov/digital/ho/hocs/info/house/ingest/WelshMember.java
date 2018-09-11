package uk.gov.digital.ho.hocs.info.house.ingest;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class WelshMember {

    @Getter
    @JacksonXmlProperty(localName = "fullusername")
    private String name;

    @Getter
    @JacksonXmlProperty(localName = "councillorid")
    private String id;

}