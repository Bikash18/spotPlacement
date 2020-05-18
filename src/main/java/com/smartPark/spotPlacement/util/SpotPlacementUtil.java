package com.smartPark.spotPlacement.util;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;

import java.text.SimpleDateFormat;
import java.util.*;

public class SpotPlacementUtil {

    @Value("${spring.data.mongodb.url}")
    private static String url;

    // convert unix timestamp to Date timestamp 12 hour format
    public static String convertUnixTimeToSimpleDateFormat(int unixTimeStamp) {
        // convert seconds to milliseconds
        Date date = new java.util.Date(unixTimeStamp * 1000L);
        // the format of your date
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        // give a timezone reference for formatting
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT"));
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    public static String between(String value, String a, String b) {
        // Return a substring between the two strings.
        int posA = value.indexOf(a);
        if (posA == -1) {
            return "";
        }
        int posB = value.lastIndexOf(b);
        if (posB == -1) {
            return "";
        }
        int adjustedPosA = posA + a.length();
        if (adjustedPosA >= posB) {
            return "";
        }
        return value.substring(adjustedPosA, posB);
    }

    public static String before(String value, String a) {
        // Return substring containing all characters before a string.
        int posA = value.indexOf(a);
        if (posA == -1) {
            return "";
        }
        return value.substring(0, posA);
    }

    public static String after(String value, String a) {
        // Returns a substring containing all characters after a string.
        int posA = value.lastIndexOf(a);
        if (posA == -1) {
            return "";
        }
        int adjustedPosA = posA + a.length();
        if (adjustedPosA >= value.length()) {
            return "";
        }
        return value.substring(adjustedPosA);
    }
    // Returns records as list of document in between StartDateTime and EndDateTime
    public static List<Document> getSpotRecordsWithinTimePeriod(double startDateTime, double endDateTime) {
        List<Document> pipeline = null;
        try {
            pipeline = Arrays.asList(
                    new Document()
                            .append("$match", new Document()
                                    .append("date",
                                            new Document()
                                                    .append("$gte", startDateTime)
                                                    .append("$lt", endDateTime)
                                    )
                            ),
                    new Document()
                            .append("$group", new Document()
                                    .append("_id", "$global_id")
                                    .append("status", new Document()
                                            .append("$last", "$status")
                                    )
                                    .append("date", new Document()
                                            .append("$last", "$date")
                                    )
                            ),
                    new Document()
                            .append("$sort", new Document()
                                    .append("date", -1.0)
                            )
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pipeline;
    }
    // Returns records as list of document based on status(open or closed) using map and spot_availability lookup operation
    public static List<Document> getMatchStatus(String status) {
        List<Document> pipeline = null;
        try {
            pipeline = Arrays.asList(
                    new Document()
                            .append("$match", new Document()
                                    .append("status", status)
                            ),
                    new Document()
                            .append("$lookup", new Document()
                                    .append("from", "map")
                                    .append("localField", "_id")
                                    .append("foreignField", "_id")
                                    .append("as", "map_spot_availabilty_lookup")
                            )
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pipeline;
    }
    // Returns  list of Area based on status(open or closed) from map and spot_availabilty look up operation
    public static List<String> getAreaForStatus(List<Document> pipeline) {

        List<String>    alStatus = null;
        MongoClientURI clientURI = new MongoClientURI(url);

        try(MongoClient mongoClient = new MongoClient(clientURI)) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase("aayushDB");
            MongoCollection<Document> collection = mongoDatabase.getCollection("spot_availability");
            for (Document document : collection.aggregate(pipeline)) {
                  List<Document> values = (List<Document>) document.get("map_spot_availabilty_lookup");
                      for(Document doc:values){
                          alStatus.add((String) doc.get("area"));
                      }
            }
        } catch (MongoException e) {
            e.printStackTrace();
        }
        return alStatus;
    }
    public static java.util.Map<Object, Integer> getAreaStatusCount(List<String> alStatus) {
        int count  = 0;
        java.util.Map<Object, Integer> status = new HashMap<>();

        for (String spot: alStatus) {
            if (status.containsKey(spot)) {
                count = status.get(spot);
                status.put(spot,count+1);
            } else {
                status.put(spot, 1);
            }
        }
        return status;
    }
}