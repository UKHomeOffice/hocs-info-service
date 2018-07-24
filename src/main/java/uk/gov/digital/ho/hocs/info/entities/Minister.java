package uk.gov.digital.ho.hocs.info.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "minister")
@AllArgsConstructor
@NoArgsConstructor
public class Minister {

    @Id
    @Column(name = "id")
    @JsonProperty("id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "display_name")
    @JsonProperty("display_name")
    private String displayName;
}
