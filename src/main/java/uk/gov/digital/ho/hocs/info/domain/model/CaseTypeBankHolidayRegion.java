package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import uk.gov.digital.ho.hocs.info.domain.model.enums.BankHolidayRegion;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Convert(attributeName = "pgsql_enum", converter = org.hibernate.type.EnumType.class)
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
    @Column(name = "region")
    @Enumerated(EnumType.ORDINAL)
    private BankHolidayRegion region;

    public CaseTypeBankHolidayRegion(UUID caseTypeUuid, String region) {
        this.caseTypeUuid = caseTypeUuid;
        this.region = BankHolidayRegion.valueOf(region);
    }

}
