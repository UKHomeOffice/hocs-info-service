package uk.gov.digital.ho.hocs.info.api;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class LocalDateWrapperImpl implements LocalDateWrapper {

    @Override
    public LocalDate now() {
        return LocalDate.now();
    }

}
