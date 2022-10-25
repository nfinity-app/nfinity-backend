package com.nfinity.aws;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.pinpoint.PinpointClient;
import software.amazon.awssdk.services.pinpoint.model.*;

import java.util.*;

public class PinPointV2Util {
    static String appId = "1074861e193240e6bfc2ab5c80d69ce8";
    static final String charset = "utf-8";
    static final String subject = "Verify Your E-mail Address";
    static final String toAddress = "1597406469@qq.com";
    static final String htmlBody = "registration is successful";
    static final String senderAddress = "nwang@nfinitymint.com";
    static final String templateName = "nfinity-register-template";
    static final String templateVersion = "1";
    public static void sendEmail() {
        Map<String, AddressConfiguration> addressMap = new HashMap<>();
        AddressConfiguration configuration = AddressConfiguration.builder()
                .channelType(ChannelType.EMAIL)
                .build();

        addressMap.put(toAddress, configuration);
//        SimpleEmailPart emailPart = SimpleEmailPart.builder()
//                .data(htmlBody)
//                .charset(charset)
//                .build() ;
//
//        SimpleEmailPart subjectPart = SimpleEmailPart.builder()
//                .data(subject)
//                .charset(charset)
//                .build() ;
//
//        SimpleEmail simpleEmail = SimpleEmail.builder()
//                .htmlPart(emailPart)
//                .subject(subjectPart)
//                .build();
//
//        EmailMessage emailMessage = EmailMessage.builder()
//                .body(htmlBody)
//                .fromAddress(senderAddress)
//                .simpleEmail(simpleEmail)
//                .build();
//
//        DirectMessageConfiguration directMessageConfiguration = DirectMessageConfiguration.builder()
//                .emailMessage(emailMessage)
//                .build();


        Template template = Template.builder()
                .name(templateName)
                .version(templateVersion)
                .build();

        TemplateConfiguration templateConfiguration = TemplateConfiguration.builder()
                .emailTemplate(template)
                .build();

        MessageRequest messageRequest = MessageRequest.builder()
                .addresses(addressMap)
//                .messageConfiguration(directMessageConfiguration)
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

        pinpoint.sendMessages(messagesRequest);
        System.out.println("Email was sent");
        pinpoint.close();
    }

    public static void main(String[] args) {
        sendEmail();
    }
}
