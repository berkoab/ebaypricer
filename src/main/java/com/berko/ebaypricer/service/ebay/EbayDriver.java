package com.berko.ebaypricer.service.ebay;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import com.berko.ebaypricer.service.URLReaderService;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author rajeev jha (xxx@yyy.com)
 *
 */
public class EbayDriver {
    public final static String EBAY_APP_ID = "AryehBer-900e-4c58-8807-7bbf64dac2c5";
    public final static String EBAY_FINDING_SERVICE_URI = "http://svcs.ebay.com/services/search/FindingService/v1?";

//        + "&paginationInput.entriesPerPage={maxresults}";
    public static final String SERVICE_VERSION = "1.13.0";
    public static final String OPERATION_NAME = "findItemsAdvanced";
    public static final String GLOBAL_ID = "EBAY-US";
    public final static int REQUEST_DELAY = 3000;
    public final static int MAX_RESULTS = 10;
    private int maxResults;

    public EbayDriver() {
        this.maxResults = MAX_RESULTS;
    }

    public EbayDriver(int maxResults) {
        this.maxResults = maxResults;
    }

    public List<EbayResponse> search(String tag,
                                     String minPrice,
                                     String maxPrice,
                                     List<String> conditions,
                                     String sortOrder) throws Exception {

        String address = createAddress(java.net.URLEncoder.encode(tag, "UTF-8"),
            (minPrice!=null?minPrice:"0"), maxPrice!=null?maxPrice:"1000000", conditions, sortOrder);
        print("sending request to :: ", address);
        String response = URLReaderService.read(address);
//        print("response :: ", response);
        //process xml dump returned from EBAY
        return processResponse(response);
        //Honor rate limits - wait between results
//        Thread.sleep(REQUEST_DELAY);
    }

    private String createAddress(String tag,
                                 String minPrice,
                                 String maxPrice,
                                 List<String> conditions,
                                 String sortOrder) {

        //substitute token
        StringBuffer address = new StringBuffer();
        address.append(EBAY_FINDING_SERVICE_URI);
        address.append("OPERATION-NAME="+OPERATION_NAME);
        address.append("&SERVICE-VERSION="+SERVICE_VERSION);
        address.append("&SECURITY-APPNAME="+EBAY_APP_ID);
        address.append("&GLOBAL-ID="+GLOBAL_ID);
        address.append("&keywords="+tag);
        address.append("&itemFilter(0).name=MinPrice");
        address.append("&itemFilter(0).value(0)="+minPrice);
        address.append("&itemFilter(1).name=MaxPrice");
        address.append("&itemFilter(1).value(0)="+maxPrice);
        if(conditions!=null) {
            address.append("&itemFilter(2).name=Condition");
            int i = 0;
            for(String condition:conditions) {
                address.append("&itemFilter(2).value("+ i++ +")="+condition);
            }
        }
        if(sortOrder!=null) {
            address.append("&sortOrder="+sortOrder);
        }


//        String address = EbayDriver.EBAY_FINDING_SERVICE_URI;
//        address = address.replace("{version}", EbayDriver.SERVICE_VERSION);
//        address = address.replace("{operation}", EbayDriver.OPERATION_NAME);
//        address = address.replace("{globalId}", EbayDriver.GLOBAL_ID);
//        address = address.replace("{applicationId}", EbayDriver.EBAY_APP_ID);
//        address = address.replace("{keywords}", tag);
//        address = address.replace("{maxresults}", "" + this.maxResults);

        return address.toString();

    }

    private List<EbayResponse> processResponse(String response) throws Exception {

        List<EbayResponse> ebayResponses = new ArrayList<EbayResponse>();
        XPath xpath = XPathFactory.newInstance().newXPath();
        InputStream is = new ByteArrayInputStream(response.getBytes("UTF-8"));
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = domFactory.newDocumentBuilder();


        Document doc = builder.parse(is);
        XPathExpression ackExpression = xpath.compile("//findItemsAdvancedResponse/ack");
        XPathExpression itemExpression = xpath.compile("//findItemsAdvancedResponse/searchResult/item");

        String ackToken = (String) ackExpression.evaluate(doc, XPathConstants.STRING);
//        print("ACK from ebay API :: ", ackToken);
        if (!ackToken.equals("Success")) {
            throw new Exception(" service returned an error");
        }

        NodeList nodes = (NodeList) itemExpression.evaluate(doc, XPathConstants.NODESET);

        for (int i = 0; i < nodes.getLength(); i++) {
            EbayResponse ebayResponse = new EbayResponse();
            Node node = nodes.item(i);
            ebayResponse.setAckToken(ackToken);
            ebayResponse.setItemId((String) xpath.evaluate("itemId", node, XPathConstants.STRING));
            ebayResponse.setTitle((String) xpath.evaluate("title", node, XPathConstants.STRING));
            ebayResponse.setItemUrl((String) xpath.evaluate("viewItemURL", node, XPathConstants.STRING));
            ebayResponse.setGalleryUrl((String) xpath.evaluate("galleryURL", node, XPathConstants.STRING));
            ebayResponse.setCurrentPrice((String) xpath.evaluate("sellingStatus/currentPrice", node, XPathConstants.STRING));
            ebayResponse.setCondition((String) xpath.evaluate("condition/conditionDisplayName", node, XPathConstants.STRING));
            ebayResponses.add(ebayResponse);
        }

        is.close();
        return ebayResponses;
    }

    private void print(String name, String value) {
        System.out.println(name + "::" + value);
    }
}
