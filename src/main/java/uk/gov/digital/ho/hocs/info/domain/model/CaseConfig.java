package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Deprecated(forRemoval = true)
public class CaseConfig {
    @Getter
    private final String type;

    @Getter
    private final List<CaseTab> tabs;
}
