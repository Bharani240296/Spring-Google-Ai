package com.springai.BootAi.Contr;

import com.springai.BootAi.Service.AITextService;
import com.springai.BootAi.Service.AiJsonSerive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AiContro {


    @Autowired
    AiJsonSerive aiJsonSerive;

    @Autowired
    AITextService aiTextService;

    @GetMapping("/apiai")
    public String getQueryResult(@RequestParam String query){
        return aiJsonSerive.queryJson(query).get(0).getFormattedContent().toString();
    }
    @GetMapping("/api/text")
    public String getQueryTextResult(@RequestParam String query){
        return aiTextService.queryJson(query).get(0).getFormattedContent().toString();
    }
}
