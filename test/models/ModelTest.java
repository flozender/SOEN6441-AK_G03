package models;

import com.fasterxml.jackson.databind.JsonNode;
import models.Owner;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import play.test.WithApplication;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ModelTest extends WithApplication {

    /**
     * Tests the Owner model.
     * 
     * @author Pedram Nouri
     * @version 1.0.0
     */
    @Test
    public final void ownerTest() throws Exception{
        Owner o1 = new Owner();
        String stringDate="31/12/2018";
        String stringTestDate = "31/12/2018";
        Date testDate = new SimpleDateFormat("dd/MM/yyyy").parse(stringTestDate);

        o1.setName("Pedram Nouri");
        o1.setEmail("noori.pedram816@gmail.com");
        o1.setCompany("Concordia");
        Date date = new SimpleDateFormat("dd/MM/yyyy").parse(stringDate);
        o1.setCreated_at(date);
        o1.setFollowers(49);
        o1.setFollowing(29);
        o1.setLogin("pedramnoori");
        o1.setHtml_url("github.com/pedramnoori");
        o1.setPublic_repos(12);
        o1.setBio("Right On");

        assertEquals("Pedram Nouri", o1.getName());
        assertEquals("noori.pedram816@gmail.com", o1.getEmail());
        assertEquals("Concordia", o1.getCompany());
        assertEquals(testDate, o1.getCreated_at());
        assertEquals(49, o1.getFollowers());
        assertEquals(29, o1.getFollowing());
        assertEquals("pedramnoori", o1.getLogin());
        assertEquals("github.com/pedramnoori", o1.getHtml_url());
        assertEquals(12, o1.getPublic_repos());
        assertEquals("Right On", o1.getBio());

    }

    /**
     * Tests the Repository model.
     * 
     * @author Tayeeb Hasan
     * @version 1.0.0
     */
    @Test
    public final void repositoryTest() throws Exception{
        Repository r1 = new Repository();

        r1.setName("Jest");
        r1.setDescription("Delightful JavaScript Testing");

        String stringDate="31/12/2018";
        Date d1 = new SimpleDateFormat("dd/MM/yyyy").parse(stringDate);
        r1.setCreatedAt(d1);
        
        
        Owner o1 = new Owner();
        o1.setName("Tayeeb Hasan");
        r1.setOwner(o1);

        License l1 = new License();
        l1.setKey("gpl-3.0");
        r1.setLicense(l1);

        r1.setHtmlUrl("https://github.com/facebook/jest");
        r1.setLanguage("TypeScript");

        List<String> topics = Arrays.asList("JavaScript", "Testing", "Facebook");
        r1.setTopics(topics);
      
        r1.setStargazersCount(5912);
        r1.setOpenIssuesCount(332);
        r1.setForksCount(40);

        assertEquals("Jest", r1.getName());
        assertEquals("Delightful JavaScript Testing", r1.getDescription());
        assertEquals(d1, r1.getCreatedAt());
        assertEquals("Tayeeb Hasan", r1.getOwner().getName());
        assertEquals("gpl-3.0", r1.getLicense().getKey());
        assertEquals("https://github.com/facebook/jest", r1.getHtmlUrl());
        assertEquals("TypeScript", r1.getLanguage());
        assertEquals(topics, r1.getTopics());
        assertEquals(5912, r1.getStargazersCount());
        assertEquals(332, r1.getOpenIssuesCount());
        assertEquals(40, r1.getForksCount());
    }

    /**
     * Tests the License model.
     * 
     * @author Tayeeb Hasan
     * @version 1.0.0
     */
    @Test
    public final void licenseTest() throws Exception{
        License l1 = new License();
        l1.setName("MIT License");
        l1.setKey("MIT");

        assertEquals("MIT License", l1.getName());
        assertEquals("MIT", l1.getKey());
    }
}
