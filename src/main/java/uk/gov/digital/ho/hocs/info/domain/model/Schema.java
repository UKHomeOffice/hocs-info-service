package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@javax.persistence.Entity
@Table(name = "screen_schema")
@NoArgsConstructor
@AllArgsConstructor
public class Schema implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(name = "uuid")
    private UUID uuid;

    @Getter
    @Column(name = "type")
    private String type;

    @Getter
    @Column(name = "title")
    private String title;

    @Getter
    @Column(name = "action_label")
    private String actionLabel;

    @Getter
    @Column(name = "active")
    private boolean active;

    @Getter
    @Column(name = "stage_type", insertable = false, updatable = false)
    private String stageType;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "schema_uuid", referencedColumnName = "uuid", insertable = false, updatable = false)
    @OrderBy("sortOrder")
    private List<FieldScreen> fieldScreens;

    @Getter
    @OneToMany
    @JoinTable(
            name = "secondary_action_screen",
            joinColumns = @JoinColumn(name = "schema_uuid", referencedColumnName = "uuid"),
            inverseJoinColumns = @JoinColumn(name = "secondary_action_uuid", referencedColumnName = "uuid")
    )
    @OrderBy("id")
    private List<SecondaryAction> secondaryActions;

    @Getter
    @Column(name = "props")
    private String props;

    @Getter
    @Column(name = "validation")
    private String validation;

    public List<Field> getFields() {
        return fieldScreens.stream()
                .map(e -> e.getField())
                .collect(Collectors.toList());

    }

}
