package uk.gov.digital.ho.hocs.info.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "sla")
@NoArgsConstructor
@Getter
public class Sla implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(name = "stage_type")
    private String stageType;

    @Getter
    @Column(name = "value")
    private int value;

    @Getter
    @Column(name = "case_type")
    private String caseType;

    public Sla( String stageType, int value, String caseType){
        this.stageType = stageType;
        this.value = value;
        this.caseType = caseType;
    }

}
