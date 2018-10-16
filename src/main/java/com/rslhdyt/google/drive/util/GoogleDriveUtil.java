package com.rslhdyt.google.drive.util;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Collections;
import java.util.List;

@Component
public class GoogleDriveUtil {

    private static final String APPLICATION_NAME = "Gdrive Explorer";

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private static final File CREDENTIALS_FOLDER = new File(System.getProperty("user.home"), "credentials");

    private static final String CLIENT_SECRET_FILE_NAME = "client_secret_gd.json";

    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);

    private static final String REDIRECT_URI = "http://localhost:8080/oauth/google/callback";

    private static HttpTransport HTTP_TRANSPORT;

    private static Drive driveService;

    // Init HTTP Transport
    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Init google authorization code flow
     *
     * @return GoogleAuthorizationCodeFlow
     * @throws IOException IoException
     */
    public static GoogleAuthorizationCodeFlow getFlow () throws IOException {

        File clientSecretFilePath = new File(CREDENTIALS_FOLDER, CLIENT_SECRET_FILE_NAME);

        // throw error when client secret path doesn't exists
        if (!clientSecretFilePath.exists()) {
            throw new FileNotFoundException("client secret doesn't exists");
        }

        // set client secret
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(new FileInputStream(clientSecretFilePath)));

        return new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setAccessType("offline").build();
    }

    /**
     * Get google credentials by access token
     *
     * @param accessToken String
     * @return Credential
     */
    public static Credential getCredentialsByToken(String accessToken) {
        return new GoogleCredential().setAccessToken(accessToken);
    }

    /**
     * Get and generate redirect uri for oauth2
     *
     * @return redirect uri String
     * @throws IOException IoException
     */
    public static String getRedirectOauth() throws IOException {
        GoogleAuthorizationCodeRequestUrl url = getFlow().newAuthorizationUrl();

        return url.setRedirectUri(REDIRECT_URI).setAccessType("offline").build();
    }

    /**
     * Get and generate access token
     *
     * @param code
     * @param userId
     * @return accessToken String
     * @throws IOException exception
     */
    public static String getAccessToken(String code, String userId) throws IOException {
        GoogleTokenResponse response = getFlow().newTokenRequest(code).setRedirectUri(REDIRECT_URI).execute();
        Credential credential = getFlow().createAndStoreCredential(response, userId);

        return credential.getAccessToken();
    }

    /**
     * Get google drive service
     *
     * @param credential
     * @return driveService
     */
    public static Drive getDriveService(Credential credential) {
        if (driveService != null) {
            return driveService;
        }

        // Build google drive service with builder
        driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME).build();

        return driveService;
    }
}
