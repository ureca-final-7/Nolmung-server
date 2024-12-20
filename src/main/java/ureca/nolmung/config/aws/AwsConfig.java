package ureca.nolmung.config.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.personalizeruntime.PersonalizeRuntimeClient;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsConfig {
    @Value("${aws.credentials.access-key}")
    private String iamAccessKey;
    @Value("${aws.credentials.secret-key}")
    private String iamSecretKey;
    @Value("${aws.region.static}")
    private Region region;
    @Value("${aws.personalize.campaign-arn}")
    private String campaignArn;
    @Value("${aws.s3.bucket")
    private String bucketName;

    @Bean
    public PersonalizeRuntimeClient personalizeRuntimeClient(){
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(iamAccessKey, iamSecretKey);
        return PersonalizeRuntimeClient.builder()
                .region(region)
                .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                .build();
    }

    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(iamAccessKey, iamSecretKey);
        return S3Client.builder()
                .region(region)
                .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                .build();
    }

    @Bean
    public String campaignArn() {
        return campaignArn;
    }

    @Bean
    public String bucketName() {
        return bucketName;
    }
}
