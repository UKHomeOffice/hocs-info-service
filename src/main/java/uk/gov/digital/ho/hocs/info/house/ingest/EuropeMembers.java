package uk.gov.digital.ho.hocs.info.house.ingest;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@JacksonXmlRootElement(localName = "meps")
@Getter
@AllArgsConstructor
@NoArgsConstructor
class EuropeMembers {

    @JacksonXmlProperty(localName = "mep")
    @JacksonXmlElementWrapper(localName = "mep", useWrapping = false)
    private  List<EuropeMember> members;

}