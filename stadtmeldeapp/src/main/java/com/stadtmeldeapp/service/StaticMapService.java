package com.stadtmeldeapp.service;

import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StaticMapService {

    @Value("${api.key}")
    private String API_KEY;

    public String getMapImage(double latitude, double longitude) throws Exception {
        String imageUrl = "https://maps.googleapis.com/maps/api/staticmap?center=" + latitude + "," + longitude
                + "&zoom=14&size=400x400&markers=color:blue%7C" + latitude + "," + longitude
                + "&key=" + API_KEY;
        URL url = new URL(imageUrl);
        URLConnection connection = url.openConnection();
        InputStream inputStream = connection.getInputStream();
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int n = 0;
        while (-1 != (n = inputStream.read(buffer))) {
            output.write(buffer, 0, n);
        }

        byte[] imageBytes = output.toByteArray();
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        return base64Image;
    }
}
