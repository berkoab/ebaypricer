package com.berko.ebaypricer.web.rest;

import com.berko.ebaypricer.service.ebay.EbayDriver;
import com.berko.ebaypricer.service.ebay.EbayResponse;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * EbaySearch controller
 */
@RestController
@RequestMapping("/api/ebay-search")
public class EbaySearchResource {

    private final Logger log = LoggerFactory.getLogger(EbaySearchResource.class);

    /**
    * GET search
    */
    @GetMapping("/search")
    public List<EbayResponse> search(@RequestParam String tag,
                                     @RequestParam(required=false) String minPrice,
                                     @RequestParam(required=false) String maxPrice,
                                     @RequestParam(required=false) List<String> condition,
                                     @RequestParam(required=false) String sortOrder) {
        EbayDriver driver = new EbayDriver();
        List<EbayResponse> responses = new ArrayList<EbayResponse>();
        try {
            return driver.search(tag,minPrice,maxPrice,condition,sortOrder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
