package uk.gov.digital.ho.hocs.info.client.ingest;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
class UKConstituency {

    @JacksonXmlProperty(localName = "MemberFrom")
    private String constituency;
}
