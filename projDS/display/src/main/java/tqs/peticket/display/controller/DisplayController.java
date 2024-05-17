package tqs.peticket.display.controller;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger logger = LogManager.getLogger(DisplayController.class);

    @Autowired
    private Cache cache;

    @GetMapping("/all")
    public ResponseEntity<Map<String, Integer>> getAllCacheEntries() {

        logger.info("Getting all cache entries");
        
        Map<String, Integer> cacheEntries = cache.getCacheMap();
        if (cacheEntries.isEmpty()) {
            logger.info("No cache entries found");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(cacheEntries);
    }
}
