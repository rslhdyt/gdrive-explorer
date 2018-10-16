package com.rslhdyt.google.drive.web.controller.google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;
import com.rslhdyt.google.drive.model.UserModel;
import com.rslhdyt.google.drive.repository.UserRepository;
import com.rslhdyt.google.drive.util.GoogleDriveUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
public class DriveFolderController {

    public static Drive drive;

    @Autowired
    UserRepository userRepository;

    @GetMapping(value = {"/google-drive/folders"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> list(HttpServletResponse response) throws Exception {
        UserModel user = userRepository.findById((long) 1).get();
        String accessToken = user.getAccessToken();

        if (StringUtils.isEmpty(accessToken)) {
            throw new Exception("Please authorize your google account first.");
        }

        Credential credential = GoogleDriveUtil.getCredentialsByToken(accessToken);
        drive = GoogleDriveUtil.getDriveService(credential);

        String pageToken = null;
        List<com.google.api.services.drive.model.File> list = new ArrayList<>();

        do {
            FileList result = drive.files().list().setQ(" mimeType = 'application/vnd.google-apps.folder' ")
                    .setSpaces("drive")
                    .setFields("nextPageToken, files(id, name, iconLink, parents), kind, nextPageToken")//
                    .setPageToken(pageToken).execute();
            for (com.google.api.services.drive.model.File file : result.getFiles()) {
                list.add(file);
            }
            pageToken = result.getNextPageToken();
        } while (pageToken != null);


        return new ResponseEntity<Object>(list, HttpStatus.OK);
    }
}
