package com.kevinrlewis.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.kevinrlewis.util.ResponseUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Consumer;

@Log4j2
@RestController
@RequestMapping("/api")
public class JobtrakrController {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public JobtrakrController(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping({"/{collection}"})
    public ResponseEntity<List<Document>> retrieveDocuments(@PathVariable(name="collection") String collection,
                                                            @RequestParam Map<String,String> requestParams) {
        log.info("retrieveDocuments: collection={}, parameters={}", collection, requestParams);
        List<Document> documents = new ArrayList<>();
        get(collection, requestParams).forEach((Consumer<Document>) documents::add);
        return ResponseUtil.wrapOrNotFound(Optional.of(documents));
    }

    @GetMapping({"/{collection}/single"})
    public ResponseEntity<Document> retrieveDocument(@PathVariable(name="collection") String collection,
                                                     @RequestParam Map<String,String> requestParams) {
        log.info("retrieveDocument: collection={}, parameters={}", collection, requestParams);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(get( collection, requestParams).first()));
    }

    @PutMapping({"/{collection}/single"})
    public UpdateResult saveDocument(@RequestHeader(name="appName") String appName,
                                     @PathVariable(name="collection") String collection,
                                     @RequestBody JsonNode payload) {
        log.info("saveDocuments: collection={}, payload={}", collection, payload);

        // Get collection
        MongoCollection<Document> mongoCollection = mongoTemplate.getCollection(collection);

        DBObject upsert = wrapUpsert(payload, appName);

        return mongoCollection.updateMany(
                Document.parse(payload.get("key").toString()),
                Document.parse(upsert.toString()),
                new UpdateOptions().upsert( true )
        );
    }

    public FindIterable<Document> get(String collection, Map<String,String> requestParams) {
        // Get collection
        MongoCollection<Document> mongoCollection = mongoTemplate.getCollection(collection);

        // Create query
        BasicDBObject searchQuery = new BasicDBObject();
        int limit = 0;
        for(String key : requestParams.keySet()){
            if(!key.equals("limit")) {
                if (BooleanUtils.toBoolean(String.valueOf(requestParams.get(key)))) {
                    searchQuery.put(key, Boolean.parseBoolean(requestParams.get(key)));
                } else if (NumberUtils.isParsable(String.valueOf(requestParams.get(key)))) {
                    searchQuery.put(key, Long.parseLong(requestParams.get(key)));
                } else {
                    searchQuery.put(key, requestParams.get(key));
                }
            } else {
                limit = Integer.parseInt(requestParams.get(key));
            }
        }

        // Return result(s)
        if(limit != 0) {
            return mongoCollection.find(searchQuery).projection(Projections.excludeId()).limit(limit);
        } else {
            return mongoCollection.find(searchQuery).projection(Projections.excludeId());
        }
    }

    public DBObject wrapUpsert(JsonNode payload, String updatedBy) {
        DBObject upsert = new BasicDBObject();
        DBObject insert = new BasicDBObject();
        DBObject value = BasicDBObject.parse(payload.get("value").toString());

        value.put("updatedBy", updatedBy);
        value.put("updateTimestamp", new Date());
        // This field should never be updated and creates conflicts when inserting a new record
        value.removeField("uuid");

        insert.put("uuid", UUID.randomUUID().toString());
        insert.put("createdBy", updatedBy);
        insert.put("createTimestamp", new Date());

        upsert.put("$set", value);
        upsert.put("$setOnInsert", insert);

        return upsert;
    }
}
