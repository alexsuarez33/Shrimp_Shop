package com.shrimpshop.service;

import com.shrimpshop.model.Product;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;

@Service
public class DynamoProductService {

    private final String tableName = "ProductTable";
    private final DynamoDbClient dynamoDb = DynamoDbClient.builder()
        .region(Region.US_EAST_2)
        .build();

    public void saveProduct(Product product) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.fromS(product.getId()));
        item.put("type", AttributeValue.fromS(product.getType()));
        item.put("name", AttributeValue.fromS(product.getName()));
        item.put("price", AttributeValue.fromN(String.valueOf(product.getPrice())));

        //Corrected: add logic for optional attributes map
        if (product.getAttributes() != null) {
            Map<String, AttributeValue> optionalMap = new HashMap<>();
            for (Map.Entry<String, String> entry : product.getAttributes().entrySet()) {
                optionalMap.put(entry.getKey(), AttributeValue.fromS(entry.getValue()));
            }
            item.put("attributes", AttributeValue.fromM(optionalMap));
        }

        if (product.getImageUrls() != null) {
            List<AttributeValue> images = product.getImageUrls().stream()
                .map(AttributeValue::fromS)
                .toList();
            
            item.put("imageUrls", AttributeValue.fromL(images));
        }

        PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(item)
                .build();

        dynamoDb.putItem(request);
    }

    public List<Product> getAllProducts() {
        ScanRequest scanRequest = ScanRequest.builder()
                .tableName(tableName)
                .build();

        ScanResponse scanResponse = dynamoDb.scan(scanRequest);

        List<Product> products = new ArrayList<>();
        for (Map<String, AttributeValue> item : scanResponse.items()) {
            Product p = new Product();
            p.setId(item.get("id").s());
            p.setType(item.get("type").s());
            p.setName(item.get("name").s());
            p.setPrice(Double.parseDouble(item.get("price").n()));

            //Corrected: Added logic for returning attributes list
            if (item.containsKey("attributes")) {
                Map<String, AttributeValue> rawMap = item.get("attributes").m();
                Map<String, String> optional = new HashMap<>();
                for (Map.Entry<String, AttributeValue> entry : rawMap.entrySet()) {
                    optional.put(entry.getKey(), entry.getValue().s());
                }
                p.setAttributes(optional);
}

            
            if (item.containsKey("imageUrls")) {
                List<AttributeValue> imageList = item.get("imageUrls").l();
                List<String> urls = imageList.stream()
                    .map(AttributeValue::s)
                    .toList();
                p.setImageUrls(urls);
            }

            products.add(p);
        }
        return products;
    }

    public int getNextProductId() {
        String counterTable = "Counters";

        Map<String, AttributeValue> key = Map.of(
            "name", AttributeValue.fromS("product_id")
        );

        UpdateItemRequest updateRequest = UpdateItemRequest.builder()
            .tableName(counterTable)
            .key(key)
            .updateExpression("SET currentValue = if_not_exists(currentValue, :start) + :inc")
            .expressionAttributeValues(Map.of(
                ":start", AttributeValue.fromN("0"),
                ":inc", AttributeValue.fromN("1")
            ))
            .returnValues(ReturnValue.UPDATED_NEW)
            .build();

        Map<String, AttributeValue> updatedAttributes =
            dynamoDb.updateItem(updateRequest).attributes();

        return Integer.parseInt(updatedAttributes.get("currentValue").n());
    }

    public void updateProductPrice(String id, double newPrice) {
        Map<String, AttributeValue> key = Map.of("id", AttributeValue.fromS(id));
    
        UpdateItemRequest request = UpdateItemRequest.builder()
            .tableName(tableName)
            .key(key)
            .updateExpression("SET price = :p")
            .expressionAttributeValues(Map.of(":p", AttributeValue.fromN(String.valueOf(newPrice))))
            .build();
    
        dynamoDb.updateItem(request);
    }
    

}