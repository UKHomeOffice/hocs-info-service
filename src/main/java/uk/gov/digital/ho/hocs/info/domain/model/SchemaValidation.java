package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(SchemaValidation.class)
@Table(name = "screen_validation")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SchemaValidation implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "schema_uuid", referencedColumnName = "uuid")
    private Schema schemaUuid;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "validation_rule_uuid", referencedColumnName = "uuid")
    private ValidationRule validationRule;
}
