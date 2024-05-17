package tqs.peticket.display.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import tqs.peticket.display.cache.Cache;

@RestController
@RequestMapping("/api/cache")
public class DisplayController {

    @Autowired
    private Cache cache;

    @GetMapping("/all")
    public ResponseEntity<Map<String, Integer>> getAllCacheEntries() {
        Map<String, Integer> cacheEntries = cache.getCacheMap();
        return ResponseEntity.ok(cacheEntries);
    }
}
