/*
 * Dancing Numbers - Logistic growth applied to Covid-19
 *
 * A naive approach to predict the spread of virus
 *
 *
 * Copyright (c) 2020 V. Eitner
 *
 * This file is part of "Dancing Numbers".
 *
 * "Dancing Numbers" is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * "Dancing Numbers" is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with "Dancing Numbers".  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package de.vee.data;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Downloader {


    private final static int BUFFER_SIZE = 1024;

    private void copy(InputStream input, OutputStream output) throws IOException {
        byte[] buf = new byte[BUFFER_SIZE];
        int n = input.read(buf);
        while (n >= 0) {
            output.write(buf, 0, n);
            n = input.read(buf);
        }
        output.flush();
    }

    private String getDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, -1);  // number of days to add
        return sdf.format(c.getTime());
    }


    Path get(Path downloadTo) {
        URL url;
        Path filePath = null;
        try {
            Files.createDirectories(downloadTo);

            String name = "COVID-19-geographic-disbtribution-worldwide-" + getDate() + ".csv";
            filePath = downloadTo.resolve(name);
            File fout = filePath.toFile();
            if (fout.exists()) return filePath;
            //uii :)
            SSLUtilities.trustAllHostnames();
            SSLUtilities.trustAllHttpsCertificates();
            url = new URL("https://opendata.ecdc.europa.eu/covid19/casedistribution/csv");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream in = connection.getInputStream();
            FileOutputStream out = new FileOutputStream(fout);
            copy(in, out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }

    public static void main(String[] args) {
        new Downloader().get(new File("./csv/").toPath());

    }
}
