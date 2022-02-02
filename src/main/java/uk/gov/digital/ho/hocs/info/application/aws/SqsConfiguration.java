package uk.gov.digital.ho.hocs.info.application.aws;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({ "sqs" })
public class SqsConfiguration {

    @Primary
    @Bean
    public AmazonSQSAsync sqsClient(@Value("${aws.sqs.notify.account.access-key}") String accessKey,
                                    @Value("${aws.sqs.notify.account.secret-key}") String secretKey,
                                    @Value("${aws.sqs.config.region}") String region) {
        return AmazonSQSAsyncClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .withClientConfiguration(new ClientConfiguration())
                .build();
    }

}
