package uk.gov.digital.ho.hocs.info.application.aws;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StringUtils;

@Configuration
@Profile({"sns"})
public class SnsConfiguration {

    @Bean("auditSnsClient")
    public AmazonSNS auditSnsClient(@Value("${audit.aws.sns.access.key}") String accessKey,
                                    @Value("${audit.aws.sns.secret.key}") String secretKey,
                                    @Value("${aws.sqs.region}") String region) {

        if (StringUtils.isEmpty(accessKey)) {
            throw new BeanCreationException("Failed to create SNS client bean. Need non-blank value for access key");
        }

        if (StringUtils.isEmpty(secretKey)) {
            throw new BeanCreationException("Failed to create SNS client bean. Need non-blank values for secret key");
        }

        if (StringUtils.isEmpty(region)) {
            throw new BeanCreationException("Failed to create SNS client bean. Need non-blank values for region: " + region);
        }

        return AmazonSNSClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .withClientConfiguration(new ClientConfiguration())
                .build();
    }
}
