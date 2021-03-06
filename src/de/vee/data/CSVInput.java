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

    enum IDX {
        DATE(0), DAY(1), MONTH(2), YEAR(3), CASES(4), DEATHS(5), COUNTRIESANDTERRITORIES(6), GEOID(7), COUNTRYTERRITORYCODE(8), POPDATA(9), CONTINENTEXP(10);
        private final int id;

        IDX(int id) {
            this.id = id;
        }
    }

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
        long popData;

        CSVRecord(String date, String day, String month, String year, String cases, String deaths, String countriesAndTerritories, String geoId, String countryterritoryCode, String popData) {
            this.date = date;
            this.day = Integer.valueOf(day);
            this.month = Integer.valueOf(month);
            this.year = Integer.valueOf(year);
            this.cases = Long.valueOf(cases);
            this.deaths = Long.valueOf(deaths);
            this.countriesAndTerritories = countriesAndTerritories;
            this.geoId = geoId;
            this.countryterritoryCode = countryterritoryCode;
            this.popData = Long.valueOf(popData);
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
            System.out.println("Parsing data " + src.toString() + " ...");
            BufferedReader br = new BufferedReader(new FileReader(src.toFile()));
            String line = br.readLine(); //read Header
            List<String> header = new ArrayList<>();
            StringTokenizer st = new StringTokenizer(line, ", \t\n\r\f", false);
            while (st.hasMoreTokens()) {
                header.add(st.nextToken().toLowerCase());
            }
            int[] idx = new int[header.size()];
            for (int i = 0; i < header.size(); i++) {
                idx[i] = -1;
            }
            idx[0] = 0;
            IDX[] values = IDX.values();
            for (String s : header) {
                String key = s.toUpperCase().trim();
                try {
                    for (int j = 0; j < values.length; j++) {
                        IDX idx1 = (IDX) values[j];
                        String name = (idx1.name());
                        if (key.contains(name)) {
                            idx[idx1.id] = j;
                            break;
                        }
                    }
                    //          IDX v = IDX.valueOf(key);
                    //          idx[v.id] = i;
                } catch (IllegalArgumentException iae) {
                    iae.printStackTrace();
                }
            }

            int count = 0;
            while ((line = br.readLine()) != null) {
                st = new StringTokenizer(line, ", \t\n\r\f", false);
                if (st.countTokens() == 11) {
                    List<String> entry = new ArrayList<>();
                    while (st.hasMoreTokens()) {
                        entry.add(st.nextToken());
                    }
                    String dateRep = entry.get(idx[IDX.DATE.id]);
                    String day = entry.get(idx[IDX.DAY.id]);
                    String month = entry.get(idx[IDX.MONTH.id]);
                    String year = entry.get(idx[IDX.YEAR.id]);
                    String cases = entry.get(idx[IDX.CASES.id]);
                    String deaths = entry.get(idx[IDX.DEATHS.id]);
                    String geoId = entry.get(idx[IDX.GEOID.id]);
//                    String continentExp = entry.get(idx[IDX.CONTINENTEXP.id]);
                    String countryterritoryCode = entry.get(idx[IDX.COUNTRYTERRITORYCODE.id]);
                    String popData = entry.get(idx[IDX.POPDATA.id]);
                    String countriesAndTerritories = entry.get(idx[IDX.COUNTRIESANDTERRITORIES.id]);
                    CSVRecord record = new CSVRecord(dateRep, day, month, year, cases, deaths, countriesAndTerritories, geoId, countryterritoryCode, popData);
                    List<CSVRecord> lst = (data.computeIfAbsent(record.countriesAndTerritories, k -> new ArrayList<>()));
                    lst.add(record);
                    count++;
                } else {
                    System.out.println("Ignoring incomplete record: " + line);
                }
            }
            Set<String> keys = new HashSet<>(data.keySet());
            for (String key : keys) {
                List<CSVRecord> lst = data.get(key);
                lst.sort(Comparator.comparingLong(CSVRecord::daysSinceStart));
                long totalDeaths = 0;
                for (CSVRecord entry : lst) {
                    totalDeaths += entry.deaths;
                }
                if (totalDeaths < 1) {
                    data.remove(key);
                    System.out.println("Ignoring data for " + key + " (not enough records)");
                }
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
//        long cases = 0;
//        long deaths = 0;
        long oc = 0;
        for (CSVRecord record : lst) {
            if (N[0] != record.popData) N[0] = record.popData;
            long day = record.daysSinceStart();
            if ((day > 20) && (record.cases > 0)) {
                if (oc != record.cases) {
                    x[i] = day - 20;
                    y[i] = record.cases; //(cases += record.cases);
                    dr[i] = record.deaths; //(deaths += record.deaths);
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
            input = input.withDelta(new double[]{0.1, 0.2, 0.1});
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

/* //looks also weird
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
                    while ((y[i + o + 1] <= y[i]) && (i + o + 2 < y.length)) o++;
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
*/

            l.clear();
        } else if ("europe".compareToIgnoreCase(key) == 0) {
            input.withDelta(new double[]{1e-5, 0.5, 0.001});
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

/*
//do not care about
        if (key.toLowerCase().contains("china")) {
            System.out.println("Modifying data because the reporting strategy has changed in between ...");

            //create an artifical dataset - "correct" data for china
            int k = -1;
            for (int i = 1; i < x.length; i++) { //find the index
                if (y[i] - y[i - 1] > 12000) {
                    k = i;
                    break;
                }
            }
            double b1 = (y[k + 1] - y[k]) / (x[k + 1] - x[k]);
            double xx = y[k] + b1 * (x[k - 1] - x[k]);
            double yy = y[k - 1];
            double rd = (xx - yy) / yy;
            for (int i = 0; i < k; i++) {
                y[i] *= (1. + rd);
            }
        }
*/

        for (int i = 1; i < y.length; i++) {
            y[i] += y[i - 1];
            dr[i] += dr[i - 1];
        }
        var[0] = x;
        var[1] = y;
        var[2] = dr;
        input.setData(var);
//        save(input.getName(), x, y);
        return input;
    }

    private void save(String name, double[] x, double[] y) throws IOException {
        File f = new File(name.replace(" ", "_") + ".dat");
        PrintStream pw = new PrintStream(f);
        for (int i = 0; i < x.length; i++) {
            pw.printf("%f %f\n", x[i], y[i]);
        }
    }

    public List<String> getRegions() throws IOException {
        if (data.size() == 0) {
            parse();
        }
        List<String> regions = new ArrayList<>(data.keySet());
        regions.sort(String::compareToIgnoreCase);
        return regions;
    }

}
