package clausEnterprises.crm.config;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class GoogleAuthorizeConfig {

    private static final String CREDENTIALS_FILE_PATH = "/google-sheets-client-secret.json";

    public static Credential authorize() throws IOException {
        return GoogleCredential.fromStream(Objects.requireNonNull(GoogleAuthorizeConfig.class
                        .getResourceAsStream(CREDENTIALS_FILE_PATH)))
                .createScoped(Collections.singletonList("https://www.googleapis.com/auth/spreadsheets"));
    }
}