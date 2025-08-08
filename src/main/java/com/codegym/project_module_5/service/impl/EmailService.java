package com.codegym.project_module_5.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.springframework.beans.factory.annotation.Value;

@Service
public class EmailService {
    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;
    @Value("${sendgrid.from.email}")
    private String fromEmail;
    @Value("${sendgrid.template.id}")
    private String templateId;
    @Value("${sendgrid.templateregister.id}")
    private String templateRegisterId;

    // nội dung email xác nhận nhà hàng
    public boolean sendApprovalEmail(String toEmail, String restaurantName) {
        String message = "Your restaurant has been <b>approved</b>. We wish you great success in your business!";
        return sendEmail(toEmail, restaurantName, message, templateId);
    }

    // nội dung email từ chối nhà hàng
    public boolean sendRejectionEmail(String toEmail, String restaurantName) {
        String message = "Unfortunately, your restaurant has <b>not been approved</b>. Please review your information.";
        return sendEmail(toEmail, restaurantName, message, templateId);
    }

    // gửi mail xác nhận email từ chối nhà hàng
    private boolean sendEmail(String toEmail, String restaurantName, String message, String templateId) {
        Email from = new Email(fromEmail);
        Email to = new Email(toEmail);

        Mail mail = new Mail();
        mail.setFrom(from);
        mail.setTemplateId(templateId);

        Personalization personalization = new Personalization();
        personalization.addTo(to);
        personalization.addDynamicTemplateData("restaurantName", restaurantName);
        personalization.addDynamicTemplateData("message", message);

        mail.addPersonalization(personalization);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                System.out.println("✅ Email sent successfully to " + toEmail);
                return true;
            } else {
                System.err.println("❌ Failed to send email to " + toEmail + ". Status: " + response.getStatusCode());
                System.err.println("Response body: " + response.getBody());
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendOtpEmail(String to, String name, String otpCode) {
        try {
            Email from = new Email(fromEmail);
            Email toEmail = new Email(to);
            String subject = "Mã xác minh tài khoản của bạn";

            Map<String, String> dynamicData = new HashMap<>();
            dynamicData.put("name", name);
            dynamicData.put("otpCode", otpCode);

            Mail mail = new Mail();
            mail.setFrom(from);
            mail.setSubject(subject);
            mail.setTemplateId(templateRegisterId);

            Personalization personalization = new Personalization();
            personalization.addTo(toEmail);
            dynamicData.forEach(personalization::addDynamicTemplateData);
            mail.addPersonalization(personalization);

            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            SendGrid sg = new SendGrid(sendGridApiKey);
            Response response = sg.api(request);

            return response.getStatusCode() == 202; // true = gửi thành công
        } catch (Exception e) {
            return false; // false = gửi thất bại
        }
    }

}
