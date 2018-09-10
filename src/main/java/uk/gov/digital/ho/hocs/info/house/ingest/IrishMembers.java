package uk.gov.digital.ho.hocs.info.house.ingest;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@JacksonXmlRootElement(localName = "AllMembersList")
@Getter
@AllArgsConstructor
@NoArgsConstructor
class IrishMembers {

    @JacksonXmlProperty(localName = "Member")
    @JacksonXmlElementWrapper(localName = "Member", useWrapping = false)
    private List<IrishMember> members;

}