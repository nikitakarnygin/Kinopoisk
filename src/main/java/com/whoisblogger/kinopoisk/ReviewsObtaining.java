package com.whoisblogger.kinopoisk;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.whoisblogger.kinopoisk.RequestConfiguration.*;

public class ReviewsObtaining {

    private static final String GET_REVIEWS_URL = URL + "review";
    private static final String LIMIT_PER_PAGE = "250";

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException, ParseException {

        parseJsonToCsv(getReviews("326"));
    }

    public static File getReviews(String movieID) throws URISyntaxException, IOException, InterruptedException, ParseException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(GET_REVIEWS_URL + "?movieId=" + movieID + "&limit=" + LIMIT_PER_PAGE))
                .header("accept", "application/json")
                .header("X-API-KEY", TOKEN)
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Object object = new JSONParser().parse(response.body());
        JSONObject jsonObject = (JSONObject) object;
        JSONArray reviewsArr = (JSONArray) jsonObject.get("docs");
        String jsonString = String.valueOf(reviewsArr);

        Long pages = (Long) jsonObject.get("pages");
        if (pages > 1) {
            jsonString = jsonString.substring(0, jsonString.length() - 1);

            for (int i = 2; i <= pages; i++) {
                HttpRequest requestByPage = HttpRequest.newBuilder()
                        .uri(new URI(GET_REVIEWS_URL + "?movieId=" + movieID + "&limit=" + LIMIT_PER_PAGE
                                + "&page=" + i))
                        .header("accept", "application/json")
                        .header("X-API-KEY", TOKEN)
                        .build();

                HttpResponse<String> responseByPage = httpClient.send(requestByPage, HttpResponse.BodyHandlers.ofString());
                Object objectByPage = new JSONParser().parse(responseByPage.body());
                JSONObject jsonObjectByPage = (JSONObject) objectByPage;
                JSONArray reviewsArrByPage = (JSONArray) jsonObjectByPage.get("docs");

                jsonString += "," + String.valueOf(reviewsArrByPage).substring(1,
                        String.valueOf(reviewsArrByPage).length() - 1);
            }
            jsonString += "]";
        }

        File jsonFile = new File("jsonFile.json");
        try(FileWriter writer = new FileWriter(jsonFile, false))
        {
            writer.write(jsonString);
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }

        return jsonFile;
    }

    public static void parseJsonToCsv(File json) throws IOException {

        JsonNode jsonTree = new ObjectMapper().readTree(json);

        CsvSchema.Builder csvSchemaBuilder = CsvSchema.builder()
                .addColumn("title")
                .addColumn("type")
                .addColumn("review")
                .addColumn("date")
                .addColumn("author")
                .addColumn("userRating")
                .addColumn("reviewLikes")
                .addColumn("reviewDislikes");
        CsvSchema csvSchema = csvSchemaBuilder.build().withHeader();

        CsvMapper csvMapper = new CsvMapper();
        csvMapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
        csvMapper.writerFor(JsonNode.class)
                .with(csvSchema)
                .writeValue(new File("reviews.csv"), jsonTree);

//        CsvMapper csvMapper = new CsvMapper();
//        CsvSchema csvSchema = csvMapper
//                .schemaFor(ReviewsForCsv.class)
//                .withHeader();
//
//        csvMapper.addMixIn(Review.class, ReviewsForCsv.class);
//
//        Review[] reviews = new ObjectMapper()
//                .readValue(json, Review[].class);
//
//        csvMapper.writerFor(Review[].class)
//                .with(csvSchema)
//                .writeValue(new File("reviews.csv"), reviews);
    }
}
