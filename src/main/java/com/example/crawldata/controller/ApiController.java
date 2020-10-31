package com.example.crawldata.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiController {
    @GetMapping("/{tentruyen}/{id}")
    public ResponseEntity<?> getData(@PathVariable("tentruyen") String tentruyen,@PathVariable("id") int id) throws IOException {
        String url = "https://truyenfull.vn/"+tentruyen+"/chuong-"+id+"/";
        Document doc = Jsoup.connect(url).get();
        Element datas = doc.select("div.chapter-c").first();
        Element title = doc.getElementsByClass("truyen-title").first();
        Element chap = doc.getElementsByClass("chapter-title").first();
//        System.out.println(id);

        if(datas == null) {
            return ResponseEntity.ok("truyen bi loi");
        }

        HashMap<String, String> fullData = new HashMap<>();
        fullData.put("title", title.text());

        fullData.put("datas", datas.html());

        fullData.put("chap", chap.text());


        return ResponseEntity.ok(fullData);
//        return ResponseEntity.ok(datas.select(">p").html());
    }
    @GetMapping("/search")
    public ResponseEntity<?> searchData(@RequestParam String name) throws IOException {
        String uri = "https://truyenfull.vn/ajax.php?type=quick_search&str="+name;
        Document doc = Jsoup.connect(uri).get();
        Elements datas = doc.getElementsByClass("list-group-item");
        ArrayList<String> arrayList = new ArrayList<>();
        for ( Element v:datas
             ) {
            arrayList.add(v.text());
        }
        return ResponseEntity.ok(arrayList);
    }

    /**************Trang chu *************/


    //Truyen Hot
    @GetMapping("/home/truyentop")
    public ResponseEntity<?> truyenTop() throws IOException {
        String uri = "https://truyenfull.vn";
        Document doc = Jsoup.connect(uri).get();
        Elements datas = doc.getElementsByClass("index-intro").first().select("div");
        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
        for ( int i = 0 ;i < datas.size();i++
        ) {
            if(i%2==0) continue;
            else {
                Element v = datas.get(i);
                HashMap<String, String> fullData = new HashMap<>();

                fullData.put("imageLink", v.select("a").select("img").attr("lazysrc"));

                fullData.put("ten", v.text());

                fullData.put("bookLink", v.select("a").attr("href").replace("https://truyenfull.vn", "http://localhost:8085/api") + "1");

                arrayList.add(fullData);
            }

        }
//        System.out.println(datas.get(1).select("a").select("img").attr("lazysrc")); //link anh
//        System.out.println(datas.get(1).text()); //Ten truyen
//        System.out.println(datas.get(1).select("a").attr("href").replace("https://truyenfull.vn","http://localhost:8085/api")+"1"); //link truyen
        return ResponseEntity.ok(arrayList);
    }

    @GetMapping("/home/newupdate")
    public ResponseEntity<?> newUpdate() throws IOException {
        String uri = "https://truyenfull.vn";
        Document doc = Jsoup.connect(uri).get();
        Elements datas = doc.getElementsByClass("row");

        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
        for ( int i = 2 ;i < datas.size()-2;i++
        ) {

                Element v = datas.get(i);

                HashMap<String, String> fullData = new HashMap<>();

                fullData.put("imageLink", v.select("a").select("img").attr("lazysrc"));

                fullData.put("ten", v.select("h3").text());

                fullData.put("bookLink", v.select("a").attr("href").replace("https://truyenfull.vn", "http://localhost:8085/api") + "1");

                fullData.put("date", v.select("div:nth-child(4)").text());

                arrayList.add(fullData);


        }
        return ResponseEntity.ok(arrayList);
    }

    @GetMapping("/home/truyendone")
    public ResponseEntity<?> truyenDone() throws IOException {
        String uri = "https://truyenfull.vn";
        Document doc = Jsoup.connect(uri).get();
        Elements datas = doc.select("div.list-thumbnail div");
        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
        for ( int i = 2 ;i < datas.size();i++
        ) {

            Element v = datas.get(i);
            if(v.select("a div:nth-child(1)").attr("data-desk-image").length() == 0 ){
                continue;
            }
            else {
                HashMap<String, String> fullData = new HashMap<>();

                fullData.put("imageLink", v.select("a div:nth-child(1)").attr("data-desk-image"));

                fullData.put("ten", v.select("a>div>h3").text());

                fullData.put("bookLink", v.select("a").attr("href").replace("https://truyenfull.vn", "http://localhost:8085/api") + "1");

                arrayList.add(fullData);

            }
        }
        return ResponseEntity.ok(arrayList);
    }
    @GetMapping("/{tentruyen}")
    public ResponseEntity<?> info(@PathVariable("tentruyen") String tentruyen) throws IOException {
        String url = "https://truyenfull.vn/"+tentruyen+"/chuong-"+"/";
        Document doc = Jsoup.connect(url).get();
        Element datas = doc.select("div.chapter-c").first();
        String image = doc.select("div.books > div > img").first().attr("src");
        System.out.println(image);


        HashMap<String, String> fullData = new HashMap<>();
        fullData.put("image", image);
//
//        fullData.put("datas", datas.html());
//
//        fullData.put("chap", chap.text());


        return ResponseEntity.ok(fullData);
//        return ResponseEntity.ok(datas.select(">p").html());
    }

}
