package com.zerobase.weather.service;

import com.zerobase.weather.WeatherApplication;
import com.zerobase.weather.domain.DateWeather;
import com.zerobase.weather.domain.Diary;
import com.zerobase.weather.error.InvalidDate;
import com.zerobase.weather.repository.DateWeatherRepository;
import com.zerobase.weather.repository.DiaryRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class DiaryService {
    @Value("${openweathermap.key}")
    private String apiKey;

    private final DiaryRepository diaryRepository;
    private final DateWeatherRepository dateWeatherRepository;
    private static final Logger logger = LoggerFactory.getLogger(WeatherApplication.class);

    public DiaryService(DiaryRepository diaryRepository, DateWeatherRepository dateWeatherRepository) {
        this.diaryRepository = diaryRepository;
        this.dateWeatherRepository = dateWeatherRepository;
    }

    @Transactional
    @Scheduled(cron = "0 0 1 * * *")
    public void saveWeatherDate(){
        dateWeatherRepository.save(getWeatherFromApi());
        logger.info("오늘 날씨 데이터 잘 가져옴");
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createDiary(LocalDate data, String text){
        logger.info("started to create diary");
        DateWeather dateWeather = getDateWeather(data);

        Diary nowDiary = new Diary();
        nowDiary.setDateWeather(dateWeather);
        nowDiary.setText(text);

        diaryRepository.save(nowDiary);
        logger.info("end to create diary");
        logger.error("don`t create diary");
    }

    private DateWeather getWeatherFromApi(){
        String weatherDate = getWeatherString();
        Map<String, Object> parsedWeather = parseWeather(weatherDate);

        DateWeather dateWeather = new DateWeather();

        dateWeather.setDate(LocalDate.now());
        dateWeather.setWeather(parsedWeather.get("main").toString());
        dateWeather.setIcon(parsedWeather.get("icon").toString());
        dateWeather.setTemperature((double) parsedWeather.get("temp"));

        return dateWeather;
    }

    private DateWeather getDateWeather(LocalDate date){
        List<DateWeather> dateWeatherListFromDB =
                dateWeatherRepository.findAllByDate(date);
        if(dateWeatherListFromDB.size() == 0){
            //현재 날씨를 가져옴
            return getWeatherFromApi();
        } else {
            return dateWeatherListFromDB.get(0);
        }
    }

    @Transactional(readOnly = true)
    public List<Diary> readDiary(LocalDate date){
        if(date.isAfter(LocalDate.ofYearDay(3050, 1))){
            throw new InvalidDate();
        }

        logger.info("read diary");
        return diaryRepository.findAllByDate(date);
    }

    public List<Diary> readDiaries(LocalDate startDate, LocalDate endDate){
        return diaryRepository.findAllByDateBetween(startDate, endDate);
    }

    public void updateDiary(LocalDate date, String text) {
        Diary nowDiary = diaryRepository.getFirstByDate(date);
        nowDiary.setText(text);
        diaryRepository.save(nowDiary);
    }

    public void deleteDiary(LocalDate date){
        diaryRepository.deleteAllByDate(date);
    }

    private String getWeatherString(){
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=seoul&appid=" + apiKey;

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            BufferedReader br;

            if(responseCode == 200){
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else{
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = br.readLine()) != null){
                response.append(inputLine);
            }
            br.close();

            return response.toString();
        } catch (Exception e) {
            return "failed to get response";
        }
    }

    private Map<String, Object> parseWeather(String jsonString){
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;

        try {
            jsonObject = (JSONObject) jsonParser.parse(jsonString);
        } catch (ParseException e){
            throw new RuntimeException(e);
        }

        Map<String, Object> resultMap = new HashMap<>();

        JSONObject mainData = (JSONObject) jsonObject.get("main");
        resultMap.put("temp", mainData.get("temp"));

        JSONArray weatherArray = (JSONArray) jsonObject.get("weather");
        JSONObject weatherDate = (JSONObject) weatherArray.get(0);
        resultMap.put("main", weatherDate.get("main"));
        resultMap.put("icon", weatherDate.get("icon"));

        return resultMap;
    }

}
