package com.springai.BootAi.Service;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.JsonMetadataGenerator;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AiJsonSerive {

    @Autowired
    private VectorStore vectorStore;

    @Value("classpath:data/photos.json")
    private Resource photoRes;

    private boolean loaded = false;

    public List<Document> queryJson(String query) {

        if (!loaded) {

            JsonReader jsonReader = new JsonReader(
                    photoRes,new ProductMetaData(),
                    "albumId",
                    "id",
                    "title",
                    "url",
                    "thumbnailUrl"
            );

            List<Document> documents = jsonReader.get();

            vectorStore.add(documents);

            loaded = true;
        }

        return vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(query)
                        .topK(3)
                        .build()
        );
    }
    public class ProductMetaData implements JsonMetadataGenerator{

        @Override
        public Map<String, Object> generate(Map<String, Object> map) {
            return Map.of("url",map.get("url"),"albumId",map.get("albumId"));
        }
    }
}
