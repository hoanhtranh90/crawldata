package com.example.crawldata.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping("/api/{id}")
public class ApiController {
    @GetMapping
    public ResponseEntity<?> getData(@PathVariable("id") int id) throws IOException {
        String url = "https://truyenfull.vn/tu-cam/chuong-"+id+"/";
        Document doc = Jsoup.connect(url).get();
        Element datas = doc.select("div.chapter-c").first();
        Element title = doc.getElementsByClass("truyen-title").first();
        Element chap = doc.getElementsByClass("chapter-title").first();


        HashMap<String, String> fullData = new HashMap<>();
        fullData.put("title", title.html());

        fullData.put("datas", datas.html());

        fullData.put("chap", chap.text());


        return ResponseEntity.ok(fullData);
//        return ResponseEntity.ok(datas.select(">p").html());
    }
}
