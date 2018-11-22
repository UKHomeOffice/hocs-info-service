package uk.gov.digital.ho.hocs.info.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "case_type")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = {"type"})
public class CaseTypeEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "type")
    private String type;

    @Column(name = "tenant_role")
    private String role;

    @Column(name = "active")
    private boolean active;

}
