package uk.gov.digital.ho.hocs.info.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "member")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Access(AccessType.FIELD)
@EqualsAndHashCode(of = {"fullTitle", "referenceName"})
public class Member implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "house_id")
    private Long houseId;

    @Column(name = "full_title")
    private String fullTitle;

    @Column(name = "external_reference")
    private String externalReference;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "updated")
    private LocalDateTime updated;

    @Column(name = "deleted")
    @Setter
    private Boolean deleted = false;


    public Member( String fullTitle, String externalReference) {
        this.fullTitle = toListText(fullTitle);
        this.externalReference = toListValue(externalReference);
        this.uuid = toListValue(UUID.randomUUID().toString());
        this.updated = LocalDateTime.now();
    }

    private static String toListText(String text) {
        text = text.startsWith("\"") ? text.substring(1) : text;
        text = text.endsWith("\"") ? text.substring(0, text.length() - 1) : text;
        return text;
    }

    private static String toListValue(String value) {
        return value.replaceAll(" ", "_")
                .replaceAll("[^a-zA-Z0-9_]+", "")
                .replaceAll("__", "_")
                .toUpperCase();
    }
}