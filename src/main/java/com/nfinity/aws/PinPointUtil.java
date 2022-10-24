package com.nfinity.aws;

import com.amazonaws.services.pinpoint.AmazonPinpoint;
import com.amazonaws.services.pinpoint.AmazonPinpointClientBuilder;
import com.amazonaws.services.pinpoint.model.*;

import java.util.HashMap;
import java.util.Map;

public class PinPointUtil {
    public static String region = "us-east-1";
    public static String appId = "1074861e193240e6bfc2ab5c80d69ce8";

    static final String templateName = "nfinity-register-template";
    static final String templateVersion = "1";

    static final String subject = "Verify Your E-mail Address";
    static final String textBody = "Congratulations, the registration is successful, please click the link to jump back to the website. The link is ";

    static final String charset = "utf-8";

    static final String websiteUrl = "http://localhost:3000/user/";

    private static String createContent(String link){
        return textBody + link;
    }
    public static boolean sendEmail(String toAddress, String verificationCode, String type) {
        String link = websiteUrl + type + "?email=" + toAddress + "&verification_code=" + verificationCode;

//        try {
            Map<String, AddressConfiguration> addressMap = new HashMap<>();

            addressMap.put(toAddress, new AddressConfiguration().withChannelType(ChannelType.EMAIL));

            AmazonPinpoint client = AmazonPinpointClientBuilder.standard().withRegion(region).build();

            SendMessagesRequest request = (new SendMessagesRequest()
                    .withApplicationId(appId)
                    .withMessageRequest(new MessageRequest()
                            .withAddresses(addressMap)
//                            .withTemplateConfiguration(new TemplateConfiguration()
//                                    .withEmailTemplate(new Template()
//                                            .withName(templateName)
//                                            .withVersion(templateVersion)
//                                    )
//                            )
                            .withMessageConfiguration(new DirectMessageConfiguration()
                                    .withEmailMessage(new EmailMessage()
                                            .withSimpleEmail(new SimpleEmail()
//                                                    .withHtmlPart(new SimpleEmailPart()
//                                                            .withCharset(charset)
//                                                            .withData(htmlBody)
//                                                    )
                                                    .withTextPart(new SimpleEmailPart()
                                                            .withCharset(charset)
                                                            .withData(createContent(link))
                                                    )
                                                    .withSubject(new SimpleEmailPart()
                                                            .withCharset(charset)
                                                            .withData(subject)
                                                    )
                                            )
                                    )
                            )
                    )
            );

            System.out.println("Sending message...");
            SendMessagesResult result = client.sendMessages(request);
            System.out.println("send email result: " + result);
            System.out.println("Message sent!");
            return result.getMessageResponse().getResult().get(toAddress).getStatusCode() == 200;
//        } catch (Exception ex) {
//            System.out.println("The message wasn't sent. Error message: " + ex.getMessage());
//        }
    }
}
