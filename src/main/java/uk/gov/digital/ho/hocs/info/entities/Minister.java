package uk.gov.digital.ho.hocs.info.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "minister")
@NoArgsConstructor
@AllArgsConstructor
public class Minister {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private int id;

    @Column(name = "display_name")
    @JsonProperty("name")
    private String displayName;
}
