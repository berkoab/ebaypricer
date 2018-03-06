package com.berko.ebaypricer.web.rest;

import com.berko.ebaypricer.EbaypricerApp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 * Test class for the EbaySearch REST controller.
 *
 * @see EbaySearchResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EbaypricerApp.class)
public class EbaySearchResourceIntTest {

    private MockMvc restMockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        EbaySearchResource ebaySearchResource = new EbaySearchResource();
        restMockMvc = MockMvcBuilders
            .standaloneSetup(ebaySearchResource)
            .build();
    }

    /**
    * Test search
    */
    @Test
    public void testSearch() throws Exception {
        restMockMvc.perform(get("/api/ebay-search/search"))
            .andExpect(status().isOk());
    }

}
