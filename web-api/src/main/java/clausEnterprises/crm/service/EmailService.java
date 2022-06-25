package clausEnterprises.crm.service;

import clausEnterprises.crm.consts.MessagesConst;
import clausEnterprises.crm.consts.enums.Behaviour;
import clausEnterprises.crm.consts.enums.RejectionReason;
import clausEnterprises.crm.dto.MailDto;
import clausEnterprises.crm.dto.ProcessedParentResponse;
import clausEnterprises.crm.model.Client;
import clausEnterprises.crm.model.ParentResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import static clausEnterprises.crm.consts.MailCredentials.SUBJECT_FOR_A_CORPORATION;
import static clausEnterprises.crm.consts.MailCredentials.SUBJECT_FOR_A_PARENT;

@Service
public class EmailService {
    private final JavaMailSenderImpl mailSender;
    private final ObjectMapper mapper = new ObjectMapper();
    private final ParentResponseService parentResponseService;
    private final ClientService clientService;

    private final String username;

    public EmailService(JavaMailSenderImpl javaMailSender,
                        ParentResponseService parentResponseService,
                        ClientService clientService,
                        @Value("${spring.mail.username}") String username) {
        this.username = username;
        this.clientService = clientService;
        this.parentResponseService = parentResponseService;
        this.mailSender = javaMailSender;

    }

    public String sendEmailToCompany(MailDto letter, Model model) throws MessagingException, JsonProcessingException {
        letter.setSubject(username);
        model.addAttribute("letter", letter);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        helper.setTo(username);
        helper.setSubject(SUBJECT_FOR_A_CORPORATION);
        helper.setText(mapper.writeValueAsString(letter));
        mailSender.send(message);
        return "new-letter";
    }

    public void sendLetterToParents(MailDto mailDto) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        helper.setTo(mailDto.getParentEmail());
        helper.setSubject(SUBJECT_FOR_A_PARENT);
        helper.setText("Hello! Claus Enterprises just got a message from your children " + mailDto.getFirstName() + " "
                + mailDto.getLastName() + " with email " + mailDto.getEmail() + "" +
                " regarding getting a gift for a good behaviour.\nWould you kindly choose \"YES\" if they acted good, " +
                "and the address \"" + mailDto.getAddress() + "\" is correct" +
                ", or NO otherwise so we could be sure " +
                "that your kid actually behaved good last year and the address is valid\nHere's the link to the form - " +
                "https://forms.gle/qa2sZCjgbbgUV5Ks5\nThank you\nClaus Enterprises (c)");
        mailSender.send(message);
        Client client = Client.builder()
                .parentEmail(mailDto.getParentEmail())
                .behaviour(Behaviour.UNKNOWN)
                .country(mailDto.getCountry())
                .address(mailDto.getAddress())
                .firstName(mailDto.getFirstName())
                .lastName(mailDto.getLastName())
                .lastTimeDelivered(null)
                .email(mailDto.getEmail())
                .build();
        clientService.saveClient(client);
        parentResponseService.saveResponse(ParentResponse.builder()
                .children(client)
                .behaviour(null)
                .dateCreated(LocalDateTime.now())
                .build());
    }

    public List<ProcessedParentResponse> findEmailsForUnclearResponses() {
        List<ProcessedParentResponse> processedParentResponses = new ArrayList<>();
        parentResponseService.checkParentResponsesSecondTime()
                .forEach(parentResponse ->
                {
                    processedParentResponses.add(ProcessedParentResponse.builder()
                            .clientEmail(parentResponse.getChildren().getEmail())
                            .parentEmail(parentResponse.getChildren().getParentEmail())
                            .behaviour(parentResponse.getBehaviour())
                            .build());
                });
        return processedParentResponses;
    }

    public void sendRejectionToClient(String email, RejectionReason reason) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        helper.setTo(email);
        helper.setSubject("Rejection for a gift");
        helper.setText(MessagesConst.REJECTION_MAP.get(reason));
        mailSender.send(message);
    }


    public boolean isAddressValid(String address) {
        int pos = address.indexOf('@');
        if (pos == -1) return false;
        String domain = address.substring(++pos);
        ArrayList<String> mxList;
        try {
            mxList = getMX(domain);
        } catch (NamingException ex) {
            return false;
        }

        if (mxList.size() == 0) return false;

        for (String o : mxList) {
            boolean valid = false;
            try {
                int res;
                Socket skt = new Socket(o, 25);
                BufferedReader rdr = new BufferedReader
                        (new InputStreamReader(skt.getInputStream()));
                BufferedWriter wtr = new BufferedWriter
                        (new OutputStreamWriter(skt.getOutputStream()));

                res = listen(rdr);
                if (res != 220) throw new Exception("Invalid header");
                bounceTheMail(wtr, "EHLO orbaker.com");

                res = listen(rdr);
                if (res != 250) throw new Exception("Not ESMTP");

                bounceTheMail(wtr, "MAIL FROM: <tim@orbaker.com>");
                res = listen(rdr);
                if (res != 250) throw new Exception("Sender rejected");

                bounceTheMail(wtr, "RCPT TO: <" + address + ">");
                res = listen(rdr);

                bounceTheMail(wtr, "RSET");
                listen(rdr);
                bounceTheMail(wtr, "QUIT");
                listen(rdr);
                if (res != 250)
                    throw new Exception("Address is not valid!");

                valid = true;
                rdr.close();
                wtr.close();
                skt.close();
            } catch (Exception ex) {
            }
            if (valid) return true;
        }
        return false;
    }

    private int listen(BufferedReader in) throws IOException {
        String line;
        int res = 0;

        while ((line = in.readLine()) != null) {
            String pfx = line.substring(0, 3);
            try {
                res = Integer.parseInt(pfx);
            } catch (Exception ex) {
                res = -1;
            }
            if (line.charAt(3) != '-') break;
        }

        return res;
    }

    private void bounceTheMail(BufferedWriter wr, String text)
            throws IOException {
        wr.write(text + "\r\n");
        wr.flush();

    }

    private ArrayList<String> getMX(String hostName)
            throws NamingException {
        Hashtable<String, String> env = new Hashtable<>();
        env.put("java.naming.factory.initial",
                "com.sun.jndi.dns.DnsContextFactory");
        DirContext ictx = new InitialDirContext(env);
        Attributes attrs = ictx.getAttributes
                (hostName, new String[]{"MX"});
        Attribute attr = attrs.get("MX");

        if ((attr == null) || (attr.size() == 0)) {
            attrs = ictx.getAttributes(hostName, new String[]{"A"});
            attr = attrs.get("A");
            if (attr == null)
                throw new NamingException
                        ("No match for name '" + hostName + "'");
        }
        ArrayList<String> res = new ArrayList<>();
        NamingEnumeration<?> en = attr.getAll();

        while (en.hasMore()) {
            String x = (String) en.next();
            String[] f = x.split(" ");
            if (f[1].endsWith("."))
                f[1] = f[1].substring(0, (f[1].length() - 1));
            res.add(f[1]);
        }
        return res;
    }
}
