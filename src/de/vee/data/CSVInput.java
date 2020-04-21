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

import de.vee.model.Input;

import java.io.*;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class CSVInput {
    private static final Date start;

    static {
        Calendar c = Calendar.getInstance();
        c.set(2019, Calendar.DECEMBER, 30, 12, 0, 0);
        start = c.getTime();
    }

    static final Map<String, List<CSVRecord>> data = new HashMap<>();

    private static final String[] europe = {
            "Italy",
            "Germany",
            "France",
            "United_Kingdom",
            "Turkey",
            "Switzerland",
            "Belgium",
            "Netherlands",
            "Austria",
            "Portugal",
            "Israel",
            "Sweden",
            "Norway",
            "Russia",
            "Ireland",
            "Czechia",
            "Denmark",
            "Poland",
            "Romania",
            "Luxembourg",
            "Finland",
            "Serbia",
            "Greece",
            "Iceland",
            "Ukraine",
            "Croatia",
            "Estonia",
            "Slovenia",
            "Moldova",
            "Lithuania",
            "Armenia",
            "Hungary",
            "Bosnia_and_Herzegovina",
            "Kazakhstan",
            "Azerbaijan",
            "Belarus",
            "North_Macedonia",
            "Latvia",
            "Bulgaria",
            "Andorra",
            "Slovakia",
            "Cyprus",
            "Uzbekistan",
            "Albania",
            "San_Marino",
            "Malta",
            "Kyrgyzstan",
            "Montenegro",
            "Georgia",
            "Liechtenstein",
            "Monaco",
            "Holy_See",
            "Faroe_Islands",
            "Kosovo",
            "Guernsey",
            "Isle_of_Man",
            "Jersey",
            "Gibraltar",
            "Greenland"
    };

    private class CSVRecord {
        String date;
        int day;
        int month;
        int year;
        long cases;
        long deaths;
        String countriesAndTerritories;
        String geoId;
        String countryterritoryCode;
        long popData2018;

        CSVRecord(String date, String day, String month, String year, String cases, String deaths, String countriesAndTerritories, String geoId, String countryterritoryCode, String popData2018) {
            this.date = date;
            this.day = Integer.valueOf(day);
            this.month = Integer.valueOf(month);
            this.year = Integer.valueOf(year);
            this.cases = Long.valueOf(cases);
            this.deaths = Long.valueOf(deaths);
            this.countriesAndTerritories = countriesAndTerritories;
            this.geoId = geoId;
            this.countryterritoryCode = countryterritoryCode;
            this.popData2018 = Long.valueOf(popData2018);
        }

        /**
         * Get a diff between two dates
         *
         * @param date1    the oldest date
         * @param date2    the newest date
         * @param timeUnit the unit in which you want the diff
         * @return the diff value, in the provided unit
         */
        private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
            long diffInMillies = date2.getTime() - date1.getTime();
//            return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
            return diffInMillies / 1000L / 60 / 60 / 24;
        }

        long daysSinceStart() {
            String dt = year + "-" + month + "-" + day;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            try {
                c.set(year, month - 1, day, 12, 0, 0);
                c.setTime(sdf.parse(dt));
            } catch (ParseException e) {
                return -1;
            }
            long diff = getDateDiff(start, c.getTime(), TimeUnit.DAYS);
            if (diff == 0) {
                //strange - same for 19th and 20th of january
            }
            return diff;
        }
    }

    private void parse() throws IOException {
        Path src = new Downloader().get(new File("./csv/").toPath());
        if (src != null) {
            BufferedReader br = new BufferedReader(new FileReader(src.toFile()));
            br.readLine(); //skipHeader
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, ", \t\n\r\f", false);
                if (st.countTokens() == 11) {
                    String dateRep = st.nextToken();
                    String day = st.nextToken();
                    String month = st.nextToken();
                    String year = st.nextToken();
                    String cases = st.nextToken();
                    String deaths = st.nextToken();
                    String geoId = st.nextToken();
                    String continentExp = st.nextToken();
                    String countryterritoryCode = st.nextToken();
                    String popData2018 = st.nextToken();
                    String countriesAndTerritories = st.nextToken();
                    CSVRecord record = new CSVRecord(dateRep, day, month, year, cases, deaths, countriesAndTerritories, geoId, countryterritoryCode, popData2018);
                    List<CSVRecord> lst = (data.computeIfAbsent(record.countriesAndTerritories, k -> new ArrayList<>()));
                    lst.add(record);
                    count++;
                } else {
                    System.out.println(line);
                }
            }
            for (String key : data.keySet()) {
                List<CSVRecord> lst = data.get(key);
                lst.sort(Comparator.comparingLong(CSVRecord::daysSinceStart));
/*
//no - I do not care about
                int what = lst.size() - 1;
                CSVRecord r = lst.get(what);
                if ((r.month >= 4) && (r.day >= 9)) {
                    //they changed their reporting strategy somehow - really strange
                    //the dataset of the 9th contains data until 8th
                    //and the dataset of the 10th until 10th
                    //but a dataset including data up to the 9th of april is missing
                    //
                    //looks like they are extrapolating - so remove the last entry
                    lst.remove(what);
                    count--;
                }
*/
            }
            System.out.printf("Read %d records\n", count);
        }
    }

    private void convert(List<CSVRecord> lst, double[] x, double[] y, double[] dr, int[] count, double[] N) {
        lst.sort(Comparator.comparingLong(CSVRecord::daysSinceStart));

        int i = 0;
        long cases = 0;
        long deaths = 0;
        long oc = 0;
        for (CSVRecord record : lst) {
            if (N[0] != record.popData2018) N[0] = record.popData2018;
            long day = record.daysSinceStart();
            if ((day > 20) && (record.cases > 0)) {
                if (oc != record.cases) {
                    x[i] = day - 20;
                    y[i] = (cases += record.cases);
                    dr[i] = (deaths += record.deaths);
                    i++;
                }
                oc = record.cases;
            }
        }
        count[0] = i;
    }

    private class Entry {
        double x;
        double y;
        double dr;
    }

    public Input inputFor(String key) throws IOException {
        Input input;
        if (data.size() == 0) {
            parse();
        }
        input = new Input(key.replace("_", " "), 4E8);
        List<CSVRecord> lst;
        double[] x;
        double[] y;
        double[] dr;
        if ("world".compareToIgnoreCase(key) == 0) {
            input = input.withDelta(new double[]{0.1, 0.2, 0.1, 1e-2, 1e-1, 1e-2});
//                    .withInflectionPoint(true);
//                .withInitial(new double[]{1.E-5,17,14.E-2,14});

            input.setPopulationSize(0);
            HashMap<Integer, Entry> map = new HashMap<>();
            for (String id : data.keySet()) {
                List<CSVRecord> l = data.get(id);
                if (l.size() < 1) continue;
                x = new double[l.size()];
                y = new double[l.size()];
                dr = new double[l.size()];
                double[] n = new double[1];
                int[] count = new int[1];
                convert(l, x, y, dr, count, n);
                input.setPopulationSize(input.getPopulationSize() + n[0]);

                for (int i = 0; i < count[0]; i++) {
                    Entry e2 = map.get((int) x[i]);
                    if (e2 == null) e2 = new Entry();
                    map.put((int) x[i], e2);
                    e2.x = x[i];
                    e2.y += y[i];
                    e2.dr += dr[i];
                }
            }
            List<Entry> l = new ArrayList<>();
            for (int k : map.keySet()) {
                l.add(map.get(k));
            }
            l.sort(Comparator.comparingDouble(o -> o.x));
            x = new double[l.size()];
            y = new double[l.size()];
            dr = new double[l.size()];

            map.clear();


            for (int i = 0; i < l.size(); i++) {
                Entry e = l.get(i);
                x[i] = e.x;
                y[i] = e.y;
                dr[i] = e.dr;
            }

///* //looks also weird
            Input china1 = Input.get("China", 1428E6); //remove china
//                    .withDelta(new double[]{0.1, 0.2, 0.1, 0})
//                    .withInitial(new double[]{1.E-5, 17, 14.E-2, 14});

            double[][] china = china1.getData();
            double[] cx = china[0];
            double[] cy = china[1];
            double[] cdr = china[2];
            int k = 0;
            for (int i = 0; i < x.length; i++) {
                if (k == cx.length) break;
                if (k > 65) break; //begin of second wave in china
                if (cx[k] == x[i]) {
                    y[i] -= cy[k];
                    dr[i] -= cdr[k];
                    k++;
                }
            }

            //make it monotone again
            int i = 0;
            int m = 0;
            while (i < y.length - m - 1) {
                if (y[i + 1] <= y[i]) {
                    int o = 1;
                    while (y[i + o + 1] <= y[i]) o++;
                    for (int j = i + 1; j + o < y.length - m; j++) {
                        x[j] = x[j + o];
                        y[j] = y[j + o];
                        dr[j] = dr[j + o];
                    }
                    m += o;
                } else {
                    i++;
                }
            }
            x = Arrays.copyOf(x, x.length - m);
            y = Arrays.copyOf(y, y.length - m);
            dr = Arrays.copyOf(dr, dr.length - m);
//*/

            l.clear();
        } else if ("europe".compareToIgnoreCase(key) == 0) {
            input.setPopulationSize(0);
            HashMap<Integer, Entry> map = new HashMap<>();
            List<String> leur = new ArrayList<>(Arrays.asList(europe));
            for (String id : data.keySet()) {
                List<CSVRecord> l = data.get(id);
                if (l.size() < 1) continue;
                if (!leur.contains(id)) {
                    System.out.println("Ignoring " + id + " for Europe");
                    continue;
                }
                leur.remove(id);
                x = new double[l.size()];
                y = new double[l.size()];
                dr = new double[l.size()];
                double[] n = new double[1];
                int[] count = new int[1];
                convert(l, x, y, dr, count, n);
                input.setPopulationSize(input.getPopulationSize() + n[0]);

                for (int i = 0; i < count[0]; i++) {
                    Entry e2 = map.get((int) x[i]);
                    if (e2 == null) e2 = new Entry();
                    map.put((int) x[i], e2);
                    e2.x = x[i];
                    e2.y += y[i];
                    e2.dr += dr[i];
                }
            }
            if (leur.size() > 0) {
                System.out.println("Entries not found for Europe:");
                for (String s : leur) {
                    System.out.println(" -> " + s);
                }
            }
            List<Entry> l = new ArrayList<>();
            for (int k : map.keySet()) {
                l.add(map.get(k));
            }
            l.sort(Comparator.comparingDouble(o -> o.x));
            x = new double[l.size()];
            y = new double[l.size()];
            dr = new double[l.size()];

            map.clear();


            for (int i = 0; i < l.size(); i++) {
                Entry e = l.get(i);
                x[i] = e.x;
                y[i] = e.y;
                dr[i] = e.dr;
            }

            //make it monotone again
            int i = 0;
            int m = 0;
            while (i < y.length - m - 1) {
                if (y[i + 1] <= y[i]) {
                    int o = 1;
                    while (y[i + o + 1] <= y[i]) o++;
                    for (int j = i + 1; j + o < y.length - m; j++) {
                        x[j] = x[j + o];
                        y[j] = y[j + o];
                        dr[j] = dr[j + o];
                    }
                    m += o;
                } else {
                    i++;
                }
            }
            x = Arrays.copyOf(x, x.length - m);
            y = Arrays.copyOf(y, y.length - m);
            dr = Arrays.copyOf(dr, dr.length - m);
//*/

            l.clear();

        } else {
            lst = data.get(key);
            if (lst == null) return input;
            x = new double[lst.size()];
            y = new double[lst.size()];
            dr = new double[lst.size()];

            double[] n = new double[1];
            int[] count = new int[1];
            convert(lst, x, y, dr, count, n);

            input.setPopulationSize(n[0]);

            x = Arrays.copyOf(x, count[0]);
            y = Arrays.copyOf(y, count[0]);
            dr = Arrays.copyOf(dr, count[0]);
        }

        double[][] var = new double[3][x.length];
/*
        double dx = 1.;
        double[] x1 = new double[x.length * 2];
        double[] y1 = new double[x.length * 2];
        double[] dr1 = new double[x.length * 2];
        int i = 0;
        int k = 0;
        while (i < x.length) {
            x1[k] = x[i];
            y1[k] = y[i];
            dr1[k] = dr[i];
            if (i == x.length - 1) break;
            while (x[i + 1] > x1[k]) {
                k++;
                x1[k] = x1[k - 1] + dx;
            }
            i++;
        }
        for (i = k+1; i<k+15; i++) {
            y1[i] = y1[i-1]+(y1[k]-y1[k-1]); //simply extrapolate
        }
        k+=1;
        y1 = eval(x1, y1, 1., 7., 1., 7);
        var[0] = Arrays.copyOf(x1, k);
        var[1] = Arrays.copyOf(y1, k);
        var[2] = Arrays.copyOf(dr1, k);
*/
        var[0] = x;
        var[1] = y;
        var[2] = dr;
        input.setData(var);
        save(input.getName(), x, y);
        return input;
    }

    private void save(String name, double[] x, double[] y) throws IOException {
        File f = new File(name.replace(" ", "_") + ".dat");
        PrintStream pw = new PrintStream(f);
        for (int i = 0; i < x.length; i++) {
            pw.printf("%f %f\n", x[i], y[i]);
        }
    }

    public static void main(String[] args) {
        try {
            Input input = new CSVInput().inputFor("Italy");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
