package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor()
@NoArgsConstructor()
@Getter
@Entity
@Table(name = "system_configuration")
@EqualsAndHashCode(of = {"systemName"})
public class Configuration implements Serializable {


    @Id
    @Column(name = "system_name")
    private String systemName;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "document_labels")
    private String documentLabels;

    @Column(name = "workstack_columns")
    private String workstackColumns;

}
