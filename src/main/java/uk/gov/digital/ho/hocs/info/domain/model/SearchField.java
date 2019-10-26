package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "search_field")
@NoArgsConstructor
@AllArgsConstructor
public class SearchField implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(name = "parent_system_name")
    private String parentSystemName;

    @Getter
    @Column(name = "name")
    private String name;

    @Getter
    @Column(name = "component")
    private String component;

    @Getter
    @Column(name = "validation")
    private String validation;

    @Getter
    @Column(name = "props")
    private String props;


}
