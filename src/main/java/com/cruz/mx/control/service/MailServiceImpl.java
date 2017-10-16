package com.cruz.mx.control.service;

import com.cruz.mx.control.dao.beans.PersonalBean;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import java.io.File;

@SuppressWarnings("deprecation")
@Service("mailService")
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private Configuration freemarkerConfiguration;

    @Override
    public void sendEmail(final PersonalBean personal, final File archivo, final String destino){
        MimeMessagePreparator preparator = getMessagePreparatorAsistencias(personal, archivo, destino);
        try {
            mailSender.send(preparator);
            System.out.println("Message has been sent.............................");
        } catch (MailException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private MimeMessagePreparator getMessagePreparatorAsistencias(final PersonalBean personal, final File archivo, final String destino) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                helper.setSubject("Formato de asistencias");
                helper.setFrom("customerserivces@yourshop.com");
                helper.setTo(destino);

                Map<String, Object> model = new HashMap<String, Object>();
                model.put("personal", personal);

                String text = getAsistenciasTemplateContent(model);
                System.out.println("Template content asistencias: " + text);

                // use the true flag to indicate you need a multipart message
                helper.setText(text, true);

                //Additionally, let's add a resource as an attachment as well.
                helper.addAttachment("asistencias.xls", archivo);
            }
        };
        return preparator;
    }
    
    public String getFreeMarkerTemplateContent(Map<String, Object> model) {
        StringBuffer content = new StringBuffer();
        try {
            content.append(FreeMarkerTemplateUtils.processTemplateIntoString(
                    freemarkerConfiguration.getTemplate("fm_mailTemplate.txt"), model));
            return content.toString();
        } catch (Exception e) {
            System.out.println("Exception occured while processing fmtemplate:" + e.getMessage());
        }
        return "";
    }

    public String getAsistenciasTemplateContent(Map<String, Object> model) {
        StringBuffer content = new StringBuffer();
        try {
            content.append(FreeMarkerTemplateUtils.processTemplateIntoString(
                    freemarkerConfiguration.getTemplate("fm_mailAsistencias.txt"), model));
            return content.toString();
        } catch (Exception e) {
            System.out.println("Exception occured while processing fmtemplate:" + e.getMessage());
        }
        return "";
    }

}
