package com.springai.BootAi.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AITextService {

    @Autowired
    private VectorStore vectorStore;

    @Value("classpath:wrapData/story.txt")
    private Resource photoRes;
private static  final Logger log= LoggerFactory.getLogger(AITextService.class);
    private boolean loaded = false;

    public List<Document> queryJson(String query) {

        if (!loaded) {

            TextReader textReader = new TextReader(photoRes);

            List<Document> documents = textReader.get();

            List<Document> finalDocuments = new ArrayList<>();
            for (Document document : documents) {
                log.info(documents.toString());

                String content = document.getText();

                String[] parts = content.split("(?i)Moral:");


                String story = parts[0].trim();

                if (!story.isEmpty()) {

                    finalDocuments.add(
                            new Document(
                                    story,
                                    Map.of("type", "story")
                            )
                    );
                }

                // Moral
                if (parts.length > 1) {

                    String moral = parts[1].trim();

                    if (!moral.isEmpty()) {

                        finalDocuments.add(
                                new Document(
                                        moral,
                                        Map.of("type", "moral")
                                )
                        );
                    }
                }
            }

            TokenTextSplitter splitter = TokenTextSplitter.builder()
                    .withChunkSize(500)
                    .withMinChunkSizeChars(350)
                    .withMinChunkLengthToEmbed(5)
                    .withMaxNumChunks(10000)
                    .build();

            vectorStore.add(
                    splitter.apply(finalDocuments)
            );

            loaded = true;
        }

        // =========================
        // MORAL SEARCH
        // =========================

        if (query.toLowerCase().contains("moral")) {

            return vectorStore.similaritySearch(
                    SearchRequest.builder()
                            .query("moral of the story")
                            .topK(1)
                            .filterExpression("type == 'moral'")
                            .build()
            );
        }

        // =========================
        // NORMAL WORD SEARCH
        // =========================

        return vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(query)
                        .topK(3)
                        .build()
        );
    }
}
