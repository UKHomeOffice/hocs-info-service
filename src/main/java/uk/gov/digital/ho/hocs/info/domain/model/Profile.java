package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;

@jakarta.persistence.Entity
@Table(name = "profile")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Profile implements Serializable {

    @Id
    @Column(name = "profile_name")
    private String profileName;

    @Column(name = "parent_system_name")
    private String parentSystemName;

    @Column(name = "summary_deadlines_enabled")
    private boolean summaryDeadlinesEnabled;

}
