package uk.gov.digital.ho.hocs.info.client.ingest;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
class WelshMembers {

    @JacksonXmlProperty(localName = "councillor")
    @JacksonXmlElementWrapper(localName = "councillor", useWrapping = false)
    private List<WelshMember> members;

}