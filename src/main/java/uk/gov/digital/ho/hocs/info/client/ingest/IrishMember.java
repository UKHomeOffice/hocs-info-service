package uk.gov.digital.ho.hocs.info.client.ingest;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class IrishMember {

    @JacksonXmlProperty(localName = "MemberFirstName")
    private String firstName;

    @JacksonXmlProperty(localName = "MemberLastName")
    private String lastName;

    @Getter
    @JacksonXmlProperty(localName = "MemberFullDisplayName")
    private String fullDisplayName;

    @Getter
    @JacksonXmlProperty(localName = "PersonId")
    private String personId;

    public String getName() {
        return firstName + " " + lastName;
    }

}