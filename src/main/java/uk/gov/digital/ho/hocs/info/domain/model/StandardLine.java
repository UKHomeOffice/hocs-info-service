package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;
import uk.gov.digital.ho.hocs.info.api.dto.UpdateStandardLineDto;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@javax.persistence.Entity
@Table(name = "standard_line")
@NoArgsConstructor
public class StandardLine implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Getter
    @Column(name = "uuid")
    private UUID uuid;

    @Getter
    @Setter
    @Column(name = "document_uuid")
    private UUID documentUUID;

    @Getter
    @Column(name = "display_name")
    private String displayName;

    @Getter
    @Column(name = "topic_uuid")
    private UUID topicUUID;

    @Getter
    @Column(name = "expires")
    private LocalDateTime expires;

    public StandardLine(String displayName, UUID topicUUID, LocalDateTime expires) {
        this.uuid = UUID.randomUUID();
        this.displayName = displayName;
        this.topicUUID = topicUUID;
        this.expires = expires;
    }

    public void expire() {
        this.expires = LocalDateTime.now();
    }

    public void update(UpdateStandardLineDto request) {
        if (StringUtils.hasText(request.getDisplayName())) {
            this.displayName = request.getDisplayName();
        }

        if (request.getExpires() != null) {
            this.expires = LocalDateTime.of(request.getExpires(), LocalTime.MAX.minusHours(1));
        }
    }

}
