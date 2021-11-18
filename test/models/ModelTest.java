package models;

import com.fasterxml.jackson.databind.JsonNode;
import models.Owner;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import play.test.WithApplication;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class ModelTest extends WithApplication {

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

        assertEquals("Pedram Nouri", o1.getName());
        assertEquals("noori.pedram816@gmail.com", o1.getEmail());
        assertEquals("Concordia", o1.getCompany());
        assertEquals(testDate, o1.getCreated_at());
        assertEquals(49, o1.getFollowers());
        assertEquals(29, o1.getFollowing());
        assertEquals("pedramnoori", o1.getLogin());
        assertEquals("github.com/pedramnoori", o1.getHtml_url());
        assertEquals(12, o1.getPublic_repos());

    }
}
