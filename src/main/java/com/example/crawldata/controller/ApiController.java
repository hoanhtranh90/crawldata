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
@RequestMapping("/api/truyenfull")
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
        String url = "https://truyenfull.vn/"+tentruyen+"/";        

        Document doc = Jsoup.connect(url).get();
        String title = doc.getElementsByTag("title").text();
        Element content = doc.getElementById("total-page");          
    
        
        
//        Elements getListDetai2 = getListChuong.get(0).getElementsByTag("a");
        String urlCheck = doc.getElementsByClass("list-chapter").get(0).getElementsByAttribute("href").get(0).attr("href");
        if(urlCheck.contains("quyen")){
        String totalPage = content.select("input").attr("value");
        ArrayList<String> listChuong = new ArrayList<>();
        for (int i = 1; i <= Integer.parseInt(totalPage); i++) {
            String url1 = "https://truyenfull.vn/"+tentruyen+"/trang-"+i+"/";        
            Document doc1 = Jsoup.connect(url1).get();
            Elements getListChuong = doc1.getElementsByClass("list-chapter");
            for (int k = 0; k < getListChuong.size(); k++) {
                for (int j = 0; j < getListChuong.get(k).getElementsByAttribute("href").size(); j++) {
               String url2 = getListChuong.get(k).getElementsByAttribute("href").get(j).attr("href");
               listChuong.add(url2);
            }  
           }     
        }
//        ArrayList<HashMap<String, String>> last = new ArrayList<>();
//        HashMap<String, String> fullData = new HashMap<>();
////        for (int i = 0; i < listChuong.size(); i++) {
//        for (int i = 0; i < 1; i++) {
//            String url_n = listChuong.get(i);
//            Document doc_n = Jsoup.connect(url_n).get();
//            Element datas = doc_n.select("div.chapter-c").first();
//            Element title = doc_n.getElementsByClass("truyen-title").first();
//            Element chap = doc_n.getElementsByClass("chapter-title").first();
//            if(datas == null) {
//                return ResponseEntity.ok("truyen bi loi");
//            }
//            HashMap<String, String> fullDatax = new HashMap<>();
//            fullDatax.put("title", title.text());
//
//            fullDatax.put("datas", datas.html());
//            String h = datas.html();
//            fullDatax.put("chap", chap.text()); 
//            
//
//            last.add(fullDatax);
//        }
       

//        return ResponseEntity.ok(last);
            return ResponseEntity.ok(listChuong);
        }
        else {
            String totalPage = content.select("input").attr("value");
            ArrayList<String> listChuong = new ArrayList<>();
            String url1 = "https://truyenfull.vn/"+tentruyen+"/trang-"+Integer.parseInt(totalPage)+"/";        
            Document doc1 = Jsoup.connect(url1).get();
            Elements getListChuong = doc1.getElementsByClass("list-chapter");
            String urlLast = getListChuong.get(getListChuong.size()-1)
                    .getElementsByAttribute("href")
                    .get(getListChuong.get(getListChuong.size()-1)
                    .getElementsByAttribute("href").size()-1).attr("href");
            int count = Integer.parseInt(urlLast.replaceAll("[^0-9]", ""));
            for (int i = 1; i <= count; i++) {
             String urlv = "https://truyenfull.vn/"+tentruyen+"/chuong-"+i+"/"; 
             listChuong.add(urlv); 
            }
            return ResponseEntity.ok(listChuong);
        }

//        return ResponseEntity.ok(datas.select(">p").html());
    }

}
