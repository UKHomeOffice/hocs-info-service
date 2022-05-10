package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "case_tab")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class CaseTab implements Serializable {

    @Id
    @Column(name = "uuid")
    private UUID uuid;

    @Getter
    @Column(name = "tab_name")
    private String name;

    @Getter
    @Column(name = "tab_label")
    private String label;

    @Getter
    @Column(name = "tab_screen")
    private String screen;

}
