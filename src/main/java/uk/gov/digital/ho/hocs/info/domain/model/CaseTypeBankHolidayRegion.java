package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import uk.gov.digital.ho.hocs.info.domain.model.enums.BankHolidayRegion;

import javax.persistence.Entity;
import javax.persistence.*;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@TypeDef(name = "pgsql_enum", typeClass = org.hibernate.type.EnumType.class)
@Table(name = "case_type_bank_holiday_region")
public class CaseTypeBankHolidayRegion {

    @Getter
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Getter
    @Column(name = "case_type_uuid")
    private UUID caseTypeUuid;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name = "region")
    @Type(type = "pgsql_enum")
    private BankHolidayRegion region;

    public CaseTypeBankHolidayRegion(UUID caseTypeUuid, String region) {
        this.caseTypeUuid = caseTypeUuid;
        this.region = BankHolidayRegion.valueOf(region);
    }

}
