package com.nfinity.aws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.pinpoint.PinpointClient;
import software.amazon.awssdk.services.pinpoint.model.*;

import java.util.*;

@Slf4j
public class PinPointV2Util {
    static String appId = "1074861e193240e6bfc2ab5c80d69ce8";
    static final String senderAddress = "nwang@nfinitymint.com";
    static final String templateName = "nfinity-register-template";
    static final String templateVersion = "1";

    @Value("${website.url}")
    private String websiteUrl;

    public void sendEmail(String email, String verificationCode, String type) {
        String url = websiteUrl + type + "?email=" + email + "&verification_code=" + verificationCode;

        Map<String, AddressConfiguration> addressMap = new HashMap<>();
        AddressConfiguration configuration = AddressConfiguration.builder()
                .channelType(ChannelType.EMAIL)
                .build();

        addressMap.put(email, configuration);

        Map<String, List<String>> substitutions = new HashMap<>();
        substitutions.put("url", Collections.singletonList(url));

        EmailMessage emailMessage = EmailMessage.builder()
                .fromAddress(senderAddress)
                .substitutions(substitutions)
                .build();

        DirectMessageConfiguration directMessageConfiguration = DirectMessageConfiguration.builder()
                .emailMessage(emailMessage)
                .build();

        Template template = Template.builder()
                .name(templateName)
                .version(templateVersion)
                .build();

        TemplateConfiguration templateConfiguration = TemplateConfiguration.builder()
                .emailTemplate(template)
                .build();

        MessageRequest messageRequest = MessageRequest.builder()
                .addresses(addressMap)
                .messageConfiguration(directMessageConfiguration)
                .templateConfiguration(templateConfiguration)
                .build();

        SendMessagesRequest messagesRequest = SendMessagesRequest.builder()
                .applicationId(appId)
                .messageRequest(messageRequest)
                .build();


        PinpointClient pinpoint = PinpointClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();

        SendMessagesResponse response = pinpoint.sendMessages(messagesRequest);
        log.info(String.valueOf(response));
        log.info("Email was sent");
        pinpoint.close();
    }
}
