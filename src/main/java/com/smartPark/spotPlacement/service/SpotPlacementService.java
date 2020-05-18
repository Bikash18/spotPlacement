package com.smartPark.spotPlacement.service;

import com.mongodb.*;

import java.util.*;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.Arrays;
import java.util.List;
import com.smartPark.spotPlacement.model.Map;
import com.smartPark.spotPlacement.util.SpotPlacementUtil;
import org.bson.Document;
import com.smartPark.spotPlacement.model.*;
import com.smartPark.spotPlacement.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.smartPark.spotPlacement.model.HistoryOfSpotRecords;

@Service
public class SpotPlacementService {

    @Autowired
    private MapRepository mapRepo;

    @Autowired
    private AreaRepository areaRepo;

    @Autowired
    private SpotAvailabilityRepository spotAvailRepo;

    @Autowired
    private CamToGlobalIdRepository camToGlobalIdRepo;

    @Autowired
    private CamStatusRepository camStatusRepo;

    @Autowired
    private HistoryOfSpotRecordsRepository historyOfSpotRecordsRepository;

    @Autowired
    private CamerasRepository camerasRepo;

    @Autowired
    private NotificationRepository notificationRepo;

    @Value("${spring.data.mongodb.url}")
    private String url;

    private static final Logger log = LoggerFactory.getLogger(SpotPlacementService.class);


    public List<SpotAvailability> getSpotStatus() {
        return spotAvailRepo.findAll();
    }

    public List<SpotAvailability> updateSpotStatus(ArrayList<SpotUpdateRequest> spotUpdateStatusBody) {

        List<SpotAvailability> spotAvailUpdatedList = new ArrayList<>();
        for (SpotUpdateRequest SpotsStatusObj : spotUpdateStatusBody) {
            SpotAvailability spotAvailUpdatedObj = this.updateSpots(SpotsStatusObj);
            spotAvailUpdatedList.add(spotAvailUpdatedObj);
        }

        // Fetching AREA VS spot availability records
        Object[] areaVsSpotAvailabilityRecords = null;
        areaVsSpotAvailabilityRecords = this.getAreaVsSpotAvailability();

        java.util.Map<String, Integer> areaAvailabilityPercentageWiseRecords = null;

        areaAvailabilityPercentageWiseRecords = this.areaAvailabilityPercentage(areaVsSpotAvailabilityRecords);
        log.info("areaAvailabilityPercentageWiseRecords:" + areaAvailabilityPercentageWiseRecords);

        addNotification(areaAvailabilityPercentageWiseRecords);
        return spotAvailUpdatedList;
    }

    public SpotAvailability updateSpots(SpotUpdateRequest spotUpdateObj) {
        int count = 0;
        String cameraId = spotUpdateObj.getCamId();
        ArrayList<SpotUpdateStatusRequest> spaceUpdates = spotUpdateObj.getSpaceUpdates();

        SpotAvailability res = null;
        int spaceUpdatesLength = spaceUpdates.size();
        // Loop through all the space updates
        for (int i = 0; i < spaceUpdatesLength; i++) {
            SpotUpdateStatusRequest spotStatusObj = spaceUpdates.get(i);
            String cameraSpotId = spotStatusObj.getId();
            String spotStatusString = spotStatusObj.getStatus();
            String camSpotId = cameraId + cameraSpotId;
            long currentUnixTime = System.currentTimeMillis() / 1000L;
            // TODO : Spot detail is used in updating the spot avaiability record
            Map spotDetail = this.fetchSpotDetailsByGlobalId(camSpotId);

            int globalId = spotDetail.getId();
            // TODO : Check if spot availability record is present or not in collection with particular global id

            SpotAvailability spotRecord = this.fetchSpotAvailabilityRecord(globalId);
            if (spotRecord != null) {
                // TODO : Update spot record
                ArrayList<SpotStatus> camArr = spotRecord.getCam_reports();
                for (int arrSize = 0; arrSize < camArr.size(); arrSize++) {
                    String camId = camArr.get(arrSize).getId();
                    //  CamStatus camStatusRecord = this.fetchCamStatus(camId);
                    //  Cameras cameraObj = this.fetchCamerasRecord(camId);
                    res = this.updateSpotAvailability(globalId, spotStatusString);
                }
            } else {
                // TODO : Create a spot record
                SpotStatus spotStatusObject = new SpotStatus(cameraSpotId, spotStatusString);
                ArrayList<SpotStatus> spotStatusList = new ArrayList<>();
                spotStatusList.add(spotStatusObject);
                SpotAvailability newSpotAvailObj = new SpotAvailability(globalId, spotStatusString, spotStatusList, currentUnixTime);
                res = spotAvailRepo.save(newSpotAvailObj);

            }
            // TODO : Create a HistoryOfSpotRecords
            HistoryOfSpotRecords historyOfSpotRecords = new HistoryOfSpotRecords(globalId, spotStatusString, currentUnixTime);
            historyOfSpotRecordsRepository.save(historyOfSpotRecords);


        }
        //System.out.println(res);
        return res;
    }

    public SpotAvailability updateSpotAvailability(int globalId, String spotStatus) {

        List<SpotAvailability> spotAvailList = spotAvailRepo.findAll();

        SpotAvailability spotAvailObj = null;
        for (int i = 0; i < spotAvailList.size(); i++) {
            int spotAvailId = spotAvailList.get(i).getId();
            if (globalId == spotAvailId) {
                spotAvailObj = spotAvailList.get(i);
                break;
            }
        }

        assert spotAvailObj != null;
        spotAvailObj.setDate(System.currentTimeMillis() / 1000L);
        spotAvailObj.setStatus(spotStatus);
        spotAvailObj = spotAvailRepo.save(spotAvailObj);
        return spotAvailObj;
    }

    public CamStatus fetchCamStatus(String camId) {
        List<CamStatus> camStatusList = camStatusRepo.findAll();
        CamStatus finalCamStatus = null;
        for (int i = 0; i < camStatusList.size(); i++) {
            String camStatusId = camStatusList.get(i).getId();
            if (camStatusId == camId) {
                finalCamStatus = camStatusList.get(i);
                break;
            }
        }
        return finalCamStatus;
    }

//    public Cameras fetchCamerasRecord(String camId){
//        List<Cameras> camerasList =  camerasRepo.findAll();
//        Cameras finalCamerasRec = null;
//        for(int i=0;i < camerasList.size(); i++){
//            String cameraId = camerasList.get(i).getId();
//            if(cameraId.equals(camId)){
//                finalCamerasRec = camerasList.get(i);
//                break;
//            }
//        }
//        return finalCamerasRec;
//    }

    public SpotAvailability fetchSpotAvailabilityRecord(int globalId) {

        List<SpotAvailability> spotAvailList = spotAvailRepo.findAll();
        SpotAvailability finalSpotAvailRecord = null;
        for (int i = 0; i < spotAvailList.size(); i++) {
            int spotAvailId = spotAvailList.get(i).getId();
            if (spotAvailId == globalId) {
                finalSpotAvailRecord = spotAvailList.get(i);
            }
        }
        return finalSpotAvailRecord;
    }

    public Map fetchSpotDetailsByGlobalId(String camSpotId) {
        // Get global id with
        List<CamToGlobalId> camToGlobalIdList = camToGlobalIdRepo.findAll();

        CamToGlobalId CamToGlobalIdRec = null;
        int globalId = 0;

        for (int i = 0; i < camToGlobalIdList.size(); i++) {
            String currentId = camToGlobalIdList.get(i).getId();
            if (currentId.equals(camSpotId)) {
                globalId = camToGlobalIdList.get(i).getGlobalId();
                break;
            }
        }

        Map finalMapObj = null;
        if (globalId > 0) {
            List<Map> mapList = mapRepo.findAll();
            for (int j = 0; j < mapList.size(); j++) {
                Map mapObj = mapList.get(j);
                int mapId = mapObj.getId();
                if (mapId == globalId) {

                    finalMapObj = mapObj;
                    break;
                }
            }
        }
        return finalMapObj;
    }

    public List<Map> getSpotDetails() {
        List<Map> mapList = mapRepo.findAll();
        return mapList;
    }

    public List<Area> getAreaDetails() {
        return areaRepo.findAll();
    }

    // TODO : (Bikash Need to update )
    public Object[] getHistoryOfSpotRecords(int startDateTime, int endDateTime, int intervalTimeInHour) {
        List<Object> listHistoryOfRecords = new ArrayList<>();
        HashMap<String, Object> mapHistoryOfRecords = new HashMap<>();
        MongoClientURI clientURI = new MongoClientURI(url);

        try (MongoClient mongoClient = new MongoClient(clientURI)) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase("aayushDB");
            MongoCollection<Document> collection = mongoDatabase.getCollection("history_of_spot_records");
            //conversion of Interval hour to second
            int timeInSeconds = intervalTimeInHour * 3600;
            for (int i = startDateTime; i < endDateTime; i = i + timeInSeconds) {
                int countOpen = 0;
                int countClosed = 0;
                int countTotal = 0;
                String dateTime = null;
                String time12HourFormat = null;
                Double doubleStartDateHourTime = null;
                Double doubleEndDateHourTime = null;
                doubleStartDateHourTime = Math.pow(i, 1);
                doubleEndDateHourTime = Math.pow(i + timeInSeconds, 1);
                final List<Object> result = new ArrayList<>();
                //fetching and storing mongo db query output
                List<Document> pipeline = SpotPlacementUtil.getSpotRecordsWithinTimePeriod(doubleStartDateHourTime,
                        doubleEndDateHourTime);
                //Iterating query output
                for (Document document : collection.aggregate(pipeline)) {
                    if (document.containsValue("open")) {
                        countOpen++;
                    }
                    if (document.containsValue("closed")) {
                        countClosed++;
                    }
                }
                pipeline = null;
                countTotal = countOpen + countClosed;
                mapHistoryOfRecords.put("No_Of_Open_Spot", countOpen);
                mapHistoryOfRecords.put("No_Of_Closed_Spot", countClosed);
                mapHistoryOfRecords.put("Total_No_Of_Spot", countTotal);
                dateTime = SpotPlacementUtil.convertUnixTimeToSimpleDateFormat(i);
                time12HourFormat = dateTime.substring(10);
                mapHistoryOfRecords.put("Time", time12HourFormat);
                mapHistoryOfRecords.forEach((key, value) -> {
                    result.add(key + ":" + value);
                });
                listHistoryOfRecords.add(result);
            }
        } catch (MongoException e) {
            e.printStackTrace();
        }
        mapHistoryOfRecords = null;
        return listHistoryOfRecords.toArray();
    }

    public Object[] getAreaVsSpotAvailability() {

        List<Object> result = new ArrayList<>();
        java.util.Map<Object, Integer> openStatus = null;
        java.util.Map<Object, Integer> closedStatus = null;

        int totalOpenCount = 0;
        int totalClosedCount = 0;
        int totalSpotCount = 0;
        int closedNoSpot = 0;
        int openNoSpot = 0;

        String open = "open";
        String closed = "closed";

        //fetching and storing mongo db query output
        List<Document> pipelineOpen = SpotPlacementUtil.getMatchStatus(open);
        System.out.println("pipelineOpen:" + pipelineOpen);
        List<Document> pipelineClosed = SpotPlacementUtil.getMatchStatus(closed);
        System.out.println("pipelineClosed:" + pipelineClosed);
        List<String> alOpen = SpotPlacementUtil.getAreaForStatus(pipelineOpen);
        System.out.println();
        List<String> alClosed = SpotPlacementUtil.getAreaForStatus(pipelineClosed);
        System.out.println("alClosed:" + alClosed);
        pipelineOpen = null;
        pipelineClosed = null;
        List<Object> list = new ArrayList<>();
        totalOpenCount = alOpen.size();
        openStatus = SpotPlacementUtil.getAreaStatusCount(alOpen);
        System.out.println("openStatus:" + openStatus);
        alOpen = null;
        totalClosedCount = alClosed.size();
        closedStatus = SpotPlacementUtil.getAreaStatusCount(alClosed);
        System.out.println("closedStatus:" + closedStatus);
        alClosed = null;
        totalSpotCount = totalOpenCount + totalClosedCount;
        result.add("TotalOpenSpot:" + totalOpenCount + "," + "TotalClosedSpot:" + totalClosedCount + "," + "TotalSpot:" + totalSpotCount);
        java.util.Map<Object, Integer> mergedStatus = new HashMap<>(openStatus);
        mergedStatus.putAll(closedStatus);
        for (Object area : mergedStatus.keySet()) {
            if (openStatus.get(area) == null) {
                openNoSpot = 0;
            } else {
                openNoSpot = openStatus.get(area);
            }
            if (closedStatus.get(area) == null) {
                closedNoSpot = 0;
            } else {
                closedNoSpot = closedStatus.get(area);
            }
            int totalNoSpot = openNoSpot + closedNoSpot;
            list.add(area + "{" + "open:" + openNoSpot + "," + "closed:" + closedNoSpot + "," + "Total:" + totalNoSpot + "}");
        }

        openStatus = null;
        closedStatus = null;
        mergedStatus = null;

        result.add("AreaVsSpotAvailability" + list);

        return result.toArray();
    }

    public java.util.Map<String, Integer> areaAvailabilityPercentage(Object[] array) {
        if (array.length == 0) {
            return null;
        }
        String stringObject = array[1].toString();
        stringObject = SpotPlacementUtil.after(stringObject, "[");
        String[] arr = stringObject.split(", ");
        java.util.Map<String, Integer> mapAreaPercentage = new HashMap<>();
        for (String ss : arr) {
            int closedCount;
            int totalCount;
            float floatAreaPercentage;
            int areaPercentage;
            String area;
            try {
                closedCount = Integer.parseInt(SpotPlacementUtil.between(ss, "closed:", ","));
                totalCount = Integer.parseInt(SpotPlacementUtil.between(ss, "Total:", "}"));
                area = SpotPlacementUtil.before(ss, "{");
                floatAreaPercentage = (float) (closedCount * 100.0f) / totalCount;
                areaPercentage = (int) Math.round(floatAreaPercentage);
                mapAreaPercentage.put(area, areaPercentage);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return mapAreaPercentage;
    }

    public java.util.Map<String, Integer> TotalAreaAvailabilityPercentage(Object[] array) {
        if (array.length == 0) {
            return null;
        }
        String stringObject = array[0].toString();
        java.util.Map<String, Integer> mapAreaPercentage = new HashMap<>();
        int closedCount;
        int totalCount;
        float floatAreaPercentage;
        int areaPercentage;
        try {
            closedCount = Integer.parseInt(SpotPlacementUtil.between(stringObject, "TotalClosedSpot:", ","));
            totalCount = Integer.parseInt(SpotPlacementUtil.after(stringObject, "TotalSpot:").trim());
            floatAreaPercentage = (float) (closedCount * 100.0f) / totalCount;
            areaPercentage = (int) Math.round(floatAreaPercentage);
            mapAreaPercentage.put("Total_Area", areaPercentage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapAreaPercentage;
    }

    public void addNotification(java.util.Map<String, Integer> areaPercentageBody) {
        long currentUnixTime = System.currentTimeMillis() / 1000L;
        Notification notificationObjectForFull = null;
        int sum = 0;
        for (int i : areaPercentageBody.values()) {
            sum += i;
        }
        if (sum == 100 * areaPercentageBody.size()) {
            // TODO : Update logic for update with same 100 % status coverage
            notificationObjectForFull = new Notification("Parking lot" + " " + "is" + " " + "100" + "%" + " " + "full", "Area", "All area", 1, "Parking lot" + " " + "is" + " " + "100" + "%" + " " + "full", currentUnixTime);
            // List<Notification> notificationTitleList = null;
            // List<Notification> notificationSubTypeList = null;
            //String tempTitle = "Parking lot" + " "+ "is" + " " + "100"+ "%" + " " + "full";
            //String tempNotificationSubType = "All area";
            // notificationTitleList = notificationRepo.findByTitle(tempTitle);
            //notificationSubTypeList = notificationRepo.findByNotificationSubType(tempNotificationSubType);
            //if( notificationTitleList.isEmpty())
            notificationRepo.save(notificationObjectForFull);
            Notification notificationObjectA = new Notification("Area" + " " + "A1" + " " + "is" + " " + "100" + "%" + " " + "full", "Area", "" + "A1", 6, "Area" + " " + "A1" + " " + "is" + " " + "100" + "%" + " " + "full", currentUnixTime);
            notificationRepo.save(notificationObjectA);


            // Area B1
            Notification notificationObjectB = new Notification("Area" + " " + "B1" + " " + "is" + " " + "100" + "%" + " " + "full", "Area", "" + "B1", 6, "Area" + " " + "B1" + " " + "is" + " " + "100" + "%" + " " + "full", currentUnixTime);
            notificationRepo.save(notificationObjectB);

            // Area C1
            Notification notificationObjectC = new Notification("Area" + " " + "C1" + " " + "is" + " " + "100" + "%" + " " + "full", "Area", "" + "C1", 6, "Area" + " " + "C1" + " " + "is" + " " + "100" + "%" + " " + "full", currentUnixTime);
            notificationRepo.save(notificationObjectC);

            // Area D1
            Notification notificationObjectD = new Notification("Area" + " " + "D1" + " " + "is" + " " + "100" + "%" + " " + "full", "Area", "" + "D1", 6, "Area" + " " + "D1" + " " + "is" + " " + "100" + "%" + " " + "full", currentUnixTime);
            notificationRepo.save(notificationObjectD);

            // Area E1
            Notification notificationObjectE = new Notification("Area" + " " + "E1" + " " + "is" + " " + "100" + "%" + " " + "full", "Area", "" + "E1", 6, "Area" + " " + "E1" + " " + "is" + " " + "100" + "%" + " " + "full", currentUnixTime);
            notificationRepo.save(notificationObjectE);


            // Area F1
            Notification notificationObjectF = new Notification("Area" + " " + "F1" + " " + "is" + " " + "100" + "%" + " " + "full", "Area", "" + "F1", 6, "Area" + " " + "F1" + " " + "is" + " " + "100" + "%" + " " + "full", currentUnixTime);
            notificationRepo.save(notificationObjectF);

            // Area G1
            Notification notificationObjectG = new Notification("Area" + " " + "G1" + " " + "is" + " " + "100" + "%" + " " + "full", "Area", "" + "G1", 6, "Area" + " " + "G1" + " " + "is" + " " + "100" + "%" + " " + "full", currentUnixTime);
            notificationRepo.save(notificationObjectG);

            // Area H1
            Notification notificationObjectH = new Notification("Area" + " " + "H1" + " " + "is" + " " + "100" + "%" + " " + "full", "Area", "" + "H1", 6, "Area" + " " + "H1" + " " + "is" + " " + "100" + "%" + " " + "full", currentUnixTime);
            notificationRepo.save(notificationObjectH);

            //else if( !tempTitle.equals(notificationSubTypeList.get(notificationSubTypeList.size()-1).getTitle()))
            //  notificationRepo.save(notificationObjectForFull);
        } else {
            Iterator keySetIterator = areaPercentageBody.keySet().iterator();
            while (keySetIterator.hasNext()) {
                Notification notificationObject = null;
                List<Notification> notificationTitleList = null;
                List<Notification> notificationSubTypeList = null;
                String key = (String) keySetIterator.next();
                int percentage = areaPercentageBody.get(key);
                String tempTitle = "Area" + " " + key + " " + "is" + " " + percentage + "%" + " " + "full";
                String tempNotificationSubType = "" + key;
                notificationTitleList = notificationRepo.findByTitle(tempTitle);
                notificationSubTypeList = notificationRepo.findByNotificationSubType(tempNotificationSubType);
                try {
                    if (percentage >= 90 && percentage <= 100) {
                        notificationObject = new Notification("Area" + " " + key + " " + "is" + " " + percentage + "%" + " " + "full", "Area", "" + key, 2, "Area" + " " + key + " " + "is" + " " + percentage + "%" + " " + "full", currentUnixTime);
                        if (notificationTitleList.isEmpty())
                            notificationRepo.save(notificationObject);
                        else if (!tempTitle.equals(notificationSubTypeList.get(notificationSubTypeList.size() - 1).getTitle()))
                            notificationRepo.save(notificationObject);
                    } else if (percentage >= 80 && percentage < 90) {
                        notificationObject = new Notification("Area" + " " + key + " " + "is" + " " + percentage + "%" + " " + "full", "Area", "" + key, 3, "Area" + " " + key + " " + "is" + " " + percentage + "%" + " " + "full", currentUnixTime);
                        if (notificationTitleList.isEmpty())
                            notificationRepo.save(notificationObject);
                        else if (!tempTitle.equals(notificationSubTypeList.get(notificationSubTypeList.size() - 1).getTitle()))
                            notificationRepo.save(notificationObject);
                    } else if (percentage >= 70 && percentage < 80) {
                        notificationObject = new Notification("Area" + " " + key + " " + "is" + " " + percentage + "%" + " " + "full", "Area", "" + key, 4, "Area" + " " + key + " " + "is" + " " + percentage + "%" + " " + "full", currentUnixTime);
                        if (notificationTitleList.isEmpty())
                            notificationRepo.save(notificationObject);
                        else if (!tempTitle.equals(notificationSubTypeList.get(notificationSubTypeList.size() - 1).getTitle()))
                            notificationRepo.save(notificationObject);
                    } else if (percentage >= 60 && percentage < 70) {
                        notificationObject = new Notification("Area" + " " + key + " " + "is" + " " + percentage + "%" + " " + "full", "Area", "" + key, 4, "Area" + " " + key + " " + "is" + " " + percentage + "%" + " " + "full", currentUnixTime);
                        if (notificationTitleList.isEmpty())
                            notificationRepo.save(notificationObject);
                        else if (!tempTitle.equals(notificationSubTypeList.get(notificationSubTypeList.size() - 1).getTitle()))
                            notificationRepo.save(notificationObject);
                    } else if (percentage >= 30 && percentage < 60) {
                        notificationObject = new Notification("Area" + " " + key + " " + "is" + " " + percentage + "%" + " " + "full", "Area", "" + key, 5, "Area" + " " + key + " " + "is" + " " + percentage + "%" + " " + "full", currentUnixTime);
                        if (notificationTitleList.isEmpty())
                            notificationRepo.save(notificationObject);
                        else if (!tempTitle.equals(notificationSubTypeList.get(notificationSubTypeList.size() - 1).getTitle()))
                            notificationRepo.save(notificationObject);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
