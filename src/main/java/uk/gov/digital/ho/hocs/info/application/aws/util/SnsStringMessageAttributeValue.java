package uk.gov.digital.ho.hocs.info.application.aws.util;

import com.amazonaws.services.sns.model.MessageAttributeValue;
import org.springframework.util.StringUtils;

public class SnsStringMessageAttributeValue extends MessageAttributeValue {

    private static final String type = "String";

    public SnsStringMessageAttributeValue(String value) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException("Value should be a non-empty String");
        }

        this.setDataType(type);
        this.setStringValue(value);
    }

}
